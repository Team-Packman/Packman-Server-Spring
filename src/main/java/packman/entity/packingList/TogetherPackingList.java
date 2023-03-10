package packman.entity.packingList;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import packman.entity.Group;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "together_packing_list")
public class TogetherPackingList {

    @Id
    @Column(name = "together_packing_list_id")
    private Long id;

    @OneToOne(fetch = LAZY)
    @MapsId
    @JoinColumn(name = "together_packing_list_id")
    private PackingList packingList;

    @Column(length = 5, nullable = false, unique = true)
    private String inviteCode;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    @OneToMany(mappedBy = "togetherPackingList", cascade = CascadeType.ALL)
    private List<TogetherAlonePackingList> togetherAlonePackingLists = new ArrayList<>();

    public TogetherPackingList(PackingList packingList, Group group, String inviteCode){
        this.packingList = packingList;
        this.group = group;
        this.inviteCode = inviteCode;
    }
}
