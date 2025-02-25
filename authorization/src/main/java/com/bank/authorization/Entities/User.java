package com.bank.authorization.Entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;

@Entity
@Table(name = "\"user\"", schema = "\"authorization\"")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false, length = 40)
    private String role;

    @Column(name = "profile_id", nullable = false)
    private Long profileId;

    @Column(name = "password", nullable = false, length = 500)
    private String password;

    public User(String role, Long profileId, String password) {
        this.role = role;
        this.profileId = profileId;
        this.password = password;
    }
}
