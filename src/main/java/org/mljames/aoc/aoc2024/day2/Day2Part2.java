package org.mljames.aoc.aoc2024.day2;

import org.mljames.aoc.PuzzleInputReader;
import org.mljames.aoc.aoc2024.day1.Day1Part1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class Day2Part2
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Day1Part1.class);

    public static void main(String[] args)
    {
        final List<List<Integer>> input = PuzzleInputReader.readInput("aoc2024/day2/part2/puzzle_input.txt", "\\s{1}");

        int safeCount = 0;
        for (final List<Integer> row : input)
        {
            if (isListTolerablySafe(row))
            {
                safeCount += 1;
            }
        }

        LOGGER.info("The number of reports that are safe is equal to: {}", safeCount);
    }

    private static boolean isListTolerablySafe(final List<Integer> row)
    {
        for (int i = 0; i < row.size(); i++)
        {
            final List<Integer> rowCopy = new ArrayList<>(row);
            // shallow copy is fine here
            rowCopy.remove(i);

            if (isListSafe(rowCopy))
            {
                return true;
            }
        }
        return false;
    }

    private static boolean isListSafe(final List<Integer> row)
    {
        int comparatorRunningTotal = 0;
        for (int i = 0; i < row.size() - 1; i++)
        {
            comparatorRunningTotal += compare(row.get(i), row.get(i + 1)).value;
            if (Math.abs(comparatorRunningTotal) != i + 1)
            {
                return false;
            }
        }
        return true;
    }

    private static LevelDelta compare(final Integer l1, final Integer l2)
    {
        if (l1 > l2)
        {
            if (l1 - l2 > 3)
            {
                return LevelDelta.INTOLERABLE_INCREASE;
            }
            else
            {
                return LevelDelta.TOLERABLE_INCREASE;
            }
        }
        if (l2 > l1)
        {
            if (l2 - l1 > 3)
            {
                return  LevelDelta.INTOLERABLE_DECREASE;
            }
            else
            {
                return LevelDelta.TOLERABLE_DECREASE;
            }
        }
        return LevelDelta.EQUIVALENT;
    }

    private enum LevelDelta
    {
        EQUIVALENT(0),
        TOLERABLE_INCREASE(1),
        TOLERABLE_DECREASE(-1),
        INTOLERABLE_INCREASE(Integer.MAX_VALUE),
        INTOLERABLE_DECREASE(Integer.MIN_VALUE);

        private final int value;

        LevelDelta(final int value)
        {
            this.value = value;
        }
    }
}
