package packman.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import packman.entity.packingList.PackingList;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id", nullable = false, unique = true)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "list_id")
    private PackingList packingList;

    @Column(length = 12, nullable = false)
    private String name;

    @OneToMany(mappedBy = "pack", cascade = CascadeType.ALL)
    private List<Pack> packs = new ArrayList<>();

}
