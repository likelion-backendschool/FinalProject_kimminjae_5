package com.example.reader.mybook.controller;

import com.example.base.dto.RsData;
import com.example.config.entity.MemberContext;
import com.example.mybook.entity.MyBook;
import com.example.mybook.service.MyBookService;
import com.example.util.Ut;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/myBooks")
@Tag(name = "ApiV1MyBooksController", description = "로그인 된 회윈이 구매한 책 정보")
public class MyBookController {
    private final MyBookService myBookService;
    @GetMapping("")
    @Operation(summary =  "로그인된 회원이 보유한 도서 목록", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<RsData> myBooksList(@AuthenticationPrincipal MemberContext memberContext) {
        List<MyBook> myBookList = myBookService.getAllByBuyerId(memberContext.getId());

        return Ut.spring.responseEntityOf(RsData.successOf(Ut.mapOf("myBooks", myBookList)));
    }
    @GetMapping("/{id}")
    @Operation(summary =  "도서 상세", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<RsData> myBookDetail(@PathVariable long id, @AuthenticationPrincipal MemberContext memberContext) {
        MyBook myBook = myBookService.getById(id);

        if(myBook == null) return Ut.spring.responseEntityOf(RsData.failOf("존재하지 않습니다."));

        if(myBook.getBuyer().getId() != memberContext.getId()) return Ut.spring.responseEntityOf(RsData.failOf("권한이 없습니다"));

        return Ut.spring.responseEntityOf(RsData.successOf(Ut.mapOf("myBook", myBook)));
    }
}
