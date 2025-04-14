package model;

import java.util.List;

/**
 * Exporter implementation for JSON format.
 */
public class JSONExporter extends AbstractExporter {
    
    @Override
    protected String formatData(List<Influencer> data) {
        if (data == null || data.isEmpty()) {
            return "[]";
        }
        
        StringBuilder builder = new StringBuilder();
        builder.append("[\n");
        
        for (int i = 0; i < data.size(); i++) {
            Influencer influencer = data.get(i);
            builder.append("  {\n");
            builder.append("    \"name\": \"").append(escapeJSON(influencer.getName())).append("\",\n");
            builder.append("    \"platform\": \"").append(escapeJSON(influencer.getPlatform())).append("\",\n");
            builder.append("    \"category\": \"").append(escapeJSON(influencer.getCategory())).append("\",\n");
            builder.append("    \"followerCount\": ").append(influencer.getFollowerCount()).append(",\n");
            builder.append("    \"country\": \"").append(escapeJSON(influencer.getCountry())).append("\",\n");
            builder.append("    \"adRate\": ").append(influencer.getAdRate()).append("\n");
            builder.append("  }");
            
            if (i < data.size() - 1) {
                builder.append(",");
            }
            
            builder.append("\n");
        }
        
        builder.append("]");
        return builder.toString();
    }
    
    /**
     * Escapes special characters in JSON strings.
     *
     * @param str the string to escape
     * @return the escaped string
     */
    private String escapeJSON(String str) {
        if (str == null) {
            return "";
        }
        
        return str.replace("\\", "\\\\")
                 .replace("\"", "\\\"")
                 .replace("\b", "\\b")
                 .replace("\f", "\\f")
                 .replace("\n", "\\n")
                 .replace("\r", "\\r")
                 .replace("\t", "\\t");
    }
} 