package com.packman.server.entity;

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
@Table(name = "template_category")
public class TemplateCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "template_category_id", nullable = false, unique = true)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "template_id")
    private Template template;

    @Column(length = 12, nullable = false)
    private String name;

    @OneToMany(mappedBy = "template_pack", cascade = CascadeType.ALL)
    private List<TemplatePack> packs = new ArrayList<>();
}
