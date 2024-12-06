package org.mljames.aoc.aoc2024.day6;

import org.mljames.aoc.PuzzleInputReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
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

        final Grid grid = Grid.createGrid(input, height, width);

        final int guardsDistinctPositionsInPath = getGuardsDistinctPositionsInPathCount(grid);

        LOGGER.info("The number of distinct grid points visited by the guard is: {}.", guardsDistinctPositionsInPath);
    }

    private static int getGuardsDistinctPositionsInPathCount(final Grid grid)
    {
        final List<Vector> visitedVectors = new ArrayList<>();
        visitedVectors.add(grid.currentVector);
        Optional<Vector> currentCoordinate = grid.move();
        while (currentCoordinate.isPresent())
        {
            visitedVectors.add(currentCoordinate.get());
            currentCoordinate = grid.move();
        }

        return visitedVectors
                .stream()
                .map(c -> c.position)
                .distinct()
                .toList()
                .size();
    }

    private static final class Grid
    {
        private final char[][] grid;
        private final int height;
        private final int width;

        private Vector currentVector;

        private static Grid createGrid(final List<String> input, final int height, final int width)
        {
            final char[][] grid = new char[height][width];
            Vector startingVector = null;
            for (int i = 0; i < height; i++)
            {
                final String row = input.get(i);

                for (int j = 0; j < width; j++)
                {
                    if (row.charAt(j) == '^')
                    {
                        startingVector = new Vector(i, j, Direction.UP);
                    }
                    if (row.charAt(j) == '>')
                    {
                        startingVector = new Vector(i, j, Direction.RIGHT);
                    }
                    if (row.charAt(j) == '<')
                    {
                        startingVector = new Vector(i, j, Direction.LEFT);
                    }
                    if (row.charAt(j) == 'v')
                    {
                        startingVector = new Vector(i, j, Direction.DOWN);
                    }
                    grid[i][j] = row.charAt(j);
                }
            }

            if (startingVector == null)
            {
                throw new RuntimeException("Should have found a starting coordinate!!");
            }
            return new Grid(grid, height, width, startingVector);
        }

        private Grid(final char[][] grid, final int height, final int width, final Vector currentVector)
        {
            this.grid = grid;
            this.height = height;
            this.width = width;
            this.currentVector = currentVector;
        }

        private boolean isPositionTerminal(final Vector vector)
        {
            switch (vector.direction)
            {
                case UP ->
                {
                    return vector.getY() == 0;
                }
                case RIGHT ->
                {
                    return vector.getX() + 1 == width;
                }
                case DOWN ->
                {
                    return vector.getY() + 1 == height;
                }
                case LEFT ->
                {
                    return vector.getX() == 0;
                }
                default -> throw new RuntimeException("Unrecognised direction!!");
            }
        }

        private Optional<Vector> move()
        {
            if (currentVector.direction.equals(Direction.UP))
            {
                if (isPositionTerminal(currentVector))
                {
                    return Optional.empty();
                }
                else
                {
                    if (isPositionObstructed(currentVector.getY() - 1, currentVector.getX()))
                    {
                        currentVector = new Vector(currentVector.getY(), currentVector.getX(), Direction.RIGHT);
                    }
                    else
                    {
                        currentVector = new Vector(currentVector.getY() - 1, currentVector.getX(), Direction.UP);
                    }
                    return Optional.of(currentVector);
                }
            }
            else if (currentVector.direction.equals(Direction.RIGHT))
            {
                if (isPositionTerminal(currentVector))
                {
                    return Optional.empty();
                }
                else
                {
                    if (isPositionObstructed(currentVector.getY(), currentVector.getX() + 1))
                    {
                        currentVector = new Vector(currentVector.getY(), currentVector.getX(), Direction.DOWN);
                    }
                    else
                    {
                        currentVector = new Vector(currentVector.getY(), currentVector.getX() + 1, Direction.RIGHT);
                    }
                    return Optional.of(currentVector);
                }
            }
            else if (currentVector.direction.equals(Direction.DOWN))
            {
                if (isPositionTerminal(currentVector))
                {
                    return Optional.empty();
                }
                else
                {
                    if (isPositionObstructed(currentVector.getY() + 1, currentVector.getX()))
                    {
                        currentVector = new Vector(currentVector.getY(), currentVector.getX(), Direction.LEFT);
                    }
                    else
                    {
                        currentVector = new Vector(currentVector.getY() + 1, currentVector.getX(), Direction.DOWN);
                    }
                    return Optional.of(currentVector);
                }
            }
            else if (currentVector.direction.equals(Direction.LEFT))
            {
                if (isPositionTerminal(currentVector))
                {
                    return Optional.empty();
                }
                else
                {
                    if (isPositionObstructed(currentVector.getY(), currentVector.getX() - 1))
                    {
                        currentVector = new Vector(currentVector.getY(), currentVector.getX(), Direction.UP);
                    }
                    else
                    {
                        currentVector = new Vector(currentVector.getY(), currentVector.getX() - 1, Direction.LEFT);
                    }
                    return Optional.of(currentVector);
                }
            }
            else
            {
                throw new RuntimeException("Unrecognised direction!!");
            }
        }

        private boolean isPositionObstructed(final int y, final int x)
        {
            return grid[y][x] == '#';
        }
    }

    private enum Direction
    {
        UP,
        DOWN,
        RIGHT,
        LEFT;
    }

    private final static class Vector
    {
        private final Position position;
        private final Direction direction;

        private Vector(final int y, final int x, final Direction direction)
        {
            this.position = new Position(y, x);
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
            final Vector that = (Vector) o;
            return Objects.equals(position, that.position) && direction == that.direction;
        }

        @Override
        public int hashCode()
        {
            return Objects.hash(position, direction);
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

    }
}
