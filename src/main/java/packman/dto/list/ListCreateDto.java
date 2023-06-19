package packman.dto.list;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class ListCreateDto {
    @NotBlank
    private String departureDate;
    @NotBlank
    private String folderId;
    @NotBlank
    private String title;
    @NotNull
    private String templateId;
    @NotNull
    @Getter(AccessLevel.NONE)
    private Boolean isBasic;

    public boolean getIsBasic() {
        return this.isBasic;
    }
}
