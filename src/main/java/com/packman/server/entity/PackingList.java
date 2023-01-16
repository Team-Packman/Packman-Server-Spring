package com.packman.server.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "packing_list")
public class PackingList extends TimeStamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "packing_list_id", nullable = false, unique = true)
    private Long id;

    @Column(length = 12, nullable = false)
    private String title;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy.MM.dd")
    private LocalDate departureDate;

    @Column(nullable = false)
    private boolean isSaved = false;

    @Column(nullable = false)
    private boolean isDeleted = false;

    @OneToMany(mappedBy = "packing_list", cascade = CascadeType.ALL)
    private List<Category> categories = new ArrayList<>();

    @OneToOne(mappedBy = "packing_list", cascade = CascadeType.ALL)
    private AlonePackingList alonePackingLists;

    @OneToOne(mappedBy = "packing_list", cascade = CascadeType.ALL)
    private TogetherPackingList togetherPackingLists;
}
