package packman.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
}
