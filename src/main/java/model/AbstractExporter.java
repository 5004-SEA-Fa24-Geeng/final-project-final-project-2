package model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Abstract base class for exporters providing common functionality.
 */
public abstract class AbstractExporter implements IExporter {
    
    /**
     * Prepares data for export by converting it to the appropriate format.
     *
     * @param data the list of Influencer objects to export
     * @return the formatted data as a string
     */
    protected String prepareExport(List<Influencer> data) {
        return formatData(data);
    }
    
    @Override
    public boolean export(List<Influencer> data, String filePath) {
        if (data == null || filePath == null || filePath.isEmpty()) {
            return false;
        }
        
        try {
            String exportData = prepareExport(data);
            File file = new File(filePath);
            
            // Create parent directories if they don't exist
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }
            
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(exportData);
            }
            
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Formats the data for export.
     * Each concrete exporter should implement this method according to its format.
     *
     * @param data the list of Influencer objects to format
     * @return the formatted data as a string
     */
    protected abstract String formatData(List<Influencer> data);
} 