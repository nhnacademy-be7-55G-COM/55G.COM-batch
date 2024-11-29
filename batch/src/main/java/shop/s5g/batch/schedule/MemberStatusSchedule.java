package shop.s5g.batch.schedule;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import shop.s5g.batch.entity.customer.Member;
import shop.s5g.batch.entity.customer.MemberStatus;
import shop.s5g.batch.repository.customer.MemberRepository;
import shop.s5g.batch.repository.customer.MemberStatusRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemberStatusSchedule {

    private final MemberRepository memberRepository;
    private final MemberStatusRepository memberStatusRepository;

    @Scheduled(cron = "0 0 4 * * *",zone = "Asia/Seoul")
    public void updateMemberStatus() {
        log.info("Updating member status...");

        LocalDateTime threeMonthsAgo = LocalDateTime.now().minusMonths(3);

        MemberStatus inactiveMemberStatus = memberStatusRepository.findByTypeName("INACTIVE");
        MemberStatus activeMemberStatus = memberStatusRepository.findByTypeName("ACTIVE");

        List<Member> memberList = memberRepository.findByLatestLoginAtBeforeAndStatus(threeMonthsAgo, activeMemberStatus);

        for (Member member : memberList) {
            member.setStatus(inactiveMemberStatus);
            log.info("member loginId - {} , 휴면 변경", member.getLoginId());
        }

    }
}
