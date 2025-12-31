package com.itemshare;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class PasswordTest {
    public static void main(String[] args) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String password = "123456";
            byte[] hash = digest.digest(password.getBytes());
            String encodedPassword = Base64.getEncoder().encodeToString(hash);
            
            System.out.println("Raw password: " + password);
            System.out.println("Encoded password: " + encodedPassword);
            
            // Test matches
            String testPassword = "123456";
            byte[] testHash = digest.digest(testPassword.getBytes());
            String testEncodedPassword = Base64.getEncoder().encodeToString(testHash);
            
            System.out.println("Test password: " + testPassword);
            System.out.println("Test encoded password: " + testEncodedPassword);
            System.out.println("Do they match? " + encodedPassword.equals(testEncodedPassword));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
