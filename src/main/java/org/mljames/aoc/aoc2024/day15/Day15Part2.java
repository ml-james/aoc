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

        final Warehouse warehouse = Warehouse.createWarehouse(input1, input1.size(), input1.getFirst().length());

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

        static Warehouse createWarehouse(final List<String> input, final int inputHeight, final int inputWidth)
        {
            final char[][] units = new char[inputHeight * 2][inputWidth * 2];
            Position robotStartingPosition = null;
            for (int y = 0; y < inputHeight; y++)
            {
                for (int x = 0; x < inputWidth; x++)
                {
                    final char c = input.get(y).charAt(x);
                    if (c == '@')
                    {
                        robotStartingPosition = new Position(x, y);
                        units[2 * y][2 * x] = c;
                        units[2 * y][2 * x] = '.';
                    }
                    else if (c == 'O')
                    {
                        units[2 * y][2 * x] = '[';
                        units[2 * y][2 * x] = ']';
                    }
                    else if (c == '#')
                    {
                        units[2 * y][2 * x] = '#';
                        units[2 * y][2 * x] = '#';
                    }
                    else if (c == '.')
                    {
                        units[2 * y][2 * x] = '.';
                        units[2 * y][2 * x] = '.';
                    }
                    else
                    {
                        throw new RuntimeException("Unexpected char!!");
                    }
                }
            }
            return new Warehouse(units, robotStartingPosition, inputHeight * 2, inputWidth * 2);
        }

        private boolean isWall(final Position position)
        {
            return units[position.yPosition][position.xPosition] == '#';
        }

        private boolean isFree(final Position position)
        {
            return units[position.yPosition][position.xPosition] == '.';
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
                case UP -> move(robotPosition.xPosition, robotPosition.yPosition - 1, Direction.UP);
                case RIGHT -> move(robotPosition.xPosition + 1, robotPosition.yPosition, Direction.RIGHT);
                case DOWN -> move(robotPosition.xPosition, robotPosition.yPosition + 1, Direction.DOWN);
                case LEFT -> move(robotPosition.xPosition - 1, robotPosition.yPosition, Direction.LEFT);
            }
        }

        private Position nextPosition(final Position position, final Direction direction)
        {
            return switch (direction)
            {
                case UP -> new Position(position.xPosition, position.yPosition - 1);
                case RIGHT -> new Position(position.xPosition + 1, position.yPosition);
                case DOWN -> new Position(position.xPosition, position.yPosition + 1);
                case LEFT -> new Position(position.xPosition - 1, position.yPosition);
            };
        }

        private void move(final int newXPosition, final int newYPosition, final Direction direction)
        {
            final Position newRobotPosition = new Position(newXPosition, newYPosition);
            if (isFree(newRobotPosition))
            {
                moveRobot(newRobotPosition);
            }
            else
            {
                final Optional<Position> maybeNextFreeSpace = findNextFreeSpace(direction);
                if (maybeNextFreeSpace.isPresent())
                {
                    moveBoxes(direction, newRobotPosition, maybeNextFreeSpace.get());
                    moveRobot(newRobotPosition);
                }
            }
        }

        private void moveRobot(final Position newRobotPosition)
        {
            units[robotPosition.yPosition][robotPosition.xPosition] = '.';

            robotPosition = newRobotPosition;
            units[robotPosition.yPosition][robotPosition.xPosition] = '@';
        }

        private void moveBoxes(final Direction direction, final Position newRobotPosition, final Position nextFreeSpace)
        {
            Position currentPosition = newRobotPosition;
            do
            {
                currentPosition = nextPosition(currentPosition, direction);
                units[currentPosition.yPosition][currentPosition.xPosition] = 'O';
            }
            while (!currentPosition.equals(nextFreeSpace));
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
