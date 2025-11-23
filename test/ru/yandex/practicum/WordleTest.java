package ru.yandex.practicum;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.nio.file.Path;
import java.util.*;

class WordleTest {

    @TempDir // Не мог понять как сделать тест, чтобы проверять корректность загрузки слов в словарь
    // В итоге ИИ подсказал создать временную переменную TempDir. Тогда слова не будут с каждым тестом
    // увеличиваться, а буду каждый раз обновляться. Надеюсь правильно понял :)
    Path tempDir;

    private WordleDictionary dictionary;
    private List<String> testWords;

    @BeforeEach
    void setUp() {
        testWords = Arrays.asList("гараж", "парус", "кошка", "просо", "слово");
        dictionary = new WordleDictionary(testWords, 5);
    }

    @Test
    @DisplayName("Проверка создания словаря")
    void testDictionaryCreation() {
        assertEquals(5, dictionary.size());
        assertTrue(dictionary.isValidWord("парус"));
        assertFalse(dictionary.isValidWord("несуществующее"));
    }

    @Test
    @DisplayName("Проверка загрузчика словаря")
    void testDictionaryLoader() throws IOException {
        // Создал временный файл со словами
        File testFile = tempDir.resolve("test_words.txt").toFile();
        try (PrintWriter writer = new PrintWriter(testFile)) {
            writer.println("слово");
            writer.println("мышка");
            writer.println("вышка");
        }

        WordleDictionaryLoader loader = new WordleDictionaryLoader();
        WordleDictionary loadedDictionary = loader.loadDictionary(testFile.getPath(), 5);

        assertEquals(3, loadedDictionary.size());
        assertTrue(loadedDictionary.isValidWord("слово"));
    }

    @Test
    @DisplayName("Проверка инициализации игры")
    void testGameInitialization() {
        WordleGame game = new WordleGame("слово", 6, dictionary);

        assertEquals(6, game.getSteps());
        assertFalse(game.isGameOver());
    }

    @Test
    @DisplayName("Проверка на успешное угадывание слова")
    void testCorrectGuess() {
        WordleGame game = new WordleGame("слово", 6, dictionary);
        boolean guessed = game.checkGuess("слово");

        assertTrue(guessed);
        assertTrue(game.isWordGuessed());
        assertEquals("Поздравляем! Вы угадали слово!", game.getGameStateMessage());
    }

    @Test
    @DisplayName("Проверка на неуспешное угадывание слова")
    void testIncorrectGuess() {
        WordleGame game = new WordleGame("слово", 6, dictionary);
        boolean guessed = game.checkGuess("парус");

        assertFalse(guessed);
        assertEquals(5, game.getSteps());
        assertTrue(game.getGameStateMessage().contains("Неверно"));
    }

    @Test
    @DisplayName("Проверка генерации совпадений букв")
    void testPatternGeneration() {
        WordleGame game = new WordleGame("слово", 6, dictionary);
        game.checkGuess("просо");

        assertEquals("--+^+", game.getLastPattern());
    }

    @Test
    @DisplayName("Проверка исправления слов")
    void testFixText() {
        assertEquals("ежик", WordleDictionaryLoader.fixText("Ёжик"));
        assertEquals("береза", WordleDictionaryLoader.fixText("БЕРЁЗА"));
        assertEquals("парус", WordleDictionaryLoader.fixText(" ПаРуС    "));
    }

    @Test
    @DisplayName("Проверка предложенного слова")
    void testSuggestion() {
        WordleGame game = new WordleGame("слово", 6, dictionary);
        game.checkGuess("просо");

        String suggestion = game.suggestWord();
        assertNotNull(suggestion);
        assertTrue(dictionary.isValidWord(suggestion));
    }

    @Test
    @DisplayName("Проверка состояния игры после победы")
    void testGameStateAfterWin() {
        WordleGame game = new WordleGame("гараж", 6, dictionary);
        game.checkGuess("гараж");

        assertTrue(game.isGameOver());
        assertTrue(game.isWordGuessed());
    }

    @Test
    @DisplayName("Проверка состояния игры после поражения")
    void testGameStateAfterLoss() {
        WordleGame game = new WordleGame("кошка", 1, dictionary);
        game.checkGuess("парус");

        assertTrue(game.isGameOver());
        assertFalse(game.isWordGuessed());
    }
}