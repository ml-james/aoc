package org.mljames.aoc.aoc2024.day14;

import org.mljames.aoc.PuzzleInputReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day14Part2
{
    private static final int WIDTH = 101;
    private static final int HEIGHT = 103;
    private static final int SECONDS = 100;

    private static final Logger LOGGER = LoggerFactory.getLogger(Day14Part2.class);

    private static final Pattern getStartingPositionX = Pattern.compile("p\\=(\\-{0,1}[0-9]{1,3}),");
    private static final Pattern getStartingPositionY = Pattern.compile("p\\=\\-{0,1}[0-9]{1,3},(\\-{0,1}[0-9]{1,3})");
    private static final Pattern getVelocityX = Pattern.compile("v\\=(\\-{0,1}[0-9]{1,3}),");
    private static final Pattern getVelocityY = Pattern.compile("v\\=\\-{0,1}[0-9]{1,3},(\\-{0,1}[0-9]{1,3})");

    public static void main(String[] args)
    {
        final long start = System.currentTimeMillis();

        final List<String> input = PuzzleInputReader.readInputAsStrings("aoc2024/day14/part2/puzzle_input.txt");

        final Set<Position> targetChristmasTreePositions = getChristmasTreePositions();

        final List<Robot> robots = new ArrayList<>();
        for (final String startingConfiguration : input)
        {
            robots.add(new Robot(
                    extractInt(getStartingPositionX.matcher(startingConfiguration)),
                    extractInt(getStartingPositionY.matcher(startingConfiguration)),
                    extractInt(getVelocityX.matcher(startingConfiguration)),
                    extractInt(getVelocityY.matcher(startingConfiguration))));
        }

        int secondsToDisplayChristmasTree = 0;
        while (true)
        {
            robots.forEach(Robot::move);

            final Set<Position> currentRobotPositions = robots.stream().map(r -> r.currentPosition).collect(Collectors.toSet());

            if (currentRobotPositions.containsAll(targetChristmasTreePositions))
            {
                LOGGER.info("Discovered the target Christmas Tree positions after: {}.", secondsToDisplayChristmasTree);
                break;
            }
            secondsToDisplayChristmasTree += 1;
            LOGGER.info("Seconds elapsed without finding the easter egg: {}.", secondsToDisplayChristmasTree);
        }

        LOGGER.info("It takes {}s for the robots to display the easter egg {}, calculated in {}ms.", SECONDS, secondsToDisplayChristmasTree, System.currentTimeMillis() - start);
    }

    private static Set<Position> getChristmasTreePositions()
    {
        final List<String> christmasTree = PuzzleInputReader.readInputAsStrings("aoc2024/day14/part2/christmas_tree.txt");

        final Set<Position> christmasTreePositions = new HashSet<>();
        for (int i = 0; i < christmasTree.size(); i++)
        {
            for (int j = 0; j < christmasTree.get(i).length(); j++)
            {
                if (christmasTree.get(i).charAt(j) == 'X')
                {
                    christmasTreePositions.add(new Position(j, i));
                }
            }
        }
        return christmasTreePositions;
    }

    private static int extractInt(final Matcher matcher)
    {
        if (!matcher.find())
        {
            throw new RuntimeException("Struggled parsing the input!!");
        }
        return Integer.parseInt(matcher.group(1));
    }

    private static final class Robot
    {
        private Position currentPosition;
        private final Velocity velocity;

        public Robot(final int xPosition, final int yPosition, final int xVelocity, final int yVelocity)
        {
            this.currentPosition = new Position(xPosition, yPosition);
            this.velocity = new Velocity(xVelocity, yVelocity);
        }

        private void move()
        {
            int newYPosition = (currentPosition.yPosition + velocity.yVelocity + HEIGHT) % HEIGHT;
            int newXPosition = (currentPosition.xPosition + velocity.xVelocity + WIDTH) % WIDTH;

            currentPosition = new Position(newXPosition, newYPosition);
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

    private static final class Velocity
    {
        private final int xVelocity;
        private final int yVelocity;

        private Velocity(final int xVelocity, final int yVelocity)
        {
            this.xVelocity = xVelocity;
            this.yVelocity = yVelocity;
        }
    }
}
