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

        final List<String> input = PuzzleInputReader.readInput("aoc2024/day4/part1/puzzle_input.txt");

        final int width = input.getFirst().length();
        final int height = input.size();

        final char[][] grid = createGrid(input, height, width);

        int xmasCount = 0;
        for (int i = 0; i < height; i++)
        {
            for (int j = 0; j < width; j++)
            {
                if (j + XMAS.length() <= width)
                {
                    xmasCount += checkHorizontal(i, j, grid);
                }
                if (i + XMAS.length() <= height)
                {
                    xmasCount += checkVertical(i, j, grid);
                }
                if (i + XMAS.length() <= width && j + XMAS.length() <= height)
                {
                    xmasCount += checkDiagonals(i, j, grid);
                }
            }
        }

        LOGGER.info("The number of times xmas appears in the crossword is {}, calculated in {}ms.", xmasCount, System.currentTimeMillis() - start);
    }

    private static int checkDiagonals(final int i, final int j, final char[][] grid)
    {
        final String oneDiagonal = Character.toString(grid[i][j]) + grid[i + 1][j + 1] + grid[i + 2][j + 2] + grid[i + 3][j + 3];
        final String otherDiagonal = Character.toString(grid[i + 3][j]) + grid[i + 2][j + 1] + grid[i + 1][j + 2] + grid[i][j + 3];

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
            final int i,
            final int j,
            final char[][] grid)
    {
        final String vertical = Character.toString(grid[i][j]) + grid[i + 1][j] + grid[i + 2][j] + grid[i + 3][j];

        if (vertical.equals(XMAS) || vertical.equals(SAMX))
        {
            return 1;
        }
        return 0;
    }

    private static int checkHorizontal(
            final int i,
            final int j,
            final char[][] grid)
    {
        final String horizontal = Character.toString(grid[i][j]) + grid[i][j + 1] + grid[i][j + 2] + grid[i][j + 3];

        if (horizontal.equals(XMAS) || horizontal.equals(SAMX))
        {
            return 1;
        }
        return 0;
    }

    private static char[][] createGrid(final List<String> input, final int height, final int width)
    {
        final char[][] grid = new char[height][width];

        for (int i = 0; i < height; i++)
        {
            final String row = input.get(i);

            for (int j = 0; j < width; j++)
            {
                grid[i][j] = row.charAt(j);
            }
        }
        return grid;
    }
}
