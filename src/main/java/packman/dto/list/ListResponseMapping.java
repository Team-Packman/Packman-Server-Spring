package packman.dto.list;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import packman.dto.category.CategoryInfo;

import java.util.List;

public interface ListResponseMapping {
    String getId();

    List<CategoryInfo> getCategory();
}
