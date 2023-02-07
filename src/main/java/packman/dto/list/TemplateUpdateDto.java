package packman.dto.list;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class TemplateUpdateDto {
    @NotBlank
    String id;
    @Getter(AccessLevel.NONE)
    @NotNull
    boolean isSaved;
    @Getter(AccessLevel.NONE)
    @NotNull
    boolean isAloned;

    public boolean getIsSaved() {
        return this.isSaved;
    }
    public boolean getIsAloned() {
        return this.isAloned;
    }
}
