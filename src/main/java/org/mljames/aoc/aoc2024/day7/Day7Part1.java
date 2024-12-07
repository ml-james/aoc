package org.mljames.aoc.aoc2024.day7;

import org.mljames.aoc.PuzzleInputReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day7Part1
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Day7Part1.class);

    private static final char MULTIPLY = '*';
    private static final char ADD = '+';

    public static void main(String[] args)
    {
        final List<String> input = PuzzleInputReader.readInput("aoc2024/day7/part1/puzzle_input.txt");

        long sumOfTestValues = 0L;
        for (final String equation : input)
        {
            final int splitIndex = equation.indexOf(":");
            final long testValue = Long.parseLong(equation.substring(0, splitIndex));
            final List<Long> numbers = Arrays.stream(equation.substring(splitIndex + 2).split(" ")).map(Long::parseLong).toList();

            final List<String> allOperatorCombinations = possibleOperatorCombinations(numbers.size() - 1);

            if (hasValidOperatorCombination(allOperatorCombinations, numbers, testValue))
            {
                sumOfTestValues += testValue;
            }
        }

        LOGGER.info("The number of valid combinations is equal to: {}.", sumOfTestValues);
    }

    private static boolean hasValidOperatorCombination(
            final List<String> allOperatorCombinations,
            final List<Long> numbers,
            final long testValue)
    {
        for (final String operatorCombination : allOperatorCombinations)
        {
            long runningTotal = numbers.getFirst();
            for (int j = 0; j < operatorCombination.length(); j++)
            {
                runningTotal = calculate(runningTotal, numbers.get(j + 1), operatorCombination.charAt(j));
            }
            if (runningTotal == testValue)
            {
                return true;
            }
        }
        return false;
    }

    private static long calculate(final long lhs, final Long rhs, final char operator)
    {
        if (operator == '*')
        {
            return lhs * rhs;
        }
        else if (operator == '+')
        {
            return lhs + rhs;
        }
        else
        {
            throw new RuntimeException("Unrecognised operator!!");
        }
    }

    private static List<String> possibleOperatorCombinations(final int length)
    {
        return getCombinations(new ArrayList<>(), length);
    }

    private static List<String> getCombinations(final List<String> combinations, final int length)
    {
        if (length == 1)
        {
            combinations.add(Character.toString(MULTIPLY));
            combinations.add(Character.toString(ADD));

            return combinations;
        }
        else
        {
            final List<String> newCombinations = new ArrayList<>();
            for (final String combination : getCombinations(combinations, length - 1))
            {
                newCombinations.add(MULTIPLY + combination);
                newCombinations.add(ADD + combination);
            }
            return newCombinations;
        }
    }
}
