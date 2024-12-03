package org.mljames.aoc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class PuzzleInputReader
{
    public static List<List<String>> readInput(final String resource)
    {
        return readInput(resource, Optional.empty());
    }

    public static List<List<String>> readInput(final String resource, final Optional<String> delimiterRegex)
    {
        try (final InputStream inputStream = PuzzleInputReader.class.getClassLoader().getResourceAsStream(resource);
             final InputStreamReader inputStreamReader = new InputStreamReader(Objects.requireNonNull(inputStream));
             final BufferedReader bufferedReader = new BufferedReader(inputStreamReader))
        {
            final List<List<String>> puzzleInput = new ArrayList<>();
            String line = bufferedReader.readLine();
            while (line != null && !line.isEmpty())
            {
                if (delimiterRegex.isPresent())
                {
                    final String[] spaceDelimitedLine = line.split(delimiterRegex.get());
                    puzzleInput.add(Arrays.asList(spaceDelimitedLine));
                }
                else
                {
                    puzzleInput.add(List.of(line));
                }
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
