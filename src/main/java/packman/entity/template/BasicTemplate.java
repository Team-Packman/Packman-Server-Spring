package packman.entity.template;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import packman.entity.TimeStamped;
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
public class BasicTemplate extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "basic_template_id")
    private Long id;

    @Column(nullable = false)
    private boolean isAloned = true;

    @Column(length = 12, nullable = false)
    private String title;

    @Column(nullable = false)
    private boolean isDeleted = false;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "alone_packing_list_id")
    private AlonePackingList alonePackingList;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OrderBy("id asc")
    @OneToMany(mappedBy = "basicTemplate", cascade = CascadeType.ALL)
    private List<BasicTemplateCategory> categories = new ArrayList<>();

    public BasicTemplate(boolean isAloned, String title, AlonePackingList alonePackingList, User user){
        this.isAloned = isAloned;
        this.title = title;
        this.alonePackingList = alonePackingList;
        this.user  = user;
    }

    public void addTemplateCategory(BasicTemplateCategory basicTemplateCategory) { this.categories.add(basicTemplateCategory); }
}
