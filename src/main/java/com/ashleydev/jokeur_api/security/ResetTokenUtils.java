package com.ashleydev.jokeur_api.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;

public final class ResetTokenUtils {

  private static final SecureRandom RNG = new SecureRandom();

  private ResetTokenUtils() {}

  public static String generateToken() {
    byte[] bytes = new byte[32]; // 256 bits
    RNG.nextBytes(bytes);
    return toHex(bytes);
  }

  public static String sha256Hex(String input) {
    try {
      MessageDigest md = MessageDigest.getInstance("SHA-256");
      byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));
      return toHex(digest);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private static String toHex(byte[] bytes) {
    StringBuilder sb = new StringBuilder(bytes.length * 2);
    for (byte b : bytes) sb.append(String.format("%02x", b));
    return sb.toString();
  }
}
