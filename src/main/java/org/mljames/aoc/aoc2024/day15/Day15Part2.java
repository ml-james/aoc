package org.mljames.aoc.aoc2024.day15;

import org.mljames.aoc.PuzzleInputReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

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
            final char[][] units = new char[inputHeight][inputWidth * 2];
            Position robotStartingPosition = null;
            for (int y = 0; y < inputHeight; y++)
            {
                for (int x = 0; x < inputWidth; x++)
                {
                    final char c = input.get(y).charAt(x);
                    if (c == '@')
                    {
                        robotStartingPosition = new Position(2 * x, y);
                        units[y][2 * x] = c;
                        units[y][2 * x + 1] = '.';
                    }
                    else if (c == 'O')
                    {
                        units[y][2 * x] = '[';
                        units[y][2 * x + 1] = ']';
                    }
                    else if (c == '#')
                    {
                        units[y][2 * x] = '#';
                        units[y][2 * x + 1] = '#';
                    }
                    else if (c == '.')
                    {
                        units[y][2 * x] = '.';
                        units[y][2 * x + 1] = '.';
                    }
                    else
                    {
                        throw new RuntimeException("Unexpected char!!");
                    }
                }
            }
            return new Warehouse(units, robotStartingPosition, inputHeight, inputWidth * 2);
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

            final Set<Box> connectedBoxes = findConnectedBoxes(new HashSet<>(), robotPosition, direction);
            final Set<Position> moveRegionCandidate = getMoveRegionCandidate(robotPosition, connectedBoxes);

            if (regionMoveable(moveRegionCandidate, direction))
            {
                final Set<Position> postMoveRegion = new HashSet<>();
                postMoveRegion.addAll(moveBoxes(direction, connectedBoxes));
                postMoveRegion.add(moveRobot(newRobotPosition));

                moveRegionCandidate.removeAll(postMoveRegion);
                moveRegionCandidate.forEach(p -> units[p.y][p.x] = '.');
            }
        }

        private Set<Position> getMoveRegionCandidate(final Position robotPosition, final Set<Box> boxPositions)
        {
            final Set<Position> positions = new HashSet<>();
            positions.add(robotPosition);
            positions.addAll(boxPositions.stream().map(b -> b.position).toList());

            return positions;
        }

        private Set<Box> findConnectedBoxes(final Set<Box> visitedBoxes, final Position currentPosition, final Direction direction)
        {
            Position position = nextPosition(currentPosition, direction);
            while (isBox(position))
            {
                visitedBoxes.add(new Box(getUnit(position), position));

                final Box adjacentBox = getAdjacentBox(position);
                if (!visitedBoxes.contains(adjacentBox))
                {
                    visitedBoxes.add(adjacentBox);
                    findConnectedBoxes(visitedBoxes, adjacentBox.position, direction);
                }

                position = nextPosition(position, direction);
            }
            return visitedBoxes;
        }

        private Box getAdjacentBox(final Position position)
        {
            if (isLeftHalfOfBox(position))
            {
                final Position newPosition = new Position(position.x + 1, position.y);
                return new Box(getUnit(newPosition), newPosition);
            }
            else if (isRightHalfOfBox(position))
            {
                final Position newPosition = new Position(position.x - 1, position.y);
                return new Box(getUnit(newPosition), newPosition);
            }
            else
            {
                throw new RuntimeException("Unexpected box configuration!!");
            }
        }

        private void move(final Direction direction)
        {
            switch (direction)
            {
                case UP -> move(robotPosition.x, robotPosition.y - 1, Direction.UP);
                case RIGHT -> move(robotPosition.x + 1, robotPosition.y, Direction.RIGHT);
                case DOWN -> move(robotPosition.x, robotPosition.y + 1, Direction.DOWN);
                case LEFT -> move(robotPosition.x - 1, robotPosition.y, Direction.LEFT);
            }
        }

        private Position nextPosition(final Position position, final Direction direction)
        {
            return switch (direction)
            {
                case UP -> new Position(position.x, position.y - 1);
                case RIGHT -> new Position(position.x + 1, position.y);
                case DOWN -> new Position(position.x, position.y + 1);
                case LEFT -> new Position(position.x - 1, position.y);
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

            units[robotPosition.y][robotPosition.x] = '@';

            return newRobotPosition;
        }

        private Set<Position> moveBoxes(final Direction direction, final Set<Box> boxes)
        {
            final Set<Position> newPositions = new HashSet<>();
            for (final Box box : boxes)
            {
                final Position newPosition = nextPosition(box.position, direction);
                units[newPosition.y][newPosition.x] = box.box;
                newPositions.add(newPosition);
            }
            return newPositions;
        }

        private boolean isWall(final Position position)
        {
            return units[position.y][position.x] == '#';
        }

        private boolean isBox(final Position position)
        {
            return units[position.y][position.x] == '[' || units[position.y][position.x] == ']';
        }

        private boolean isLeftHalfOfBox(final Position position)
        {
            return units[position.y][position.x] == '[';
        }

        private char getUnit(final Position position)
        {
            return units[position.y][position.x];
        }

        private boolean isRightHalfOfBox(final Position position)
        {
            return units[position.y][position.x] == ']';
        }

        private long getGpsCoordinateSum()
        {
            long gpsCoordinateSum = 0L;
            for (int y = 0; y < height; y++)
            {
                for (int x = 0; x < width; x++)
                {
                    if (units[y][x] == '[')
                    {
                        gpsCoordinateSum += (100L * y + x);
                    }
                }
            }
            return gpsCoordinateSum;
        }
    }

    private static final class Box
    {
        private final char box;
        private final Position position;

        private Box(final char box, final Position position)
        {
            this.box = box;
            this.position = position;
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
            final Box box1 = (Box) o;
            return box == box1.box && Objects.equals(position, box1.position);
        }

        @Override
        public int hashCode()
        {
            return Objects.hash(box, position);
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
        public int hashCode()
        {
            return Objects.hash(x, y);
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
            } else if (c == '>')
            {
                return RIGHT;
            } else if (c == 'v')
            {
                return DOWN;
            } else if (c == '<')
            {
                return LEFT;
            } else
            {
                throw new RuntimeException("Unexpected char representing a direction!!");
            }
        }
    }
}
