package packman.dto.togetherList;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TogetherListInviteResponseDto {
    private String id;
    @JsonProperty("isMember")
    private boolean isMember;

    public TogetherListInviteResponseDto(String id, boolean isMember) {
        this.id = id;
        this.isMember = isMember;
    }
}
