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

        final List<String> input = PuzzleInputReader.readInputAsStrings("aoc2024/day10/part1/puzzle_input.txt");

        final int width = input.getFirst().length();
        final int height = input.size();

        final Counter counter = new Counter();
        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                if (Character.getNumericValue(input.get(y).charAt(x)) == 0)
                {
                    findDistinctTrailHeadSummits(input, y, x, height, width, 0, new HashSet<>(), counter);
                }
            }
        }

        LOGGER.info("The total score for all of trail heads is: {}, calculated in {}ms.", counter.count, System.currentTimeMillis() - start);
    }

    private static void findDistinctTrailHeadSummits(
            final List<String> input,
            final int y,
            final int x,
            final int height,
            final int width,
            final int currentValue,
            final Set<Position> trailHeadSummitsVisited,
            final Counter counter)
    {
        move(input, y, x + 1, height, width, currentValue, trailHeadSummitsVisited, counter);
        move(input, y, x - 1, height, width, currentValue, trailHeadSummitsVisited, counter);
        move(input, y + 1, x, height, width, currentValue, trailHeadSummitsVisited, counter);
        move(input, y - 1, x, height, width, currentValue, trailHeadSummitsVisited, counter);
    }

    private static void move(
            final List<String> input,
            final int newY,
            final int newX,
            final int height,
            final int width,
            final int currentValue,
            final Set<Position> trailHeadSummitsVisited,
            final Counter counter)
    {
        if (positionWithinBounds(newY, newX, height, width))
        {
            int move = Character.getNumericValue(input.get(newY).charAt(newX));
            if (move == currentValue + 1)
            {
                if (move == 9)
                {
                    final Position newPosition = new Position(newY, newX);
                    if (!trailHeadSummitsVisited.contains(newPosition))
                    {
                        counter.increment();
                        trailHeadSummitsVisited.add(newPosition);
                    }
                }
                else
                {
                    findDistinctTrailHeadSummits(input, newY, newX, height, width, move, trailHeadSummitsVisited, counter);
                }
            }
        }
    }

    private static boolean positionWithinBounds(final int y, final int x, final int height, final int width)
    {
        return y >= 0 && y < height && x >= 0 && x < width;
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
        private final int y;
        private final int x;

        private Position(final int y, final int x)
        {
            this.y = y;
            this.x = x;
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
