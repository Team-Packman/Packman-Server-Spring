package packman.dto.list;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class TemplateUpdateDto {
    @NotBlank
    String id;
    @NotNull
    boolean isSaved;
    @NotNull
    boolean isAloned;
}
