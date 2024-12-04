package org.mljames.aoc.aoc2024.day3;

import org.mljames.aoc.PuzzleInputReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day3Part2
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Day3Part2.class);

    private static final String REGEX_MUL = "mul\\(([0-9]{1,3}),([0-9]{1,3})\\)";
    private static final Pattern PATTERN_MUL = Pattern.compile(REGEX_MUL);

    private static final String DO = "do()";
    private static final String DONT = "don't()";

    public static void main(String[] args)
    {
        final List<List<String>> input = PuzzleInputReader.readInput("aoc2024/day3/part2/puzzle_input.txt");

        final String inputConcat = input.stream().flatMap(Collection::stream).collect(Collectors.joining(""));

        final List<Region> enabledRegions = findEnabledRegions(inputConcat);

        int sum = 0;
        for (final Region region : enabledRegions)
        {
            final Matcher matcher = PATTERN_MUL.matcher(inputConcat).region(region.start, region.end);

            while (matcher.find())
            {
                final int lhs = Integer.parseInt(matcher.group(1));
                final int rhs = Integer.parseInt(matcher.group(2));
                sum += lhs * rhs;
            }
        }

        LOGGER.info("Result of all of the multiplications within enabled regions is equal to: {}.", sum);
    }

    private static List<Region> findEnabledRegions(final String input)
    {
        boolean currentlyEnabled = true;
        int enabledRegionStart = 0;
        final List<Region> enabledRegions = new ArrayList<>();
        while (enabledRegionStart >= 0)
        {
            if (currentlyEnabled)
            {
                int maybeEnabledRegionEnd = input.substring(enabledRegionStart).indexOf(DONT);
                if (maybeEnabledRegionEnd == -1)
                {
                    enabledRegions.add(new Region(enabledRegionStart, input.length()));
                    enabledRegionStart = -1;
                }
                else
                {
                    final int enableRegionEnd = enabledRegionStart + maybeEnabledRegionEnd;
                    enabledRegions.add(new Region(enabledRegionStart, enableRegionEnd));
                    enabledRegionStart += maybeEnabledRegionEnd;
                    currentlyEnabled = false;
                }
            }
            else
            {
                final int maybeEnableRegionStart = input.substring(enabledRegionStart).indexOf(DO);
                if (maybeEnableRegionStart == -1)
                {
                    enabledRegionStart = -1;
                }
                enabledRegionStart += maybeEnableRegionStart;
                currentlyEnabled = true;
            }
        }
        return enabledRegions;
    }

    private static final class Region
    {
        final int start;
        final int end;

        private Region(int start, int end)
        {
            this.start = start;
            this.end = end;
        }
    }

}
