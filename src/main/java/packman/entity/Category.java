package packman.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import packman.entity.packingList.PackingList;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@NoArgsConstructor
@DynamicUpdate
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "packing_list_id")
    private PackingList packingList;

    @Column(length = 12, nullable = false)
    private String name;
    @OrderBy("id asc")
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Pack> pack = new ArrayList<>();

    public Category(PackingList packingList, String name) {
        this.packingList = packingList;
        this.name = name;
    }

    public void addPack(Pack pack) {
        this.pack.add(pack);
    }
}
