package shop.s5g.batch.config;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.transaction.PlatformTransactionManager;
import shop.s5g.batch.entity.coupon.Coupon;
import shop.s5g.batch.entity.coupon.UserCoupon;
import shop.s5g.batch.entity.customer.Member;
import shop.s5g.batch.repository.coupon.UserCouponRepository;
import shop.s5g.batch.repository.customer.MemberRepository;
import shop.s5g.batch.service.coupon.CouponService;
import shop.s5g.batch.util.coupon.SharedData;

@Slf4j
@Configuration
public class BirthConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final SharedData<List<Member>> sharedData;
    private final UserCouponRepository userCouponRepository;
    private final MemberRepository memberRepository;
    private final CouponService couponService;

    private static final Integer CHUNK_SIZE = 100;

    public BirthConfig(JobRepository jobRepository, PlatformTransactionManager transactionManager,
        SharedData<List<Member>> sharedData,
        UserCouponRepository userCouponRepository, MemberRepository memberRepository,
        CouponService couponService) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
        this.sharedData = sharedData;
        this.userCouponRepository = userCouponRepository;
        this.memberRepository = memberRepository;
        this.couponService = couponService;
    }

    /**
     * 생일자 조회해서 쿠폰 발급해주는 Job
     *
     * @return
     */
    @Bean
    public Job birthJob() {

        return new JobBuilder("birthJob", jobRepository)
            .start(birthMemberReadStep())
            .next(birthCouponProcessStep())
            .build();
    }

    /**
     * 모든 멤버 조회 후 생일자들만 골라 내는 작업 - Step
     *
     * @return Step
     */
    @Bean
    @JobScope
    public Step birthMemberReadStep() {
        return new StepBuilder("birthMemberRead", jobRepository)
            .<Member, Member>chunk(CHUNK_SIZE, transactionManager)
            .reader(memberReader())
            .writer(birthMemberWriter())
            .build();
    }

    /**
     * 모든 멤버 조회 - Reader
     *
     * @return Member
     */
    @Bean
    @StepScope
    public RepositoryItemReader<Member> memberReader() {

        int currentMonth = LocalDate.now().getMonthValue();
        String formattedMonth = String.format("%02d", currentMonth);

        return new RepositoryItemReaderBuilder<Member>()
            .name("checkBirthMember")
            .pageSize(CHUNK_SIZE)
            .methodName("findAllMembersByBirthMonth")
            .arguments(formattedMonth)
            .repository(memberRepository)
            .sorts(Map.of("id", Direction.ASC))
            .build();
    }

    @Bean
    @StepScope
    public ItemWriter<Member> birthMemberWriter() {
        return items -> sharedData.put("birthMember", new LinkedList<>(items.getItems()));
    }

    /**
     * 골라낸 생일자들을 대상으로 생일쿠폰을 넣어주는 작업 - Step
     *
     * @return Step
     */
    @Bean
    @JobScope
    public Step birthCouponProcessStep() {
        return new StepBuilder("birthCouponProcess", jobRepository)
            .<Member, UserCoupon>chunk(CHUNK_SIZE, transactionManager)
            .reader(birthMemberReader())
            .processor(birthCouponIssuedProcessor())
            .writer(birthCouponWriter())
            .build();
    }

    @Bean
    @StepScope
    public ItemReader<Member> birthMemberReader() {
        return new ItemReader<>() {

            private final Iterator<Member> memberIterator = sharedData.get("birthMember")
                .iterator();

            @Override
            public Member read() {

                if (memberIterator.hasNext()) {
                    return memberIterator.next();
                }
                return null;
            }
        };
    }

    @Bean
    @StepScope
    public ItemProcessor<Member, UserCoupon> birthCouponIssuedProcessor() {

        LocalDateTime now = LocalDateTime.now();
        YearMonth yearMonth = YearMonth.from(now);
        LocalDateTime lastDayOfMonth = yearMonth.atEndOfMonth().atTime(23, 59, 59);

        return member -> {
            if (userCouponRepository.hasBirthdayCoupon(member)) {
                log.warn("Member with ID {} already has a birthday coupon. Skipping.",
                    member.getId());
                return null;
            }

            Coupon birthCoupon = couponService.createBirthCoupon();

            return new UserCoupon(member, birthCoupon, now, lastDayOfMonth);
        };
    }

    @Bean
    @StepScope
    public ItemWriter<UserCoupon> birthCouponWriter() {
        return items -> {
            if (!items.isEmpty()) {
                try {
                    userCouponRepository.saveAll(items);
                    log.info("Successfully inserted {} UserCoupons.", items.size());
                } catch (Exception e) {
                    log.error("Failed to save UserCoupons. Details: {}", items, e);
                    throw e;
                }
            } else {
                log.warn("No UserCoupon to insert");
            }
        };
    }
}