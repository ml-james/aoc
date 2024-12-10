package org.mljames.aoc.aoc2024.day9;

import org.mljames.aoc.PuzzleInputReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Day9Part2
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Day9Part2.class);

    public static void main(String[] args)
    {
        final List<String> input = PuzzleInputReader.readInput("aoc2024/day9/part2/puzzle_input.txt");

        final Memory memory = Memory.createMemory(input.getFirst());

        final MemoryUnit[] compressedMemoryUnits = memory.getCompressedMemoryUnits();

        long checksum = 0L;
        for (int i = 0; i < compressedMemoryUnits.length; i++)
        {
            final Optional<File> file = compressedMemoryUnits[i].file;
            if (file.isPresent())
            {
                checksum += (long) i * file.get().fileId;
            }
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
                int n = Integer.parseInt(Character.toString(memoryString.charAt(i)));
                if (!isFree)
                {
                    for (int j = 0; j < n; j++)
                    {
                        memoryUnits.add(new MemoryUnit(Optional.of(new File(id, n, j))));
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

        private MemoryUnit[] getCompressedMemoryUnits()
        {
            for (int i = memoryUnits.length - 1; i >= 0; i--)
            {
                if (memoryUnits[i].file.isPresent())
                {
                    int fileSize = memoryUnits[i].file.get().size;
                    for (int j = 0; j < i - 1; j++)
                    {
                        if (memoryUnits[j].file.isEmpty())
                        {
                            final int freeMemoryRegionSize = findFreeMemoryRegionSize(memoryUnits, j);
                            if (freeMemoryRegionSize >= fileSize)
                            {
                                for (int k = 1; k <= fileSize; k++)
                                {
                                    final MemoryUnit intermediateValue = memoryUnits[i - fileSize + k];
                                    memoryUnits[i - fileSize + k] = memoryUnits[j + k - 1];
                                    memoryUnits[j + k - 1] = intermediateValue;
                                }
                                break;
                            }
                        }
                    }
                }
            }
            return memoryUnits;
        }

        private int findFreeMemoryRegionSize(final MemoryUnit[] memoryUnits, final int i)
        {
            int pointer = i;
            int counter = 0;
            while (pointer < memoryUnits.length && memoryUnits[pointer].file.isEmpty())
            {
                counter += 1;
                pointer += 1;
            }
            return counter;
        }
    }

    private static class MemoryUnit
    {
        private final Optional<File> file;

        public MemoryUnit(final Optional<File> file)
        {
            this.file = file;
        }
    }

    private static class File
    {
        private final int fileId;
        private final int size;
        private final int subsection;

        private File(final int fileId, final int size, final int subsection)
        {
            this.fileId = fileId;
            this.size = size;
            this.subsection = subsection;
        }

        @Override
        public String toString()
        {
            return "File{" +
                    "fileId=" + fileId +
                    ", size=" + size +
                    ", unit=" + subsection +
                    '}';
        }
    }
}
