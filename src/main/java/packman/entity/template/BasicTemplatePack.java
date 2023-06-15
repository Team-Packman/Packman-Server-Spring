package packman.entity.template;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class BasicTemplatePack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "basic_template_pack_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "basic_template_category_id")
    private BasicTemplateCategory basicTemplateCategory;

    @Column(length = 12, nullable = false)
    private String name;

    public BasicTemplatePack(BasicTemplateCategory basicTemplateCategory, String name){
        this.basicTemplateCategory = basicTemplateCategory;
        this.name = name;
    }
}
