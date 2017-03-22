package com.biogame.game.model;

import org.jetbrains.annotations.NotNull;

public final class ScoreCounter {

    public static int countScore(@NotNull String read,
                                 @NotNull String genome,
                                 @NotNull Difficulty difficulty) {
        float match = 0;
        float score = 0;
        for (int i = 0; i < genome.length(); i++) {
            if (genome.charAt(i) == read.charAt(i)) {
                match++;
            }
        }

        final StringBuilder truncateRead = new StringBuilder(read);
        for (int i = read.length() - 1; i >= 0; i--) {
            if (truncateRead.charAt(i) == '_') {
                truncateRead.deleteCharAt(i);
            }
        }

        score = (int) NewScoreEst.getMaxScore(truncateRead.toString(), genome);

        int reverseScore = 0;
        if (difficulty.reverse) {
            final String reverseRead = new StringBuilder(truncateRead).reverse().toString();
            final StringBuilder complimenteryBuilder = new StringBuilder();
            for (int i = 0; i < reverseRead.length(); i++) {
                final char letter = reverseRead.charAt(i);
                switch (letter) {
                    case 'A':
                        complimenteryBuilder.append('T');
                        break;
                    case 'C':
                        complimenteryBuilder.append('G');
                        break;
                    case 'G':
                        complimenteryBuilder.append('C');
                        break;
                    case 'T':
                        complimenteryBuilder.append('A');
                        break;
                    default:
                        throw new IllegalArgumentException("The letter is not a, c, g or t");
                }
            }
            reverseScore = (int) NewScoreEst.getMaxScore(complimenteryBuilder.toString(), genome);
        }

        if (score < reverseScore) {
            score = reverseScore;
        }

        if (match == score) {
            score = difficulty.maxPoints;
        } else {
            score = (match / score) * difficulty.maxPoints;
        }
        return (int) score;
    }
} 