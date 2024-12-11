package org.mljames.aoc.aoc2024.day11;

import org.mljames.aoc.PuzzleInputReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Day11Part1
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Day11Part1.class);

    public static final int BLINKS = 25;

    public static void main(String[] args)
    {
        final long start = System.currentTimeMillis();

        final List<Long> input = PuzzleInputReader.readInputAsLongs("aoc2024/day11/part1/puzzle_input.txt", " ").getFirst();

        LinkedList<Long> stones = new LinkedList<>(input);
        for (int i = 0; i < BLINKS; i++)
        {
            int j = 0;
            while (j < stones.size())
            {
                final List<Long> afterBlinkStones = blink(stones.get(j));
                int insertionIndex = j;
                for (final long stone : afterBlinkStones)
                {
                    stones.add(insertionIndex + 1, stone);
                    insertionIndex = insertionIndex + 1;
                }
                stones.remove(j);
                j = j + afterBlinkStones.size();
            }
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