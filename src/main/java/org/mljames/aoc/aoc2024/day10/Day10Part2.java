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
                    findDistinctHikingTrails(input, j, i, width, height, 0, counter);
                }
            }
        }

        LOGGER.info("The total score for all of trail heads is: {}, calculated in {}ms.", counter.count, System.currentTimeMillis() - start);
    }

    private static void findDistinctHikingTrails(
            final List<String> input,
            final int j,
            final int i,
            final int width,
            final int height,
            final int currentValue,
            final Counter counter)
    {
        int moveRight;
        if (i + 1 < width)
        {
            moveRight = Character.getNumericValue(input.get(j).charAt(i + 1));
            if (moveRight == currentValue + 1)
            {
                if (moveRight == 9)
                {
                    counter.increment();
                }
                else
                {
                    findDistinctHikingTrails(input, j, i + 1, width, height, moveRight, counter);
                }

            }
        }
        int moveLeft;
        if (i - 1 >= 0)
        {
            moveLeft = Character.getNumericValue(input.get(j).charAt(i - 1));
            if (moveLeft == currentValue + 1)
            {
                if (moveLeft == 9)
                {
                    counter.increment();
                }
                else
                {
                    findDistinctHikingTrails(input, j, i - 1, width, height, moveLeft, counter);
                }
            }

        }
        int moveUp;
        if (j - 1 >= 0)
        {
            moveUp = Character.getNumericValue(input.get(j - 1).charAt(i));
            if (moveUp == currentValue + 1)
            {
                if (moveUp == 9)
                {
                    counter.increment();
                }
                else
                {
                    findDistinctHikingTrails(input, j - 1, i, width, height, moveUp, counter);
                }

            }
        }
        int moveDown;
        if (j + 1 < height)
        {
            moveDown = Character.getNumericValue(input.get(j + 1).charAt(i));
            if (moveDown == currentValue + 1)
            {

                if (moveDown == 9)
                {
                    counter.increment();
                }
                else
                {
                    findDistinctHikingTrails(input, j + 1, i, width, height, moveDown, counter);
                }
            }
        }
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
