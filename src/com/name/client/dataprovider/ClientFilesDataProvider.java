package com.name.client.dataprovider;

import com.name.common.entities.SearchItem;
import com.name.common.entities.Card;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;


public class ClientFilesDataProvider extends AClientFilesDataProvider {
    private final static int LONG_SIZE = 8;

    private static final int NUM_OF_FILES = 4;
    private static final int NUM_OF_CARD_FIELDS = 2;

    // TODO pack in enum
    private static final int FIELDS = 0;
    private static final int INDEX = 1;
    private static final int ENG_SEARCH = 2;
    private static final int RUS_SEARCH = 3;

    private final String DIRECTORY = "client";

    private static ClientFilesDataProvider instance;

    private ClientFilesDataProvider() {
        files = new File[NUM_OF_FILES];
        files[FIELDS] = new File(DIRECTORY + File.separator + "fields_of_cards.data");
        files[INDEX] = new File(DIRECTORY + File.separator + "index.data");
        files[ENG_SEARCH] = new File(DIRECTORY + File.separator + "eng_search.data");
        files[RUS_SEARCH] = new File(DIRECTORY + File.separator + "rus_search.data");
    }

    public static ClientFilesDataProvider getInstance() {
        if (instance == null) {
            instance = new ClientFilesDataProvider();
        }
        return instance;
    }


    /**
     * @param phrase the {@code String} to search in data storage.
     * @return an array of {@code Card} objects whose either <tt>word</tt> or <tt>translation</tt>
     * field matches with the given phrase; otherwise, <tt>null</tt>
     */
    @Override
    public Card[] find(String phrase) throws FileNotFoundException {
        Card[] findRes = null;

        if (phrase != null) {
            // Remove spaces at the edges
            phrase = phrase.trim();

            // Search in case if search phrase in English
            if (Pattern.matches("[\\p{Alpha}\\s-]+", phrase)) {
                findRes = search(files[ENG_SEARCH], phrase);
            } else {
                // Search in case if search phrase in Russian
                if (Pattern.matches("[А-яё\\s-]+", phrase)) {
                    findRes = search(files[RUS_SEARCH], phrase);
                }
            }
        }

        return findRes;
    }

    /**
     * @param   concreteLangSearchFile - where will be searched
     * @param   searchPhrase - what will be searched
     * @return  an array of {@code Card} objects. One of their field defined by file parameter
     *          matches with the given phrase. Otherwise, <tt>null</tt>
     * @throws  FileNotFoundException if one of the files from <tt>Data Storage</tt> wasn't found.
     *          {@code String} message that can be obtained through{@code getMessage()} method
     *          is a name of this file, so it ca
     */
    private Card[] search(File concreteLangSearchFile, String searchPhrase) throws FileNotFoundException {
        // TODO exceptions handling
        Card[] searchRes = null;

        try (ObjectInputStream searchStream = new ObjectInputStream(new FileInputStream(concreteLangSearchFile))) {
            // TODO [?] How can I handle with it in a proper way?
            ArrayList<SearchItem> searchList = (ArrayList<SearchItem>) searchStream.readObject();

            // Get index of SearchItem object whose field phrase matches with searchPhrase
            // Binary search is used in SearchList
            int searchPhraseIndex = Collections.binarySearch(searchList,
                    new SearchItem(searchPhrase),
                    Comparator.comparing(SearchItem::getPhrase));

            if (searchPhraseIndex >= 0) {
                ArrayList<Integer> IDs = searchList.get(searchPhraseIndex).getIdes();
                int numOfIDs = IDs.size();
                ArrayList<Card> cardsInList = new ArrayList<>();

                // TODO [?] It's not effective to use loop for, isn't it?
                for(int i = 0; i < numOfIDs; ++i) {
                    cardsInList.add(read(IDs.get(i)));
                }

                cardsInList.toArray(searchRes = new Card[cardsInList.size()]);
            }
        } catch (FileNotFoundException e) {
            // TODO Check English in this commentary
            // TODO Send a query to a Server to restore a not found file
            // Send a name of a not found file to a calling procedure to restore it from a Server
            throw new FileNotFoundException(concreteLangSearchFile.getName());
        } catch (ClassNotFoundException e) {
            System.err.println("Class" + e.getMessage() + "wasn't found.");
        } catch (IOException e) {
            System.err.println("IOException occurred while reading " + concreteLangSearchFile.getName() + " file");
        }
        return searchRes;
    }

    private Card read(int entryNum) throws FileNotFoundException {
        Card card = null;

        RandomAccessFile dataStream = null;
        RandomAccessFile indexStream = null;

        try {
            dataStream = newRandomAccessReader(files[FIELDS]);
            indexStream = newRandomAccessReader(files[INDEX]);

            long pos = (entryNum == 0) ? 0 : LONG_SIZE * (NUM_OF_CARD_FIELDS * entryNum - 1);
            indexStream.seek(pos);

            long[] tags = new long[NUM_OF_CARD_FIELDS + 1];

            for (int i = 0; i <= NUM_OF_CARD_FIELDS; ++i) {
                tags[i] = indexStream.readLong();
            }

            int[] lengthsOfCardFields = new int[NUM_OF_CARD_FIELDS];

            for (int i = 0; i < NUM_OF_CARD_FIELDS; ++i) {
                lengthsOfCardFields[i] = (int) (tags[i + 1] - tags[i]);
            }

            byte[][] cardFieldsInBytes = new byte[NUM_OF_CARD_FIELDS][];

            dataStream.seek(tags[0]);

            for (int i = 0; i < NUM_OF_CARD_FIELDS; ++i) {
                cardFieldsInBytes[i] = new byte[lengthsOfCardFields[i]];
                dataStream.read(cardFieldsInBytes[i]);
            }

            card = new Card(cardFieldsInBytes);

        } catch (FileNotFoundException e) {
            // TODO Send a query to a Server to restore a not found file
            // Exception from newRandomAccessReader(File, String) method
            // Its message contains name of file that doesn't exist
            throw e;
        } catch (IOException e) {
            System.err.println("IOException occurred while" + files[FIELDS].getName());
            System.err.print(" and " + files[INDEX].getName() + " files reading");
        } finally {
            closeRandomAccessFile(dataStream, files[FIELDS]);
            closeRandomAccessFile(indexStream, files[INDEX]);
        }
        return card;
    }

    private RandomAccessFile newRandomAccessReader(File file) throws FileNotFoundException {
        RandomAccessFile randomAccessStream;
        try {
            randomAccessStream = new RandomAccessFile(file, "r");
        } catch (FileNotFoundException e) {
            // TODO Send a query to a Server to restore a not found file
            String fileName = file.getName();
            System.err.println("File " + fileName + " wasn't found.");
            throw new FileNotFoundException(fileName);
        }
        return randomAccessStream;
    }

    private void closeRandomAccessFile(RandomAccessFile randomAccessStream, File tiedFile) {
        try{
            if(randomAccessStream != null) {
                randomAccessStream.close();
            }
        } catch (IOException e) {
            System.err.println("IOException occurred while closing a stream tied with " + tiedFile.getName() + " file.");
            System.err.println("Therefore, system resources associated with this stream weren't released.");
        }
    }

}