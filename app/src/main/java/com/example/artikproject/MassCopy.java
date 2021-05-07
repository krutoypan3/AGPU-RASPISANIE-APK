package com.example.artikproject;

public class MassCopy {
    public static String[][][][] copy4d(String[][][][] original) {//Независимая копия четырехмерного массива
        String[][][][] copy = new String[original.length][][][];
        for (int i = 0; i < original.length; i++) {
            copy[i] = copy3d(original[i]);
        }
        return copy;
    }
    public static String[][][] copy3d(String[][][] original) {//Независимая копия трехмерного массива
        String[][][] copy = new String[original.length][][];
        for (int i = 0; i < original.length; i++) {
            copy[i] = copy2d(original[i]);
        }
        return copy;
    }

    public static String[][] copy2d(String[][] original) {//Независимая копия двухмерного массива
        String[][] copy = new String[original.length][];
        for (int i = 0; i < original.length; i++) {
            copy[i] = copy1d(original[i]);
        }
        return copy;
    }

    public static String[] copy1d(String[] original) {//Независимая копия одномерного массива (double)
        int length = original.length;
        String[] copy = new String[length];
        System.arraycopy(original, 0, copy, 0, length);
        return copy;
    }
}
