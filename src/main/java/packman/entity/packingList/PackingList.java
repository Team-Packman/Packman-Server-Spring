package packman.entity.packingList;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import packman.entity.Category;
import packman.entity.TimeStamped;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@DynamicUpdate
@Table(name = "packing_list")
public class PackingList extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "packing_list_id")
    private Long id;

    @Column(length = 12, nullable = false)
    private String title;

    @Column(nullable = false)
    private LocalDate departureDate;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @Column(nullable = false)
    private boolean isSaved = false;

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @Column(nullable = false)
    private boolean isDeleted = false;
    @OrderBy("id asc")
    @OneToMany(mappedBy = "packingList", cascade = CascadeType.ALL)
    private List<Category> category = new ArrayList<>();

    @OneToOne(mappedBy = "packingList", cascade = CascadeType.ALL)
    private AlonePackingList alonePackingList;

    @OneToOne(mappedBy = "packingList", cascade = CascadeType.ALL)
    private TogetherPackingList togetherPackingList;

    public void addCategory(Category category) {
        this.category.add(category);
    }

    public boolean getIsSaved() {
        return this.isSaved;
    }

    public boolean getIsDeleted() {
        return this.isDeleted;
    }
    public void setIsSaved(boolean isSaved) { this.isSaved = isSaved; }
    public void setIsDeleted(boolean isDeleted) { this.isDeleted = isDeleted;}

    public PackingList(String title, LocalDate departureDate) {
        this.title = title;
        this.departureDate = departureDate;
    }
}
