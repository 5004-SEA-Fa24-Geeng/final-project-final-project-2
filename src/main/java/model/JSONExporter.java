package model;

import java.util.List;

/**
 * Exporter implementation for JSON format.
 */
public class JSONExporter extends AbstractExporter {

    /**
     * Formats influencer data into JSON format.
     *
     * @param data list of influencers to be exported
     * @return formatted JSON string
     */
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
            builder.append("    \"name\": \"").append(escapeJson(influencer.getName())).append("\",\n");
            builder.append("    \"platform\": \"").append(escapeJson(influencer.getPlatform())).append("\",\n");
            builder.append("    \"category\": \"").append(escapeJson(influencer.getCategory())).append("\",\n");
            builder.append("    \"followers\": ").append(influencer.getFollowers()).append(",\n");
            builder.append("    \"adRate\": ").append(influencer.getAdRate()).append(",\n");
            builder.append("    \"country\": \"").append(escapeJson(influencer.getCountry())).append("\"\n");
            builder.append("  }");

            // Add comma for all items except the last one
            if (i < data.size() - 1) {
                builder.append(",");
            }
            builder.append("\n");
        }

        builder.append("]");
        return builder.toString();
    }

    /**
     * Escapes special characters in strings for JSON format.
     *
     * @param text the text to escape
     * @return the escaped text
     */
    private String escapeJson(String text) {
        if (text == null) {
            return "";
        }

        return text
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\b", "\\b")
                .replace("\f", "\\f")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}

