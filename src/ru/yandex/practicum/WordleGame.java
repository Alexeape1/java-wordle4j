package ru.yandex.practicum;

import java.util.*;

public class WordleGame {
    private final String answer;
    private int steps;
    private final WordleDictionary dictionary;
    private final Map<Integer, Character> correctPositions = new LinkedHashMap<>();
    private final Set<Character> incorrectLetters = new HashSet<>();
    private final List<String> attempts = new ArrayList<>();
    private final Map<Character, Set<Integer>> yellowLetters = new HashMap<>();
    private boolean wordGuessed = false;
    private String lastPattern = "";

    public WordleGame(String answer, int maxSteps, WordleDictionary dictionary) {
        if (answer == null || dictionary == null) {
            throw new IllegalArgumentException("Ответ и словарь не могут быть null");
        }
        if (!dictionary.isValidWord(answer)) {
            throw new IllegalArgumentException("Ответ должен быть из словаря");
        }
        this.answer = answer;
        this.steps = maxSteps;
        this.dictionary = dictionary;
    }

    public boolean checkGuess(String guess) {
        if (steps <= 0) {
            throw new GameOverException("Игра окончена. Не осталось попыток");
        }
        if (!dictionary.isValidWord(guess)) {
            throw new InvalidWordException("Слово '" + guess + "' нет в словаре");
        }

        attempts.add(guess);
        steps--;
        wordGuessed = answer.equals(guess);

        if (wordGuessed) {
            lastPattern = "+++++";
            return true;
        }

        StringBuilder match = new StringBuilder();
        for (int i = 0; i < guess.length(); i++) {

            if (answer.charAt(i) == guess.charAt(i)) {
                match.append("+");
                correctPositions.put(i, guess.charAt(i));
            } else if (answer.contains(String.valueOf(guess.charAt(i)))) {
                match.append("^");
                yellowLetters.put(guess.charAt(i), new HashSet<>());
                if (yellowLetters.containsKey(guess.charAt(i))) {
                    yellowLetters.get(guess.charAt(i)).add(i);
                }
            } else {
                match.append("-");
                incorrectLetters.add(guess.charAt(i));
            }
        }

        lastPattern = match.toString();
        return false;
    }

    public String getLastPattern() {
        return lastPattern;
    }

    public String getGameStateMessage() {
        if (wordGuessed) {
            return "Поздравляем! Вы угадали слово!";
        } else if (steps > 0) {
            return "Неверно. Совпадения: " + lastPattern + ". Осталось попыток: " + steps;
        } else {
            return "Увы, вы проиграли... Загаданное слово: " + answer;
        }
    }

    public String suggestWord() {
        for (String word : dictionary.getWords()) {
            if (isWordValidSuggestion(word)) {
                return word;
            }
        }
        return null;
    }

    private boolean isWordValidSuggestion(String word) {

        for (Map.Entry<Integer, Character> entry : correctPositions.entrySet()) {
            if (word.charAt(entry.getKey()) != entry.getValue()) {
                return false;
            }
        }

        for (int i = 0; i < word.length(); i++) {
            char letter = word.charAt(i);
            if (incorrectLetters.contains(letter)) {
                return false;
            }
        }

        for (Map.Entry<Character, Set<Integer>> entry : yellowLetters.entrySet()) {

            if (!word.contains(String.valueOf(entry.getKey()))) {
                return false;
            }

            for (int pos : entry.getValue()) {
                if (word.charAt(pos) == entry.getKey()) {
                    return false;
                }
            }
        }

        return !attempts.contains(word);
    }

    public int getSteps() {
        return steps;
    }

    public boolean isGameOver() {
        return steps <= 0 || wordGuessed;
    }

    public boolean isWordGuessed() {
        return wordGuessed;
    }
}