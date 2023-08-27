package com.vv.VisualVibes.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vv.VisualVibes.entity.role.ERole;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDateTime;
import java.util.*;

@Data
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(unique = true, updatable = false)
    private String username;
    @Column(nullable = false)
    private String lastname;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(columnDefinition = "text")
    private String bio;
    @Column(length = 3000)
    private String password;
    @JsonFormat(pattern = "yyyy-mm-dd")
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @ElementCollection(targetClass = ERole.class)
    @CollectionTable(name = "user_role")
    private Set<ERole> roles = new HashSet<>();
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    @Transient
    private Collection<? extends GrantedAuthority> authorities;

    public User() {

    }

    @PrePersist
    protected void onCreate() {
        this.createdDate = LocalDateTime.now();
    }
}
