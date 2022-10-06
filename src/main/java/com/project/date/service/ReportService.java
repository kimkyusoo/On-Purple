package com.project.date.service;

import com.project.date.dto.request.PostRequestDto;
import com.project.date.dto.request.ReportRequestDto;
import com.project.date.dto.response.PostResponseDto;
import com.project.date.dto.response.ReportResponseDto;
import com.project.date.dto.response.ResponseDto;
import com.project.date.jwt.TokenProvider;
import com.project.date.model.Img;
import com.project.date.model.Post;
import com.project.date.model.Report;
import com.project.date.model.User;
import com.project.date.repository.ImgRepository;
import com.project.date.repository.ReportRepository;
import com.project.date.util.AwsS3UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final TokenProvider tokenProvider;
    private final ImgRepository imgRepository;
    private final AwsS3UploadService awsS3UploadService;

    // 게시글 작성
    @Transactional
    public ResponseDto<?> createReport(ReportRequestDto requestDto,
                                       HttpServletRequest request,
                                       List<String> imgPaths) {

        if (null == request.getHeader("RefreshToken")) {
            return ResponseDto.fail("USER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("USER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        User user = validateUser(request);
        if (null == user) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        Report report = Report.builder()
                .user(user)
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .category(requestDto.getCategory())
                .build();

        reportRepository.save(report);

        postBlankCheck(imgPaths);

        List<String> imgList = new ArrayList<>();
        for (String imgUrl : imgPaths) {
            Img img = new Img(imgUrl, report);
            imgList.add(img.getImageUrl());
        }

        report.imageSave(imgList.get(0));
        return ResponseDto.success(
                ReportResponseDto.builder()
                        .reportId(report.getId())
                        .title(report.getTitle())
                        .content(report.getContent())
                        .nickname(report.getUser().getNickname())
                        .imageUrl(report.getImageUrl())
                        .category(report.getCategory())
                        .createdAt(report.getCreatedAt())
                        .modifiedAt(report.getModifiedAt())
                        .build()
        );
    }

    private void postBlankCheck(List<String> imgPaths) {
        if(imgPaths == null || imgPaths.isEmpty()){ //.isEmpty()도 되는지 확인해보기
            throw new NullPointerException("이미지를 등록해주세요(Blank Check)");
        }
    }

    @Transactional
    public User validateUser(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("RefreshToken"))) {
            return null;
        }
        return tokenProvider.getUserFromAuthentication();
    }


}
