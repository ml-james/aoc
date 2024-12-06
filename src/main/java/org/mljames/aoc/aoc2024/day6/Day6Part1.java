package org.mljames.aoc.aoc2024.day6;

import org.mljames.aoc.PuzzleInputReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Day6Part1
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Day6Part1.class);

    public static void main(String[] args)
    {
        final List<String> input = PuzzleInputReader.readInput("aoc2024/day6/part1/puzzle_input.txt");

        final int width = input.getFirst().length();
        final int height = input.size();

        final Grid grid = createGrid(input, height, width);

        Optional<Coordinate> currentCoordinate = grid.move();
        while (currentCoordinate.isPresent())
        {
            currentCoordinate = grid.move();
        }

        LOGGER.info("The number of distinct grid points visited by the guard is: {}.", grid.getNumberOfDistinctPositions());
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
                    startingCoordinate = new Coordinate(new Position(i, j), Direction.UP);
                }
                if (row.charAt(j) == '>')
                {
                    startingCoordinate = new Coordinate(new Position(i, j), Direction.RIGHT);
                }
                if (row.charAt(j) == '<')
                {
                    startingCoordinate = new Coordinate(new Position(i, j), Direction.LEFT);
                }
                if (row.charAt(j) == 'v')
                {
                    startingCoordinate = new Coordinate(new Position(i, j), Direction.DOWN);
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
        private final List<Coordinate> visited;

        private Coordinate currentCoordinate;

        private Grid(final char[][] grid, final int height, final int width, final Coordinate currentCoordinate)
        {
            this.grid = grid;
            this.height = height;
            this.width = width;
            this.currentCoordinate = currentCoordinate;
            this.visited = new ArrayList<>(Collections.singleton(currentCoordinate));
        }

        public Optional<Coordinate> move()
        {
            if (currentCoordinate.direction.equals(Direction.UP))
            {
                if (currentCoordinate.getY() > 0)
                {
                    if (grid[currentCoordinate.getY() - 1][currentCoordinate.getX()] != '#')
                    {
                        currentCoordinate = new Coordinate(currentCoordinate.getY() - 1, currentCoordinate.getX(), Direction.UP);
                        visited.add(currentCoordinate);
                        return Optional.of(currentCoordinate);
                    }
                    else if (grid[currentCoordinate.getY() - 1][currentCoordinate.getX()] == '#')
                    {
                        currentCoordinate = new Coordinate(currentCoordinate.getY(), currentCoordinate.getX(), Direction.RIGHT);
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
                if (currentCoordinate.getX() < width - 1)
                {
                    if (grid[currentCoordinate.getY()][currentCoordinate.getX() + 1] != '#')
                    {
                        currentCoordinate = new Coordinate(currentCoordinate.getY(), currentCoordinate.getX() + 1, Direction.RIGHT);
                        visited.add(currentCoordinate);
                        return Optional.of(currentCoordinate);
                    }
                    else if (grid[currentCoordinate.getY()][currentCoordinate.getX() + 1] == '#')
                    {
                        currentCoordinate = new Coordinate(currentCoordinate.getY(), currentCoordinate.getX(), Direction.DOWN);
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
                if (currentCoordinate.getY() < height - 1)
                {
                    if (grid[currentCoordinate.getY() + 1][currentCoordinate.getX()] != '#')
                    {
                        currentCoordinate = new Coordinate(currentCoordinate.getY() + 1, currentCoordinate.getX(), Direction.DOWN);
                        visited.add(currentCoordinate);
                        return Optional.of(currentCoordinate);
                    }
                    else if (grid[currentCoordinate.getY() + 1][currentCoordinate.getX()] == '#')
                    {
                        currentCoordinate = new Coordinate(currentCoordinate.getY(), currentCoordinate.getX(), Direction.LEFT);
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
                if (currentCoordinate.getX() > 0)
                {
                    if (grid[currentCoordinate.getY()][currentCoordinate.getX() - 1] != '#')
                    {
                        currentCoordinate = new Coordinate(currentCoordinate.getY(), currentCoordinate.getX() - 1, Direction.LEFT);
                        visited.add(currentCoordinate);
                        return Optional.of(currentCoordinate);
                    }
                    else if (grid[currentCoordinate.getY()][currentCoordinate.getX() - 1] == '#')
                    {
                        currentCoordinate = new Coordinate(currentCoordinate.getY(), currentCoordinate.getX(), Direction.UP);
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

        int getNumberOfDistinctPositions()
        {
            return visited
                    .stream()
                    .map(c -> c.position)
                    .distinct()
                    .toList()
                    .size();
        }
    }

    private enum Direction
    {
        UP,
        DOWN,
        RIGHT,
        LEFT;
    }

    private final static class Position
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
            if (this == o)
            {
                return true;
            }
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

    private final static class Coordinate
    {
        private final Position position;
        private final Direction direction;

        private Coordinate(final int y, final int x, final Direction direction)
        {
            this.position = new Position(y, x);
            this.direction = direction;
        }

        private Coordinate(final Position position, final Direction direction)
        {
            this.position = position;
            this.direction = direction;
        }

        public int getY()
        {
            return position.y;
        }

        public int getX()
        {
            return position.x;
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
            final Coordinate that = (Coordinate) o;
            return Objects.equals(position, that.position) && direction == that.direction;
        }

        @Override
        public int hashCode()
        {
            return Objects.hash(position, direction);
        }
    }
}
