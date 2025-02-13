package com.yigit.social_media.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
@Entity
@Table(name = "images")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"post"})
public class Image extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "Image URL cannot be blank")
    @Size(max = 255, message = "Image URL cannot exceed 255 characters")
    @Column(name = "url", nullable = false)
    private String url;

    @NotBlank(message = "Image type cannot be blank")
    @Size(max = 50, message = "Image type cannot exceed 50 characters")
    @Column(name = "type", nullable = false)
    private String type;

    @NotNull(message = "Post cannot be null")
    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;
}
