package org.mljames.aoc.aoc2024.day5;

import org.mljames.aoc.PuzzleInputReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day5Part1
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Day5Part1.class);

    public static void main(String[] args)
    {
        final List<List<String>> pageOrderingRules = PuzzleInputReader.readInput("aoc2024/day5/part1/puzzle_input1.txt", "\\|");
        final List<List<Integer>> candidateUpdates = PuzzleInputReader.readInputAsInts("aoc2024/day5/part1/puzzle_input2.txt", ",");

        final Map<Integer, Set<Integer>> pageOrderingRulesByPage = new HashMap<>();
        for (final List<String> rule : pageOrderingRules)
        {
            final int key = Integer.parseInt(rule.get(0));
            final int value = Integer.parseInt(rule.get(1));

            pageOrderingRulesByPage.computeIfAbsent(key, k -> new HashSet<>()).add(value);
        }

        final List<List<Integer>> goodUpdates = new ArrayList<>();
        for (final List<Integer> update : candidateUpdates)
        {
            if (isGoodUpdate(update, pageOrderingRulesByPage))
            {
                goodUpdates.add(update);
            }
        }

        final int sum = goodUpdates.stream().mapToInt(Day5Part1::getMiddlePage).sum();

        LOGGER.info("The sum of the middle pages for all of the valid updates is: {}.", sum);
    }

    private static boolean isGoodUpdate(final List<Integer> update, final Map<Integer, Set<Integer>> pageOrderingRulesByPage)
    {
        for (int i = update.size() - 1; i > 0; i--)
        {
            final int page = update.get(i);

            if (pageOrderingRulesByPage.containsKey(page))
            {
                final Set<Integer> earlierPages = new HashSet<>(update.subList(0, i));
                final Set<Integer> pageOrderingRule = pageOrderingRulesByPage.get(page);

                if (pagesAppearInPageOrderingRule(earlierPages, pageOrderingRule))
                {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean pagesAppearInPageOrderingRule(final Set<Integer> earlierPages, final Set<Integer> pageOrderingRule)
    {
        return pageOrderingRule.stream().anyMatch(earlierPages::contains);
    }

    private static int getMiddlePage(final List<Integer> list)
    {
        if (list.size() % 2 == 0)
        {
            throw new RuntimeException("Can't really find the middle here I don't think...");
        }
        else
        {
            return list.get((list.size() - 1) / 2);
        }
    }
}
