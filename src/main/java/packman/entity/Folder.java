package packman.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import packman.dto.folder.FolderRequestDto;

import javax.persistence.*;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Folder extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "folder_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(length = 8, nullable = false)
    private String name;

    @Column(nullable = false)
    private boolean isAloned = true;

    @OneToMany(mappedBy = "folder", cascade = CascadeType.ALL)
    private List<FolderPackingList> folderPackingList;

    public Folder(FolderRequestDto folderRequestDto, User user) {
        this.user = user;
        this.name = folderRequestDto.getName();
        this.isAloned = folderRequestDto.getIsAloned();
    }
    @Transient
    @Getter(AccessLevel.NONE)
    private String listNum;
    public String getListNum() {
        return String.valueOf(folderPackingList.size());
    }
}
