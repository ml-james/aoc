package org.mljames.aoc.aoc2024.day4;

import org.mljames.aoc.PuzzleInputReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Day4Part1
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Day4Part1.class);

    private static final String XMAS = "XMAS";
    private static final String SAMX = "SAMX";

    public static void main(String[] args)
    {
        final long start = System.currentTimeMillis();

        final List<String> input = PuzzleInputReader.readInputAsStrings("aoc2024/day4/part1/puzzle_input.txt");

        final int width = input.getFirst().length();
        final int height = input.size();

        final char[][] grid = createGrid(input, height, width);

        int xmasCount = 0;
        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                if (x + XMAS.length() <= width)
                {
                    xmasCount += checkHorizontal(y, x, grid);
                }
                if (y + XMAS.length() <= height)
                {
                    xmasCount += checkVertical(y, x, grid);
                }
                if (y + XMAS.length() <= width && x + XMAS.length() <= height)
                {
                    xmasCount += checkDiagonals(y, x, grid);
                }
            }
        }

        LOGGER.info("The number of times xmas appears in the crossword is {}, calculated in {}ms.", xmasCount, System.currentTimeMillis() - start);
    }

    private static int checkDiagonals(final int y, final int x, final char[][] grid)
    {
        final String oneDiagonal = Character.toString(grid[y][x]) + grid[y + 1][x + 1] + grid[y + 2][x + 2] + grid[y + 3][x + 3];
        final String otherDiagonal = Character.toString(grid[y + 3][x]) + grid[y + 2][x + 1] + grid[y + 1][x + 2] + grid[y][x + 3];

        int diagonalsTotal = 0;
        if (oneDiagonal.equals(XMAS) || oneDiagonal.equals(SAMX))
        {
            diagonalsTotal += 1;
        }
        if (otherDiagonal.equals(XMAS) || otherDiagonal.equals(SAMX))
        {
            diagonalsTotal += 1;
        }
        return diagonalsTotal;
    }

    private static int checkVertical(
            final int y,
            final int x,
            final char[][] grid)
    {
        final String vertical = Character.toString(grid[y][x]) + grid[y + 1][x] + grid[y + 2][x] + grid[y + 3][x];

        if (vertical.equals(XMAS) || vertical.equals(SAMX))
        {
            return 1;
        }
        return 0;
    }

    private static int checkHorizontal(
            final int y,
            final int x,
            final char[][] grid)
    {
        final String horizontal = Character.toString(grid[y][x]) + grid[y][x + 1] + grid[y][x + 2] + grid[y][x + 3];

        if (horizontal.equals(XMAS) || horizontal.equals(SAMX))
        {
            return 1;
        }
        return 0;
    }

    private static char[][] createGrid(final List<String> input, final int height, final int width)
    {
        final char[][] grid = new char[height][width];

        for (int y = 0; y < height; y++)
        {
            final String row = input.get(y);

            for (int x = 0; x < width; x++)
            {
                grid[y][x] = row.charAt(x);
            }
        }
        return grid;
    }
}
