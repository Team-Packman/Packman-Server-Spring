
package packman.dto.auth;

import lombok.Data;

@Data
public class KakaoProfileDto {

    public Long id;
    public String connected_at;
    public Properties properties;
    public KaKaoAccount kakao_account;

    @Data
    public static class Properties {
        public String nickname;
    }

    @Data
    public class KaKaoAccount {
        public Boolean profile_nickname_needs_agreement;
        public Profile profile;
        public Boolean has_email;
        public Boolean email_needs_agreement;
        public Boolean is_email_valid;
        public Boolean is_email_verified;
        public String email;
        public Boolean has_age_range;
        public Boolean age_range_needs_agreement;
        public String age_range;
        public Boolean has_gender;
        public Boolean gender_needs_agreement;
        public String gender;
    }

    @Data
    public static class Profile {
        public String nickname;
    }
}