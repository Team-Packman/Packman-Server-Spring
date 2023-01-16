package packman.entity.template;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import packman.entity.User;
import packman.entity.packingList.AlonePackingList;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Template {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "template_id")
    private Long id;

    @Column(nullable = false)
    private boolean isAloned = true;

    @Column(length = 8, nullable = false)
    private String title;

    @Column(nullable = false)
    private boolean isDeleted = false;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "alone_packing_list_id")
    private AlonePackingList alonePackingList;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "template", cascade = CascadeType.ALL)
    private List<TemplateCategory> categories = new ArrayList<>();
}
