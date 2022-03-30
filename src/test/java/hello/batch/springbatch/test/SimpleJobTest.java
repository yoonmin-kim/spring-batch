package hello.batch.springbatch.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import hello.batch.springbatch.jdbcbatch.itemwriter.JdbcBatchItemWriterConfiguration;

@SpringBatchTest
@SpringBootTest(classes = {TestBatchConfig.class, JdbcBatchItemWriterConfiguration.class})
public class SimpleJobTest {

	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Test
	void simpleJobTest() throws Exception {
		JobExecution jobExecution = jobLauncherTestUtils.launchJob();
		assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
		assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
	}

	@AfterEach
	void afterEach() {
		jdbcTemplate.execute("delete from client2");
	}
}
