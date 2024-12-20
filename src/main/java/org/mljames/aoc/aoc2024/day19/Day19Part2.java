package org.mljames.aoc.aoc2024.day19;

import org.mljames.aoc.PuzzleInputReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

public class Day19Part2
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Day19Part2.class);

    public static void main(String[] args)
    {
        final long start = System.currentTimeMillis();

        final List<List<String>> patterns = PuzzleInputReader.readInputAsStrings("aoc2024/day19/part2/puzzle_input_1.txt", ", ");
        final List<String> designs = PuzzleInputReader.readInputAsStrings("aoc2024/day19/part2/puzzle_input_2.txt");

        final Set<String> allowedPatterns = new HashSet<>(
                patterns.stream()
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Expected a single line of towel patterns, got none!!")));

        final int longestAllowedPattern = allowedPatterns
                .stream()
                .map(String::length)
                .sorted()
                .toList()
                .getLast();

        long allCombinations = 0;
        for (final String design : designs)
        {
            final Map<String, Long> substringCombinationsCount = new HashMap<>();
            allCombinations += countCombinations(design, allowedPatterns, longestAllowedPattern, substringCombinationsCount);
        }

        LOGGER.info("The number of valid towel designs is: {}, calculated in {}ms.", allCombinations, System.currentTimeMillis() - start);
    }

    private static long countCombinations(
            final String design,
            final Set<String> allowedPatterns,
            final int longestAllowedPattern,
            final Map<String, Long> subStringCombinations)
    {
        final Queue<StringBuilder> queue = new PriorityQueue<>(Comparator.comparing(sb -> sb.length()));
        queue.add(new StringBuilder());

        long total = 0;
        while (!queue.isEmpty())
        {
            final StringBuilder candidate = queue.poll();
            final long candidateCombinations = subStringCombinations.getOrDefault(candidate.toString(), 1L);

            if (candidate.toString().equals(design))
            {
                total += candidateCombinations;
                continue;
            }

            final String substringToMatch = design.substring(candidate.length());
            for (int c = 1; c <= Math.min(substringToMatch.length(), longestAllowedPattern); c++)
            {
                final String newSubstringToMatch = substringToMatch.substring(0, c);
                if (allowedPatterns.contains(newSubstringToMatch))
                {
                    final StringBuilder newCandidate = new StringBuilder(candidate).append(newSubstringToMatch);
                    if (!subStringCombinations.containsKey(newCandidate.toString()))
                    {
                        queue.add(newCandidate);
                    }
                    subStringCombinations.merge(newCandidate.toString(), candidateCombinations, Long::sum);
                }
            }
        }
        return total;
    }
}
