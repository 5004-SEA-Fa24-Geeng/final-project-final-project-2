package model;

import java.util.List;
import java.util.StringJoiner;

/**
 * Exporter implementation for CSV format.
 */
public class CSVExporter extends AbstractExporter {
    
    private static final String CSV_HEADER = "Name,Platform,Category,FollowerCount,Country,AdRate";
    private static final String CSV_DELIMITER = ",";
    
    @Override
    protected String formatData(List<Influencer> data) {
        if (data == null || data.isEmpty()) {
            return CSV_HEADER;
        }
        
        StringBuilder builder = new StringBuilder();
        builder.append(CSV_HEADER).append("\n");
        
        for (Influencer influencer : data) {
            StringJoiner joiner = new StringJoiner(CSV_DELIMITER);
            
            // Escape commas in fields by wrapping in quotes if necessary
            joiner.add(escapeCSV(influencer.getName()));
            joiner.add(escapeCSV(influencer.getPlatform()));
            joiner.add(escapeCSV(influencer.getCategory()));
            joiner.add(String.valueOf(influencer.getFollowerCount()));
            joiner.add(escapeCSV(influencer.getCountry()));
            joiner.add(String.valueOf(influencer.getAdRate()));
            
            builder.append(joiner).append("\n");
        }
        
        return builder.toString();
    }
    
    /**
     * Escapes CSV fields according to CSV formatting rules.
     * If a field contains commas, quotes, or newlines, it is wrapped in quotes and any quotes are escaped.
     *
     * @param field the field to escape
     * @return the escaped field
     */
    private String escapeCSV(String field) {
        if (field == null) {
            return "";
        }
        
        boolean needsQuotes = field.contains(",") || field.contains("\"") || field.contains("\n");
        
        if (needsQuotes) {
            // Replace any quotes with double quotes for escaping
            String escapedField = field.replace("\"", "\"\"");
            return "\"" + escapedField + "\"";
        }
        
        return field;
    }
} 