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
        final long start = System.currentTimeMillis();
        
        final List<String> input = PuzzleInputReader.readInputAsStrings("aoc2024/day6/part1/puzzle_input.txt");

        final int width = input.getFirst().length();
        final int height = input.size();

        final Grid grid = Grid.createGrid(input, height, width);
        final Vector guardsStartingVector = grid.currentVector;

        final List<Position> guardsVisitedPositions = getGuardsDistinctPositionsInPath(grid);

        int potentialObstacles = 0;
        for (int i = 1; i < guardsVisitedPositions.size(); i++)
        {
            final Grid gridUnderInspection = Grid.createGrid(input, height, width);
            final Position potentialObstaclePosition = guardsVisitedPositions.get(i);

            if (positionsNotEqual(potentialObstaclePosition, guardsStartingVector.position))
            {
                if (isValidObstaclePositionForInfiniteLoop(gridUnderInspection, potentialObstaclePosition))
                {
                    potentialObstacles += 1;
                }
            }
        }

        LOGGER.info("The number of possible obstructions are: {}, calculated in {}ms.", potentialObstacles, System.currentTimeMillis() - start);
    }

    private static boolean positionsNotEqual(final Position potentialObstaclePosition, final Position guardsStartingVector)
    {
        return !potentialObstaclePosition.equals(guardsStartingVector);
    }

    private static boolean isValidObstaclePositionForInfiniteLoop(final Grid gridUnderInspection, final Position position)
    {
        gridUnderInspection.insertObstacle(position);

        final Set<Vector> visitedVectors = new HashSet<>();
        Optional<Vector> currentCoordinate = gridUnderInspection.move();
        while (currentCoordinate.isPresent())
        {
            visitedVectors.add(currentCoordinate.get());
            currentCoordinate = gridUnderInspection.move();
            if (currentCoordinate.isPresent() && visitedVectors.contains(currentCoordinate.get()))
            {
                return true;
            }
        }
        return false;
    }

    private static List<Position> getGuardsDistinctPositionsInPath(final Grid grid)
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
                .collect(Collectors.toList());
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
            for (int y = 0; y < height; y++)
            {
                final String row = input.get(y);

                for (int x = 0; x < width; x++)
                {
                    if (row.charAt(x) == '^')
                    {
                        startingVector = new Vector(x, y, Direction.UP);
                    }
                    if (row.charAt(x) == '>')
                    {
                        startingVector = new Vector(x, y, Direction.RIGHT);
                    }
                    if (row.charAt(x) == '<')
                    {
                        startingVector = new Vector(x, y, Direction.LEFT);
                    }
                    if (row.charAt(x) == 'v')
                    {
                        startingVector = new Vector(x, y, Direction.DOWN);
                    }
                    grid[y][x] = row.charAt(x);
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
            return switch (vector.direction)
            {
                case UP ->  vector.getY() == 0;
                case RIGHT -> vector.getX() + 1 == width;
                case DOWN -> vector.getY() + 1 == height;
                case LEFT -> vector.getX() == 0;
            };
        }

        private Optional<Vector> move()
        {
            if (isPositionTerminal(currentVector))
            {
                return Optional.empty();
            }

            int y = currentVector.getY();
            int x = currentVector.getX();
            Direction direction = currentVector.direction;

            switch (currentVector.direction)
            {
                case UP:
                    if (isPositionObstructed(y - 1, x))
                    {
                        direction = direction.turn();
                    }
                    else
                    {
                        y--;
                    }
                    break;

                case RIGHT:
                    if (isPositionObstructed(y, x + 1))
                    {
                        direction = direction.turn();
                    }
                    else
                    {
                        x++;
                    }
                    break;

                case DOWN:
                    if (isPositionObstructed(y + 1, x))
                    {
                        direction = direction.turn();
                    }
                    else
                    {
                        y++;
                    }
                    break;

                case LEFT:
                    if (isPositionObstructed(y, x - 1))
                    {
                        direction = direction.turn();
                    }
                    else
                    {
                        x--;
                    }
                    break;

                default:
                    throw new RuntimeException("Unrecognized direction!!");
            }

            currentVector = new Vector(x, y, direction);

            return Optional.of(currentVector);
        }

        private boolean isPositionObstructed(final int y, final int x)
        {
            return grid[y][x] == '#';
        }

        private void insertObstacle(final Position position)
        {
            grid[position.y][position.x] = '#';
        }
    }

    private enum Direction
    {
        UP,
        DOWN,
        RIGHT,
        LEFT;

        private Direction turn()
        {
            return switch (this)
            {
                case UP -> RIGHT;
                case RIGHT -> DOWN;
                case DOWN -> LEFT;
                case LEFT -> UP;
            };
        }
    }

    private final static class Vector
    {
        private final Position position;
        private final Direction direction;

        private Vector(final int x, final int y, final Direction direction)
        {
            this.position = new Position(x, y);
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
    }

    private static final class Position
    {
        private final int x;
        private final int y;

        private Position(final int x, final int y)
        {
            this.x = x;
            this.y = y;
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
