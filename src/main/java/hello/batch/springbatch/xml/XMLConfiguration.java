package hello.batch.springbatch.xml;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.xml.builder.StaxEventItemReaderBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.xstream.XStreamMarshaller;

import com.thoughtworks.xstream.security.AnyTypePermission;

import hello.batch.springbatch.flatfile.Client;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class XMLConfiguration {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;

	@Bean
	public Job xmlJob() {
		return jobBuilderFactory.get("xmlJob")
			.start(step1())
			.next(step2())
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
	public ItemReader<? extends Client> itemReader() {
		return new StaxEventItemReaderBuilder<Client>()
			.name("staxXml")
			.resource(new ClassPathResource("client.xml"))
			.addFragmentRootElements("client")
			.unmarshaller(itemUnmarshaller())
			.build();
	}

	@Bean
	public Unmarshaller itemUnmarshaller() {
		Map<String, Class<?>> alias = new HashMap<>();
		alias.put("client", Client.class);
		alias.put("name", String.class);
		alias.put("age", Integer.class);
		alias.put("year", String.class);

		XStreamMarshaller xStreamMarshaller = new XStreamMarshaller();
		xStreamMarshaller.setAliases(alias);
		xStreamMarshaller.setTypePermissions(new AnyTypePermission());
		return xStreamMarshaller;
	}

	@Bean
	public ItemWriter<? super Client> itemWriter() {
		return (ItemWriter<Client>)items -> {
			for (Client item : items) {
				System.out.println("item = " + item);
			}
		};
	}

	@Bean
	public Step step2() {
		return stepBuilderFactory.get("step2")
			.tasklet((stepContribution, chunkContext) -> RepeatStatus.FINISHED)
			.build();
	}
}
