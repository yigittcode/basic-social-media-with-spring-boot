package com.yigit.social_media.model;

import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "comments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"post", "author"})
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "Comment content cannot be blank")
    @Size(max = 1000, message = "Comment content cannot exceed 1000 characters")
    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @NotNull(message = "Post cannot be null")
    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @NotNull(message = "Author cannot be null")
    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

}
