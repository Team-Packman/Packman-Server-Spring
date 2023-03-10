package packman.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import packman.entity.packingList.AlonePackingList;
import packman.entity.packingList.PackingList;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class FolderPackingList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "folder_packing_list_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "folder_id")
    private Folder folder;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "alone_packing_list_id")
    private AlonePackingList alonePackingList;

    public FolderPackingList(Folder folder, AlonePackingList aloneList){
        this.folder = folder;
        this.alonePackingList = aloneList;
    }
}
