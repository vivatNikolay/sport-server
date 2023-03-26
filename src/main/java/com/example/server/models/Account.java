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
public class Account {
    @Id
    private String email;
    private String password;
    private String phone;
    private String firstName;
    private String lastName;
    private Boolean gender;
    private int iconNum;
    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;
    @OneToMany(cascade = {CascadeType.ALL})
    private List<Subscription> subscriptions = Collections.emptyList();
    @Enumerated(EnumType.STRING)
    private Role role;
}
