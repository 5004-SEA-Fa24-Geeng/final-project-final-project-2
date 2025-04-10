package model;

import java.util.List;

public interface IImporter {

    List<Influencer> importData(String filePath);
}
