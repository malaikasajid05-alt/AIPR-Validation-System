package com.aiprteam.backend.exception;

public class GithubException extends RuntimeException {
    public GithubException(String message) {
        super(message);
    }

    public GithubException(String message, Throwable cause) {
        super(message, cause);
    }
}
