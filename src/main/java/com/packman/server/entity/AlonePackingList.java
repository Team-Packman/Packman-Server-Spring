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
@Table(name = "alone_packing_list")
public class AlonePackingList {
    @Id
    @Column(name = "alone_packing_list_id", nullable = false, unique = true)
    private Long id;

    @OneToOne(fetch = LAZY)
    @MapsId
    @JoinColumn(name = "packingList_id")
    private PackingList packingList;

    @Column(nullable = false)
    private boolean isAloned = false;
    @Column(length = 5, unique = true)
    private String inviteCode;

    @OneToMany(mappedBy = "alone_packing_list", cascade = CascadeType.ALL)
    private List<Template> templates = new ArrayList<>();

    @OneToMany(mappedBy = "alone_packing_list", cascade = CascadeType.ALL)
    private List<FolderPackingList> folderPackingLists = new ArrayList<>();

    @OneToMany(mappedBy = "alone_packing_list", cascade = CascadeType.ALL)
    private List<TogetherAlonePackingList> togetherAlonePackingLists = new ArrayList<>();
}
