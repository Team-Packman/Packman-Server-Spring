package packman.dto.folder;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class FolderRequestDto {
    @NotNull
    private String name;
    @Getter(AccessLevel.NONE)
    @NotNull
    private Boolean isAloned;

    public boolean getIsAloned() {
        return this.isAloned;
    }
}
