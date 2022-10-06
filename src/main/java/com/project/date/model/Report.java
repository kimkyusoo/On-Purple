package com.project.date.model;

import lombok.*;

import javax.persistence.*;
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Report {

    @Id
    @Column(name = "postId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //게시글제목
    @Column(nullable = false)
    private String title;
    //게시글내용
    @Column(nullable = false)
    private String content;

    @JoinColumn(name = "userId", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
}
