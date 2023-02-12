package packman.dto.auth;

import lombok.Data;

@Data
public class KaKaoAccount {
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
