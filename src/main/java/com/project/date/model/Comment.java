package com.project.date.model;

import javax.persistence.*;

import com.project.date.dto.request.CommentRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Comment extends Timestamped {

  @Id
  @JoinColumn(name = "commentId", nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @JoinColumn(name = "userId", nullable = false)
  @ManyToOne(fetch = FetchType.LAZY)
  private User user;

  @JoinColumn(name = "postId", nullable = false)
  @ManyToOne(fetch = FetchType.LAZY)
  private Post post;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ReComment> reComments;
  @Column(nullable = false)
  private String comment;

  public void update(CommentRequestDto commentRequestDto) {
    this.comment = commentRequestDto.getComment();
  }

  public boolean validateUser(User user) {
    return !this.user.equals(user);
  }
}
