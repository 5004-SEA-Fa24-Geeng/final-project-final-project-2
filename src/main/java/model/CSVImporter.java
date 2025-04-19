package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Importer implementation for CSV format.
 * Parses CSV files containing influencer data in the format:
 * name,platform,category,followers,country,adRate
 * Supports quoted fields and escaped quotes.
 *
 * @author Your Name
 * @version 1.0
 */
public class CSVImporter extends AbstractImporter {

    /**
     * Parses the content of a CSV file into a list of Influencer objects.
     * Handles header row detection and empty lines.
     *
     * @param content the CSV content to parse
     * @return a list of parsed Influencer objects
     * @throws IllegalArgumentException if content is null
     */
    @Override
    protected List<Influencer> parseData(String content) {
        List<Influencer> influencers = new ArrayList<>();

        if (content == null || content.isEmpty()) {
            return influencers;
        }

        String[] lines = content.split("\\n");

        // Skip the header row if it exists
        int startLine = 0;
        if (lines.length > 0 && lines[0].toLowerCase().contains("name") &&
                lines[0].toLowerCase().contains("platform")) {
            startLine = 1;
        }

        for (int i = startLine; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.isEmpty()) {
                continue;
            }

            try {
                Influencer influencer = parseCSVLine(line);
                if (influencer != null) {
                    influencers.add(influencer);
                }
            } catch (Exception e) {
                System.err.println("Error parsing line " + (i + 1) + ": " + line);
                e.printStackTrace();
            }
        }

        return influencers;
    }

    /**
     * Parses a single CSV line into an Influencer object.
     * Handles quoted fields and escaped quotes according to CSV standards.
     *
     * @param line the CSV line to parse
     * @return the parsed Influencer object, or null if parsing fails
     * @throws NumberFormatException if numeric fields cannot be parsed
     * @throws IllegalArgumentException if the line format is invalid
     */
    private Influencer parseCSVLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder field = new StringBuilder();
        boolean inQuotes = false;

        // Parse CSV taking into account quoted fields
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '"') {
                // If we're in quotes and the next character is also a quote, it's an escaped quote
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    field.append('"');
                    i++; // Skip the next quote
                } else {
                    // Toggle inQuotes state
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                // End of field
                fields.add(field.toString());
                field.setLength(0);
            } else {
                field.append(c);
            }
        }

        // Add the last field
        fields.add(field.toString());

        // Validate field count
        if (fields.size() < 6) {
            System.err.println("Invalid CSV line (not enough fields): " + line);
            return null;
        }

        try {
            String name = fields.get(0).trim();
            String platform = fields.get(1).trim();
            String category = fields.get(2).trim();
            int followerCount = Integer.parseInt(fields.get(3).trim());
            String country = fields.get(4).trim();
            double adRate = Double.parseDouble(fields.get(5).trim());

            return new Influencer(name, platform, category, followerCount, adRate, country);
        } catch (NumberFormatException e) {
            System.err.println("Error parsing numbers in CSV line: " + line);
            e.printStackTrace();
            return null;
        }
    }
}