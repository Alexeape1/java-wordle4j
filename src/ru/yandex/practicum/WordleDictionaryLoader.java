package ru.yandex.practicum;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WordleDictionaryLoader {

    public WordleDictionary loadDictionary(String filePath, int wordLength) throws IOException {
        List<String> dictionary = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String word;
            while ((word = br.readLine()) != null) {
                word = fixText(word);
                if (word.length() == wordLength) {
                    dictionary.add(word);
                }
            }
        }
        if (dictionary.isEmpty()) {
            throw new IOException("Словарь пуст или не содержит слов нужной длины: " + wordLength);
        }
        return new WordleDictionary(dictionary, wordLength);
    }

    public static String fixText(String word) {
        return word.replace("ё", "е")
                .replace("Ё", "е")
                .toLowerCase()
                .trim();
    }
}