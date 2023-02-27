package packman.dto.togetherList;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TogetherListInviteResponseDto {
    private String id;
    @JsonProperty("isMember")
    private boolean isMember;

    public TogetherListInviteResponseDto(String id, boolean isMember) {
        this.id = id;
        this.isMember = isMember;
    }
}
