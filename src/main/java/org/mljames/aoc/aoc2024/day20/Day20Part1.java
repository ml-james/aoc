package org.mljames.aoc.aoc2024.day20;

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
import java.util.Queue;
import java.util.Set;

public class Day20Part1
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Day20Part1.class);

    public static void main(String[] args)
    {
        final long start = System.currentTimeMillis();

        final List<String> input = PuzzleInputReader.readInputAsStrings("aoc2024/day20/part2/puzzle_input.txt");

        final int width = input.getFirst().length();
        final int height = input.size();

        final Racetrack racetrack = Racetrack.create(input, height, width);

        final Map<Position, Integer> noCheatBestPositionTimes = new HashMap<>(Map.of(racetrack.start, 0));
        final Race noCheatRace = racetrack.race(noCheatBestPositionTimes, racetrack.start, 0);

        final List<Cheat> allCheats = new ArrayList<>();
        for (final Map.Entry<Position, Integer> entry : noCheatRace.racePositions.entrySet())
        {
            allCheats.addAll(racetrack.cheat(
                    entry.getKey(),
                    entry.getValue(),
                    2,
                    noCheatBestPositionTimes,
                    noCheatRace.racePositions.keySet()));
        }

        if (false)
        {
            allCheats.forEach(c -> printRoute(racetrack, noCheatRace, c));
        }

        LOGGER.info("The number of cheats that would save 100 picoseconds are: {}, calculated in {}ms.",
                allCheats.stream().filter(c -> c.picosecondsSaved >= 100).toList().size(),
                System.currentTimeMillis() - start);
    }

    private static void printRoute(
            final Racetrack racetrack,
            final Race race,
            final Cheat cheat)
    {
        LOGGER.info("Printing cheat {},", cheat);
        final Set<Position> routePositions = race.racePositions.keySet();
        for (int y = 0; y < racetrack.height; y++)
        {
            for (int x = 0; x < racetrack.width; x++)
            {
                if (racetrack.track[y][x] == 'E')
                {
                    System.out.print(racetrack.track[y][x]);
                }
                else if (racetrack.track[y][x] == 'S')
                {
                    System.out.print(racetrack.track[y][x]);
                }
                else if (cheat.start.x == x && cheat.start.y == y)
                {
                    System.out.print("C");
                }
                else if (cheat.end.x == x && cheat.end.y == y)
                {
                    System.out.print("D");
                }
                else if (ifPositionOnBestRoute(routePositions, x, y))
                {
                    System.out.print("X");
                }
                else
                {
                    System.out.print(racetrack.track[y][x]);
                }
            }
            System.out.print("\n");
        }
        System.out.print("\n\n");
    }

    private static boolean ifPositionOnBestRoute(final Set<Position> routePositions, final int x, final int y)
    {
        for (final Position routePosition : routePositions)
        {
            if (routePosition.x == x && routePosition.y == y)
            {
                return true;
            }
        }
        return false;
    }

    private static final class Racetrack
    {
        private final int height;
        private final int width;
        private final char[][] track;
        private final Position start;

        private static Racetrack create(final List<String> input, final int height, final int width)
        {
            Position start = null;
            final char[][] track = new char[height][width];
            for (int y = 0; y < height; y++)
            {
                for (int x = 0; x < width; x++)
                {
                    final char c = input.get(y).charAt(x);
                    if (c == 'S')
                    {
                        start = new Position(x, y);
                    }
                    track[y][x] = c;
                }
            }
            return new Racetrack(track, height, width, start);
        }

        private Racetrack(
                final char[][] track,
                final int height,
                final int width,
                final Position start)
        {
            this.track = track;
            this.height = height;
            this.width = width;
            this.start = start;
        }

        private boolean isWall(final Position position)
        {
            return track[position.y][position.x] == '#';
        }

        private boolean isEnd(final Position position)
        {
            return track[position.y][position.x] == 'E';
        }

        private boolean isInBounds(final Position position)
        {
            return position.x >= 0 && position.x < width && position.y >= 0 && position.y < height;
        }

        private Race race(
                final Map<Position, Integer> positionTimes,
                final Position startPosition,
                final int startScore)
        {
            final Queue<Race> queue = new PriorityQueue<>(Comparator.comparing((Race r) -> r.currentTime));
            queue.add(new Race(startPosition, startScore));

            while (!queue.isEmpty())
            {
                final Race raceCandidate = queue.poll();

                if (isEnd(raceCandidate.currentPosition))
                {
                    return raceCandidate;
                }

                exploreMove(raceCandidate, MovementType.FORWARD, queue, positionTimes);
                exploreMove(raceCandidate, MovementType.BACKWARD, queue, positionTimes);
                exploreMove(raceCandidate, MovementType.LEFT, queue, positionTimes);
                exploreMove(raceCandidate, MovementType.RIGHT, queue, positionTimes);
            }

            throw new RuntimeException("Could not find the endpoint!!");
        }

        private void exploreMove(
                final Race race,
                final MovementType movementType,
                final Queue<Race> queue,
                final Map<Position, Integer> bestPositionTimes)
        {
            final Race newRaceCandidate = Race.copy(race);
            newRaceCandidate.move(movementType);

            final Position currentPosition = newRaceCandidate.currentPosition;
            final int currentTime = newRaceCandidate.currentTime;

            if (isInBounds(currentPosition) && !isWall(currentPosition))
            {
                if (currentTime < bestPositionTimes.getOrDefault(currentPosition, Integer.MAX_VALUE))
                {
                    queue.add(newRaceCandidate);
                    bestPositionTimes.put(currentPosition, currentTime);
                }
            }
        }

        public List<Cheat> cheat(
                final Position cheatStart,
                final int cheatStartTime,
                final int cheatLength,
                final Map<Position, Integer> noCheatBestPositionTimes,
                final Set<Position> noCheatRacePositions)
        {
            final Queue<Race> queue = new PriorityQueue<>(Comparator.comparing((Race r) -> r.currentTime));
            queue.add(new Race(cheatStart, cheatStartTime));

            final List<Cheat> cheats = new ArrayList<>();
            final Set<Position> cheatPositionsVisited = new HashSet<>();
            while (!queue.isEmpty())
            {
                final Race routeCandidate = queue.poll();

                if (shouldRecordCheat(routeCandidate, noCheatBestPositionTimes, noCheatRacePositions))
                {
                    cheats.add(new Cheat(
                            cheatStart,
                            routeCandidate.currentPosition,
                            noCheatBestPositionTimes.get(routeCandidate.currentPosition) - routeCandidate.currentTime));

                }
                else
                {
                    exploreCheat(
                            routeCandidate,
                            MovementType.FORWARD,
                            queue,
                            cheatStartTime,
                            cheatLength,
                            cheatPositionsVisited);

                    exploreCheat(
                            routeCandidate,
                            MovementType.BACKWARD,
                            queue,
                            cheatStartTime,
                            cheatLength,
                            cheatPositionsVisited);

                    exploreCheat(
                            routeCandidate,
                            MovementType.LEFT,
                            queue,
                            cheatStartTime,
                            cheatLength,
                            cheatPositionsVisited);

                    exploreCheat(
                            routeCandidate,
                            MovementType.RIGHT,
                            queue,
                            cheatStartTime,
                            cheatLength,
                            cheatPositionsVisited);
                }
            }

            return cheats;
        }

        private void exploreCheat(
                final Race race,
                final MovementType movementType,
                final Queue<Race> queue,
                final int cheatStartTime,
                final int cheatLength,
                final Set<Position> cheatPositionsVisited)
        {
            final Race newRaceCandidate = Race.copy(race);
            newRaceCandidate.move(movementType);

            final Position currentPosition = newRaceCandidate.currentPosition;
            final int currentTime = newRaceCandidate.currentTime;

            if (isInBounds(currentPosition))
            {
                if (cheatActive(currentTime, cheatStartTime, cheatLength) && !cheatPositionsVisited.contains(currentPosition))
                {
                    cheatPositionsVisited.add(currentPosition);
                    queue.add(newRaceCandidate);
                }
            }
        }

        private boolean cheatActive(final int currentTime, final int cheatStartTime, final int cheatLength)
        {
            return currentTime - cheatStartTime <= cheatLength;
        }

        private boolean shouldRecordCheat(
                final Race routeCandidate,
                final Map<Position, Integer> noCheatBestPositionTimes,
                final Set<Position> noCheatRacePositions)
        {
            return noCheatRacePositions.contains(routeCandidate.currentPosition) &&
                    routeCandidate.currentTime < noCheatBestPositionTimes.get(routeCandidate.currentPosition);
        }
    }

    private static final class Cheat
    {
        private final Position start;
        private final Position end;
        private final int picosecondsSaved;

        private Cheat(final Position start, final Position end, final int picosecondsSaved)
        {
            this.start = start;
            this.end = end;
            this.picosecondsSaved = picosecondsSaved;
        }

        @Override
        public String toString()
        {
            return "Cheat{" +
                    "start=" + start +
                    ", end=" + end +
                    ", picosecondsSaved=" + picosecondsSaved +
                    '}';
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
            final Cheat cheat = (Cheat) o;
            return picosecondsSaved == cheat.picosecondsSaved && Objects.equals(start, cheat.start) && Objects.equals(end, cheat.end);
        }

        @Override
        public int hashCode()
        {
            return Objects.hash(start, end, picosecondsSaved);
        }
    }

    private enum MovementType
    {
        FORWARD, RIGHT, LEFT, BACKWARD
    }

    private static final class Race
    {
        private final Map<Position, Integer> racePositions;
        private Position currentPosition;
        private int currentTime;

        private Race(final Position currentPosition, final int currentTime)
        {
            this.racePositions = new HashMap<>(Map.of(currentPosition, currentTime));
            this.currentPosition = currentPosition;
            this.currentTime = currentTime;
        }

        private Race(final Map<Position, Integer> racePositions, final Position currentPosition, int currentTime)
        {
            this.racePositions = racePositions;
            this.currentPosition = currentPosition;
            this.currentTime = currentTime;
        }

        private static Race copy(final Race race)
        {
            return new Race(new HashMap<>(race.racePositions), race.currentPosition, race.currentTime);
        }

        void move(final MovementType movementType)
        {
            final Position newPosition = switch (movementType)
            {
                case LEFT -> currentPosition.move(MovementType.LEFT);
                case RIGHT -> currentPosition.move(MovementType.RIGHT);
                case FORWARD -> currentPosition.move(MovementType.FORWARD);
                case BACKWARD -> currentPosition.move(MovementType.BACKWARD);
            };

            currentTime += 1;
            currentPosition = newPosition;
            racePositions.put(currentPosition, currentTime);
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
            final Race race = (Race) o;
            return currentTime == race.currentTime && Objects.equals(racePositions, race.racePositions) && Objects.equals(currentPosition, race.currentPosition);
        }

        @Override
        public int hashCode()
        {
            return Objects.hash(racePositions, currentPosition, currentTime);
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

        private Position move(final MovementType movementType)
        {
            return switch (movementType)
            {
                case LEFT -> new Position(x - 1, y);
                case RIGHT -> new Position(x + 1, y);
                case FORWARD -> new Position(x, y + 1);
                case BACKWARD -> new Position(x, y - 1);
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
