package org.mljames.aoc.aoc2024.day11;

import org.mljames.aoc.PuzzleInputReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.stream.Collectors;

public class Day11Part1
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Day11Part1.class);

    public static final int BLINKS = 25;

    public static void main(String[] args)
    {
        final long start = System.currentTimeMillis();

        final List<Long> input = PuzzleInputReader.readInputAsLongs("aoc2024/day11/part1/puzzle_input.txt", " ").getFirst();

        Queue<Long> stones = new ArrayDeque<>(input);
        for (int i = 0; i < BLINKS; i++)
        {
            int j = 0;
            int endIndex = stones.size();
            LOGGER.info("Blink {}.", i);
            while (j < endIndex)
            {
                final List<Long> afterBlinkStones = blink(Objects.requireNonNull(stones.poll()));
                stones.addAll(afterBlinkStones);
                j++;
            }
            LOGGER.info("Distinct numbers in my list {}.", new HashSet<>(stones).size());
        }

        LOGGER.info("After blinking {} times there are {} stones, calculated in {}ms.", BLINKS, stones.size(), System.currentTimeMillis() - start);
    }

    private static List<Long> blink(final long stone)
    {
        final List<Long> afterBlink = new ArrayList<>();
        if (stone == 0)
        {
            afterBlink.add(1L);
        }
        else if (Long.toString(stone).length() % 2 == 0)
        {
            final String s = Long.toString(stone);
            int length = s.length();
            int splitIndex = length / 2;
            afterBlink.add(Long.parseLong(s.substring(0, splitIndex)));
            afterBlink.add(Long.parseLong(s.substring(splitIndex)));
        }
        else
        {
            afterBlink.add(stone * 2024L);
        }
        return afterBlink;
    }
}
