package org.mljames.aoc.aoc2024.day16;

import org.mljames.aoc.PuzzleInputReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Set;

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

        final Set<Position> positionsOnBestRoute = maze.findPositionsOnBestRoute();

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

        private char getValue(final Position position)
        {
            return maze[position.y][position.x];
        }

        private Set<Position> findPositionsOnBestRoute()
        {
            final PriorityQueue<Route> queue = new PriorityQueue<>(Comparator.comparingInt(r -> r.score));
            final Map<Vector, Integer> bestScores = new HashMap<>();

            queue.add(new Route(0, startingPosition, startingDirection, new ArrayList<>(List.of(startingPosition))));

            final Set<Position> positionsOnABestRoute = new HashSet<>();
            while (!queue.isEmpty())
            {
                final Route route = queue.poll();

                if (isEnd(route.currentPosition))
                {
                    positionsOnABestRoute.addAll(route.positions);
                    continue;
                }

                final Vector vector = new Vector(route.currentPosition, route.currentDirection);

                if (route.score <= bestScores.getOrDefault(vector, Integer.MAX_VALUE))
                {
                    bestScores.put(vector, route.score);

                    exploreMove(queue, route, MovementType.FORWARD);
                    exploreMove(queue, route, MovementType.TURN_CLOCKWISE);
                    exploreMove(queue, route, MovementType.TURN_ANTI_CLOCKWISE);
                }
            }

            return positionsOnABestRoute;
        }

        private void exploreMove(
                final PriorityQueue<Route> queue,
                final Route currentRoute,
                final MovementType movementType)
        {
            final Route route = currentRoute.copy();

            switch (movementType)
            {
                case FORWARD -> route.moveForward();
                case TURN_CLOCKWISE ->
                {
                    route.turnClockwise();
                    route.moveForward();
                }
                case TURN_ANTI_CLOCKWISE ->
                {
                    route.turnAntiClockwise();
                    route.moveForward();
                }
            }

            if (!isWall(route.currentPosition) && route.score <= BEST_SCORE)
            {
                queue.add(route);
            }
        }

        private boolean isEnd(final Position position)
        {
            return maze[position.y][position.x] == 'E';
        }

        private boolean isWall(final Position position)
        {
            return maze[position.y][position.x] == '#';
        }
    }

    private static final class Route
    {
        private int score;
        private Position currentPosition;
        private Direction currentDirection;
        public List<Position> positions;

        public Route(final int score, final Position position, final Direction direction, final List<Position> positions)
        {
            this.score = score;
            this.currentPosition = position;
            this.currentDirection = direction;
            this.positions = new ArrayList<>(positions);
        }

        public Route copy()
        {
            return new Route(this.score, this.currentPosition, this.currentDirection, this.positions);
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

        void moveForward()
        {
            score += 1;
            currentPosition = currentPosition.move(currentDirection);
            positions.add(currentPosition);
        }
    }

    private enum MovementType
    {
        FORWARD, TURN_CLOCKWISE, TURN_ANTI_CLOCKWISE
    }

    private enum Direction
    {
        WEST, SOUTH, EAST, NORTH;

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
        final int x, y;

        private Position(final int x, final int y)
        {
            this.x = x;
            this.y = y;
        }

        Position move(final Direction direction)
        {
            return switch (direction)
            {
                case NORTH -> new Position(x, y - 1);
                case EAST -> new Position(x + 1, y);
                case SOUTH -> new Position(x, y + 1);
                case WEST -> new Position(x - 1, y);
            };
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
        public int hashCode()
        {
            return Objects.hash(x, y);
        }
    }
}
