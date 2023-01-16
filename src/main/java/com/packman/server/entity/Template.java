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
@Table(name = "template")
public class Template {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "template_id", nullable = false, unique = true)
    private Long id;

    @Column(nullable = false)
    private boolean isAloned = true;

    @Column(length = 8, nullable = false)
    private String title;

    @Column(nullable = false)
    private boolean isDeleted = false;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "packing_list_id")
    private PackingList packingList;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "template_category", cascade = CascadeType.ALL)
    private List<TemplateCategory> categorys = new ArrayList<>();
}
