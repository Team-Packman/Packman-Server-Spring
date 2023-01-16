package packman.entity.packingList;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "together_alone_packing_list")
public class TogetherAlonePackingList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "together_alone_packing_list_id", nullable = false, unique = true)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "togetherPackingList_id", nullable = false)
    private TogetherPackingList togetherPackingList;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "myPackingList_id", nullable = false, unique = true)
    private AlonePackingList myPackingList;
}
