package com.ashleydev.jokeur_api.exceptions.rss;

public class RssReadException extends RuntimeException {

  public RssReadException(String message) {
    super(message);
  }

  public RssReadException(String message, Throwable cause) {
    super(message, cause);
  }
}
