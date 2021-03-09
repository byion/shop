package com.ecommerce.batch;

import com.ecommerce.batch.dto.ProductBatchDto;
import com.ecommerce.batch.listeners.JobCompletionNotificationListener;
import com.ecommerce.batch.processors.ProductItemProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Bean
    public ItemReader<ProductBatchDto> reader(DataSource dataSource) throws Exception {
        JdbcPagingItemReader<ProductBatchDto> reader = new JdbcPagingItemReader<>();
        reader.setDataSource(dataSource);
        reader.setFetchSize(50);
        reader.setPageSize(50);

        MySqlPagingQueryProvider provider = new MySqlPagingQueryProvider();
        provider.setSelectClause("select products.id,\n" +
                "       products.name,\n" +
                "       (select prices.price\n" +
                "        from prices\n" +
                "        where prices.product_id = products.id\n" +
                "        ORDER BY prices.created_at DESC\n" +
                "        LIMIT 1)");
        provider.setFromClause("from products");
        provider.setWhereClause("valid is true");

        reader.setRowMapper((resultSet, rowNum) -> new ProductBatchDto(
                resultSet.getObject(1, UUID.class),
                resultSet.getString(2),
                resultSet.getBigDecimal(3)
        ));

        Map<String, Order> sort = new HashMap<>(1);
        sort.put("id", Order.ASCENDING);
        provider.setSortKeys(sort);

        reader.setQueryProvider(provider);

        reader.afterPropertiesSet();
        return reader;

    }

    @Bean
    public ProductItemProcessor processor() {
        return new ProductItemProcessor();
    }

    @Bean
    public FlatFileItemWriter<ProductBatchDto> itemWriter() {
        return new FlatFileItemWriterBuilder<ProductBatchDto>()
                .name("productItemWriter")
                .resource(new FileSystemResource("outputs/products.csv"))
                .delimited()
                .delimiter(", ")
                .names(new String[]{"id", "name", "price"}).build();
    }

    @Bean
    public Job exportProductJob(JobCompletionNotificationListener listener, Step step1) {
        return jobBuilderFactory.get("exportProductJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1)
                .end()
                .build();
    }

    @Bean
    public Step step1(FlatFileItemWriter<ProductBatchDto> itemWriter, ItemReader<ProductBatchDto> reader, TaskExecutor batchTaskExecutor) {
        return stepBuilderFactory.get("step1")
                .<ProductBatchDto, ProductBatchDto>chunk(50)
                .reader(reader)
                .processor(processor())
                .writer(itemWriter)
                .taskExecutor(batchTaskExecutor)
                .build();
    }

    @Bean
    public SimpleJobLauncher simpleJobLauncher(JobRepository jobRepository) {
        SimpleJobLauncher launcher = new SimpleJobLauncher();
        launcher.setJobRepository(jobRepository);
        return launcher;
    }


    @Bean
    public TaskExecutor batchTaskExecutor() {
        return new SimpleAsyncTaskExecutor("spring_batch");
    }
}
