package packman.dto.folder;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"id", "name"})
public interface FolderIdNameMapping {
    String getId();

    String getName();
}
