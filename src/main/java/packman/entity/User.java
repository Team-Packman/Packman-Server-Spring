package packman.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import packman.dto.user.UserCreateDto;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import packman.entity.template.Template;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@DynamicUpdate
@Table(name = "users")
public class User extends TimeStamped implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(length = 50, nullable = false, unique = true)
    private String email;

    @Column(length = 4, nullable = false)
    private String nickname;

    @Column(length = 1, nullable = false)
    private String profileImage;

    @Column(nullable = false)
    private boolean isDeleted = false;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String refreshToken;

    @Column(length = 1, nullable = false)
    private String path = "0";

    @Column(length = 6)
    private String gender = null;

    @Column(length = 5)
    private String ageRange = null;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Folder> folders = new ArrayList<>();

    @OneToMany(mappedBy = "packer", cascade = CascadeType.ALL)
    private List<Pack> packs = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Template> templates = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserGroup> userGroups = new ArrayList<>();

    public User(UserCreateDto userCreateDto, String refreshToken) {
        this.email = userCreateDto.getEmail();
        this.nickname = userCreateDto.getNickname();
        this.profileImage = userCreateDto.getProfileImage();
        this.name = userCreateDto.getName();
        this.path = userCreateDto.getPath();
        this.gender = userCreateDto.getGender();
        this.ageRange = userCreateDto.getAgeRange();
        this.refreshToken = refreshToken;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return String.valueOf(id);
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
