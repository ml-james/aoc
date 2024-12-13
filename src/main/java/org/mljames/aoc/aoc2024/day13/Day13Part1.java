package org.mljames.aoc.aoc2024.day13;

import org.mljames.aoc.PuzzleInputReader;
import org.mljames.aoc.aoc2024.day12.Day12Part1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day13Part1
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Day12Part1.class);

    private static final Pattern getButtonXValue = Pattern.compile("X\\+([0-9]*),");
    private static final Pattern getButtonYValue = Pattern.compile("Y\\+([0-9]*)$");
    private static final Pattern getPrizeXValue = Pattern.compile("X=([0-9]*),");
    private static final Pattern getPrizeYValue = Pattern.compile("Y=([0-9]*)");

    public static void main(String[] args)
    {
        final long start = System.currentTimeMillis();

        final List<String> input = PuzzleInputReader.readInputAsStrings("aoc2024/day13/part1/puzzle_input.txt");

        final int clawMachines = input.size() / 3;

        int tokenSpent = 0;
        for (int i = 0; i < clawMachines; i++)
        {
            final int x1 = extractInt(getButtonXValue.matcher(input.get(i * 3)));
            final int y1 = extractInt(getButtonYValue.matcher(input.get(i * 3)));
            final int x2 = extractInt(getButtonXValue.matcher(input.get(i * 3 + 1)));
            final int y2 = extractInt(getButtonYValue.matcher(input.get(i * 3 + 1)));
            final int x3 = extractInt(getPrizeXValue.matcher(input.get(i * 3 + 2)));
            final int y3 = extractInt(getPrizeYValue.matcher(input.get(i * 3 + 2)));

            final int bNumerator = (x3 * y1) - (x1 * y3);
            final int bDenominator = (x2 * y1) - (x1 * y2);

            if (bDenominator != 0 && bNumerator % bDenominator == 0)
            {
                final int b = bNumerator / bDenominator;
                final int a = (y3 - b * y2) / y1;
                tokenSpent += (b + a * 3);
            }
        }

        LOGGER.info("The fewest tokens you would have to spend to win all possible prizes is equal to: {}, calculated in {}ms.", tokenSpent, System.currentTimeMillis() - start);
    }

    private static int extractInt(final Matcher matcher)
    {
        if (!matcher.find())
        {
            throw new RuntimeException("Struggled parsing the input!!");
        }
        return Integer.parseInt(matcher.group(1));
    }
}
