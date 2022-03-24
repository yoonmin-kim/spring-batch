package hello.batch.springbatch.multi_thread.partition;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class PartitionConfiguration {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	private final DataSource dataSource;

	@Bean
	public Job partitionJob() {
		return jobBuilderFactory.get("partitionJob")
			.incrementer(new RunIdIncrementer())
			.start(masterStep())
			.build();
	}

	@Bean
	public Step masterStep() {
		return stepBuilderFactory.get("masterStep")
			.partitioner(slaveStep().getName(), partitioner())
			.step(slaveStep())
			.gridSize(4)
			.taskExecutor(new SimpleAsyncTaskExecutor())
			.build();
	}

	@Bean
	public Partitioner partitioner() {
		ColumnRangePartitioner columnRangePartitioner = new ColumnRangePartitioner();
		columnRangePartitioner.setColumn("id");
		columnRangePartitioner.setDataSource(dataSource);
		columnRangePartitioner.setTable("client");
		return columnRangePartitioner;
	}

	@Bean
	public Step slaveStep() {
		return stepBuilderFactory.get("slaveStep")
			.<Client, Client>chunk(100)
			.reader(itemReader())
			.writer(itemWriter())
			.build();
	}

	@Bean
	public ItemWriter<? super Client> itemWriter() {

		return new JdbcBatchItemWriterBuilder<Client>()
			.sql(" INSERT INTO client2 VALUES(:id, :name, :age) ")
			.beanMapped()
			.build();
	}

	@Bean
	public ItemReader<? extends Client> itemReader() {

		Map<String, Order> sortKeys = new HashMap<>();
		sortKeys.put("id", Order.ASCENDING);

		return new JdbcPagingItemReaderBuilder<Client>()
			.name("clientReader")
			.dataSource(dataSource)
			.rowMapper(new BeanPropertyRowMapper<>())
			.selectClause("id, name, age")
			.fromClause("from client")
			.sortKeys(sortKeys)
			.build();
	}
}
