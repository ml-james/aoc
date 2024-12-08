package org.mljames.aoc.aoc2024.day8;

import org.mljames.aoc.PuzzleInputReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Day8Part2
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Day8Part1.class);
    private static final String EMPTY_SPACE = ".";

    public static void main(String[] args)
    {
        final List<String> input = PuzzleInputReader.readInput("aoc2024/day8/part2/puzzle_input.txt");

        final int width = input.getFirst().length();
        final int height = input.size();

        final Map<String, Grid> gridsByAntennaType = new HashMap<>();
        for (int i = 0; i < height; i++)
        {
            for (int j = 0; j < width; j++)
            {
                final String type = Character.toString(input.get(i).charAt(j));
                if (!type.equals(EMPTY_SPACE))
                {
                    gridsByAntennaType.computeIfAbsent(type, val -> new Grid(height, width));
                    gridsByAntennaType.get(type).addAntenna(new Position(i, j));
                }
            }
        }

        long numberOfAntinodes = gridsByAntennaType.values().stream().flatMap(x -> x.antinodes.stream()).distinct().count();

        LOGGER.info("The number of antinodes on the grid is equal to: {}.", numberOfAntinodes);
    }

    private static final class Grid
    {
        private final int height;
        private final int width;

        private final List<Position> antennas = new ArrayList<>();
        private final List<Position> antinodes = new ArrayList<>();

        public Grid(final int height, final int width)
        {
            this.height = height;
            this.width = width;
        }

        void addAntenna(final Position newAntenna)
        {
            if (!antennas.isEmpty())
            {
                for (final Position antenna : antennas)
                {
                    antinodes.addAll(calculateAntinodeCandidates(antenna, newAntenna));
                }
            }
            antennas.add(newAntenna);
        }

        private List<Position> calculateAntinodeCandidates(final Position antenna, final Position newAntenna)
        {
            int yDifference = antenna.y - newAntenna.y;
            int xDifference = antenna.x - newAntenna.x;

            antinodes.add(antenna);
            antinodes.add(newAntenna);

            final List<Position> antinodes = new ArrayList<>();

            Position candidateAntinodeHarmonic1 = new Position(antenna.y + yDifference, antenna.x + xDifference);
            while (positionWithinBounds(candidateAntinodeHarmonic1))
            {
                antinodes.add(candidateAntinodeHarmonic1);
                candidateAntinodeHarmonic1 = new Position(candidateAntinodeHarmonic1.y + yDifference, candidateAntinodeHarmonic1.x + xDifference);
            }

            Position candidateAntinodeHarmonic2 = new Position(newAntenna.y - yDifference, newAntenna.x - xDifference);
            while (positionWithinBounds(candidateAntinodeHarmonic2))
            {
                antinodes.add(candidateAntinodeHarmonic2);
                candidateAntinodeHarmonic2 = new Position(candidateAntinodeHarmonic2.y - yDifference, candidateAntinodeHarmonic2.x - xDifference);
            }

            return antinodes;
        }

        private boolean positionWithinBounds(final Position position)
        {
            return position.y >= 0 && position.y < height && position.x >= 0 && position.x < width;
        }
    }

    private static final class Position
    {
        private final int y;
        private final int x;

        private Position(final int y, final int x)
        {
            this.y = y;
            this.x = x;
        }

        @Override
        public boolean equals(final Object o)
        {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass())
            {
                return false;
            }
            final Position position = (Position) o;
            return y == position.y && x == position.x;
        }

        @Override
        public int hashCode()
        {
            return Objects.hash(y, x);
        }
    }
}
