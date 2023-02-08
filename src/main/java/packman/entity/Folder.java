package packman.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import packman.dto.folder.FolderRequestDto;

import javax.persistence.*;
import javax.validation.constraints.Null;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@NoArgsConstructor
@DynamicUpdate
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
    @Getter(AccessLevel.NONE)
    private boolean isAloned = true;

    @OneToMany(mappedBy = "folder", cascade = CascadeType.ALL)
    private List<FolderPackingList> folderPackingList;

    public Folder(User user, String name, boolean isAloned) {
        this.user = user;
        this.name = name;
        this.isAloned = isAloned;
    }

    @Transient
    @Getter(AccessLevel.NONE)
    private String listNum;

    public String getListNum() {

        if (this.folderPackingList == null) {
            return "0";
        } else {
            return String.valueOf(folderPackingList.size());
        }
    }

    public boolean getIsAloned() {
        return this.isAloned;
    }
}
