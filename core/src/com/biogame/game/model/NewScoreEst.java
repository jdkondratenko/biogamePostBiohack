package com.biogame.game.model;

import org.jetbrains.annotations.NotNull;

public class NewScoreEst {

    public static double getMaxScore(@NotNull String read,
                                     @NotNull String genome) {
        int maxScore = 0;
        for (int i = 0; i < genome.length() - read.length() + 1; i++) {
            int match = 0;
            for (int j = 0; j < read.length(); j++) {
                if (genome.charAt(i + j) == read.charAt(j)) {
                    match++;
                }
            }
            if (match > maxScore) {
                maxScore = match;
            }
        }
        return (maxScore);
    }
}
