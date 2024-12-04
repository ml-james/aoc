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
    private static final Pattern PATTERN = Pattern.compile(REGEX_MUL);

    private static final String DO = "do()";
    private static final String DONT = "don't()";

    public static void main(String[] args)
    {
        final List<List<String>> input = PuzzleInputReader.readInput("aoc2024/day3/part2/puzzle_input.txt");

        final String inputConcat = input.stream().flatMap(Collection::stream).collect(Collectors.joining(""));

        final List<Integer> dos = findIndexesOf(DO, inputConcat);
        final List<Integer> donts = findIndexesOf(DONT, inputConcat);

        final List<Region> enabledRegions = findEnabledRegions(dos, donts, inputConcat.length());

        int sum = 0;
        for (final Region region : enabledRegions)
        {
            final Matcher matcher = PATTERN.matcher(inputConcat).region(region.start, region.end);

            while (matcher.find())
            {
                final int lhs = Integer.parseInt(matcher.group(1));
                final int rhs = Integer.parseInt(matcher.group(2));
                sum += lhs * rhs;
            }
        }

        LOGGER.info("Result of all of the multiplications within enabled regions is equal to: {}.", sum);
    }

    private static List<Integer> findIndexesOf(final String pattern, final String line)
    {
        int currentIndex = 0;
        final List<Integer> indexes = new ArrayList<>();
        while (currentIndex >= 0)
        {
            int indexOf = line.substring(currentIndex).indexOf(pattern);
            if (indexOf != -1)
            {
                currentIndex += indexOf;
                indexes.add(currentIndex);
                currentIndex = currentIndex + pattern.length();
            }
            else
            {
                currentIndex = -1;
            }
        }
        return indexes;
    }

    private static List<Region> findEnabledRegions(
            final List<Integer> doIndexes,
            final List<Integer> dontIndexes,
            final int lastIndex)
    {
        boolean currentlyEnabled = true;
        int regionStart = 0;
        final List<Region> enabledRegions = new ArrayList<>();
        while (regionStart >= 0)
        {
            if (currentlyEnabled)
            {
                int regionEnd = findNextNearestValue(regionStart, dontIndexes);
                if (regionEnd == -1)
                {
                    enabledRegions.add(new Region(regionStart, lastIndex));
                }
                else
                {
                    enabledRegions.add(new Region(regionStart, regionEnd));
                }
                regionStart = regionEnd;
                currentlyEnabled = false;
            }
            else
            {
                regionStart = findNextNearestValue(regionStart, doIndexes);
                currentlyEnabled = true;
            }
        }
        return enabledRegions;
    }

    private static int findNextNearestValue(final int targetValue, final List<Integer> values)
    {
        for (final int value : values)
        {
            if (value > targetValue)
            {
                return value;
            }
        }
        return -1;
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
