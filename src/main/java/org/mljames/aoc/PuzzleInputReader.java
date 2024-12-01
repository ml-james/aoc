package org.mljames.aoc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PuzzleInputReader
{
    public static List<String[]> readInput(final String resource)
    {
        try (final InputStream inputStream = PuzzleInputReader.class.getClassLoader().getResourceAsStream(resource);
             final InputStreamReader inputStreamReader = new InputStreamReader(Objects.requireNonNull(inputStream));
             final BufferedReader bufferedReader = new BufferedReader(inputStreamReader))
        {
            final List<String[]> puzzleInput = new ArrayList<>();
            String line = bufferedReader.readLine();
            while (line != null && !line.isEmpty())
            {
                final String[] spaceDelimitedLine = line.split("\\s{3}");
                puzzleInput.add(spaceDelimitedLine);
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
