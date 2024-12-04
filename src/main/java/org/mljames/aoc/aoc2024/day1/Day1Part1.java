package org.mljames.aoc.aoc2024.day1;

import org.mljames.aoc.PuzzleInputReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Day1Part1
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Day1Part1.class);

    public static void main(String[] args)
    {
        final List<List<String>> input = PuzzleInputReader.readInput("aoc2024/day1/part1/puzzle_input.txt","\\s{3}");

        final List<Integer> listA = new ArrayList<>();
        final List<Integer> listB = new ArrayList<>();
        for (final List<String> row : input)
        {
            listA.add(Integer.parseInt(row.get(0)));
            listB.add(Integer.parseInt(row.get(1)));
        }

        final List<Integer> sortedListA = listA.stream().sorted().toList();
        final List<Integer> sortedListB = listB.stream().sorted().toList();

        assert sortedListA.size() == sortedListB.size();

        int distance = 0;
        for (int i = 0; i < sortedListA.size(); i++)
        {
            distance += Math.abs(sortedListA.get(i) - sortedListB.get(i));
        }

        LOGGER.info("Distance between lists is: {}", distance);
    }
}
