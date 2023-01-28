package packman.dto.folder;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"id", "name"})
public interface AloneFolderMapping {
    String getId();

    String getName();
}
