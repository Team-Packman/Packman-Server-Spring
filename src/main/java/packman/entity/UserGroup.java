package packman.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import packman.entity.packingList.PackingList;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class UserGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_group_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    public UserGroup(User user, Group group) {
        this.user = user;
        this.group = group;
    }
}
