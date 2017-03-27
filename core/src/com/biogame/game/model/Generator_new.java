package com.biogame.game.model;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public final class Generator_new {

    private static final Random RANDOM = new Random();
    private static final String ALPHABET = "AGCT";

    private static final int[] PARTITION_ONE = {1, 1, 1, 4};
    private static final int[] PARTITION_TWO = {2, 1, 1, 3};
    private static final int[] PARTITION_THREE = {2, 2, 2, 1};

    @NotNull
    public static String generateRead() {
        final StringBuilder read = new StringBuilder("");
        for (int i = 0; i < 5; i++) {
            char nucleotide = ALPHABET.charAt(RANDOM.nextInt(4));
            read.append(nucleotide);
        }
        return read.toString();
    }

    @NotNull
    public static String generateGenome(@NotNull Difficulty difficulty,
                                        @NotNull String read,
                                        @NotNull LetterPermutation letterPermutation,
                                        int subsublevel) {
        // reed preprocessing
        final StringBuilder read_projection = new StringBuilder(read);

        if (difficulty.substitutions > 0 && difficulty.substitutions < 3) {
            final List<Integer> ind_list = new ArrayList<Integer>();
            for (int i = 0; i < 5; i++) {
                ind_list.add(i);
            }
            Collections.shuffle(ind_list);
            final List<Integer> substitution_indexes = ind_list.subList(0, difficulty.substitutions);
            for (Integer ind : substitution_indexes) {
                final char toChange = read.charAt(ind);
                final StringBuilder variants = new StringBuilder(ALPHABET);
                variants.deleteCharAt(variants.indexOf(String.valueOf(toChange)));
                read_projection.setCharAt(ind, variants.charAt(RANDOM.nextInt(3)));
            }
        }


        final int number_of_sur_letters;
        switch (letterPermutation) {
            case EASY:
                number_of_sur_letters = 1;
                break;
            case MEDIUM:
                number_of_sur_letters = 2;
                break;
            case HARD:
                number_of_sur_letters = 3;
                break;
            case VERY_HARD:
                number_of_sur_letters = 4;
                break;
            default:
                number_of_sur_letters = 0;
        }

        final List<Integer> nucleotide_indexes = new ArrayList<Integer>();
        for (int i = 0; i < 4; i++) {
            nucleotide_indexes.add(i);
        }
        Collections.shuffle(nucleotide_indexes);
        final List sur_nucleotides_indexes = nucleotide_indexes.subList(0, number_of_sur_letters);
        final StringBuilder surrounding_letters_dict = new StringBuilder();

        for (int i = 0; i < sur_nucleotides_indexes.size(); i++) {
            int ind = (Integer) sur_nucleotides_indexes.get(i);
            surrounding_letters_dict.append(ALPHABET.charAt(ind));
        }

        char last_letter = surrounding_letters_dict.charAt(number_of_sur_letters - 1);
        while (surrounding_letters_dict.length() < 4) {
            surrounding_letters_dict.append(last_letter);
        }

        final int length_sur = 12 - read_projection.length();
        final int[] letter_counts;

        switch (subsublevel) {
            case 0:
                letter_counts = PARTITION_ONE;
                break;
            case 1:
                letter_counts = PARTITION_TWO;
                break;
            case 2:
                letter_counts = PARTITION_THREE;
                break;
            default:
                letter_counts = PARTITION_THREE;
        }

        final List<Character> surrounding_nucleotides_list = new ArrayList<Character>();

        for (int i = 0; i < 4; i ++) {
            for (int j = 0; j < letter_counts[i]; j++) {
                surrounding_nucleotides_list.add(surrounding_letters_dict.charAt(i));
            }
        }
        Collections.shuffle(surrounding_nucleotides_list);

        final StringBuilder surrounding_nucleotides = new StringBuilder();

        for(Character ch: surrounding_nucleotides_list)
        {
            surrounding_nucleotides.append(ch);
        }

        int pointer_to_insert = RANDOM.nextInt(length_sur - 1); //

        StringBuilder genome = new StringBuilder()
                .append(surrounding_nucleotides.substring(0, pointer_to_insert + 1))
                .append(read_projection)
                .append(surrounding_nucleotides.substring(pointer_to_insert + 1, length_sur));

        if (difficulty.reverse) {
            final StringBuilder reversed_genome = new StringBuilder();
            for (int i = 0; i < genome.length(); i++) {
                if (genome.charAt(i) == 'A') {
                    reversed_genome.append('T');
                } else if (genome.charAt(i) == 'G') {
                    reversed_genome.append('C');
                } else if (genome.charAt(i) == 'T') {
                    reversed_genome.append('A');
                } else if (genome.charAt(i) == 'C') {
                    reversed_genome.append('G');
                }
            }
            genome = reversed_genome;
            genome.reverse();
        }
        return genome.toString();
    }
}
