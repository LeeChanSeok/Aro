package com.nassafy.core.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Attraction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attraction_id")
    private Long id;

    private String nation;

    private String attractionName;

    private String description;

    private Float latitude;

    private Float longitude;

    private String grayStamp;

    private String colorStamp;

    private String image;

    private String colorAuth;

    private String grayAuth;

    @OneToMany(mappedBy = "attraction")
    private List<Interest> interests = new ArrayList<>();
}