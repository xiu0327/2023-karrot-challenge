package numble.karrot.security;

import lombok.RequiredArgsConstructor;
import numble.karrot.exception.MemberNotFoundException;
import numble.karrot.member.domain.Member;
import numble.karrot.member.domain.MemberRole;
import numble.karrot.member.repository.MemberRepository;
import numble.karrot.member.service.MemberService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final MemberService memberService;

    @Override
    public UserDetails loadUserByUsername(String email) throws MemberNotFoundException {

        Member findMember = memberService.findMember(email);

        HashSet<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(MemberRole.ROLE_USER.getValue()));
        return new User(findMember.getEmail(), findMember.getPassword(), authorities);
    }
}
