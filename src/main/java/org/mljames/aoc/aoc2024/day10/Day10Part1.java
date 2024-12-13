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

        final Map map = Map.createMap(input, height, width);

        LOGGER.info("The total score for all of trail heads is: {}, calculated in {}ms.", map.findDistinctTrailHeadSummits(), System.currentTimeMillis() - start);
    }

    private static final class Map
    {
        private final int[][] map;
        private final int height;
        private final int width;

        private static Map createMap(final List<String> input, final int height, final int width)
        {
            final int[][] map = new int[height][width];
            for (int y = 0; y < height; y++)
            {
                for (int x = 0; x < width; x++)
                {
                    map[y][x] = Character.getNumericValue(input.get(y).charAt(x));
                }
            }
            return new Map(map, height, width);
        }

        private Map(final int[][] map, final int height, final int width)
        {
            this.map = map;
            this.height = height;
            this.width = width;
        }

        private int getPoint(final int y, final int x)
        {
            return map[y][x];
        }

        private int findDistinctTrailHeadSummits()
        {
            final Counter counter = new Counter();
            for (int y = 0; y < height; y++)
            {
                for (int x = 0; x < width; x++)
                {
                    if (getPoint(y, x) == 0)
                    {
                        findNextPoint(y, x, 0, counter, new HashSet<>());
                    }
                }
            }
            return counter.count;
        }

        private void findNextPoint(
                final int y,
                final int x,
                final int value,
                final Counter counter,
                final Set<Position> positionsVisited)
        {
            move(y, x + 1, value, counter, positionsVisited);
            move(y, x - 1, value, counter, positionsVisited);
            move(y + 1, x, value, counter, positionsVisited);
            move(y - 1, x, value, counter, positionsVisited);
        }

        private void move(
                final int newY,
                final int newX,
                final int currentValue,
                final Counter counter,
                final Set<Position> positionsVisited)
        {
            if (positionWithinBounds(newY, newX))
            {
                int move = getPoint(newY, newX);
                if (move == currentValue + 1)
                {
                    if (move == 9)
                    {
                        final Position newPosition = new Position(newY, newX);
                        if (!positionsVisited.contains(newPosition))
                        {
                            counter.increment();
                            positionsVisited.add(newPosition);
                        }
                    }
                    else
                    {
                        findNextPoint(newY, newX, move, counter, positionsVisited);
                    }
                }
            }
        }

        private boolean positionWithinBounds(final int y, final int x)
        {
            return y >= 0 && y < height && x >= 0 && x < width;
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
