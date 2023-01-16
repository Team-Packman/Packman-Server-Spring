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
public class TemplatePack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "template_pack_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "template_category_id")
    private TemplateCategory templateCategory;

    @Column(length = 12, nullable = false)
    private String name;
}
