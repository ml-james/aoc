package org.mljames.aoc.aoc2024.day14;

import org.mljames.aoc.PuzzleInputReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.locks.LockSupport;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day14Part2
{
    private static final int WIDTH = 101;
    private static final int HEIGHT = 103;

    private static final Logger LOGGER = LoggerFactory.getLogger(Day14Part2.class);

    private static final Pattern getStartingPositionX = Pattern.compile("p\\=(\\-{0,1}[0-9]{1,3}),");
    private static final Pattern getStartingPositionY = Pattern.compile("p\\=\\-{0,1}[0-9]{1,3},(\\-{0,1}[0-9]{1,3})");
    private static final Pattern getVelocityX = Pattern.compile("v\\=(\\-{0,1}[0-9]{1,3}),");
    private static final Pattern getVelocityY = Pattern.compile("v\\=\\-{0,1}[0-9]{1,3},(\\-{0,1}[0-9]{1,3})");

    public static void main(String[] args)
    {
        final long start = System.currentTimeMillis();

        final List<String> input = PuzzleInputReader.readInputAsStrings("aoc2024/day14/part2/puzzle_input.txt");

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
        boolean keepGoing = true;
        while (keepGoing)
        {
            robots.forEach(Robot::move);

            secondsToDisplayChristmasTree += 1;

            final Set<Position> currentRobotPositions = robots.stream().map(r -> r.currentPosition).collect(Collectors.toSet());

            final Map<Integer, List<Position>> robotsByX = currentRobotPositions.stream().collect(Collectors.groupingBy(position -> position.xPosition));
            final Map<Integer, List<Position>> robotsByY = currentRobotPositions.stream().collect(Collectors.groupingBy(position -> position.yPosition));

            int maxColumnStreakOfRobots = robotsByX.values().stream().map(Day14Part2::findMaxColumnStreakOfRobots).mapToInt(Integer::intValue).max().orElseThrow();
            int maxRowStreakOfRobots = robotsByY.values().stream().map(Day14Part2::findMaxRowStreakOfRobots).mapToInt(Integer::intValue).max().orElseThrow();

            if (maxColumnStreakOfRobots > WIDTH / 10 || maxRowStreakOfRobots > HEIGHT / 10)
            {
                printRobots(currentRobotPositions, HEIGHT, WIDTH);
                LockSupport.parkNanos(Duration.ofSeconds(1).toNanos());

                LOGGER.info("Seconds elapsed and finding a point of interest for the easter egg: {}.", secondsToDisplayChristmasTree);
                keepGoing = false;
            }
            else
            {
                if (secondsToDisplayChristmasTree % 1000 == 0)
                {
                    LOGGER.info("Seconds elapsed and looking for the easter egg: {}.", secondsToDisplayChristmasTree);
                }
            }
        }

        LOGGER.info("It takes {}s for the robots to display the easter egg, calculated in {}ms.", secondsToDisplayChristmasTree, System.currentTimeMillis() - start);
    }

    private static int findMaxRowStreakOfRobots(final List<Position> yConstantPositions)
    {
        final List<Position> sortedPositionsByX = new ArrayList<>(yConstantPositions);
        sortedPositionsByX.sort(Comparator.comparingInt((Position p) -> p.xPosition));

        int maxStreak = 0;
        int currentXValue = sortedPositionsByX.getFirst().xPosition;
        int currentStreak = 0;
        for (int i = 1; i < sortedPositionsByX.size(); i++)
        {
            if (sortedPositionsByX.get(i).xPosition == currentXValue + 1)
            {
                currentStreak += 1;
            }
            else
            {
                if (currentStreak > maxStreak)
                {
                    maxStreak = currentStreak;
                    currentStreak = 0;
                }
            }
            currentXValue = sortedPositionsByX.get(i).xPosition;
        }
        return maxStreak;
    }

    private static int findMaxColumnStreakOfRobots(final List<Position> xConstantPositions)
    {
        final List<Position> sortedPlotsByY = new ArrayList<>(xConstantPositions);
        sortedPlotsByY.sort(Comparator.comparingInt((Position p) -> p.yPosition));

        int maxStreak = 0;
        int currentYValue = xConstantPositions.getFirst().yPosition;
        int currentStreak = 0;
        for (int i = 1; i < xConstantPositions.size(); i++)
        {
            if (xConstantPositions.get(i).yPosition == currentYValue + 1)
            {
                currentStreak += 1;
            }
            else
            {
                if (currentStreak > maxStreak)
                {
                    maxStreak = currentStreak;
                    currentStreak = 0;
                }
            }
            currentYValue = xConstantPositions.get(i).yPosition;
        }
        return maxStreak;
    }

    private static void printRobots(final Set<Position> robotPositions, final int yRange, final int xRange)
    {
        for (int y = 0; y < yRange; y++)
        {
            for (int x = 0; x < xRange; x++)
            {
                if (robotPositions.contains(new Position(x, y)))
                {
                    System.out.print("X");
                }
                else
                {
                    System.out.print(".");
                }
            }
            System.out.print("\n");
        }
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
