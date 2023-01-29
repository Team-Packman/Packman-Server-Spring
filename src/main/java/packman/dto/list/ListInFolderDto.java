package packman.dto.list;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ListInFolderDto {
    private String id;

    private String title;

    private String departureDate;

    private String packTotalNum;

    private String packRemainNum;
}
