package packman.dto.togetherList;

import lombok.Data;

@Data
public class TogetherListInviteResponseDto {
    private String id;
    private boolean isMember;

    public TogetherListInviteResponseDto(String id, boolean isMember) {
        this.id = id;
        this.isMember = isMember;
    }
}
