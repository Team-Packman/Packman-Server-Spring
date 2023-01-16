package com.packman.server.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "template_pack")
public class TemplatePack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "template_pack_id", nullable = false, unique = true)
    private Long id;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "category_id")
    private TemplateCategory category;
    @Column(length = 12, nullable = false)
    private String name;
}
