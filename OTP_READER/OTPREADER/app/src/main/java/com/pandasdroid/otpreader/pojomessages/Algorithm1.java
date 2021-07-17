package com.pandasdroid.otpreader.pojomessages;

import java.io.*;
import java.util.Scanner;

public class Algorithm1 {

    static int min(int A, int B, int C) {
        if (A < B)
            return (A < C) ? A : C;
        else
            return (B < C) ? B : C;
    }

    static int TotalALgo(int Arr[][], int x, int y) {

        int result[][] = new int[x + 1][y + 1];

        result[0][0] = Arr[0][0];


        for (int i = 1; i <= x; i++)
            result[i][0] = result[i - 1][0] + Arr[i][0];

        for (int j = 1; j <= y; j++)
            result[0][j] = result[0][j - 1] + Arr[0][j];


        //bro where did you defined var m?
        int m = 20;
        int n = 20;
        for (int i = 1; i <= m; i++)
            for (int j = 1; j <= n; j++)
                result[i][j] = min(result[i - 1][j - 1],
                        result[i - 1][j],
                        result[i][j - 1]) + Arr[i][j];

        return result[m][n];
    }

    public static void main(String args[]) {
        System.out.print("Enter the value of N for array: ");
        Scanner borad = new Scanner(System.in);

        System.out.print("Enter dimension: ");
        int rows = borad.nextInt();
        int columns = borad.nextInt();

        System.out.println("Enter array elements in array : ");

        int Arr[][] = new int[rows][columns];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                Arr[i][j] = borad.nextInt();
            }
        }
        System.out.print("Total will be: \n");
        System.out.print(TotalALgo(Arr, rows, columns));
    }
}