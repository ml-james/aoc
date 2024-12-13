package org.mljames.aoc.aoc2024.day12;

import org.mljames.aoc.PuzzleInputReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Day12Part1
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Day12Part1.class);

    public static void main(String[] args)
    {
        final long start = System.currentTimeMillis();

        final List<String> input = PuzzleInputReader.readInputAsStrings("aoc2024/day12/part1/puzzle_input.txt");

        final int width = input.getFirst().length();
        final int height = input.size();

        final Map map = Map.createMap(input, height, width);

        final Set<Plot> seenPlots = new HashSet<>();
        final List<Region> regions = new ArrayList<>();
        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                final Plot currentPlot = map.getPlot(y, x);
                if (!seenPlots.contains(currentPlot))
                {
                    regions.add(findRegion(currentPlot, map, seenPlots, new Region()));
                }
            }
        }

        final int totalFencePrice = regions.stream().map(Region::getFencePrice).mapToInt(Integer::intValue).sum();

        LOGGER.info("The total price of all fencing all regions is equal to: {}, calculated in {}ms.", totalFencePrice, System.currentTimeMillis() - start);
    }

    private static final class Map
    {
        private final Plot[][] map;
        private final int height;
        private final int width;

        private static Map createMap(final List<String> input, final int height, final int width)
        {
            final Plot[][] map = new Plot[height][width];
            for (int y = 0; y < height; y++)
            {
                for (int x = 0; x < width; x++)
                {
                    final String plant = Character.toString(input.get(y).charAt(x));
                    map[y][x] = new Plot(y, x, plant, calculateBoundaryForPlot(input, y, x, height, width, plant));
                }
            }
            return new Map(map, height, width);
        }

        private static Set<Boundary> calculateBoundaryForPlot(
                final List<String> input,
                final int y,
                final int x,
                final int height,
                final int width,
                final String plant)
        {
            final Set<Boundary> boundarys = new HashSet<>();

            if (isPositionABoundary(input, y, x + 1, height, width, plant))
            {
                boundarys.add(Boundary.RIGHT);
            }
            if (isPositionABoundary(input, y, x - 1, height, width, plant))
            {
                boundarys.add(Boundary.LEFT);
            }
            if (isPositionABoundary(input, y + 1, x, height, width, plant))
            {
                boundarys.add(Boundary.TOP);
            }
            if (isPositionABoundary(input, y - 1, x, height, width, plant))
            {
                boundarys.add(Boundary.BOTTOM);
            }

            return boundarys;
        }

        private static boolean isPositionABoundary(
                final List<String> input,
                final int y,
                final int x,
                final int height,
                final int width,
                final String plant
        )
        {
            if (isPositionWithinBounds(y, x, height, width))
            {
                if (!Character.toString(input.get(y).charAt(x)).equals(plant))
                {
                    return true;
                }
                return false;
            }
            return true;
        }

        private static boolean isPositionWithinBounds(final int y, final int x, final int height, final int width)
        {
            return y >= 0 && y < height && x >= 0 && x < width;
        }

        private Map(final Plot[][] map, final int height, final int width)
        {
            this.map = map;
            this.height = height;
            this.width = width;
        }

        private Plot getPlot(final int y, final int x)
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

        nextPlot(startingPlot.y, startingPlot.x + 1, startingPlot, map, seenPlots, region);
        nextPlot(startingPlot.y, startingPlot.x - 1, startingPlot, map, seenPlots, region);
        nextPlot(startingPlot.y + 1, startingPlot.x, startingPlot, map, seenPlots, region);
        nextPlot(startingPlot.y - 1, startingPlot.x, startingPlot, map, seenPlots, region);

        return region;
    }

    private static void nextPlot(
            final int nextY,
            final int nextX,
            final Plot lastPlot,
            final Map map,
            final Set<Plot> seenPlots,
            final Region region)
    {
        if (map.isPositionWithinBounds(nextY, nextX))
        {
            final Plot nextPlant = map.getPlot(nextY, nextX);
            if (nextPlant.plant.equals(lastPlot.plant))
            {
                final Plot nextPlot = map.getPlot(nextY, nextX);
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

        private int getFencePrice()
        {
            int perimeter = 0;
            for (final Plot plot : plots)
            {
                perimeter += plot.boundaries.size();
            }
            return plots.size() * perimeter;
        }
    }

    private static final class Plot
    {
        private final int y;
        private final int x;
        private final String plant;
        private final Set<Boundary> boundaries;

        private Plot(final int y, final int x, final String plant, final Set<Boundary> boundaries)
        {
            this.y = y;
            this.x = x;
            this.plant = plant;
            this.boundaries = boundaries;
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
            return y == plot.y && x == plot.x && Objects.equals(plant, plot.plant) && Objects.equals(boundaries, plot.boundaries);
        }

        @Override
        public int hashCode()
        {
            return Objects.hash(y, x, plant, boundaries);
        }
    }

    private enum Boundary
    {
        TOP,
        RIGHT,
        BOTTOM,
        LEFT;
    }
}
