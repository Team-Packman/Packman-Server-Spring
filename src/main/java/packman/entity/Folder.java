package packman.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "folder")
public class Folder extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "folder_id", nullable = false, unique = true)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(length = 8, nullable = false)
    private String name;

    @Column(nullable = false)
    private boolean isAloned = true;

    @Column(nullable = false)
    private int listNum = 0;

    @OneToOne(mappedBy = "folder_packing_list", cascade = CascadeType.ALL)
    private FolderPackingList folderPackingList;
}