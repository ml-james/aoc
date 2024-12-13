package org.mljames.aoc.aoc2024.day12;

import org.mljames.aoc.PuzzleInputReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

public class Day12Part2
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Day12Part2.class);

    public static void main(String[] args)
    {
        final long start = System.currentTimeMillis();

        final List<String> input = PuzzleInputReader.readInputAsStrings("aoc2024/day12/part2/puzzle_input.txt");

        final int width = input.getFirst().length();
        final int height = input.size();

        final Map map = Map.createMap(input, height, width);

        final Set<Plot> seenPlots = new HashSet<>();
        final List<Region> regions = new ArrayList<>();
        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                final String plant = map.getPlant(y, x);
                final Plot currentPlot = new Plot(y, x, plant);
                if (!seenPlots.contains(currentPlot))
                {
                    regions.add(findRegion(currentPlot, map, seenPlots, new Region()));
                }
            }
        }

        final int totalFencePrice = regions.stream().map(region -> region.getFencePrice(map)).mapToInt(Integer::intValue).sum();

        LOGGER.info("The total price of all fencing all regions is equal to: {}, calculated in {}ms.", totalFencePrice, System.currentTimeMillis() - start);
    }

    private static final class Map
    {
        private final String[][] map;
        private final int height;
        private final int width;

        private static Map createMap(final List<String> input, final int height, final int width)
        {
            final String[][] map = new String[height][width];
            for (int y = 0; y < height; y++)
            {
                for (int x = 0; x < width; x++)
                {
                    map[y][x] = Character.toString(input.get(y).charAt(x));
                }
            }

            return new Map(map, height, width);
        }

        private Map(final String[][] map, final int height, final int width)
        {
            this.map = map;
            this.height = height;
            this.width = width;
        }

        private String getPlant(final int y, final int x)
        {
            return map[y][x];
        }

        private boolean isPositionWithinBounds(final int y, final int x)
        {
            return y >= 0 && y < height && x >= 0 && x < width;
        }
    }

    private static Region findRegion(
            final Plot startingPlot,
            final Map map,
            final Set<Plot> seenPlots,
            final Region region)
    {
        region.addPlot(startingPlot);
        seenPlots.add(startingPlot);

        nextPlot(startingPlot.y, startingPlot.x + 1, startingPlot.plant, map, seenPlots, region);
        nextPlot(startingPlot.y, startingPlot.x - 1, startingPlot.plant, map, seenPlots, region);
        nextPlot(startingPlot.y + 1, startingPlot.x, startingPlot.plant, map, seenPlots, region);
        nextPlot(startingPlot.y - 1, startingPlot.x, startingPlot.plant, map, seenPlots, region);

        return region;
    }

    private static void nextPlot(
            final int nextY,
            final int nextX,
            final String lastPlant,
            final Map map,
            final Set<Plot> seenPlots,
            final Region region)
    {
        if (map.isPositionWithinBounds(nextY, nextX))
        {
            final String nextPlant = map.getPlant(nextY, nextX);
            if (nextPlant.equals(lastPlant))
            {
                final Plot nextPlot = new Plot(nextY, nextX, nextPlant);
                if (!seenPlots.contains(nextPlot))
                {
                    findRegion(nextPlot, map, seenPlots, region);
                }
            }
        }
    }

    private static final class Region
    {
        private final List<Plot> plots = new ArrayList<>();

        private void addPlot(final Plot plot)
        {
            this.plots.add(plot);
        }

        private int getFencePrice(final Map map)
        {
            Plot plot = plots.getFirst();
            boolean recordXChange = false;
            boolean recordYChange = false;
            int sides = 4;
            for (int i = 1; i < plots.size(); i++)
            {
                final Plot nextPlot = plots.get(i);

                if (nextPlot.x != plot.x)
                {
                    if (!recordXChange)
                    {
                        recordYChange = true;
                    }
                    else
                    {
                        sides += 1;
                        recordYChange = true;
                        recordXChange = false;
                    }
                }
                else if (nextPlot.y != plot.y)
                {
                    if (!recordYChange)
                    {
                        recordXChange = true;
                    }
                    else
                    {
                        sides += 1;
                        recordXChange = true;
                        recordYChange = false;
                    }
                }
                plot = nextPlot;
            }
            return plots.size() * sides;
        }
    }

    private static final class Plot
    {
        private final int y;
        private final int x;
        private final String plant;

        private Plot(final int y, final int x, final String plant)
        {
            this.y = y;
            this.x = x;
            this.plant = plant;
        }

        @Override
        public boolean equals(final Object o)
        {
            if (this == o)
            {
                return true;
            }
            if (o == null || getClass() != o.getClass())
            {
                return false;
            }
            final Plot plot = (Plot) o;
            return y == plot.y && x == plot.x && Objects.equals(plant, plot.plant);
        }

        @Override
        public int hashCode()
        {
            return Objects.hash(y, x, plant);
        }
    }
}
