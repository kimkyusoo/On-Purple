package com.project.date.repository.post;


import com.project.date.dto.response.PostResponseDto;
import com.project.date.model.Post;
import com.project.date.model.QPost;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class PostCustomRepositoryImpl implements PostCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;
    QPost post = QPost.post;


    @Override
    public Slice<PostResponseDto> findAllByCategorySearch(String category,String keyword, Pageable pageable) {
        QueryResults<Post> postResult = jpaQueryFactory
                .selectFrom(post)
                .offset(pageable.getOffset())
                .orderBy(post.createdAt.desc())
                .where(categoryEq(category),keywordEq(keyword))
                .limit(pageable.getPageSize() + 1)
                .fetchResults();

        List<PostResponseDto> responseDtoList = new ArrayList<>();

        for(Post post : postResult.getResults()){
            responseDtoList.add(PostResponseDto.builder()
                            .postId(post.getId())
                            .title(post.getTitle())
                            .content(post.getContent())
                            .view(post.getView())
                            .likes(post.getLikes())
                            .category(post.getCategory())
                            .nickname(post.getUser().getNickname())
                            .imageUrl(post.getImgUrl())
                            .createdAt(post.getCreatedAt())
                            .modifiedAt(post.getModifiedAt())
                            .build());
        }

        boolean hasNext = false;
        if(responseDtoList.size() >pageable.getPageSize()) {
            responseDtoList.remove(pageable.getPageSize());
            hasNext = true;
        }
        return new SliceImpl<>(responseDtoList, pageable, hasNext);

        }

    @Override
    public Slice<PostResponseDto> findAllByCategory(String category,Pageable pageable) {
        QueryResults<Post> postResult = jpaQueryFactory
                .selectFrom(post)
                .offset(pageable.getOffset())
                .orderBy(post.createdAt.desc())
                .where(categoryEq(category))
                .limit(pageable.getPageSize() + 1)
                .fetchResults();

        List<PostResponseDto> responseDtoList = new ArrayList<>();

        for(Post post : postResult.getResults()){
            responseDtoList.add(PostResponseDto.builder()
                    .postId(post.getId())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .view(post.getView())
                    .likes(post.getLikes())
                    .category(post.getCategory())
                    .nickname(post.getUser().getNickname())
                    .imageUrl(post.getImgUrl())
                    .createdAt(post.getCreatedAt())
                    .modifiedAt(post.getModifiedAt())
                    .build());
        }

        boolean hasNext = false;
        if(responseDtoList.size() >pageable.getPageSize()) {
            responseDtoList.remove(pageable.getPageSize());
            hasNext = true;
        }
        return new SliceImpl<>(responseDtoList, pageable, hasNext);

    }


        private BooleanExpression categoryEq(String category) {
            if (category == null) {
                return null;
            } else if (category == "") {
                return null;
            }
            return post.category.eq(category);
        }

        private BooleanExpression keywordEq(String keyword) {
            if(keyword == null) {
                return null;
            }else if (keyword == ""){
                return null;
            }
            return (post.title.contains(keyword)).or(post.content.contains(keyword));
        }
}
