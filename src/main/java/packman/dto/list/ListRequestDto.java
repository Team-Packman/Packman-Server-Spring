package packman.dto.list;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Getter
@AllArgsConstructor
public class ListRequestDto {
    @NotBlank
    private String departureDate;
    @NotBlank
    private String folderId;
    @NotBlank
    private String title;
    @NotNull
    private String templateId;
}
