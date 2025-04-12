package model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractImporter implements IImporter {

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