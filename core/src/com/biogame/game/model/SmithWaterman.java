package com.biogame.game.model;

import org.jetbrains.annotations.NotNull;

public class SmithWaterman {
    private String str1;
    private String str2;

    private int length1, length2;

    private double[][] score;

    private static final int INDEL_SCORE = 0;

    public SmithWaterman(@NotNull String read,
                         @NotNull String genome) {
        this.str1 = read;
        this.str2 = genome;
        length1 = str1.length();
        length2 = str2.length();

        score = new double[length1 + 1][length2 + 1];

        buildMatrix();
    }

    private int similarity(int i, int j) {
        if (i == 0 || j == 0) {
            return INDEL_SCORE;
        }

        return (str1.charAt(i - 1) == str2.charAt(j - 1)) ? 1 : 0;
    }

    private void buildMatrix() {
        int i; // length of prefix substring of str1
        int j; // length of prefix substring of str2

        // base case
        score[0][0] = 0;

        // the first row
        for (i = 1; i <= length1; i++) {
            score[i][0] = 0;
        }

        // the first column
        for (j = 1; j <= length2; j++) {
            score[0][j] = 0;
        }

        // the rest of the matrix
        for (i = 1; i <= length1; i++) {
            for (j = 1; j <= length2; j++) {
                double diagScore = score[i - 1][j - 1] + similarity(i, j);
                double upScore = score[i][j - 1] + similarity(0, j);
                double leftScore = score[i - 1][j] + similarity(i, 0);

                score[i][j] = Math.max(diagScore, Math.max(upScore,
                        Math.max(leftScore, 0)));
            }
        }
    }

    public double getMaxScore() {
        double maxScore = 0;

        // skip the first row and column
        for (int i = 1; i <= length1; i++) {
            for (int j = 1; j <= length2; j++) {
                if (score[i][j] > maxScore) {
                    maxScore = score[i][j];
                }
            }
        }

        return maxScore;
    }
}