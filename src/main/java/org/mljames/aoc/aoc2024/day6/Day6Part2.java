package org.mljames.aoc.aoc2024.day6;

import org.mljames.aoc.PuzzleInputReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class Day6Part2
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Day6Part2.class);

    public static void main(String[] args)
    {
        final List<String> input = PuzzleInputReader.readInput("aoc2024/day6/part1/puzzle_input.txt");

        final int width = input.getFirst().length();
        final int height = input.size();

        final Grid grid = Grid.createGrid(input, height, width);
        final Coordinate guardsStartingCoordinate = grid.currentCoordinate;

        final List<Position> guardsVisitedPositions = getGuardsDistinctPositionsInPath(grid);

        int potentialObstacles = 0;
        for (int i = 1; i < guardsVisitedPositions.size(); i++)
        {
            final Grid gridUnderInspection = Grid.createGrid(input, height, width);
            final Position potentialObstaclePosition = guardsVisitedPositions.get(i);

            if (!potentialObstaclePosition.equals(guardsStartingCoordinate.getPosition()))
            {
                gridUnderInspection.insertObstacle(potentialObstaclePosition);

                final Set<Coordinate> visitedCoordinates = new HashSet<>();
                Optional<Coordinate> currentCoordinate = gridUnderInspection.move();
                while (currentCoordinate.isPresent())
                {
                    visitedCoordinates.add(currentCoordinate.get());
                    currentCoordinate = gridUnderInspection.move();
                    if (currentCoordinate.isPresent() && visitedCoordinates.contains(currentCoordinate.get()))
                    {
                        potentialObstacles += 1;
                        currentCoordinate = Optional.empty();
                    }
                }
            }
        }

        LOGGER.info("The number of possible obstructions are: {}.", potentialObstacles);
    }

    private static List<Position> getGuardsDistinctPositionsInPath(final Grid grid)
    {
        final List<Coordinate> visitedCoordinates = new ArrayList<>();
        visitedCoordinates.add(grid.currentCoordinate);
        Optional<Coordinate> currentCoordinate = grid.move();
        while (currentCoordinate.isPresent())
        {
            visitedCoordinates.add(currentCoordinate.get());
            currentCoordinate = grid.move();
        }
        
        return visitedCoordinates
                .stream()
                .map(c -> c.position)
                .distinct()
                .collect(Collectors.toList());
    }

    private static final class Grid
    {
        private final char[][] grid;
        private final int height;
        private final int width;

        private Coordinate currentCoordinate;

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

        private Grid(final char[][] grid, final int height, final int width, final Coordinate currentCoordinate)
        {
            this.grid = grid;
            this.height = height;
            this.width = width;
            this.currentCoordinate = currentCoordinate;
        }

        private boolean isPositionTerminal(final Coordinate coordinate)
        {
            switch (coordinate.direction)
            {
                case UP ->
                {
                    return coordinate.getY() == 0;
                }
                case RIGHT ->
                {
                    return coordinate.getX() + 1 == width;
                }
                case DOWN ->
                {
                    return coordinate.getY() + 1 == height;
                }
                case LEFT ->
                {
                    return coordinate.getX() == 0;
                }
                default -> throw new RuntimeException("Unrecognised direction!!");
            }
        }

        private Optional<Coordinate> move()
        {
            if (currentCoordinate.direction.equals(Direction.UP))
            {
                if (isPositionTerminal(currentCoordinate))
                {
                    return Optional.empty();
                }
                else
                {
                    if (isPositionObstructed(currentCoordinate.getY() - 1, currentCoordinate.getX()))
                    {
                        currentCoordinate = new Coordinate(currentCoordinate.getY(), currentCoordinate.getX(), Direction.RIGHT);
                    }
                    else
                    {
                        currentCoordinate = new Coordinate(currentCoordinate.getY() - 1, currentCoordinate.getX(), Direction.UP);
                    }
                    return Optional.of(currentCoordinate);
                }
            }
            else if (currentCoordinate.direction.equals(Direction.RIGHT))
            {
                if (isPositionTerminal(currentCoordinate))
                {
                    return Optional.empty();
                }
                else
                {
                    if (isPositionObstructed(currentCoordinate.getY(), currentCoordinate.getX() + 1))
                    {
                        currentCoordinate = new Coordinate(currentCoordinate.getY(), currentCoordinate.getX(), Direction.DOWN);
                    }
                    else
                    {
                        currentCoordinate = new Coordinate(currentCoordinate.getY(), currentCoordinate.getX() + 1, Direction.RIGHT);
                    }
                    return Optional.of(currentCoordinate);
                }
            }
            else if (currentCoordinate.direction.equals(Direction.DOWN))
            {
                if (isPositionTerminal(currentCoordinate))
                {
                    return Optional.empty();
                }
                else
                {
                    if (isPositionObstructed(currentCoordinate.getY() + 1, currentCoordinate.getX()))
                    {
                        currentCoordinate = new Coordinate(currentCoordinate.getY(), currentCoordinate.getX(), Direction.LEFT);
                    }
                    else
                    {
                        currentCoordinate = new Coordinate(currentCoordinate.getY() + 1, currentCoordinate.getX(), Direction.DOWN);
                    }
                    return Optional.of(currentCoordinate);
                }
            }
            else if (currentCoordinate.direction.equals(Direction.LEFT))
            {
                if (isPositionTerminal(currentCoordinate))
                {
                    return Optional.empty();
                }
                else
                {
                    if (isPositionObstructed(currentCoordinate.getY(), currentCoordinate.getX() - 1))
                    {
                        currentCoordinate = new Coordinate(currentCoordinate.getY(), currentCoordinate.getX(), Direction.UP);
                    }
                    else
                    {
                        currentCoordinate = new Coordinate(currentCoordinate.getY(), currentCoordinate.getX() - 1, Direction.LEFT);
                    }
                    return Optional.of(currentCoordinate);
                }
            }
            else
            {
                throw new RuntimeException("Unrecognised direction!!");
            }
        }

        private void insertObstacle(final Position position)
        {
            grid[position.y][position.x] = '#';
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

    private final static class Coordinate
    {
        private final Position position;
        private final Direction direction;

        private Coordinate(final int y, final int x, final Direction direction)
        {
            this.position = new Position(y, x);
            this.direction = direction;
        }

        public Position getPosition()
        {
            return position;
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
