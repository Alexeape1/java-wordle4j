package ru.yandex.practicum;

import java.io.*;
import java.util.*;

public class Wordle {

    private static final int WORD_LENGTH = 5;
    private static final int MAX_ATTEMPTS = 6;

    public static void main(String[] args)  {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
        try {
            FileWriter fileWriter = new FileWriter("log.txt", true);
            PrintWriter logger = new PrintWriter(fileWriter);

            WordleDictionaryLoader loader = new WordleDictionaryLoader();
            WordleDictionary dictionary = loader.loadDictionary("words_ru.txt", WORD_LENGTH);

            String randomWord = dictionary.getRandomWord(random);
            WordleGame game = new WordleGame(randomWord, MAX_ATTEMPTS, dictionary);

            System.out.println("Слово загадано, попробуй отгадать!");

            while (!game.isGameOver()) {
                System.out.print("Введите слово: ");
                String userWord = scanner.nextLine();
                userWord = loader.fixText(userWord);
                try {
                    boolean guessed = game.checkGuess(userWord);
                    System.out.println(game.getGameStateMessage());
                    logger.println("Попытка: " + userWord + " - Паттерн: " + game.getLastPattern());

                    if (guessed) {
                        break;
                    }

                    if (!game.isGameOver()) {
                        System.out.println("Взять подсказку?\n1 - ДА\n2 - НЕТ");
                        String input = scanner.nextLine().trim();

                        if (input.equals("1")) {
                            String suggestion = game.suggestWord();
                            if (suggestion != null) {
                                System.out.println("Подсказка: " + suggestion);
                                logger.println("Запрошена подсказка: " + suggestion);
                            } else {
                                System.out.println("Подходящих слов не найдено");
                            }
                        } else {
                            System.out.println("Продолжаем без подсказки");
                        }
                    }
                } catch (InvalidWordException e) {
                    System.out.println("Ошибка: " + e.getMessage());
                    logger.println("Ошибка: " + e.getMessage());
                } catch (GameOverException e) {
                    System.out.println(e.getMessage());
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка при загрузке словаря: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка инициализации игры: " + e.getMessage());
        }
    }
}