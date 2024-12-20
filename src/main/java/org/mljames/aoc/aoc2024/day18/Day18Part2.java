package org.mljames.aoc.aoc2024.day18;

import org.mljames.aoc.PuzzleInputReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;

public class Day18Part2
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Day18Part2.class);

    private static final int MEGABYTE = 1024;

    public static void main(String[] args)
    {
        final long start = System.currentTimeMillis();

        final List<List<Integer>> input = PuzzleInputReader.readInputAsInts("aoc2024/day18/part1/puzzle_input.txt", ",");

        final MemorySpace memorySpace = new MemorySpace();

        memorySpace.addStart(0, 0);
        memorySpace.addEnd(70, 70);

        for (int i = 0; i < MEGABYTE; i++)
        {
            memorySpace.addMemory(input.get(i).get(0), input.get(i).get(1));
        }

        int byteNumber = MEGABYTE;
        while (memorySpace.hasValidPath())
        {
            byteNumber += 1;
            memorySpace.addMemory(input.get(byteNumber - 1).get(0), input.get(byteNumber - 1).get(1));
        }

        LOGGER.info("The byte that prevents an escape from the memory state is: [{},{}], calculated in {}ms.",
                input.get(byteNumber - 1).get(0),
                input.get(byteNumber - 1).get(1),
                System.currentTimeMillis() - start);
    }
    
    private static final class MemorySpace
    {
        private int height = 71;
        private int width = 71;
        private char[][] space = new char[71][71];

        void addMemory(final int positionX, final int positionY)
        {
            space[positionY][positionX] = 'X';
        }

        void addStart(final int positionX, final int positionY)
        {
            space[positionY][positionX] = 'S';
        }

        void addEnd(final int positionX, final int positionY)
        {
            space[positionY][positionX] = 'E';
        }

        private boolean isInMemorySpace(final Position position)
        {
            return position.x >= 0 && position.x < width && position.y >= 0 && position.y < height;
        }
        
        private boolean isByte(final Position position)
        {
            return space[position.y][position.x] == 'X';
        }

        private boolean hasValidPath()
        {
            final PriorityQueue<Route> queue = new PriorityQueue<>(Comparator.comparingInt(r -> r.length));
            final Map<Position, Integer> positionScores = new HashMap<>();

            queue.add(new Route(0, new Position(0, 0)));

            while (!queue.isEmpty())
            {
                final Route route = queue.poll();

                if (isEnd(route.currentPosition))
                {
                    return true;
                }

                exploreMove(queue, positionScores, route, MovementType.FORWARD);
                exploreMove(queue, positionScores, route, MovementType.RIGHT);
                exploreMove(queue, positionScores, route, MovementType.LEFT);
                exploreMove(queue, positionScores, route, MovementType.BACKWARD);
            }

            return false;
        }

        private void exploreMove(
                final PriorityQueue<Route> queue,
                final Map<Position, Integer> positionLengths,
                final Route currentRoute,
                final MovementType movementType)
        {
            final Route route = currentRoute.copy();

            switch (movementType)
            {
                case FORWARD -> route.moveForward();
                case RIGHT -> route.moveRight();
                case LEFT -> route.moveLeft();
                case BACKWARD -> route.moveBackward();
            }

            if (isInMemorySpace(route.currentPosition) && !isByte(route.currentPosition) && route.length < positionLengths.getOrDefault(route.currentPosition, Integer.MAX_VALUE))
            {
                positionLengths.put(route.currentPosition, route.length);
                queue.add(route);
            }
        }

        private boolean isEnd(final Position position)
        {
            return space[position.y][position.x] == 'E';
        }
    }

    private static final class Route
    {
        private int length;
        private Position currentPosition;

        public Route(final int length, final Position position)
        {
            this.length = length;
            this.currentPosition = position;
        }

        public Route copy()
        {
            return new Route(this.length, this.currentPosition);
        }

        void moveRight()
        {
            length += 1;
            currentPosition = currentPosition.move(MovementType.RIGHT);
        }

        void moveLeft()
        {
            length += 1;
            currentPosition = currentPosition.move(MovementType.LEFT);
        }

        void moveForward()
        {
            length += 1;
            currentPosition = currentPosition.move(MovementType.FORWARD);
        }

        void moveBackward()
        {
            length += 1;
            currentPosition = currentPosition.move(MovementType.BACKWARD);
        }
    }

    private enum MovementType
    {
        FORWARD, RIGHT, LEFT, BACKWARD
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

        private Position move(final MovementType movementType)
        {
            return switch (movementType)
            {
                case LEFT -> new Position(x - 1, y);
                case RIGHT -> new Position(x + 1, y);
                case FORWARD -> new Position(x, y - 1);
                case BACKWARD -> new Position(x, y + 1);
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

        @Override
        public String toString()
        {
            return "Position{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }
}
