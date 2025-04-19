package model;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Importer implementation for JSON format.
 * Parses JSON files containing influencer data in the format:
 * {
 *   "name": "value",
 *   "platform": "value",
 *   "category": "value",
 *   "followerCount": number,
 *   "country": "value",
 *   "adRate": number
 * }
 * Uses regex-based parsing for demonstration purposes.
 * For production code, consider using a proper JSON library like Jackson.
 *
 * @author Your Name
 * @version 1.0
 */
public class JSONImporter extends AbstractImporter {

    /**
     * Parses the content of a JSON file into a list of Influencer objects.
     * Uses regex to find and parse individual JSON objects.
     *
     * @param content the JSON content to parse
     * @return a list of parsed Influencer objects
     * @throws IllegalArgumentException if content is null
     */
    @Override
    protected List<Influencer> parseData(String content) {
        List<Influencer> influencers = new ArrayList<>();

        if (content == null || content.isEmpty()) {
            return influencers;
        }

        // Simple JSON parsing using regex for demonstration
        // For production code, consider using a proper JSON library like Jackson

        // Find each JSON object
        String jsonObjectPattern = "\\{[^\\{\\}]*\\}";
        Pattern pattern = Pattern.compile(jsonObjectPattern);
        Matcher matcher = pattern.matcher(content);

        while (matcher.find()) {
            String jsonObject = matcher.group();
            try {
                Influencer influencer = parseJSONObject(jsonObject);
                if (influencer != null) {
                    influencers.add(influencer);
                }
            } catch (Exception e) {
                System.err.println("Error parsing JSON object: " + jsonObject);
                e.printStackTrace();
            }
        }

        return influencers;
    }

    /**
     * Parses a single JSON object string into an Influencer object.
     * Extracts and validates all required fields.
     *
     * @param jsonObject the JSON object string to parse
     * @return the parsed Influencer object, or null if parsing fails
     * @throws NumberFormatException if numeric fields cannot be parsed
     * @throws IllegalArgumentException if required fields are missing
     */
    private Influencer parseJSONObject(String jsonObject) {
        // Extract each field using regex
        String name = extractJsonField(jsonObject, "name");
        String platform = extractJsonField(jsonObject, "platform");
        String category = extractJsonField(jsonObject, "category");
        String followerCountStr = extractJsonField(jsonObject, "followerCount");
        String country = extractJsonField(jsonObject, "country");
        String adRateStr = extractJsonField(jsonObject, "adRate");

        // Validate fields
        if (name == null || platform == null || category == null ||
                followerCountStr == null || country == null || adRateStr == null) {
            System.err.println("Missing required fields in JSON object: " + jsonObject);
            return null;
        }

        try {
            int followerCount = Integer.parseInt(followerCountStr);
            double adRate = Double.parseDouble(adRateStr);

            return new Influencer(name, platform, category, followerCount, adRate, country);
        } catch (NumberFormatException e) {
            System.err.println("Error parsing numbers in JSON object: " + jsonObject);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Extracts a field value from a JSON object string using regex.
     * Supports both quoted and unquoted values.
     *
     * @param jsonObject the JSON object string to search
     * @param fieldName the name of the field to extract
     * @return the extracted field value, or null if not found
     */
    private String extractJsonField(String jsonObject, String fieldName) {
        // Pattern to match "fieldName": "value" or "fieldName": value
        String pattern = "\"" + fieldName + "\"\\s*:\\s*\"?([^\",}]*)\"?";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(jsonObject);

        if (m.find()) {
            return m.group(1);
        }

        return null;
    }
}