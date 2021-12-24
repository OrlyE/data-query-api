package api;

import java.util.List;

public interface DataQueryService {
    List<Item> query(String query) throws Exception;

    void save(Item Item) throws Exception;
}

