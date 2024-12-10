package org.mljames.aoc.aoc2024.day3;

import org.mljames.aoc.PuzzleInputReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day3Part1
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Day3Part1.class);
    private static final String REGEX = "mul\\(([0-9]{1,3}),([0-9]{1,3})\\)";
    private static final Pattern PATTERN = Pattern.compile(REGEX);

    public static void main(String[] args)
    {
        final long start = System.currentTimeMillis();

        final List<String> input = PuzzleInputReader.readInput("aoc2024/day3/part1/puzzle_input.txt");

        final String inputConcat = String.join("", input);

        int sum = 0;
        final Matcher matcher = PATTERN.matcher(inputConcat);
        while (matcher.find())
        {
            final int lhs = Integer.parseInt(matcher.group(1));
            final int rhs = Integer.parseInt(matcher.group(2));
            sum += lhs * rhs;
        }

        LOGGER.info("Result of all of the multiplications is equal to: {}, calculated in {}ms.", sum, System.currentTimeMillis() - start);
    }
}
