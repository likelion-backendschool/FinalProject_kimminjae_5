package com.example.member.repository;

import com.example.member.enitty.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByusername(String username);

    Optional<Member> findByNickname(String nickname);

    Optional<Member> findByEmail(String email);
}
