package org.mljames.aoc.aoc2024.day3;

import org.mljames.aoc.PuzzleInputReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day3Part2
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Day3Part2.class);

    private static final String REGEX_MUL = "mul\\(([0-9]{1,3}),([0-9]{1,3})\\)";
    private static final Pattern PATTERN_MUL = Pattern.compile(REGEX_MUL);

    private static final String DO = "do()";
    private static final String DONT = "don't()";

    public static void main(String[] args)
    {
        final long start = System.currentTimeMillis();

        final List<String> input = PuzzleInputReader.readInputAsStrings("aoc2024/day3/part2/puzzle_input.txt");

        final String inputConcat = String.join("", input);

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

        LOGGER.info("Result of all of the multiplications within enabled regions is equal to: {}, calculated in {}ms.", sum, System.currentTimeMillis() - start);
    }

    private static List<Region> findEnabledRegions(final String input)
    {
        boolean currentlyEnabled = true;
        int regionStartIndex = 0;
        final List<Region> enabledRegions = new ArrayList<>();
        while (regionStartIndex >= 0)
        {
            if (currentlyEnabled)
            {
                int maybeEnabledRegionSubstringEndIndex = input.substring(regionStartIndex).indexOf(DONT);
                if (maybeEnabledRegionSubstringEndIndex == -1)
                {
                    enabledRegions.add(new Region(regionStartIndex, input.length()));
                    regionStartIndex = -1;
                }
                else
                {
                    final int enabledRegionEndIndex = regionStartIndex + maybeEnabledRegionSubstringEndIndex;
                    enabledRegions.add(new Region(regionStartIndex, enabledRegionEndIndex));
                    regionStartIndex = enabledRegionEndIndex;
                    currentlyEnabled = false;
                }
            }
            else
            {
                final int maybeEnabledRegionSubstringStartIndex = input.substring(regionStartIndex).indexOf(DO);
                if (maybeEnabledRegionSubstringStartIndex == -1)
                {
                    regionStartIndex = -1;
                }
                regionStartIndex += maybeEnabledRegionSubstringStartIndex;
                currentlyEnabled = true;
            }
        }
        return enabledRegions;
    }

    private static final class Region
    {
        private final int start;
        private final int end;

        private Region(final int start, final int end)
        {
            this.start = start;
            this.end = end;
        }
    }
}
