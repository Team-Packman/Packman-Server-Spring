package packman.entity.template;

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
public class BasicTemplateCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "basic_template_category_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "basic_template_id")
    private BasicTemplate basicTemplate;

    @Column(length = 12, nullable = false)
    private String name;

    @OrderBy("id asc")
    @OneToMany(mappedBy = "basicTemplateCategory", cascade = CascadeType.ALL)
    private List<BasicTemplatePack> basicTemplatePacks = new ArrayList<>();

    public BasicTemplateCategory(BasicTemplate basicTemplate, String name){
        this.basicTemplate = basicTemplate;
        this.name = name;
    }
    public void addTemplatePack(BasicTemplatePack basicTemplatePacks) { this.basicTemplatePacks.add(basicTemplatePacks); }
}
