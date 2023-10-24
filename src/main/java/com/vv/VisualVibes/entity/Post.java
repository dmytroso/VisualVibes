package com.vv.VisualVibes.entity;


import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String caption;
    private Integer likes;

    @Column
    @ElementCollection(targetClass = String.class)
    private Set<String> likedUsers = new HashSet<>();
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER, mappedBy = "post", orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();
    @Column(updatable = false)
    private LocalDateTime createdDate;

    public Post() {
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", caption='" + caption + '\'' +
                ", likes=" + likes +
                ", likedUsers=" + likedUsers +
                ", comments=" + comments +
                ", createdDate=" + createdDate +
                '}';
    }

    @PrePersist
    protected void onCreate() {
        this.createdDate = LocalDateTime.now();
    }
}
