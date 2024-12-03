package org.mljames.aoc.aoc2024.day2;

import org.mljames.aoc.PuzzleInputReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Day2Part2
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Day2Part2.class);
    public static final int TOLERANCE = 3;

    public static void main(String[] args)
    {
        final List<List<String>> input = PuzzleInputReader.readInput("aoc2024/day2/part2/puzzle_input.txt", Optional.of("\\s{1}"));

        int tolerablySafeCount = 0;
        for (final List<String> row : input)
        {
            if (isListTolerablySafe(row))
            {
                tolerablySafeCount += 1;
            }
        }

        LOGGER.info("The number of reports that are tolerably safe is equal to: {}", tolerablySafeCount);
    }

    private static boolean isListTolerablySafe(final List<String> row)
    {
        for (int i = 0; i < row.size(); i++)
        {
            final List<String> rowCopy = new ArrayList<>(row);
            // shallow copy is fine here
            rowCopy.remove(i);

            if (isListSafe(rowCopy))
            {
                return true;
            }
        }
        return false;
    }

    private static boolean isListSafe(final List<String> row)
    {
        int comparisonScoreTotal = 0;
        for (int i = 0; i < row.size() - 1; i++)
        {
            comparisonScoreTotal += compareLevels(Integer.parseInt(row.get(i)), Integer.parseInt(row.get(i + 1))).score;
            if (Math.abs(comparisonScoreTotal) != i + 1)
            {
                return false;
            }
        }
        return true;
    }

    private static LevelDelta compareLevels(final Integer level1, final Integer level2)
    {
        if (level1 > level2)
        {
            if (level1 - level2 > TOLERANCE)
            {
                return LevelDelta.INTOLERABLE_INCREASE;
            }
            else
            {
                return LevelDelta.TOLERABLE_INCREASE;
            }
        }
        if (level2 > level1)
        {
            if (level2 - level1 > TOLERANCE)
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

        private final int score;

        LevelDelta(final int score)
        {
            this.score = score;
        }
    }
}
