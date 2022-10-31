package com.example.member;

import com.example.DataNotFoundException;
import com.example.base.dto.RsData;
import com.example.cart.CartItem;
import com.example.cart.CartService;
import com.example.cash.CashLog;
import com.example.cash.CashService;
import com.example.product.Product;
import com.example.product.ProductService;
import com.example.util.Ut;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    //확인 후 제거
    private final ProductService productService;
    private final CartService cartService;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final CashService cashService;

    //일반 회원 생성
    public MemberDto create(String username, String password, String email) {
        Member member = Member.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(email)
                .createDate(LocalDateTime.now())
                .authLevel(3)
                .build();
        memberRepository.save(member);
        //포인트 충전
        addCash(member, 10_000, "충전__무통장입금");
        return member.toDto();
    }
    //작가 회원 생성
    public MemberDto create(String username, String password, String nickname, String email) {
        Member member = Member.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .nickname(nickname)
                .email(email)
                .createDate(LocalDateTime.now())
                .authLevel(7)
                .build();
        memberRepository.save(member);

        addCash(member, 10_000, "충전__무통장입금");
        return member.toDto();
    }

    //id로 회원 찾기
    public MemberDto getMemberById(long id) {
        Optional<Member> optionalMember = memberRepository.findById(id);
        if(optionalMember.isPresent()) {
            return optionalMember.get().toDto();
        } else {
            throw new DataNotFoundException("회원이 존재하지 않습니다.");
        }
    }

    //회원 정보(이메일, 닉네임) 수정
    public void modify(MemberDto memberDto, String email, String nickname) {
        Optional<Member> optionalMember = memberRepository.findById(memberDto.getId());
        if(optionalMember.isPresent()) {
            Member member = optionalMember.get();
            member.setEmail(email);
            member.setNickname(nickname);
            memberRepository.save(member);
        } else throw new DataNotFoundException("회원이 존재하지 않습니다.");
    }

    //비밀번호 변경
    public void modifyPassword(MemberDto memberDto, String password) {
        Optional<Member> optionalMember = memberRepository.findById(memberDto.getId());
        if(optionalMember.isPresent()) {
            Member member = optionalMember.get();
            member.setPassword(passwordEncoder.encode(password));
            memberRepository.save(member);
        } else new DataNotFoundException("회원이 존재하지 않습니다.");
    }

    //username으로 Member찾기
    public MemberDto getMemberByUsername(String name) {
        Optional<Member> optionalMember = memberRepository.findByusername(name);
        if(optionalMember.isPresent()) {
            return optionalMember.get().toDto();
        } else {
            throw new DataNotFoundException("회원이 존재하지 않습니다.");
        }
    }

    //작가 등록
    public void signupAuthor(MemberDto memberDto, String nickname) {
        Optional<Member> optionalMember = memberRepository.findById(memberDto.getId());
        if(optionalMember.isPresent()) {
            Member member = optionalMember.get();
            member.setNickname(nickname);
            member.setAuthLevel(7);
            memberRepository.save(member);
        } else {
            throw new DataNotFoundException("회원이 존재하지 않습니다.");
        }
    }

    //작가명으로 유저 찾기
    public MemberDto getMemberByNickname(String nickname) {
        Optional<Member> optionalMember = memberRepository.findByNickname(nickname);
        if(optionalMember.isPresent()) {
            return optionalMember.get().toDto();
        } else {
            throw new DataNotFoundException("회원이 존재하지 않습니다.");
        }
    }

    //이메일로 유저 찾기
    public MemberDto getMemberByEmail(String email) {
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        if(optionalMember.isPresent()) {
            return optionalMember.get().toDto();
        } else {
            throw new DataNotFoundException("회원이 존재하지 않습니다.");
        }
    }
    @Transactional
    public RsData<AddCashRsDataBody> addCash(Member member, long price, String eventType) {
        CashLog cashLog = cashService.addCash(member, price, eventType);

        long newRestCash = member.getRestCash() + cashLog.getPrice();
        member.setRestCash(newRestCash);
        memberRepository.save(member);

        return RsData.of(
                "S-1",
                "success",
                new AddCashRsDataBody(cashLog, newRestCash)
        );
    }
    @Data
    @AllArgsConstructor
    public static class AddCashRsDataBody {
        CashLog cashLog;
        long newRestCash;
    }
    public long getRestCash(Member member) {
        Member foundMember = memberRepository.findByusername(member.getUsername()).get();

        return foundMember.getRestCash();
    }
}
