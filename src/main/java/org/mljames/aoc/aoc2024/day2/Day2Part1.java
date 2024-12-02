package org.mljames.aoc.aoc2024.day2;

import org.mljames.aoc.PuzzleInputReader;
import org.mljames.aoc.aoc2024.day1.Day1Part1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.List;

public class Day2Part1
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Day1Part1.class);

    public static void main(String[] args)
    {
        final List<List<Integer>> input = PuzzleInputReader.readInput("aoc2024/day2/part1/puzzle_input.txt", "\\s{1}");

        int safeCount = 0;
        for (final List<Integer> row : input)
        {
            if (isListSafe(row))
            {
                safeCount += 1;
            }
        }

        LOGGER.info("The number of reports that are safe is equal to: {}", safeCount);
    }

    private static boolean isListSafe(final List<Integer> row)
    {
        final ReportLevelComparator comparator = new ReportLevelComparator();
        int comparatorRunningTotal = 0;
        for (int i = 0; i < row.size() - 1; i++)
        {
            comparatorRunningTotal += comparator.compare(row.get(i), row.get(i + 1));
            if (Math.abs(comparatorRunningTotal) != i + 1)
            {
                return false;
            }
        }
        return true;
    }

    private static class ReportLevelComparator implements Comparator<Integer>
    {
        @Override
        public int compare(final Integer o1, final Integer o2)
        {
            if (o1 > o2)
            {
                if ((o1 - o2) > 3)
                {
                    // it's bigger but too big
                    return 2;
                }
                else
                {
                    return 1;
                }
            }
            if (o2 > o1)
            {
                if ((o2 - o1) > 3)
                {
                    // it's bigger but too big
                    return -2;
                }
                else
                {
                    return -1;
                }
            }
            return 0;
        }
    }
}
