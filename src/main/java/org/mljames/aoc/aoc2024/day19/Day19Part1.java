package org.mljames.aoc.aoc2024.day19;

import org.mljames.aoc.PuzzleInputReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day19Part1
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Day19Part1.class);

    public static void main(String[] args)
    {
        final long start = System.currentTimeMillis();

        final List<List<String>> patterns = PuzzleInputReader.readInputAsStrings("aoc2024/day19/part1/puzzle_input_1.txt", ", ");
        final List<String> designs = PuzzleInputReader.readInputAsStrings("aoc2024/day19/part1/puzzle_input_2.txt");

        final Set<String> allowedPatterns = new HashSet<>(
                patterns.stream()
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Expected a single line of towel patterns, got none!!")));

        final int longestAllowedPattern = allowedPatterns.stream().map(String::length).sorted(Comparator.naturalOrder()).toList().getLast();

        int validDesigns = 0;
        for (final String design : designs)
        {
            if (isValidDesign(design, allowedPatterns, longestAllowedPattern))
            {
                validDesigns += 1;
            }
        }

        LOGGER.info("The number of valid towel designs is: {}, calculated in {}ms.", validDesigns, System.currentTimeMillis() - start);
    }

    private static boolean isValidDesign(final String designToMatch, final Set<String> allowedPatterns, final int longestAllowedPattern)
    {
        if (designToMatch.isEmpty())
        {
            return true;
        }
        else
        {
            for (int c = Math.min(designToMatch.length(), longestAllowedPattern); c > 0; c--)
            {
                if (allowedPatterns.contains(designToMatch.substring(0, c)))
                {
                    boolean validDesign = isValidDesign(designToMatch.substring(c), allowedPatterns, longestAllowedPattern);
                    if (validDesign)
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
