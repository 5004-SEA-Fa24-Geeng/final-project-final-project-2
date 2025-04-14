package model;

import java.util.List;

/**
 * Interface defining operations for exporting data.
 */
public interface IExporter {
    /**
     * Exports the provided data to the specified file path.
     *
     * @param data     the list of Influencer objects to export
     * @param filePath the path where the export file should be saved
     * @return true if the export was successful, false otherwise
     */
    boolean export(List<Influencer> data, String filePath);
} 