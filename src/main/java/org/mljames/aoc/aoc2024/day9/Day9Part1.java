package org.mljames.aoc.aoc2024.day9;

import org.mljames.aoc.PuzzleInputReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Day9Part1
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Day9Part1.class);

    public static void main(String[] args)
    {
        final List<String> input = PuzzleInputReader.readInput("aoc2024/day9/part1/puzzle_input.txt");

        final Memory memory = Memory.createMemory(input.getFirst());

        final MemoryUnit[] compressedMemoryUnits = memory.getCompressedMemoryUnits();

        long checksum = 0L;
        for (int i = 0; i < compressedMemoryUnits.length; i++)
        {
            checksum += (long) i * compressedMemoryUnits[i].fileId.orElse(0);
        }

        LOGGER.info("After compaction the filesystem checksum is equal to: {}.", checksum);
    }

    private static final class Memory
    {
        private final MemoryUnit[] memoryUnits;

        public Memory(final MemoryUnit[] memoryUnits)
        {
            this.memoryUnits = memoryUnits;
        }

        private static Memory createMemory(final String memoryString)
        {
            boolean isFree = false;
            int id = 0;
            final List<MemoryUnit> memoryUnits = new ArrayList<>();
            for (int i = 0; i < memoryString.length(); i++)
            {
                long n = Long.parseLong(Character.toString(memoryString.charAt(i)));
                if (!isFree)
                {
                    for (int j = 0; j < n; j++)
                    {
                        memoryUnits.add(new MemoryUnit(Optional.of(id)));
                    }
                    id += 1;
                }
                else
                {
                    for (int j = 0; j < n; j++)
                    {
                        memoryUnits.add(new MemoryUnit(Optional.empty()));
                    }
                }
                isFree = !isFree;
            }
            return new Memory(memoryUnits.toArray(MemoryUnit[]::new));
        }

        public MemoryUnit[] getCompressedMemoryUnits()
        {
            int replacedMemoryIndex = memoryUnits.length;
            for (int i = 0; i <= memoryUnits.length; i++)
            {
                if (replacedMemoryIndex <= i)
                {
                    break;
                }
                if (memoryUnits[i].fileId.isEmpty())
                {
                    for (int j = memoryUnits.length - 1; j >= i; j--)
                    {
                        if (memoryUnits[j].fileId.isPresent())
                        {
                            final MemoryUnit intermediateValue = memoryUnits[i];
                            memoryUnits[i] = memoryUnits[j];
                            memoryUnits[j] = intermediateValue;
                            replacedMemoryIndex = j;
                            break;
                        }
                    }
                }
            }
            return memoryUnits;
        }
    }

    private static class MemoryUnit
    {
        private final Optional<Integer> fileId;

        public MemoryUnit(final Optional<Integer> fileId)
        {
            this.fileId = fileId;
        }
    }
}
