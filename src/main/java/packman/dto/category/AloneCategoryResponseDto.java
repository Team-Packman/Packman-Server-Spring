package packman.dto.category;

import lombok.AllArgsConstructor;
import lombok.Data;
import packman.dto.pack.PackInfoDto;

import java.util.ArrayList;

@Data
@AllArgsConstructor
public class AloneCategoryResponseDto {
    private String id;
    private String name;
    private ArrayList<PackInfoDto> pack;

}
