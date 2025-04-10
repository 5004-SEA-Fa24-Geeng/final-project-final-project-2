package model;

import java.util.List;


public interface IExporter {

    boolean export(List<Influencer> data, String filePath);
}
