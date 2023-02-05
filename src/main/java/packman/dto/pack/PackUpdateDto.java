package packman.dto.pack;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class PackUpdateDto {
    @NotBlank
    String id;

    @NotEmpty
    String name;

    @Getter(AccessLevel.NONE)
    @NotNull
    private Boolean isChecked;

    @NotBlank
    String listId;

    @NotBlank
    String categoryId;

    public boolean getIsChecked() {
        return this.isChecked;
    }
}
