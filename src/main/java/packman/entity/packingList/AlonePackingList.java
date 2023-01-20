package packman.entity.packingList;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import packman.entity.FolderPackingList;
import packman.entity.template.Template;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class AlonePackingList {
    @Id
    @Column(name = "alone_packing_list_id")
    private Long id;

    @OneToOne(fetch = LAZY)
    @MapsId
    @JoinColumn(name = "packing_list_id")
    private PackingList packingList;

    @Column(nullable = false)
    private boolean isAloned = false;

    @Column(length = 5, unique = true)
    private String inviteCode;

    @OneToOne(mappedBy = "alonePackingList", cascade = CascadeType.ALL)
    private Template template;

    @OneToOne(mappedBy = "alonePackingList", cascade = CascadeType.ALL)
    private FolderPackingList folderPackingList;

    @OneToMany(mappedBy = "alonePackingList", cascade = CascadeType.ALL)
    private List<TogetherAlonePackingList> togetherAlonePackingLists = new ArrayList<>();
}