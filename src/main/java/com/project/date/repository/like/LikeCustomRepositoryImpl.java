package com.project.date.repository.like;

import com.project.date.dto.response.LikeResponseDto;
import com.project.date.model.Likes;
import com.project.date.model.QLikes;
import com.project.date.model.QUser;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import java.util.*;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class LikeCustomRepositoryImpl implements LikeCustomRepository {

    private final EntityManager em;

    QLikes likes = QLikes.likes;
    QUser user = QUser.user;
    private final JPAQueryFactory queryFactory;
    @Override
    public List<Likes> findLikeToLike(Long userId) {
        return queryFactory
                .selectFrom(likes).distinct()
                .where(likes.user.id.in(
                        JPAExpressions
                                .select(likes.target.id)
                                .from(likes)
                                .where(userIdEq(userId))
                ))
                .leftJoin(user).on(likes.user.id.eq(user.id).and(likes.target.id.eq(user.id))).distinct()
                .fetch().stream().distinct().collect(Collectors.toList());
    }

    @Override
    public List<LikeResponseDto> findUserLikeToLike(Long userId) {

        List<Likes> likesResult = queryFactory
                .selectFrom(likes).distinct()
                .where(likes.user.id.in(
                        JPAExpressions
                                .select(likes.target.id)
                                .from(likes)
                                .where(userIdEq(userId))
                ))
                .innerJoin(likes.user)
                .groupBy(likes.user.id,likes.target.id)
                .fetchJoin()
                .fetch();
        //.stream().distinct().collect(Collectors.toList())

        List<LikeResponseDto> responseDtoList = new ArrayList<>();

        for (Likes likes : likesResult) {
            responseDtoList.add(LikeResponseDto.builder()
                    .likeId(likes.getId())
                    .nickname(likes.getUser().getNickname())
                    .imageUrl(likes.getUser().getImageUrl())
                    .build());
        }

        return responseDtoList;
        //.stream().distinct().collect(Collectors.toList())
    }
//
//    List<Likes> result = em.createQuery
//            ("select distinct l.user.id from Likes l where l.user.id in (select l.target.id from Likes l where l.user.id=:userId)")

    private BooleanExpression userIdEq(Long userId) {
        if (userId == null) {
            return null;

        }return likes.user.id.eq(userId);
    }
}
