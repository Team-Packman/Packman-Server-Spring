package packman.dto.list;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ListTitleRequestDto {
    private String id;
    private String title;
    @Getter(AccessLevel.NONE)
    private Boolean isAloned;

    public boolean getIsAloned() {
        return this.isAloned;
    }
}
