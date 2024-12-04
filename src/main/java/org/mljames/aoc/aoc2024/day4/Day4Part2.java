package org.mljames.aoc.aoc2024.day4;

import org.mljames.aoc.PuzzleInputReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Day4Part2
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Day4Part1.class);

    private static final String MAS = "MAS";
    private static final String SAM = "SAM";

    public static void main(String[] args)
    {
        final List<String> input = PuzzleInputReader.readInput("aoc2024/day4/part2/puzzle_input.txt");

        final int xDimension = input.getFirst().length();
        final int yDimension = input.size();

        final char[][] grid = createGrid(input, xDimension, yDimension);

        int xmasCount = 0;
        for (int i = 0; i < yDimension; i++)
        {
            for (int j = 0; j < xDimension; j++)
            {
                if (i + 2 < xDimension && j + 2 < yDimension)
                {
                    xmasCount += checkDiagonals(i, j, grid);
                }
            }
        }

        LOGGER.info("The number of times xmas appears in a cross in the crossword is {}.", xmasCount);
    }

    private static int checkDiagonals(final int i, final int j, final char[][] grid)
    {
        final String oneDiagonal = Character.toString(grid[i][j]) + grid[i + 1][j + 1] + grid[i + 2][j + 2];
        final String otherDiagonal = Character.toString(grid[i + 2][j]) + grid[i + 1][j + 1] + grid[i][j + 2];

        int diagonalsTotal = 0;
        if ((oneDiagonal.equals(MAS) || oneDiagonal.equals(SAM)) && (otherDiagonal.equals(MAS) || otherDiagonal.equals(SAM)))
        {
            diagonalsTotal += 1;
        }
        return diagonalsTotal;
    }

    private static char[][] createGrid(final List<String> input, final int xDimension, final int yDimension)
    {
        final char[][] grid = new char[xDimension][yDimension];

        for (int i = 0; i < yDimension; i++)
        {
            final String row = input.get(i);

            for (int j = 0; j < row.length(); j++)
            {
                grid[i][j] = row.charAt(j);
            }
        }
        return grid;
    }
}
