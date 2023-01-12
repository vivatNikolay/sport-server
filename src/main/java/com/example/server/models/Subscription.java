package com.example.server.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Temporal(TemporalType.DATE)
    private Date dateOfStart;
    @Temporal(TemporalType.DATE)
    private Date dateOfEnd;
    private int numberOfVisits;
    @OneToMany(cascade = {CascadeType.ALL})
    private List<Visit> visits = Collections.emptyList();
}
