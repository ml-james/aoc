package org.mljames.aoc.aoc2024.day16;

import org.mljames.aoc.PuzzleInputReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

public class Day16Part1
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Day16Part1.class);

    public static void main(String[] args)
    {
        final long start = System.currentTimeMillis();

        final List<String> input = PuzzleInputReader.readInputAsStrings("aoc2024/day16/part1/puzzle_input.txt");

        final int width = input.getFirst().length();
        final int height = input.size();

        final ReindeerMaze maze = ReindeerMaze.create(input, height, width);

        final Route cheapestRoute = maze.findCheapestRoute(
                new HashSet<>(),
                new Route(0, maze.startingPosition, maze.startingDirection),
                new HashMap<>()).orElseThrow();

        LOGGER.info("The lowest score a reindeer could get traversing the maze is: {}, calculated in {}ms.", cheapestRoute.score, System.currentTimeMillis() - start);
    }

    private static final class ReindeerMaze
    {
        private final char[][] maze;
        private final Position startingPosition;
        private final Direction startingDirection = Direction.EAST;

        private ReindeerMaze(final char[][] maze, final Position start)
        {
            this.maze = maze;
            this.startingPosition = start;
        }

        private static ReindeerMaze create(final List<String> input, final int height, final int width)
        {
            Position start = null;
            final char[][] maze = new char[height][width];
            for (int y = 0; y < height; y++)
            {
                for (int x = 0; x < width; x++)
                {
                    if (input.get(y).charAt(x) == 'S')
                    {
                        start = new Position(x, y);
                    }
                    maze[y][x] = input.get(y).charAt(x);
                }
            }
            return new ReindeerMaze(maze, start);
        }

        private Optional<Route> findCheapestRoute(
                final Set<Position> visitedPositions,
                final Route route,
                final Map<Vector, Integer> vectorScores)
        {
            final Optional<Route> routeCandidate1 = moveForward(new HashSet<>(visitedPositions), new Route(route), vectorScores);
            final Optional<Route> routeCandidate2 = turnClockwiseAndMoveForward(new HashSet<>(visitedPositions), new Route(route), vectorScores);
            final Optional<Route> routeCandidate3 = turnAntiClockwiseAndMoveForward(new HashSet<>(visitedPositions), new Route(route), vectorScores);

            return Stream.of(routeCandidate1, routeCandidate2, routeCandidate3)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .sorted(Comparator.comparing((Route r) -> r.score))
                    .filter(r -> isEnd(r.currentPosition))
                    .findFirst();
        }

        private Optional<Route> moveForward(
                final Set<Position> visitedPositions,
                final Route route,
                final Map<Vector, Integer> vectorScores)
        {
            final Position currentPosition = route.move();
            if (isEnd(currentPosition))
            {
                return Optional.of(route);
            }
            if (!visitedPositions.contains(currentPosition) && isFree(route.currentPosition))
            {
                visitedPositions.add(currentPosition);
                if (vectorScores.containsKey(new Vector(currentPosition, route.currentDirection)))
                {
                    if (route.score > vectorScores.get(new Vector(currentPosition, route.currentDirection)))
                    {
                        return Optional.empty();
                    }
                    else
                    {
                        vectorScores.put(new Vector(currentPosition, route.currentDirection), route.score);
                    }
                }
                else
                {
                    vectorScores.put(new Vector(currentPosition, route.currentDirection), route.score);
                }
                return findCheapestRoute(visitedPositions, route, vectorScores);
            }
            return Optional.empty();
        }

        private Optional<Route> turnClockwiseAndMoveForward(
                final Set<Position> visitedPositions,
                final Route route,
                final Map<Vector, Integer> vectorScores)
        {
            route.turnClockwise();
            final Position currentPosition = route.move();
            if (isEnd(currentPosition))
            {
                return Optional.of(route);
            }
            if (!visitedPositions.contains(currentPosition) && isFree(route.currentPosition))
            {
                visitedPositions.add(currentPosition);
                if (vectorScores.containsKey(new Vector(currentPosition, route.currentDirection)))
                {
                    if (route.score > vectorScores.get(new Vector(currentPosition, route.currentDirection)))
                    {
                        return Optional.empty();
                    }
                    else
                    {
                        vectorScores.put(new Vector(currentPosition, route.currentDirection), route.score);
                    }
                }
                else
                {
                    vectorScores.put(new Vector(currentPosition, route.currentDirection), route.score);
                }
                return findCheapestRoute(visitedPositions, route, vectorScores);
            }
            return Optional.empty();
        }

        private Optional<Route> turnAntiClockwiseAndMoveForward(
                final Set<Position> visitedPositions,
                final Route route,
                final Map<Vector, Integer> vectorScores)
        {
            route.turnAntiClockwise();
            final Position currentPosition = route.move();
            if (isEnd(currentPosition))
            {
                return Optional.of(route);
            }
            if (!visitedPositions.contains(currentPosition) && isFree(route.currentPosition))
            {
                visitedPositions.add(currentPosition);
                if (vectorScores.containsKey(new Vector(currentPosition, route.currentDirection)))
                {
                    if (route.score > vectorScores.get(new Vector(currentPosition, route.currentDirection)))
                    {
                        return Optional.empty();
                    }
                    else
                    {
                        vectorScores.put(new Vector(currentPosition, route.currentDirection), route.score);
                    }
                }
                else
                {
                    vectorScores.put(new Vector(currentPosition, route.currentDirection), route.score);
                }
                return findCheapestRoute(visitedPositions, route, vectorScores);
            }
            return Optional.empty();
        }

        private boolean isEnd(final Position position)
        {
            return maze[position.y][position.x] == 'E';
        }

        private boolean isFree(final Position position)
        {
            return maze[position.y][position.x] == '.';
        }
    }

    private final static class Route
    {
        private int score;
        private Position currentPosition;
        private Direction currentDirection;

        public Route(final int score, final Position position, final Direction direction)
        {
            this.score = score;
            this.currentPosition = position;
            this.currentDirection = direction;
        }

        public Route(final Route route)
        {
            this.score = route.score;
            this.currentPosition = route.currentPosition;
            this.currentDirection = route.currentDirection;
        }

        void turnClockwise()
        {
            score += 1000;
            currentDirection = currentDirection.turnClockwise();
        }

        void turnAntiClockwise()
        {
            score += 1000;
            currentDirection = currentDirection.turnAntiClockwise();
        }

        private Position move()
        {
            score += 1;
            currentPosition = move(currentPosition, currentDirection);
            
            return currentPosition;
        }

        private Position move(final Position position, final Direction direction)
        {
            return switch (direction)
            {
                case NORTH -> new Position(position.x, position.y - 1);
                case EAST -> new Position(position.x + 1, position.y);
                case SOUTH -> new Position(position.x, position.y + 1);
                case WEST -> new Position(position.x - 1, position.y);
            };
        }
    }

    private enum Direction
    {
        WEST,
        SOUTH,
        EAST,
        NORTH;

        private Direction turnClockwise()
        {
            return switch (this)
            {
                case WEST -> NORTH;
                case EAST -> SOUTH;
                case NORTH -> EAST;
                case SOUTH -> WEST;
            };
        }

        private Direction turnAntiClockwise()
        {
            return switch (this)
            {
                case WEST -> SOUTH;
                case EAST -> NORTH;
                case NORTH -> WEST;
                case SOUTH -> EAST;
            };
        }
    }

    private static final class Vector
    {
        private final Position position;
        private final Direction direction;


        private Vector(final Position position, final Direction direction)
        {
            this.position = position;
            this.direction = direction;
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
            final Vector vector = (Vector) o;
            return Objects.equals(position, vector.position) && direction == vector.direction;
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
            return x == position.x && y == position.y;
        }

        @Override
        public String toString()
        {
            return "Position{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }

        @Override
        public int hashCode()
        {
            return Objects.hash(x, y);
        }
    }
}
