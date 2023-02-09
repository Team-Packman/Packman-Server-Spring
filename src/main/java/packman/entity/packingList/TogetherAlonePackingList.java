package packman.entity.packingList;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import packman.entity.Folder;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@NoArgsConstructor
@DynamicUpdate
@Table(name = "together_alone_packing_list")
public class TogetherAlonePackingList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "together_alone_packing_list_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "together_packing_list_id", nullable = false)
    private TogetherPackingList togetherPackingList;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "my_packing_list_id", nullable = false, unique = true)
    private AlonePackingList alonePackingList;

    public TogetherAlonePackingList(TogetherPackingList togetherList, AlonePackingList aloneList){
        this.togetherPackingList = togetherList;
        this.alonePackingList = aloneList;
    }
}
