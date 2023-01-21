package numble.karrot.member.repository;

import lombok.RequiredArgsConstructor;
import numble.karrot.member.domain.Member;
import numble.karrot.exception.MemberEmailDuplicateException;
import numble.karrot.exception.MemberNotFoundException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepository{

    private final EntityManager em;

    public Long create(Member member) {
        existMemberCheck(member.getEmail());
        em.persist(member);
        return member.getId();
    }

    public Member findMemberByEmail(String email) {
        return em.createQuery("select m from Member m where m.email= :email", Member.class)
                .setParameter("email", email)
                .getResultList()
                .stream().findAny()
                .orElseThrow(() -> new MemberNotFoundException());

    }

    public Member findOne(Long memberId){
        return em.find(Member.class, memberId);
    }

    public void updateNickName(Long memberId, String nickName) {
        em.find(Member.class, memberId).updateNickName(nickName);
    }

    public void updateProfile(Long memberId, String profile) {
        em.find(Member.class, memberId).updateProfile(profile);
    }
    public void removeMember(Member member) {
        em.remove(member);
    }

    public List<Member> findAllMember() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public void existMemberCheck(String email){
        Optional<Member> findMember = em.createQuery("select m from Member m where m.email= :email", Member.class)
                .setParameter("email", email)
                .getResultList()
                .stream().findAny();
        if(findMember.isPresent()) throw new MemberEmailDuplicateException();
    }
}
