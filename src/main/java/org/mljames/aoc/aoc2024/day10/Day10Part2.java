package org.mljames.aoc.aoc2024.day10;

import org.mljames.aoc.PuzzleInputReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Day10Part2
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Day10Part2.class);

    public static void main(String[] args)
    {
        final long start = System.currentTimeMillis();

        final List<String> input = PuzzleInputReader.readInput("aoc2024/day10/part2/puzzle_input.txt");

        final int width = input.getFirst().length();
        final int height = input.size();

        final Grid grid = Grid.createGrid(input, height, width);

        int totalTrailHeads = 0;
        for (int j = 0; j < height; j++)
        {
            for (int i = 0; i < width; i++)
            {
                if (grid.grid[j][i] == 0)
                {
                    int trailHeads = grid.findTrailHeads(j, i, 0, new Counter());
                    totalTrailHeads += trailHeads;
                }
            }
        }

        LOGGER.info("The total score for all of trail heads is: {}, calculated in {}ms.", totalTrailHeads, System.currentTimeMillis() - start);
    }

    private static final class Grid
    {
        private final int[][] grid;
        private final int height;
        private final int width;

        private Grid(final int[][] grid, final int height, final int width)
        {
            this.grid = grid;
            this.height = height;
            this.width = width;
        }

        private static Grid createGrid(final List<String> input, final int height, final int width)
        {
            final int[][] grid = new int[height][width];
            for (int i = 0; i < height; i++)
            {
                for (int j = 0; j < width; j++)
                {
                    grid[i][j] = Integer.parseInt(Character.toString(input.get(i).charAt(j)));
                }
            }
            return new Grid(grid, height, width);
        }

        private int findTrailHeads(
                final int j,
                final int i,
                final int currentValue,
                final Counter counter)
        {
            int moveRight;
            if (i + 1 < width)
            {
                moveRight = grid[j][i + 1];
                if (moveRight == currentValue + 1)
                {
                    if (moveRight == 9)
                    {
                        counter.increment();
                    }
                    else
                    {
                        findTrailHeads(j, i + 1, moveRight, counter);
                    }
                }
            }
            int moveLeft;
            if (i - 1 >= 0)
            {
                moveLeft = grid[j][i - 1];

                if (moveLeft == currentValue + 1)
                {
                    if (moveLeft == 9)
                    {
                        counter.increment();
                    }
                    else
                    {
                        findTrailHeads(j, i - 1, moveLeft, counter);
                    }
                }
            }
            int moveUp;
            if (j - 1 >= 0)
            {
                moveUp = grid[j - 1][i];
                if (moveUp == currentValue + 1)
                {
                    if (moveUp == 9)
                    {
                        counter.increment();
                    } else
                    {
                        findTrailHeads(j - 1, i, moveUp, counter);
                    }
                }
            }
            int moveDown;
            if (j + 1 < height)
            {
                moveDown = grid[j + 1][i];
                if (moveDown == currentValue + 1)
                {
                    if (moveDown == 9)
                    {
                        counter.increment();
                    }
                    else
                    {
                        findTrailHeads(j + 1, i, moveDown, counter);
                    }
                }
            }
            return counter.count;
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
