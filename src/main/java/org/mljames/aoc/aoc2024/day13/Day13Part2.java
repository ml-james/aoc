package org.mljames.aoc.aoc2024.day13;

import org.mljames.aoc.PuzzleInputReader;
import org.mljames.aoc.aoc2024.day12.Day12Part1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day13Part2
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Day12Part1.class);

    private static final Pattern getButtonXValue = Pattern.compile("X\\+([0-9]*),");
    private static final Pattern getButtonYValue = Pattern.compile("Y\\+([0-9]*)$");
    private static final Pattern getPrizeXValue = Pattern.compile("X=([0-9]*),");
    private static final Pattern getPrizeYValue = Pattern.compile("Y=([0-9]*)");

    public static void main(String[] args)
    {
        final long start = System.currentTimeMillis();

        final List<String> input = PuzzleInputReader.readInputAsStrings("aoc2024/day13/part2/puzzle_input.txt");

        final int clawMachines = input.size() / 3;

        long tokensSpent = 0;
        for (int i = 0; i < clawMachines; i++)
        {
            final long x1 = extractLong(getButtonXValue.matcher(input.get(i * 3)));
            final long y1 = extractLong(getButtonYValue.matcher(input.get(i * 3)));
            final long x2 = extractLong(getButtonXValue.matcher(input.get(i * 3 + 1)));
            final long y2 = extractLong(getButtonYValue.matcher(input.get(i * 3 + 1)));
            final long x3 = extractLong(getPrizeXValue.matcher(input.get(i * 3 + 2))) + 10000000000000L;
            final long y3 = extractLong(getPrizeYValue.matcher(input.get(i * 3 + 2))) + 10000000000000L;

            final long bNumerator = (x3 * y1) - (x1 * y3);
            final long bDenominator = (x2 * y1) - (x1 * y2);

            if (bNumerator % bDenominator == 0)
            {
                final long b = bNumerator / bDenominator;
                final long a = (y3 - b * y2) / y1;
                tokensSpent += (b + a * 3);
            }
        }

        LOGGER.info("The fewest tokens you would have to spend to win all possible prizes is equal to: {}, calculated in {}ms.", tokensSpent, System.currentTimeMillis() - start);
    }

    private static long extractLong(final Matcher matcher)
    {
        if (!matcher.find())
        {
            throw new RuntimeException("Struggled parsing the input!!");
        }
        return Long.parseLong(matcher.group(1));
    }
}
