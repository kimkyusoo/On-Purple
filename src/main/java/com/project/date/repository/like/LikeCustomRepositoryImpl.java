package com.project.date.repository.like;

import com.project.date.model.Likes;
import com.project.date.model.QLikes;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class LikeCustomRepositoryImpl implements LikeCustomRepository{

    QLikes likes = QLikes.likes;

    private final JPAQueryFactory queryFactory;
    @Override
    public List<Likes> findLikeToLike(Long userId) {
        return queryFactory
                .selectFrom(likes).distinct()
                .where(likes.user.id.in(
                        JPAExpressions
                                .select(likes.profile.id).distinct()
                                .from(likes)
                                .where(userIdEq(userId))
                ))
                .fetch();
    }

    private BooleanExpression userIdEq(Long userId) {
        if (userId == null) {
            return null;

        }return likes.user.id.eq(userId);
    }
}
