package com.yigit.social_media.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.util.List;
import com.yigit.social_media.enums.RoleTypes;
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonIgnore;
@Entity
@Getter
@Setter
@ToString(exclude = {"password", "role", "comments", "posts"})
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "Username cannot be blank")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @JsonIgnore
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private RoleTypes role;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    public void addPost(Post post) {
        posts.add(post);
        post.setAuthor(this);
    }
    
    public void removePost(Post post) {
        posts.remove(post);
        post.setAuthor(null);
    }

    public void clearPosts() {
        posts.forEach(post -> post.setAuthor(null));
        posts.clear();
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
        for (Post post : posts) {
            post.setAuthor(this);
        }
    }
    
}
