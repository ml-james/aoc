package org.mljames.aoc.aoc2024.day1;

import org.mljames.aoc.PuzzleInputReader;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Day1Part1
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Day1Part1.class);

    public static void main(String[] args)
    {
        final List<List<Integer>> input = PuzzleInputReader.readInput("aoc2024/day1/part1/puzzle_input.txt", "\\s{3}");

        final List<Integer> listA = new ArrayList<>();
        final List<Integer> listB = new ArrayList<>();
        for (final List<Integer> row : input)
        {
            listA.add(row.get(0));
            listB.add(row.get(1));
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
