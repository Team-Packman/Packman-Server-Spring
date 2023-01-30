package packman.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Pack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pack_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User packer;

    @Column(length = 12, nullable = false)
    private String name;
    @Getter(AccessLevel.NONE)
    @Column(nullable = false)
    private boolean isChecked = false;

    public boolean getIsChecked() {
        return this.isChecked;
    }

    public Pack(Category category, String name) {
        this.category = category;
        this.name = name;
    }
}
