package model;

import java.util.List;

/**
 * Exporter implementation for JSON format.
 * Note: temporary stub implementation, a placeholder, yet to implement
 */
public class JSONExporter extends AbstractExporter {

    @Override
    protected String formatData(List<Influencer> data) {
        return "[]";
    }

}

