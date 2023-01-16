package packman.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import packman.entity.template.Template;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
public class User extends TimeStamped {

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
    private String age_range = null;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Folder> folders = new ArrayList<>();

    @OneToMany(mappedBy = "packer", cascade = CascadeType.ALL)
    private List<Pack> packs = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Template> templates = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserGroup> userGroups = new ArrayList<>();
}
