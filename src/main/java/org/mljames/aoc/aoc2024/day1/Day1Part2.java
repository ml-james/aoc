package org.mljames.aoc.aoc2024.day1;

import org.mljames.aoc.PuzzleInputReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Day1Part2
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Day1Part2.class);

    public static void main(String[] args)
    {
        final List<List<String>> input = PuzzleInputReader.readInput("aoc2024/day1/part2/puzzle_input.txt", Optional.of("\\s{3}"));

        final List<Integer> listA = new ArrayList<>();
        final Map<Integer, Integer> listBValueOccurrences = new HashMap<>();
        for (final List<String> row : input)
        {
            listA.add(Integer.parseInt(row.get(0)));

            final int listBValue = Integer.parseInt(row.get(1));
            listBValueOccurrences.compute(listBValue, (key, count) -> (count == null) ? 1 : count + 1);
        }

        int similarity = 0;
        for (final Integer value : listA)
        {
            similarity += value * listBValueOccurrences.getOrDefault(value, 0);
        }

        LOGGER.info("Similarity between lists is: {}", similarity);
    }
}
