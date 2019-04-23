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

    // 0: contains fields of cards written one by one
    // 1: contains positions of this fields as an array of longs
    // 2: contains ArrayList<SearchItem>, where written words (phrases) in Russian and indexes of associated Cards
    // 3: contains ArrayList<SearchItem>, where written words (phrases) in English and indexes of associated Cards
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
    public Card[] find(String phrase) throws IOException {
        Card[] findRes = null;

        if (phrase != null) {
            // Remove spaces at the edges
            phrase = phrase.trim().toLowerCase();

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
     *          is a name of this file
     */
    private Card[] search(File concreteLangSearchFile, String searchPhrase) throws IOException {
        // TODO exceptions handling
        Card[] searchRes = null;
        ObjectInputStream searchStream;
        ArrayList<SearchItem> searchList;

        try {

            try {

                searchStream = new ObjectInputStream(new BufferedInputStream(new FileInputStream(concreteLangSearchFile)));
                searchList = (ArrayList<SearchItem>) searchStream.readObject();

            } catch (FileNotFoundException e) {
                // Pass a name of a lang_search file that wasn't found to a calling procedure
                throw new FileNotFoundException(concreteLangSearchFile.getName());
            } catch (IOException e) {
                // Log Imitation
                System.err.println("[Log]IOException: " +
                        concreteLangSearchFile.getName() + " file reading failure");
                // Throw an IOException into outer try-catch block
                throw e;
            }

            /*
             * Get index of SearchItem object whose field phrase matches with searchPhrase
             * Binary search is used in SearchList
             */

            int searchPhraseIndex = Collections.binarySearch(searchList,
                    new SearchItem(searchPhrase),
                    Comparator.comparing(SearchItem::getPhrase));

            if (searchPhraseIndex >= 0) {
                ArrayList<Integer> IDs = searchList.get(searchPhraseIndex).getIDs();
                ArrayList<Card> listOfCards = new ArrayList<>();

                for (int ID: IDs) {
                    listOfCards.add(read(ID));
                }

                listOfCards.toArray(searchRes = new Card[listOfCards.size()]);
            }
        } catch (IOException e) {
            /*
             * Up an existing IOException one level higher
             * This catch block captures FileNotFoundException as well
             */
            throw e;
        } catch (ClassNotFoundException e) {
            // Log Imitation
            System.err.println("[Log]ClassNotFoundException: class " + e.getMessage() + " wasn't found.");
            throw new IOException(e);
        }
        return searchRes;
    }

    private Card read(int entryNum) throws IOException {
        Card card;

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
            // Up an existing FileNotFoundException one level higher
            throw e;
        } catch (IOException e) {
            // Log Imitation
            System.err.println("[Log]IOException: " + files[FIELDS].getName() +
                    " or " + files[INDEX].getName() + " file reading failure");
            // Up an existing IOException one level higher
            throw e;
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
            // Pass a name of a file that wasn't found to a calling procedure
            throw new FileNotFoundException(file.getName());
        }
        return randomAccessStream;
    }

    private void closeRandomAccessFile(RandomAccessFile randomAccessStream, File tiedFile) {
        try{
            if(randomAccessStream != null) {
                randomAccessStream.close();
            }
        } catch (IOException e) {
            System.err.println("IOException: a stream tied to " + tiedFile.getName() +
                    " file wasn't closed correctly." +
                    "\nSystem resources associated with it weren't released.");
        }
    }

}