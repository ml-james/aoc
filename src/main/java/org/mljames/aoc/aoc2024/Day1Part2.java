package org.mljames.aoc.aoc2024;

import org.mljames.aoc.PuzzleInputReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day1Part2
{
    public static void main(String[] args)
    {
        final List<String[]> input = PuzzleInputReader.readInput("2024/day1/part2/puzzle_input.txt");

        final List<Integer> listA = new ArrayList<>();
        final Map<Integer, Integer> listBValueOccurrences = new HashMap<>();
        for (final String[] row : input)
        {
            listA.add(Integer.parseInt(row[0]));

            final int listBValue = Integer.parseInt(row[1]);
            if (listBValueOccurrences.containsKey(listBValue))
            {
                listBValueOccurrences.put(listBValue, listBValueOccurrences.get(listBValue) + 1);
            }
            else
            {
                listBValueOccurrences.put(listBValue, 1);
            }
        }

        int similarity = 0;
        for (final Integer value : listA)
        {
            similarity += value * listBValueOccurrences.getOrDefault(value, 0);
        }

        System.out.printf("Similarity between lists is: %s%n", similarity);
    }
}
