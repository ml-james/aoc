package org.mljames.aoc.aoc2024.day11;

import org.mljames.aoc.PuzzleInputReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Day11Part2
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Day11Part2.class);

    public static final int BLINKS = 75;

    public static void main(String[] args)
    {
        final long start = System.currentTimeMillis();

        final List<Long> input = PuzzleInputReader.readInputAsLongs("aoc2024/day11/part2/puzzle_input.txt", " ").getFirst();

        final Map<Long, Long> stonesByFrequency = constructInitialStonesByFrequencyCollection(input);
        for (int i = 0; i < BLINKS; i++)
        {
            final Set<Long> stones = new HashSet<>(stonesByFrequency.keySet());
            final Map<Long, Long> nextBlinkStonesByFrequency = new HashMap<>();
            for (final long stone : stones)
            {
                final List<Long> afterBlinkStones = blink(stone);
                final long numberOfPreBlinkStones = stonesByFrequency.get(stone);
                for (final long afterBlinkStone : afterBlinkStones)
                {
                    nextBlinkStonesByFrequency.merge(afterBlinkStone, numberOfPreBlinkStones, Long::sum);
                }
            }
            stonesByFrequency.clear();
            stonesByFrequency.putAll(nextBlinkStonesByFrequency);
        }

        LOGGER.info("After blinking {} times there are {} stones, calculated in {}ms.", BLINKS, calculateTotalStones(stonesByFrequency), System.currentTimeMillis() - start);
    }

    private static Map<Long, Long> constructInitialStonesByFrequencyCollection(final List<Long> stones)
    {
        final Map<Long, Long> stonesByFrequency = new HashMap<>();
        for (final long stone : stones)
        {
            stonesByFrequency.merge(stone, 1L, Long::sum);
        }
        return stonesByFrequency;
    }

    private static List<Long> blink(final long stone)
    {
        final List<Long> afterBlink = new ArrayList<>();
        if (stone == 0)
        {
            afterBlink.add(1L);
        } else if (Long.toString(stone).length() % 2 == 0)
        {
            final String s = Long.toString(stone);
            int length = s.length();
            int splitIndex = length / 2;
            afterBlink.add(Long.parseLong(s.substring(0, splitIndex)));
            afterBlink.add(Long.parseLong(s.substring(splitIndex)));
        } else
        {
            afterBlink.add(stone * 2024L);
        }
        return afterBlink;
    }

    private static long calculateTotalStones(final Map<Long, Long> stonesByFrequency)
    {
        long total = 0;
        for (final long frequency : stonesByFrequency.values())
        {
            total += frequency;
        }
        return total;
    }
}
