package shop.s5g.batch.config;

import java.time.LocalDate;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.transaction.PlatformTransactionManager;
import shop.s5g.batch.entity.customer.Member;
import shop.s5g.batch.repository.customer.MemberRepository;
import shop.s5g.batch.service.coupon.UserCouponService;

@Slf4j
@Configuration
public class BirthConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final MemberRepository memberRepository;
    private final UserCouponService userCouponService;

    public BirthConfig(JobRepository jobRepository, PlatformTransactionManager transactionManager,
        MemberRepository memberRepository, UserCouponService userCouponService) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
        this.memberRepository = memberRepository;
        this.userCouponService = userCouponService;
    }

    /**
     * 생일 체크 Job 등록
     * @return
     */
    @Bean
    public Job birthJob() {

        return new JobBuilder("couponJob", jobRepository)
            .start(birthStep())
            .build();
    }

    /**
     * 생일 체크 batch Step
     * @return
     */
    @Bean
    public Step birthStep() {

        return new StepBuilder("birthStep", jobRepository)
            .<Member, Member> chunk(10, transactionManager)
            .reader(readMemberBirth())
            .processor(processBirthCoupon())
            .build();
    }

    /**
     * 해당 월의 생일인 Member 읽기 - read
     */
    @Bean
    public RepositoryItemReader<Member> readMemberBirth() {

        int currentMonth = LocalDate.now().getMonthValue();
        String formattedMonth = String.format("%02d", currentMonth);

        return new RepositoryItemReaderBuilder<Member>()
            .name("checkBirthMember")
            .pageSize(10)
            .methodName("findAllMembersByBirthMonth")
            .arguments(formattedMonth)
            .repository(memberRepository)
            .sorts(Map.of("id", Direction.ASC))
            .build();
    }

    /**
     * 해당 월의 생일자에게 각각 쿠폰 생성 - processor
     */
    @Bean
    public ItemProcessor<Member, Member> processBirthCoupon() {

        return item -> {
            log.info("Processing Member with ID : {}, Birth Date : {}", item.getId(), item.getBirth());

            userCouponService.createBirthCoupon(item);

            return item;
        };
    }

    /**
     * 해당 월의 생일자 쿠폰함에 넣어주기 - writer
     */
    // Writer
}
