package shop.s5g.batch.schedule;

import java.text.SimpleDateFormat;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
@Configuration
public class BirthSchedule {

    private final JobLauncher jobLauncher;
    private final JobRegistry jobRegistry;

    public BirthSchedule(JobLauncher jobLauncher, JobRegistry jobRegistry) {
        this.jobLauncher = jobLauncher;
        this.jobRegistry = jobRegistry;
    }

    /**
     * Birth 배치 작업 실행
     * cron - 매 달 1일 자정 00시 00분 00초에 스케쥴 실행
     * @throws Exception
     */
//    @Scheduled(cron = "0 0 0 1 * *",zone = "Asia/Seoul")

    @Scheduled(cron = "0 0/1 * * * ?",zone = "Asia/Seoul")
    public void runBirthJob() throws Exception {

        log.info("Start Birth Job");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
        String date = dateFormat.format(new Date());

        JobParameters jobParameters = new JobParametersBuilder()
            .addString("date", date)
            .toJobParameters();

        jobLauncher.run(jobRegistry.getJob("birthJob"), jobParameters);
    }
}
