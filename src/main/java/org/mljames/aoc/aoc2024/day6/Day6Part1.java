package org.mljames.aoc.aoc2024.day6;

import org.mljames.aoc.PuzzleInputReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class Day6Part1
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Day6Part1.class);

    public static void main(String[] args)
    {
        final List<String> input = PuzzleInputReader.readInput("aoc2024/day6/part1/puzzle_input.txt");

        final int width = input.getFirst().length();
        final int height = input.size();

        final Grid grid = createGrid(input, height, width);

        Optional<Coordinate> currentCoordinate = grid.nextCoordinate();
        while (currentCoordinate.isPresent())
        {
            currentCoordinate = grid.nextCoordinate();
        }

        LOGGER.info("The number of distinct grid points visited by the guard is: {}.", grid.visited.size());
    }

    private static Grid createGrid(final List<String> input, final int height, final int width)
    {
        final char[][] grid = new char[height][width];
        Coordinate startingCoordinate = null;
        for (int i = 0; i < height; i++)
        {
            final String row = input.get(i);

            for (int j = 0; j < width; j++)
            {
                if (row.charAt(j) == '^')
                {
                    startingCoordinate = new Coordinate(i, j, Direction.UP);
                }
                if (row.charAt(j) == '>')
                {
                    startingCoordinate = new Coordinate(i, j, Direction.RIGHT);
                }
                if (row.charAt(j) == '<')
                {
                    startingCoordinate = new Coordinate(i, j, Direction.LEFT);
                }
                if (row.charAt(j) == 'v')
                {
                    startingCoordinate = new Coordinate(i, j, Direction.DOWN);
                }
                grid[i][j] = row.charAt(j);
            }
        }

        if (startingCoordinate == null)
        {
            throw new RuntimeException("Should have found a starting coordinate!!");
        }
        return new Grid(grid, height, width, startingCoordinate);
    }

    private static final class Grid
    {
        private final char[][] grid;
        private final int height;
        private final int width;
        private final Set<Coordinate> visited;

        private Coordinate currentCoordinate;

        private Grid(final char[][] grid, final int height, final int width, final Coordinate currentCoordinate)
        {
            this.grid = grid;
            this.height = height;
            this.width = width;
            this.currentCoordinate = currentCoordinate;
            this.visited = new HashSet<>(List.of(currentCoordinate));
        }

        public Optional<Coordinate> nextCoordinate()
        {
            if (currentCoordinate.direction.equals(Direction.UP))
            {
                if (currentCoordinate.y > 0)
                {
                    if (grid[currentCoordinate.y - 1][currentCoordinate.x] != '#')
                    {
                        currentCoordinate = new Coordinate(currentCoordinate.y - 1, currentCoordinate.x, Direction.UP);
                        visited.add(currentCoordinate);
                        return Optional.of(currentCoordinate);
                    }
                    else if (grid[currentCoordinate.y - 1][currentCoordinate.x] == '#')
                    {
                        currentCoordinate = new Coordinate(currentCoordinate.y, currentCoordinate.x, Direction.RIGHT);
                        return Optional.of(currentCoordinate);
                    }
                    else
                    {
                        throw new RuntimeException("Unrecognised coordinate!!");
                    }
                }
                else
                {
                    return Optional.empty();
                }
            }
            else if (currentCoordinate.direction.equals(Direction.RIGHT))
            {
                if (currentCoordinate.x < width - 1)
                {
                    if (grid[currentCoordinate.y][currentCoordinate.x + 1] != '#')
                    {
                        currentCoordinate = new Coordinate(currentCoordinate.y, currentCoordinate.x + 1, Direction.RIGHT);
                        visited.add(currentCoordinate);
                        return Optional.of(currentCoordinate);
                    }
                    else if (grid[currentCoordinate.y][currentCoordinate.x + 1] == '#')
                    {
                        currentCoordinate = new Coordinate(currentCoordinate.y, currentCoordinate.x, Direction.DOWN);
                        return Optional.of(currentCoordinate);
                    }
                    else
                    {
                        throw new RuntimeException("Unrecognised coordinate!!");
                    }
                }
                else
                {
                    return Optional.empty();
                }
            }
            else if (currentCoordinate.direction.equals(Direction.DOWN))
            {
                if (currentCoordinate.y < height - 1)
                {
                    if (grid[currentCoordinate.y + 1][currentCoordinate.x] != '#')
                    {
                        currentCoordinate = new Coordinate(currentCoordinate.y + 1, currentCoordinate.x, Direction.DOWN);
                        visited.add(currentCoordinate);
                        return Optional.of(currentCoordinate);
                    }
                    else if (grid[currentCoordinate.y + 1][currentCoordinate.x] == '#')
                    {
                        currentCoordinate = new Coordinate(currentCoordinate.y, currentCoordinate.x, Direction.LEFT);
                        return Optional.of(currentCoordinate);
                    }
                    else
                    {
                        throw new RuntimeException("Unrecognised coordinate!!");
                    }
                }
                else
                {
                    return Optional.empty();
                }
            }
            else if (currentCoordinate.direction.equals(Direction.LEFT))
            {
                if (currentCoordinate.x > 0)
                {
                    if (grid[currentCoordinate.y][currentCoordinate.x - 1] != '#')
                    {
                        currentCoordinate = new Coordinate(currentCoordinate.y, currentCoordinate.x - 1, Direction.LEFT);
                        visited.add(currentCoordinate);
                        return Optional.of(currentCoordinate);
                    }
                    else if (grid[currentCoordinate.y][currentCoordinate.x - 1] == '#')
                    {
                        currentCoordinate = new Coordinate(currentCoordinate.y, currentCoordinate.x, Direction.UP);
                        return Optional.of(currentCoordinate);
                    }
                    else
                    {
                        throw new RuntimeException("Unrecognised coordinate!!");
                    }
                }
                else
                {
                    return Optional.empty();
                }
            }
            else
            {
                throw new RuntimeException("Unrecognised direction!!");
            }
        }
    }

    private enum Direction
    {
        UP,
        DOWN,
        RIGHT,
        LEFT;
    }

    private final static class Coordinate
    {
        private final int y;
        private final int x;
        private final Direction direction;

        private Coordinate(final int y, final int x, final Direction direction)
        {
            this.y = y;
            this.x = x;
            this.direction = direction;
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o)
            {
                return true;
            }
            if (o == null || getClass() != o.getClass())
            {
                return false;
            }
            final Coordinate that = (Coordinate) o;
            return y == that.y && x == that.x;
        }

        @Override
        public int hashCode()
        {
            return Objects.hash(y, x);
        }
    }
}
