package packman.dto.folder;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
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
