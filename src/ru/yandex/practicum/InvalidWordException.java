package ru.yandex.practicum;

class InvalidWordException extends WordleException {
    public InvalidWordException(String message) {
        super(message);
    }
}