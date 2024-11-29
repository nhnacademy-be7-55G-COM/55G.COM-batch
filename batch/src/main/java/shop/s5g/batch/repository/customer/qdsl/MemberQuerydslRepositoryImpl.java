package shop.s5g.batch.repository.customer.qdsl;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Objects;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import shop.s5g.batch.entity.customer.Member;
import shop.s5g.batch.entity.customer.QMember;


public class MemberQuerydslRepositoryImpl extends QuerydslRepositorySupport implements MemberQuerydslRepository {

    private final JPAQueryFactory queryFactory;

    public MemberQuerydslRepositoryImpl(EntityManager em) {
        super(Member.class);
        this.queryFactory = new JPAQueryFactory(em);
    }

    QMember member = QMember.member;

    @Override
    public Page<Member> findAllMembersByBirthMonth(String formattedMonth, Pageable pageable) {

        List<Member> memberList = queryFactory
            .selectFrom(member)
            .where(Expressions.stringTemplate("substring({0}, 5, 2)", member.birth).eq(formattedMonth))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        Long total = queryFactory
            .select(member.count())
            .from(member)
            .where(Expressions.stringTemplate("substring({0}, 5, 2)", member.birth).eq(formattedMonth))
            .fetchOne();

        long totalCnt = Objects.isNull(total) ? 0 : total;

        return new PageImpl<>(memberList, pageable, totalCnt);
    }
}
