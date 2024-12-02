package org.mljames.aoc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PuzzleInputReader
{
    public static List<List<Integer>> readInput(final String resource, final String delimiterRegex)
    {
        try (final InputStream inputStream = PuzzleInputReader.class.getClassLoader().getResourceAsStream(resource);
             final InputStreamReader inputStreamReader = new InputStreamReader(Objects.requireNonNull(inputStream));
             final BufferedReader bufferedReader = new BufferedReader(inputStreamReader))
        {
            final List<List<Integer>> puzzleInput = new ArrayList<>();
            String line = bufferedReader.readLine();
            while (line != null && !line.isEmpty())
            {
                final String[] spaceDelimitedLine = line.split(delimiterRegex);
                final List<Integer> rowAsIntegers = Arrays.stream(spaceDelimitedLine).map(Integer::parseInt).collect(Collectors.toList());
                puzzleInput.add(rowAsIntegers);
                line = bufferedReader.readLine();
            }
            return puzzleInput;
        }
        catch (IOException e)
        {
            throw new RuntimeException("Could not read resource file", e);
        }
    }
}
