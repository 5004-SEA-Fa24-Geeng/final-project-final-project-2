package model;

import java.util.List;

/**
 * Interface defining operations for importing data.
 * Provides methods for importing influencer data from different sources.
 */
public interface IImporter {

    /**
     * Imports influencer data from a file path.
     *
     * @param filePath the path to the file containing the data
     * @return a list of imported Influencer objects
     */
    List<Influencer> importData(String filePath);

    /**
     * Imports influencer data directly from a file.
     * This method can be used for more specific file import operations.
     *
     * @param filePath the path to the file containing the data
     * @return a list of imported Influencer objects
     */
    List<Influencer> importFromFile(String filePath);

}