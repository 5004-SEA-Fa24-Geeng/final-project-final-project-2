package model;

import java.util.List;

/**
 * Interface defining operations for importing data.
 */
public interface IImporter {
    /**
     * Imports data from the specified file path.
     *
     * @param filePath the path of the file to import
     * @return a list of imported Influencer objects, or empty list if import failed
     */
    List<Influencer> importData(String filePath);
} 