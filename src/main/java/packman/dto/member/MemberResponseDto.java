package packman.dto.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class MemberResponseDto {
    private String title;
    private String departureDate;
    private String remainDay;
    private List<MemberUserDto> member;
    private String inviteCode;
}