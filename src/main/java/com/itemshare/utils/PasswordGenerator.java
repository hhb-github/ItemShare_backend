package com.itemshare.utils;

public class PasswordGenerator {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java PasswordGenerator <password>");
            return;
        }
        String password = args[0];
        System.out.println("Raw password: " + password);
        System.out.println("Encoded password: " + PasswordUtils.encode(password));
    }
}