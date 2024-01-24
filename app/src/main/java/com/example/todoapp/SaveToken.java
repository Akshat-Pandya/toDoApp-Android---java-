package com.example.todoapp;

public class SaveToken {
    private static String token;

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        SaveToken.token = token;
    }
}
