package hello.batch.springbatch.jdbcbatch.itemwriter;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import hello.batch.springbatch.flatfile.Client;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class JdbcBatchItemWriterConfiguration {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	private final DataSource dataSource;

	@Bean
	public Job jdbcBatchWriterJob() {
		return jobBuilderFactory.get("jdbcBatchWriterJob")
			.incrementer(new RunIdIncrementer())
			.start(step1())
			.build();
	}

	@Bean
	public Step step1() {
		return stepBuilderFactory.get("step1")
			.<Client, Client>chunk(5)
			.reader(itemReader())
			.writer(itemWriter())
			.build();
	}

	@Bean
	public ItemWriter<? super Client> itemWriter() {
		return new JdbcBatchItemWriterBuilder<Client>()
			.dataSource(dataSource)
			.sql(" insert into client values(:name, :age, :year) ")
			.beanMapped()
			.build();
	}

	@Bean
	public ItemReader<? extends Client> itemReader() {
		return new FlatFileItemReaderBuilder<Client>()
			.name("flatFileReader")
			.resource(new FileSystemResource("C:\\study\\spring-batch\\src\\main\\resources\\client.csv"))
			.delimited().delimiter(",")
			.names("name", "age", "year")
			.fieldSetMapper(new BeanWrapperFieldSetMapper<>())
			.targetType(Client.class)
			.linesToSkip(1)
			.build();
	}

}
