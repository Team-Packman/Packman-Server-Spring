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
public class TemplateCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "template_category_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "template_id")
    private Template template;

    @Column(length = 12, nullable = false)
    private String name;

    @OrderBy("id asc")
    @OneToMany(mappedBy = "templateCategory", cascade = CascadeType.ALL)
    private List<TemplatePack> templatePacks = new ArrayList<>();

    public TemplateCategory(Template template, String name){
        this.template = template;
        this.name = name;
    }
    public void addTemplatePack(TemplatePack templatePack) { this.templatePacks.add(templatePack); }
}
