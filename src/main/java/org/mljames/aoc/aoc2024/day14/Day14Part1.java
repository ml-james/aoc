package org.mljames.aoc.aoc2024.day14;

import org.mljames.aoc.PuzzleInputReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day14Part1
{
    private static final int WIDTH = 101;
    private static final int HEIGHT = 103;
    private static final int SECONDS = 100;

    private static final Logger LOGGER = LoggerFactory.getLogger(Day14Part1.class);

    private static final Pattern getStartingPositionX = Pattern.compile("p\\=(\\-{0,1}[0-9]{1,3}),");
    private static final Pattern getStartingPositionY = Pattern.compile("p\\=\\-{0,1}[0-9]{1,3},(\\-{0,1}[0-9]{1,3})");
    private static final Pattern getVelocityX = Pattern.compile("v\\=(\\-{0,1}[0-9]{1,3}),");
    private static final Pattern getVelocityY = Pattern.compile("v\\=\\-{0,1}[0-9]{1,3},(\\-{0,1}[0-9]{1,3})");

    public static void main(String[] args)
    {
        final long start = System.currentTimeMillis();

        final List<String> input = PuzzleInputReader.readInputAsStrings("aoc2024/day14/part1/puzzle_input.txt");

        final List<Robot> robots = new ArrayList<>();
        for (final String configuration : input)
        {
            robots.add(new Robot(
                    new Position(extractInt(getStartingPositionX.matcher(configuration)), extractInt(getStartingPositionY.matcher(configuration))),
                    new Velocity(extractInt(getVelocityX.matcher(configuration)), extractInt(getVelocityY.matcher(configuration))))
            );
        }

        for (int i = 0; i < SECONDS; i++)
        {
            robots.forEach(Robot::move);
        }

        final int robotsInTopLeftQuadrant = (int) robots.stream().filter(r -> r.getQuadrant().equals(Quadrant.TOP_LEFT)).count();
        final int robotsInTopRightQuadrant = (int) robots.stream().filter(r -> r.getQuadrant().equals(Quadrant.TOP_RIGHT)).count();
        final int robotsInBottomLeftQuadrant = (int) robots.stream().filter(r -> r.getQuadrant().equals(Quadrant.BOTTOM_LEFT)).count();
        final int robotsInBottomRightQuadrant = (int) robots.stream().filter(r -> r.getQuadrant().equals(Quadrant.BOTTOM_RIGHT)).count();

        final int totalSafetyFactor = robotsInTopLeftQuadrant * robotsInTopRightQuadrant * robotsInBottomLeftQuadrant * robotsInBottomRightQuadrant;

        LOGGER.info("The total safety factor after {}s is equal to: {}, calculated in {}ms.", SECONDS, totalSafetyFactor, System.currentTimeMillis() - start);
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

        public Robot(final Position currentPosition, final Velocity velocity)
        {
            this.currentPosition = currentPosition;
            this.velocity = velocity;
        }

        private void move()
        {
            int newYPosition = (currentPosition.yPosition + velocity.yVelocity + HEIGHT) % HEIGHT;
            int newXPosition = (currentPosition.xPosition + velocity.xVelocity + WIDTH) % WIDTH;

            currentPosition = new Position(newXPosition, newYPosition);
        }

        public Quadrant getQuadrant()
        {
            if (currentPosition.xPosition < ((WIDTH - 1) / 2) && currentPosition.yPosition < ((HEIGHT - 1) / 2))
            {
                return Quadrant.TOP_LEFT;
            }
            if (currentPosition.xPosition > ((WIDTH - 1) / 2) && currentPosition.yPosition < ((HEIGHT - 1) / 2))
            {
                return Quadrant.TOP_RIGHT;
            }
            if (currentPosition.xPosition < ((WIDTH - 1) / 2) && currentPosition.yPosition > ((HEIGHT - 1) / 2))
            {
                return Quadrant.BOTTOM_LEFT;
            }
            if (currentPosition.xPosition > ((WIDTH - 1) / 2) && currentPosition.yPosition > ((HEIGHT - 1) / 2))
            {
                return Quadrant.BOTTOM_RIGHT;
            }
            return Quadrant.NO_QUADRANT;
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

    private enum Quadrant
    {
        TOP_LEFT,
        TOP_RIGHT,
        BOTTOM_LEFT,
        BOTTOM_RIGHT,
        NO_QUADRANT;
    }
}
