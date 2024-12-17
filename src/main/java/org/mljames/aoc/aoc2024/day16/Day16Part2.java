package org.mljames.aoc.aoc2024.day16;

import org.mljames.aoc.PuzzleInputReader;
import org.mljames.aoc.aoc2024.day14.Day14Part2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day16Part2
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Day16Part2.class);

    private static final int BEST_SCORE = 99448;

    public static void main(String[] args)
    {
        final long start = System.currentTimeMillis();

        final List<String> input = PuzzleInputReader.readInputAsStrings("aoc2024/day16/part2/puzzle_input.txt");

        final int width = input.getFirst().length();
        final int height = input.size();

        final ReindeerMaze maze = ReindeerMaze.create(input, height, width);

        final Set<Position> positionsOnBestRoute = maze.findPositionsOnBestRoute(
                new HashSet<>(),
                new Route(0, maze.startingPosition, maze.startingDirection),
                new HashMap<>());

        printBestPath(maze, positionsOnBestRoute, height, width);

        LOGGER.info("The number of tiles on the maze positioned on the best route is: {}, calculated in {}ms.", positionsOnBestRoute.size(), System.currentTimeMillis() - start);
    }

    private static void printBestPath(final ReindeerMaze maze, final Set<Position> tiles, final int yRange, final int xRange)
    {
        for (int y = 0; y < yRange; y++)
        {
            for (int x = 0; x < xRange; x++)
            {
                if (tiles.contains(new Position(x, y)))
                {
                    if (maze.getValue(new Position(x, y)) == '#')
                    {
                        throw new RuntimeException("Incorrectly placed tile!!");
                    }
                    System.out.print("O");
                }
                else if (maze.getValue(new Position(x, y)) == '#')
                {
                    System.out.print("#");
                }
                else
                {
                    System.out.print(".");
                }
            }
            System.out.print("\n");
        }
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

        public char getValue(final Position position)
        {
            return maze[position.y][position.x];
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

        private Set<Position> findPositionsOnBestRoute(
                final Set<Position> visitedPositions,
                final Route route,
                final Map<Vector, Integer> vectorScores)
        {
            final Set<Position> routeCandidate1 = moveForward(new HashSet<>(visitedPositions), new Route(route), vectorScores);
            final Set<Position> routeCandidate2 = turnClockwiseAndMoveForward(new HashSet<>(visitedPositions), new Route(route), vectorScores);
            final Set<Position> routeCandidate3 = turnAntiClockwiseAndMoveForward(new HashSet<>(visitedPositions), new Route(route), vectorScores);

            final Set<Position> allPositions = new HashSet<>();
            allPositions.addAll(routeCandidate1);
            allPositions.addAll(routeCandidate2);
            allPositions.addAll(routeCandidate3);

            return allPositions;
        }

        private Set<Position> moveForward(
                final Set<Position> visitedPositions,
                final Route route,
                final Map<Vector, Integer> vectorScores)
        {
            final Position currentPosition = route.move();
            if (isEnd(currentPosition))
            {
                if (route.score == BEST_SCORE)
                {
                    return route.positions;
                }
            }
            if (route.score > BEST_SCORE)
            {
                return new HashSet<>();
            }
            if (!visitedPositions.contains(currentPosition) && isFree(route.currentPosition))
            {
                visitedPositions.add(currentPosition);
                if (vectorScores.containsKey(new Vector(currentPosition, route.currentDirection)))
                {
                    if (route.score > vectorScores.get(new Vector(currentPosition, route.currentDirection)))
                    {
                        return new HashSet<>();
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
                return findPositionsOnBestRoute(visitedPositions, route, vectorScores);
            }
            return new HashSet<>();
        }

        private Set<Position> turnClockwiseAndMoveForward(
                final Set<Position> visitedPositions,
                final Route route,
                final Map<Vector, Integer> vectorScores)
        {
            route.turnClockwise();
            final Position currentPosition = route.move();
            if (isEnd(currentPosition))
            {
                if (route.score == BEST_SCORE)
                {
                    return route.positions;
                }
            }
            if (route.score > BEST_SCORE)
            {
                return new HashSet<>();
            }
            if (!visitedPositions.contains(currentPosition) && isFree(route.currentPosition))
            {
                visitedPositions.add(currentPosition);
                if (vectorScores.containsKey(new Vector(currentPosition, route.currentDirection)))
                {
                    if (route.score > vectorScores.get(new Vector(currentPosition, route.currentDirection)))
                    {
                        return new HashSet<>();
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
                return findPositionsOnBestRoute(visitedPositions, route, vectorScores);
            }
            return new HashSet<>();
        }

        private Set<Position> turnAntiClockwiseAndMoveForward(
                final Set<Position> visitedPositions,
                final Route route,
                final Map<Vector, Integer> vectorScores)
        {
            route.turnAntiClockwise();
            final Position currentPosition = route.move();
            if (isEnd(currentPosition))
            {
                if (route.score == BEST_SCORE)
                {
                    return route.positions;
                }
                else
                {
                    return new HashSet<>();
                }
            }
            if (route.score > BEST_SCORE)
            {
                return new HashSet<>();
            }
            if (!visitedPositions.contains(currentPosition) && isFree(route.currentPosition))
            {
                visitedPositions.add(currentPosition);
                if (vectorScores.containsKey(new Vector(currentPosition, route.currentDirection)))
                {
                    if (route.score > vectorScores.get(new Vector(currentPosition, route.currentDirection)))
                    {
                        return new HashSet<>();
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
                return findPositionsOnBestRoute(visitedPositions, route, vectorScores);
            }
            return new HashSet<>();
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
        private final Set<Position> positions;

        public Route(final int score, final Position position, final Direction direction)
        {
            this.score = score;
            this.currentPosition = position;
            this.currentDirection = direction;
            this.positions = new HashSet<>(List.of(position));
        }

        public Route(final Route route)
        {
            this.score = route.score;
            this.currentPosition = route.currentPosition;
            this.currentDirection = route.currentDirection;
            this.positions = route.positions.stream().map(p -> new Position(p.x, p.y)).collect(Collectors.toSet());
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
            positions.add(currentPosition);
            
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
