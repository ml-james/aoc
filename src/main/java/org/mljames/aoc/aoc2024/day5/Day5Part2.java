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

public class Day5Part2
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Day5Part2.class);

    public static void main(String[] args)
    {
        final List<List<String>> pageOrderingRules = PuzzleInputReader.readInput("aoc2024/day5/part2/puzzle_input1.txt", "\\|");
        final List<List<Integer>> candidateUpdates = PuzzleInputReader.readInputAsInts("aoc2024/day5/part2/puzzle_input2.txt", ",");

        final Map<Integer, Set<Integer>> pageOrderingRulesByPage = new HashMap<>();
        for (final List<String> rule : pageOrderingRules)
        {
            final int key = Integer.parseInt(rule.get(0));
            final int value = Integer.parseInt(rule.get(1));

            pageOrderingRulesByPage.computeIfAbsent(key, k -> new HashSet<>()).add(value);
        }

        final List<List<Integer>> badUpdates = new ArrayList<>();
        for (final List<Integer> update : candidateUpdates)
        {
            if (!checkUpdate(update, pageOrderingRulesByPage))
            {
                badUpdates.add(update);
            }
        }

        final List<List<Integer>> reformedBadUpdates = new ArrayList<>();
        for (final List<Integer> update : badUpdates)
        {
            reformedBadUpdates.add(reformBadUpdate(update, pageOrderingRulesByPage));
        }

        final int sum = reformedBadUpdates.stream().mapToInt(Day5Part2::getMiddlePage).sum();

        LOGGER.info("The sum of the middle pages for all of the valid updates is: {}.", sum);
    }

    private static List<Integer> reformBadUpdate(final List<Integer> badUpdate, final Map<Integer, Set<Integer>> pageOrderingRulesByPage)
    {
        final Set<Integer> pagesToAllocate = new HashSet<>(badUpdate);
        final List<Integer> reformedUpdate = new ArrayList<>();
        for (int i = 0; i < badUpdate.size(); i++)
        {
            final int allocatedPage = allocatePage(badUpdate, pagesToAllocate, pageOrderingRulesByPage);
            pagesToAllocate.remove(allocatedPage);
            reformedUpdate.add(allocatedPage);
        }
        return reformedUpdate;
    }

    private static int allocatePage(
            final List<Integer> badUpdate,
            final Set<Integer> pagesToAllocate,
            final Map<Integer, Set<Integer>> pageOrderingRulesByPage)
    {
        for (final Integer page : badUpdate)
        {
            if (pagesToAllocate.contains(page))
            {
                if (canAllocatePage(page, pagesToAllocate, pageOrderingRulesByPage))
                {
                    return page;
                }
            }
        }
        throw new RuntimeException("Unable to allocate page, something has gone wrong...");
    }

    private static boolean canAllocatePage(
            final Integer page,
            final Set<Integer> pagesToAllocate,
            final Map<Integer, Set<Integer>> pageOrderingRulesByPage)
    {
        if (!pageOrderingRulesByPage.containsKey(page))
        {
            return true;
        }
        else
        {
            final Set<Integer> pageOrderingRule = pageOrderingRulesByPage.get(page);
            if (pagesDoNotAppearInPageOrderingRule(pagesToAllocate, pageOrderingRule))
            {
                return true;
            }
        }
        return false;
    }

    private static boolean checkUpdate(final List<Integer> update, final Map<Integer, Set<Integer>> pageOrderingRulesByPage)
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

    private static boolean pagesAppearInPageOrderingRule(final Set<Integer> pages, final Set<Integer> pageOrderingRule)
    {
        return pageOrderingRule.stream().anyMatch(pages::contains);
    }

    private static boolean pagesDoNotAppearInPageOrderingRule(final Set<Integer> pages, final Set<Integer> pageOrderingRule)
    {
        return pageOrderingRule.stream().noneMatch(pages::contains);
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
