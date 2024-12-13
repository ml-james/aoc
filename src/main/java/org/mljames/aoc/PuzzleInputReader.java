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
    public static List<String> readInputAsStrings(final String resource)
    {
        try (final InputStream inputStream = PuzzleInputReader.class.getClassLoader().getResourceAsStream(resource);
             final InputStreamReader inputStreamReader = new InputStreamReader(Objects.requireNonNull(inputStream));
             final BufferedReader bufferedReader = new BufferedReader(inputStreamReader))
        {
            final List<String> puzzleInput = new ArrayList<>();
            String line = bufferedReader.readLine();
            while (line != null)
            {
                if (!line.isEmpty())
                {
                    puzzleInput.add(line);
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

    public static List<List<String>> readInputAsStrings(final String resource, final String delimiterRegex)
    {
        try (final InputStream inputStream = PuzzleInputReader.class.getClassLoader().getResourceAsStream(resource);
             final InputStreamReader inputStreamReader = new InputStreamReader(Objects.requireNonNull(inputStream));
             final BufferedReader bufferedReader = new BufferedReader(inputStreamReader))
        {
            final List<List<String>> puzzleInput = new ArrayList<>();
            String line = bufferedReader.readLine();
            while (line != null && !line.isEmpty())
            {
                final String[] spaceDelimitedLine = line.split(delimiterRegex);
                puzzleInput.add(Arrays.asList(spaceDelimitedLine));

                line = bufferedReader.readLine();
            }
            return puzzleInput;
        }
        catch (IOException e)
        {
            throw new RuntimeException("Could not read resource file", e);
        }
    }

    public static List<List<Integer>> readInputAsInts(final String resource, final String delimiterRegex)
    {
        try (final InputStream inputStream = PuzzleInputReader.class.getClassLoader().getResourceAsStream(resource);
             final InputStreamReader inputStreamReader = new InputStreamReader(Objects.requireNonNull(inputStream));
             final BufferedReader bufferedReader = new BufferedReader(inputStreamReader))
        {
            final List<List<Integer>> puzzleInput = new ArrayList<>();
            String line = bufferedReader.readLine();
            while (line != null && !line.isEmpty())
            {
                final String[] delimitedLine = line.split(delimiterRegex);
                puzzleInput.add(Arrays.stream(delimitedLine).map(Integer::parseInt).collect(Collectors.toList()));

                line = bufferedReader.readLine();
            }
            return puzzleInput;
        }
        catch (IOException e)
        {
            throw new RuntimeException("Could not read resource file", e);
        }
    }

    public static List<List<Long>> readInputAsLongs(final String resource, final String delimiterRegex)
    {
        try (final InputStream inputStream = PuzzleInputReader.class.getClassLoader().getResourceAsStream(resource);
             final InputStreamReader inputStreamReader = new InputStreamReader(Objects.requireNonNull(inputStream));
             final BufferedReader bufferedReader = new BufferedReader(inputStreamReader))
        {
            final List<List<Long>> puzzleInput = new ArrayList<>();
            String line = bufferedReader.readLine();
            while (line != null && !line.isEmpty())
            {
                final String[] delimitedLine = line.split(delimiterRegex);
                puzzleInput.add(Arrays.stream(delimitedLine).map(Long::parseLong).collect(Collectors.toList()));

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
