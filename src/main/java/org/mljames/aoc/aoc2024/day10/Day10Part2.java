package org.mljames.aoc.aoc2024.day10;

import org.mljames.aoc.PuzzleInputReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Day10Part2
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Day10Part2.class);

    public static void main(String[] args)
    {
        final long start = System.currentTimeMillis();

        final List<String> input = PuzzleInputReader.readInput("aoc2024/day10/part2/puzzle_input.txt");

        final int width = input.getFirst().length();
        final int height = input.size();

        final Counter counter = new Counter();
        for (int j = 0; j < height; j++)
        {
            for (int i = 0; i < width; i++)
            {
                if (Character.getNumericValue(input.get(j).charAt(i)) == 0)
                {
                    findDistinctHikingTrails(input, j, i, height, width, 0, counter);
                }
            }
        }

        LOGGER.info("The total score for all of trail heads is: {}, calculated in {}ms.", counter.count, System.currentTimeMillis() - start);
    }

    private static void findDistinctHikingTrails(
            final List<String> input,
            final int j,
            final int i,
            final int height,
            final int width,
            final int currentValue,
            final Counter counter)
    {
        move(input, j, i + 1, height, width, currentValue, counter);
        move(input, j, i - 1, height, width, currentValue, counter);
        move(input, j + 1, i, height, width, currentValue, counter);
        move(input, j - 1, i, height, width, currentValue, counter);
    }

    private static void move(
            final List<String> input,
            final int newY,
            final int newX,
            final int height,
            final int width,
            final int currentValue,
            final Counter counter)
    {
        if (positionWithinBounds(newY, newX, height, width))
        {
            int move = Character.getNumericValue(input.get(newY).charAt(newX));
            if (move == currentValue + 1)
            {
                if (move == 9)
                {
                    counter.increment();
                }
                else
                {
                    findDistinctHikingTrails(input, newY, newX, height, width, move, counter);
                }
            }
        }
    }

    private static boolean positionWithinBounds(final int j, final int i, final int height, final int width)
    {
        return j >= 0 && j < height && i >= 0 && i < width;
    }

    private static class Counter
    {
        int count = 0;

        public void increment()
        {
            count = count + 1;
        }
    }
}
