package com.biogame.game;

import org.jetbrains.annotations.NotNull;

public final class Utils {

    private static int score;

    private Utils() {
    }

    public static float getResizeCoef() {
        return 0.01f;
    }

    public static int getScore() {
        return score;
    }

    public static void setScore(int newScore) {
        score = newScore;
    }

    @NotNull
    public static String getStartGameMessage() {
        return "Let’s align short DNA strand against \nlong one! Swipe left/right to place \nshort DNA in the best way. \n" +
                "Long press to increase speed.\n\n\n\n\nTap to start.";
    }

    @NotNull
    public static String getReplacementInDnaMessage() {
        return "Even if there is no exact copy of \nshort DNA, we can still find the most \nsimilar part in long DNA. Try it out! \n\n\n\n\nTap to start.";
    }

    @NotNull
    public static String getReverseAlignMessage() {
        return "DNA is a double strand, and short \nsequence can be aligned to both \nstrands. In next levels you will see \nboth strands of long DNA. If you want \nto align short sequence to the strand, \npainted in pale colors, use button with \nan arrow to turn short sequence. \n\n\nTap to start.";
    }
}
