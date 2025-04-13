package model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractImporter implements IImporter {

    // Imports influencers from a file.
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


    protected abstract List<Influencer> parseData(String content);

    @Override
    public List<Influencer> importData(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return new ArrayList<>();
        }

        try {
            String content = Files.readString(Path.of(filePath));
            return parseData(content);
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}