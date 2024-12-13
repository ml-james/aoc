package org.mljames.aoc.aoc2024.day4;

import org.mljames.aoc.PuzzleInputReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Day4Part2
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Day4Part2.class);

    private static final String MAS = "MAS";
    private static final String SAM = "SAM";

    public static void main(String[] args)
    {
        final long start = System.currentTimeMillis();

        final List<String> input = PuzzleInputReader.readInputAsStrings("aoc2024/day4/part2/puzzle_input.txt");

        final int width = input.getFirst().length();
        final int height = input.size();

        final char[][] grid = createGrid(input, height, width);

        int xmasCount = 0;
        for (int x = 0; x < width; x++)
        {
            for (int y = 0; y < height; y++)
            {
                if (x + MAS.length() <= width && y + MAS.length() <= height)
                {
                    xmasCount += checkDiagonals(x, y, grid);
                }
            }
        }

        LOGGER.info("The number of times xmas appears in a cross in the crossword is {}, calculated in {}ms.", xmasCount, System.currentTimeMillis() - start);
    }

    private static int checkDiagonals(final int y, final int x, final char[][] grid)
    {
        final String oneDiagonal = Character.toString(grid[y][x]) + grid[y + 1][x + 1] + grid[y + 2][x + 2];
        final String otherDiagonal = Character.toString(grid[y + 2][x]) + grid[y + 1][x + 1] + grid[y][x + 2];

        int diagonalsTotal = 0;
        if ((oneDiagonal.equals(MAS) || oneDiagonal.equals(SAM)) && (otherDiagonal.equals(MAS) || otherDiagonal.equals(SAM)))
        {
            diagonalsTotal += 1;
        }
        return diagonalsTotal;
    }

    private static char[][] createGrid(final List<String> input, final int height, final int width)
    {
        final char[][] grid = new char[height][width];

        for (int y = 0; y < height; y++)
        {
            final String row = input.get(y);

            for (int x = 0; x < row.length(); x++)
            {
                grid[y][x] = row.charAt(x);
            }
        }
        return grid;
    }
}
