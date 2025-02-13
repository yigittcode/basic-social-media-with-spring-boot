package com.yigit.social_media.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;
import java.util.ArrayList;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import com.yigit.social_media.enums.PostStatus;
@Entity
@Table(name = "posts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"author", "comments", "images"})
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "Title cannot be blank")
    @Size(max = 255, message = "Title cannot exceed 255 characters")
    @Column(name = "title", nullable = false)
    private String title;

    @NotBlank(message = "Content cannot be blank")
    @Size(max = 10000, message = "Content cannot exceed 10000 characters")
    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Image> images = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private PostStatus status = PostStatus.PENDING;

    public void addComment(Comment comment) {
     
        comments.add(comment);
        comment.setPost(this);
    }

    public void removeComment(Comment comment) {
            comments.remove(comment);
            comment.setPost(null);
        
    }

    public void clearComments() {
        comments.forEach(comment -> comment.setPost(null));
        comments.clear();
    }

    public void addImage(Image image) {
        images.add(image);
        image.setPost(this);
    }

    public void removeImage(Image image) {
        images.remove(image);
        image.setPost(null);
    }

    public void clearImages() {
        images.forEach(image -> image.setPost(null));
        images.clear();
    }

    public void setImages(List<Image> images) {
        this.images.clear();
        if (images != null) {
            images.forEach(this::addImage);
        }
    }
}