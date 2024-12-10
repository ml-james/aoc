package org.mljames.aoc.aoc2024.day10;

import org.mljames.aoc.PuzzleInputReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Day10Part1
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Day10Part1.class);

    public static void main(String[] args)
    {
        final long start = System.currentTimeMillis();

        final List<String> input = PuzzleInputReader.readInput("aoc2024/day10/part1/puzzle_input.txt");

        final int width = input.getFirst().length();
        final int height = input.size();

        final Counter counter = new Counter();
        for (int j = 0; j < height; j++)
        {
            for (int i = 0; i < width; i++)
            {
                if (Character.getNumericValue(input.get(j).charAt(i)) == 0)
                {
                    findDistinctTrailHeadSummits(input, j, i, width, height, 0, new HashSet<>(), counter);
                }
            }
        }

        LOGGER.info("The total score for all of trail heads is: {}, calculated in {}ms.", counter.count, System.currentTimeMillis() - start);
    }

    private static void findDistinctTrailHeadSummits(
            final List<String> input,
            final int j,
            final int i,
            final int width,
            final int height,
            final int currentValue,
            final Set<Position> positionsVisited,
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
                    if (!positionsVisited.contains(new Position(j, i + 1)))
                    {
                        positionsVisited.add(new Position(j, i + 1));
                        counter.increment();
                    }
                }
                else
                {
                    findDistinctTrailHeadSummits(input, j, i + 1, width, height, moveRight, positionsVisited, counter);
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
                    if (!positionsVisited.contains(new Position(j, i - 1)))
                    {
                        positionsVisited.add(new Position(j, i - 1));
                        counter.increment();
                    }
                }
                else
                {
                    findDistinctTrailHeadSummits(input, j, i - 1, width, height, moveLeft, positionsVisited, counter);
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
                    if (!positionsVisited.contains(new Position(j - 1, i)))
                    {
                        positionsVisited.add(new Position(j - 1, i));
                        counter.increment();
                    }
                }
                else
                {
                    findDistinctTrailHeadSummits(input, j - 1, i, width, height, moveUp, positionsVisited, counter);
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
                    if (!positionsVisited.contains(new Position(j + 1, i)))
                    {
                        positionsVisited.add(new Position(j + 1, i));
                        counter.increment();
                    }
                }
                else
                {
                    findDistinctTrailHeadSummits(input, j + 1, i, width, height, moveDown, positionsVisited, counter);
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

    private static final class Position
    {
        private final int x;
        private final int y;

        private Position(final int x, final int y)
        {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(final Object o)
        {
            if (this == o)
            {
                return true;
            }
            if (o == null || getClass() != o.getClass())
            {
                return false;
            }
            final Position position = (Position) o;
            return x == position.x && y == position.y;
        }

        @Override
        public int hashCode()
        {
            return Objects.hash(x, y);
        }
    }
}
