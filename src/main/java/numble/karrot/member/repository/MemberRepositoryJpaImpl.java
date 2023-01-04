package numble.karrot.member.repository;

import lombok.RequiredArgsConstructor;
import numble.karrot.member.domain.Member;
import numble.karrot.exception.MemberEmailDuplicateException;
import numble.karrot.exception.MemberNotFoundException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryJpaImpl implements MemberRepository{

    private final EntityManager em;

    @Override
    public Member create(Member member) {
        if(existMemberCheck(member.getEmail())){ throw new MemberEmailDuplicateException();}
        em.persist(member);
        return member;
    }

    @Override
    public Member findMemberById(Long memberId) {
        Member member = em.find(Member.class, memberId);
        return member;
    }

    @Override
    public Member findMemberByEmail(String email) {
        return em.createQuery("select m from Member m where m.email= :email", Member.class)
                .setParameter("email", email)
                .getResultList()
                .stream().findAny()
                .orElseThrow(() -> new MemberNotFoundException());
    }

    @Override
    public Member updateMemberByNickName(String email, String nickName) {
        Member member = findMemberByEmail(email);
        member.setNickName(nickName);
        return member;
    }

    @Override
    public Member updateMemberByProfile(String email, String profile) {
        Member member = findMemberByEmail(email);
        member.setProfile(profile);
        return member;
    }

    @Override
    public void removeMember(Member member) {
        em.remove(member);
    }

    public Boolean existMemberCheck(String email){
        Optional<Member> member = em.createQuery("select m from Member m where m.email= :email", Member.class)
                .setParameter("email", email)
                .getResultList()
                .stream().findAny();
        if(member.isEmpty()){ return false; }
        return true;
    }
}
