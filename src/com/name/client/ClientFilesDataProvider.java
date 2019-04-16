package com.name.client;

import com.name.common.SearchItem;
import com.name.entities.Card;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;
import java.util.regex.Pattern;


public class ClientFilesDataProvider extends AClientFilesDataProvider {
    private final static int LONG_SIZE = 8;

    private static final int NUM_OF_FILES = 4;
    private static final int NUM_OF_CARD_FIELDS = 2;

    private static final int FIELDS = 0;
    private static final int INDEX = 1;
    private static final int ENG_SEARCH = 2;
    private static final int RUS_SEARCH = 3;

    private static final String DIRECTORY = "client";

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
     * Not final version.
     * @param   phrase the {@code String} to search in data storage.
     * @return  an array of {@code Card} objects whose either <tt>word</tt> or <tt>translation</tt>
     *          field matches with the given phrase; otherwise, <tt>null</tt>
     */
    @Override
    public Card[] find(String phrase) throws FileNotFoundException {
        Card[] findRes = null;

        if (phrase != null) {
            // Remove spaces at the edges
            phrase.trim();

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

    private Card[] search(File concreteLangSearchFile, String searchPhrase) throws FileNotFoundException {
        // TODO exception handling
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
                // TODO [?] Is it OK to just skip null link if a Card index is written in one file, but not exist in another one? (check read and filter below)
                searchRes = searchList.get(searchPhraseIndex).getIdes().stream()
                        .map(this::read)
                        .filter(Objects::nonNull)
                        .toArray(Card[]::new);
            }
        } catch (FileNotFoundException e) {
            // TODO Check English in this commentary
            // TODO Send a query to a Server to restore a not found file
            // Send a name of a not found file to a calling procedure to restore it from a Server
            throw new FileNotFoundException(concreteLangSearchFile.getName());
        } catch (ClassNotFoundException e) {
            System.err.println("Class" + e.getMessage() + "wasn't found.");
        } catch (IOException e) {
            System.err.println("IOException occurred while reading " + concreteLangSearchFile + " file");
        }
        return searchRes;
    }

    private Card read(int entryNum) {
        Card card = null;

        if (files[FIELDS].exists() && files[INDEX].exists()) {
            try (RandomAccessFile dataStream = new RandomAccessFile(files[FIELDS], "r");
                 RandomAccessFile indexStream = new RandomAccessFile(files[INDEX], "r")) {

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

//                int wordLength = (int) (tags[1] - tags[0]);
//                int translationLength = (int) (tags[2] - tags[1]);
//                byte[] wordInBytes = new byte[wordLength];
//                byte[] translationInBytes = new byte[translationLength];
//                dataStream.seek(tags[0]);
//                dataStream.read(wordInBytes);
//                dataStream.read(translationInBytes);

                // TODO method which get array of byte arrays and build Card object on them
                card = new Card(new String(cardFieldsInBytes[0]),
                        new String(cardFieldsInBytes[1]));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return card;
       // else { throw new IOException("One of the database files wasn't found"); }
    }
}

//======================================================================================================================
