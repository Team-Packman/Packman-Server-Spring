package packman.dto.alonelist;

import lombok.AllArgsConstructor;
import lombok.Data;
import packman.dto.category.AloneCategoryResponseDto;

import java.util.ArrayList;

@Data
@AllArgsConstructor
public class AloneListResponseDto {

    private String id;

    private String folderId;

    private String title;

    private String departureDate;

    private ArrayList<AloneCategoryResponseDto> category;

    private String inviteCode;

    private Boolean isSaved;
}
