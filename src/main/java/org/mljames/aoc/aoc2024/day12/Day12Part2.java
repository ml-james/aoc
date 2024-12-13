package org.mljames.aoc.aoc2024.day12;

import org.mljames.aoc.PuzzleInputReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class Day12Part2
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Day12Part2.class);

    public static void main(String[] args)
    {
        final long start = System.currentTimeMillis();

        final List<String> input = PuzzleInputReader.readInputAsStrings("aoc2024/day12/part2/puzzle_input.txt");

        final int width = input.getFirst().length();
        final int height = input.size();

        final Farm farm = Farm.createFarm(input, height, width);

        final Set<Plot> seenPlots = new HashSet<>();
        final List<Region> regions = new ArrayList<>();
        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                final Plot currentPlot = farm.getPlot(y, x);
                if (!seenPlots.contains(currentPlot))
                {
                    regions.add(findRegion(currentPlot, farm, seenPlots, new Region()));
                }
            }
        }

        final int totalFencePrice = regions.stream().map(Region::getFencePrice).mapToInt(Integer::intValue).sum();

        LOGGER.info("The total price of all fencing all regions is equal to: {}, calculated in {}ms.", totalFencePrice, System.currentTimeMillis() - start);
    }

    private static final class Farm
    {
        private final Plot[][] farm;
        private final int height;
        private final int width;

        private static Farm createFarm(final List<String> input, final int height, final int width)
        {
            final Plot[][] farm = new Plot[height][width];
            for (int y = 0; y < height; y++)
            {
                for (int x = 0; x < width; x++)
                {
                    final String plant = Character.toString(input.get(y).charAt(x));
                    farm[y][x] = new Plot(y, x, plant, calculateBoundaryForPlot(input, y, x, height, width, plant));
                }
            }
            return new Farm(farm, height, width);
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
                boundarys.add(Boundary.BOTTOM);
            }
            if (isPositionABoundary(input, y - 1, x, height, width, plant))
            {
                boundarys.add(Boundary.TOP);
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

        private Farm(final Plot[][] farm, final int height, final int width)
        {
            this.farm = farm;
            this.height = height;
            this.width = width;
        }

        private Plot getPlot(final int y, final int x)
        {
            return farm[y][x];
        }

        private boolean isPositionWithinBounds(final int y, final int x)
        {
            return y >= 0 && y < height && x >= 0 && x < width;
        }
    }

    private static Region findRegion(
            final Plot startingPlot,
            final Farm farm,
            final Set<Plot> seenPlots,
            final Region region)
    {
        region.addPlot(startingPlot);
        seenPlots.add(startingPlot);

        nextPlot(startingPlot.y, startingPlot.x + 1, startingPlot, farm, seenPlots, region);
        nextPlot(startingPlot.y, startingPlot.x - 1, startingPlot, farm, seenPlots, region);
        nextPlot(startingPlot.y + 1, startingPlot.x, startingPlot, farm, seenPlots, region);
        nextPlot(startingPlot.y - 1, startingPlot.x, startingPlot, farm, seenPlots, region);

        return region;
    }

    private static void nextPlot(
            final int nextY,
            final int nextX,
            final Plot lastPlot,
            final Farm farm,
            final Set<Plot> seenPlots,
            final Region region)
    {
        if (farm.isPositionWithinBounds(nextY, nextX))
        {
            final Plot nextPlant = farm.getPlot(nextY, nextX);
            if (nextPlant.plant.equals(lastPlot.plant))
            {
                final Plot nextPlot = farm.getPlot(nextY, nextX);
                if (!seenPlots.contains(nextPlot))
                {
                    findRegion(nextPlot, farm, seenPlots, region);
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
            int sides = 0;

            final Map<Integer, List<Plot>> sortedPlotsByY = plots.stream().collect(Collectors.groupingBy(plot -> plot.y));
            final List<Integer> rowIndexes = sortedPlotsByY.keySet().stream().sorted().toList();
            for (final Integer rowIndex : rowIndexes)
            {
                final List<Plot> row = sortedPlotsByY.get(rowIndex);
                row.sort(Comparator.comparingInt((Plot p) -> p.x));

                Plot currentPlot = row.getFirst();
                sides += (int) currentPlot.boundarys.stream().filter(b -> b == Boundary.TOP || b == Boundary.BOTTOM).count();
                for (int j = 1; j < row.size(); j++)
                {
                    final Plot nextPlot = row.get(j);
                    if (!plotsAreAdjacent(currentPlot, nextPlot))
                    {
                        if (nextPlot.containsBoundary(Boundary.TOP))
                        {
                            sides += 1;
                        }
                        if (nextPlot.containsBoundary(Boundary.BOTTOM))
                        {
                            sides += 1;
                        }
                    }
                    else
                    {
                        if (!currentPlot.containsBoundary(Boundary.TOP) && nextPlot.containsBoundary(Boundary.TOP))
                        {
                            sides += 1;
                        }
                        if (!currentPlot.containsBoundary(Boundary.BOTTOM) && nextPlot.containsBoundary(Boundary.BOTTOM))
                        {
                            sides += 1;
                        }
                    }
                    currentPlot = nextPlot;
                }
            }

            final Map<Integer, List<Plot>> sortedPlotsByX = plots.stream().collect(Collectors.groupingBy(plot -> plot.x));
            final List<Integer> columnIndexes = sortedPlotsByX.keySet().stream().sorted().toList();
            for (final Integer columnIndex : columnIndexes)
            {
                final List<Plot> column = sortedPlotsByX.get(columnIndex);
                column.sort(Comparator.comparingInt((Plot p) -> p.y));

                Plot currentPlot = column.getFirst();
                sides += (int) currentPlot.boundarys.stream().filter(b -> b == Boundary.RIGHT || b == Boundary.LEFT).count();
                for (int j = 1; j < column.size(); j++)
                {
                    final Plot nextPlot = column.get(j);
                    if (!plotsAreAdjacent(currentPlot, nextPlot))
                    {
                        if (nextPlot.containsBoundary(Boundary.RIGHT))
                        {
                            sides += 1;
                        }
                        if (nextPlot.containsBoundary(Boundary.LEFT))
                        {
                            sides += 1;
                        }
                    }
                    else
                    {
                        if (!currentPlot.containsBoundary(Boundary.RIGHT) && nextPlot.containsBoundary(Boundary.RIGHT))
                        {
                            sides += 1;
                        }
                        if (!currentPlot.containsBoundary(Boundary.LEFT) && nextPlot.containsBoundary(Boundary.LEFT))
                        {
                            sides += 1;
                        }
                    }
                    currentPlot = nextPlot;
                }
            }

            return plots.size() * sides;
        }

        private boolean plotsAreAdjacent(final Plot plot1, final Plot plot2)
        {
            return (plot1.y == plot2.y && Math.abs(plot1.x - plot2.x) == 1) || (plot1.x == plot2.x && Math.abs(plot1.y - plot2.y) == 1);
        }

        private boolean plotsShareBoundary(final Plot plot1, final Plot plot2, final Boundary boundary)
        {
            return plotsAreAdjacent(plot1, plot2) && plot1.boundarys.contains(boundary) && plot2.boundarys.contains(boundary);
        }
    }

    private static final class Plot
    {
        private final int y;
        private final int x;
        private final String plant;
        private final Set<Boundary> boundarys;

        private Plot(final int y, final int x, final String plant, final Set<Boundary> boundarys)
        {
            this.y = y;
            this.x = x;
            this.plant = plant;
            this.boundarys = boundarys;
        }

        private boolean containsBoundary(final Boundary boundary)
        {
            return boundarys.contains(boundary);
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
            return y == plot.y && x == plot.x && Objects.equals(plant, plot.plant) && Objects.equals(boundarys, plot.boundarys);
        }

        @Override
        public int hashCode()
        {
            return Objects.hash(y, x, plant, boundarys);
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
