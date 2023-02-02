package packman.dto.list;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class ListDto {
    @NotBlank
    private String departureDate;
    @NotBlank
    private String folderId;
    @NotBlank
    private String title;
    @NotNull
    private String templateId;
}
