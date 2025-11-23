package ru.yandex.practicum;

import java.util.*;

public class WordleDictionary {
    private List<String> words;
    private final int wordLength;

    public WordleDictionary(List<String> words, int wordLength) {
        this.words = words;
        this.wordLength = wordLength;
    }

    public List<String> getWords() {
        return words;
    }

    public String getRandomWord(Random random) {
        if (words.isEmpty()) {
            throw new IllegalStateException("Словарь пуст");
        }
        return words.get(random.nextInt(words.size()));
    }

    public boolean isValidWord(String word) {
        return word != null &&
                word.length() == wordLength &&
                words.contains(word.toLowerCase());
    }

    public int size() {
        return words.size();
    }
}