package model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class implementing the IImporter interface.
 * Provides common functionality for importing influencer data from files.
 * This class handles file reading and path resolution, while leaving
 * the actual data parsing to concrete subclasses.
 *
 * @author Your Name
 * @version 1.0
 */
public abstract class AbstractImporter implements IImporter {

    /**
     * Imports influencers from a file.
     * This method reads the file content and delegates parsing to the
     * concrete implementation's parseData method.
     *
     * @param filePath the path to the file containing influencer data
     * @return a list of Influencer objects
     * @throws IllegalArgumentException if the file path is null or empty
     * @throws RuntimeException if there is an error reading the file
     */
    @Override
    public List<Influencer> importFromFile(String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("File path cannot be null or empty");
        }

        try {
            Path path = Paths.get(filePath);
            String content = Files.readString(path);
            return parseData(content);
        } catch (IOException e) {
            throw new RuntimeException("Error reading file: " + filePath, e);
        }
    }

    /**
     * Imports influencer data from the specified file path.
     * Handles both relative and absolute paths, converting relative paths
     * to absolute paths based on the current working directory.
     *
     * @param filePath the path to the file containing influencer data
     * @return a list of Influencer objects, or null if there was an error
     * @throws IllegalArgumentException if the file path is null or empty
     */
    @Override
    public List<Influencer> importData(String filePath) {
        try {
            // Convert to absolute path if it's relative
            Path path = Paths.get(filePath);
            if (!path.isAbsolute()) {
                path = Paths.get(System.getProperty("user.dir"), filePath);
            }

            String content = Files.readString(path);
            return parseData(content);
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            return null;
        }
    }

    /**
     * Abstract method to parse the content of the file into a list of Influencer objects.
     * This method must be implemented by concrete subclasses to handle
     * specific file formats and data structures.
     *
     * @param content the string content of the file to parse
     * @return a list of Influencer objects
     * @throws RuntimeException if there is an error parsing the content
     */
    protected abstract List<Influencer> parseData(String content);
}