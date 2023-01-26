package packman.dto.folder;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonPropertyOrder({ "id", "name", "listNum" })
public class FolderInfo {
    String id;
    String name;
    String listNum;

    public FolderInfo(String id, String name, String listNum) {
        this.id = id;
        this.name = name;
        this.listNum = listNum;
    }
}
