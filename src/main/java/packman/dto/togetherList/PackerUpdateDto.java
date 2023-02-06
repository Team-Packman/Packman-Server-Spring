package packman.dto.togetherList;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class PackerUpdateDto {
    @NotBlank
    String listId;
    @NotBlank
    String packId;
    @NotBlank
    String packerId;
}
