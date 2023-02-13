package packman.dto.list;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class DepartureDateRequestDto {
    @NotBlank
    private String id;
    @NotBlank
    private String departureDate;
    @NotNull
    @Getter(AccessLevel.NONE)
    private Boolean isAloned;

    public boolean getIsAloned() {
        return this.isAloned;
    }
}
