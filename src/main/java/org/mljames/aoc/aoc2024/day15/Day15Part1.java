package org.mljames.aoc.aoc2024.day15;

import org.mljames.aoc.PuzzleInputReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Day15Part1
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Day15Part1.class);

    public static void main(String[] args)
    {
        final long start = System.currentTimeMillis();

        final List<String> input1 = PuzzleInputReader.readInputAsStrings("aoc2024/day15/part1/puzzle_input_1.txt");
        final List<String> input2 = PuzzleInputReader.readInputAsStrings("aoc2024/day15/part1/puzzle_input_2.txt");

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

        private Warehouse(final char[][] units, final Position robotPosition, final int height, final int width)
        {
            this.units = units;
            this.robotPosition = robotPosition;
            this.height = height;
            this.width = width;
        }

        private void move(final int newXPosition, final int newYPosition, final Direction direction)
        {
            final Position newRobotPosition = new Position(newXPosition, newYPosition);

            final List<Position> connectedBoxes = findConnectedBoxes(direction);
            final Set<Position> moveRegionCandidate = getMoveRegionCandidate(robotPosition, connectedBoxes);

            if (regionMoveable(moveRegionCandidate, direction))
            {
                final Set<Position> postMoveRegion = new HashSet<>();
                postMoveRegion.addAll(moveBoxes(direction, connectedBoxes));
                postMoveRegion.add(moveRobot(newRobotPosition));

                moveRegionCandidate.removeAll(postMoveRegion);
                moveRegionCandidate.forEach(p -> units[p.yPosition][p.xPosition] = '.');
            }
        }

        private Set<Position> getMoveRegionCandidate(final Position robotPosition, final List<Position> boxPositions)
        {
            final Set<Position> positions = new HashSet<>();
            positions.add(robotPosition);
            positions.addAll(boxPositions);

            return positions;
        }

        private List<Position> findConnectedBoxes(final Direction direction)
        {
            Position position = nextPosition(robotPosition, direction);
            final List<Position> connectedPositions = new ArrayList<>();
            while (isBox(position))
            {
                connectedPositions.add(position);
                position = nextPosition(position, direction);
            }
            return connectedPositions;
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

        private boolean regionMoveable(final Set<Position> positions, final Direction direction)
        {
            for (final Position position : positions)
            {
                if (isWall(nextPosition(position, direction)))
                {
                    return false;
                }
            }
            return true;
        }

        private Position moveRobot(final Position newRobotPosition)
        {
            robotPosition = newRobotPosition;
            units[robotPosition.yPosition][robotPosition.xPosition] = '@';

            return newRobotPosition;
        }

        private Set<Position> moveBoxes(final Direction direction, final List<Position> boxes)
        {
            final Set<Position> newPositions = new HashSet<>();
            for (final Position position : boxes)
            {
                final Position newPosition = nextPosition(position, direction);
                units[newPosition.yPosition][newPosition.xPosition] = 'O';
                newPositions.add(newPosition);
            }
            return newPositions;
        }

        private boolean isWall(final Position position)
        {
            return units[position.yPosition][position.xPosition] == '#';
        }

        private boolean isBox(final Position position)
        {
            return units[position.yPosition][position.xPosition] == 'O';
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
            return xPosition == position.xPosition && yPosition == position.yPosition;
        }

        @Override
        public int hashCode()
        {
            return Objects.hash(xPosition, yPosition);
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
