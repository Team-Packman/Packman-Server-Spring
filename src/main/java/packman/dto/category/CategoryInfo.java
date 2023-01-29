package packman.dto.category;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import packman.dto.pack.PackInfo;

import java.util.List;

@JsonPropertyOrder({"id", "name", "pack"})
public interface CategoryInfo {
    String getId();

    String getName();

    List<PackInfo> getPack();
}
