package org.mljames.aoc.aoc2024.day15;

import org.mljames.aoc.PuzzleInputReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class Day15Part2
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Day15Part2.class);

    public static void main(String[] args)
    {
        final long start = System.currentTimeMillis();

        final List<String> input1 = PuzzleInputReader.readInputAsStrings("aoc2024/day15/part2/puzzle_input_1.txt");
        final List<String> input2 = PuzzleInputReader.readInputAsStrings("aoc2024/day15/part2/puzzle_input_2.txt");

        final int width = input1.getFirst().length();
        final int height = input1.size();

        final Warehouse warehouse = Warehouse.createWarehouse(input1, height, width);

        final char[] robotMoves = String.join("", input2).toCharArray();

        for (final char robotMove : robotMoves)
        {
            warehouse.move(Direction.fromChar(robotMove));
        }

        LOGGER.info("After the robot has finished moving the sum of all the boxes GPS coordinates is: {}, calculated in {}ms.", warehouse.getGpsCoordinateSum(), System.currentTimeMillis() - start);

    }


    private static class Warehouse
    {
        private final char[][] units;
        private final int height;
        private final int width;
        private Position robotPosition;

        static Warehouse createWarehouse(final List<String> input, final int height, final int width)
        {
            final char[][] units = new char[height][width];
            Position robotStartingPosition = null;
            for (int y = 0; y < height; y++)
            {
                for (int x = 0; x < width; x++)
                {
                    final char c = input.get(y).charAt(x);
                    units[y][x] = c;
                    if (c == '@')
                    {
                        robotStartingPosition = new Position(x, y);
                    }
                }
            }
            return new Warehouse(units, robotStartingPosition, height, width);
        }

        private boolean isWall(final Position position)
        {
            return units[position.yPosition][position.xPosition] == '#';
        }

        private boolean isFree(final Position position)
        {
            return units[position.yPosition][position.xPosition] != '#' && units[position.yPosition][position.xPosition] != 'O';
        }

        private Optional<Position> findNextFreeSpace(final Direction direction)
        {
            switch (direction)
            {
                case UP ->
                {
                    Position position = new Position(robotPosition.xPosition, robotPosition.yPosition - 1);
                    while (!isWall(position))
                    {
                        if (isFree(position))
                        {
                            return Optional.of(position);
                        }
                        position = new Position(position.xPosition, position.yPosition - 1);
                    }
                    return Optional.empty();
                }
                case RIGHT ->
                {
                    Position position = new Position(robotPosition.xPosition + 1, robotPosition.yPosition);
                    while (!isWall(position))
                    {
                        if (isFree(position))
                        {
                            return Optional.of(position);
                        }
                        position = new Position(position.xPosition + 1, position.yPosition);
                    }
                    return Optional.empty();
                }
                case DOWN ->
                {
                    Position position = new Position(robotPosition.xPosition, robotPosition.yPosition + 1);
                    while (!isWall(position))
                    {
                        if (isFree(position))
                        {
                            return Optional.of(position);
                        }
                        position = new Position(position.xPosition, position.yPosition + 1);
                    }
                    return Optional.empty();
                }
                case LEFT ->
                {
                    Position position = new Position(robotPosition.xPosition - 1, robotPosition.yPosition);
                    while (!isWall(position))
                    {
                        if (isFree(position))
                        {
                            return Optional.of(position);
                        }
                        position = new Position(position.xPosition - 1, position.yPosition);
                    }
                    return Optional.empty();
                }
                default -> throw new RuntimeException("Unexpected direction!!");
            }
        }

        private void move(final Direction direction)
        {
            switch (direction)
            {
                case UP -> moveRobotPosition(robotPosition.xPosition, robotPosition.yPosition - 1, Direction.UP);
                case RIGHT -> moveRobotPosition(robotPosition.xPosition + 1, robotPosition.yPosition, Direction.RIGHT);
                case DOWN -> moveRobotPosition(robotPosition.xPosition, robotPosition.yPosition + 1, Direction.DOWN);
                case LEFT -> moveRobotPosition(robotPosition.xPosition - 1, robotPosition.yPosition, Direction.LEFT);
            }
        }

        private void moveRobotPosition(int newXPosition, int newYPosition, final Direction direction)
        {
            final Position newRobotPosition = new Position(newXPosition, newYPosition);
            if (isFree(newRobotPosition))
            {
                units[robotPosition.yPosition][robotPosition.xPosition] = '.';

                robotPosition = newRobotPosition;
                units[robotPosition.yPosition][robotPosition.xPosition] = '@';
            }
            else
            {
                final Optional<Position> maybeNextFreeSpace = findNextFreeSpace(direction);
                if (maybeNextFreeSpace.isPresent())
                {
                    units[robotPosition.yPosition][robotPosition.xPosition] = '.';

                    robotPosition = newRobotPosition;
                    units[robotPosition.yPosition][robotPosition.xPosition] = '@';

                    units[maybeNextFreeSpace.get().yPosition][maybeNextFreeSpace.get().xPosition] = 'O';
                }
            }
        }

        private Warehouse(final char[][] units, final Position robotPosition, final int height, final int width)
        {
            this.units = units;
            this.robotPosition = robotPosition;
            this.height = height;
            this.width = width;
        }

        private long getGpsCoordinateSum()
        {
            long gpsCoordinateSum = 0L;
            for (int y = 0; y < height; y++)
            {
                for (int x = 0; x < width; x++)
                {
                    if (units[y][x] == 'O')
                    {
                        gpsCoordinateSum = gpsCoordinateSum + (100L * y + x);
                    }
                }
            }
            return gpsCoordinateSum;
        }
    }

    private static final class Position
    {
        private final int xPosition;
        private final int yPosition;

        private Position(final int xPosition, final int yPosition)
        {
            this.xPosition = xPosition;
            this.yPosition = yPosition;
        }
    }

    private enum Direction
    {
        LEFT,
        DOWN,
        RIGHT,
        UP;

        private static Direction fromChar(final char c)
        {
            if (c == '^')
            {
                return UP;
            }
            else if (c == '>')
            {
                return RIGHT;
            }
            else if (c == 'v')
            {
                return DOWN;
            }
            else if (c == '<')
            {
                return LEFT;
            }
            else
            {
                throw new RuntimeException("Unexpected char representing a direction!!");
            }
        }
    }
}
