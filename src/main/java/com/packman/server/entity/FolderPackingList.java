package com.packman.server.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
public class FolderPackingList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "folder_id", nullable = false, unique = true)
    private Long id;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "folder_id")
    private Folder folder;
    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "list_id")
    private PackingList packingList;
}
