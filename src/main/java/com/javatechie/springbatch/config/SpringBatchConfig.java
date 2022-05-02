package com.javatechie.springbatch.config;


import com.javatechie.springbatch.entity.Customer;
import com.javatechie.springbatch.entity.CustomerDetail;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
@EnableBatchProcessing
public class SpringBatchConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;



//    @Autowired
//    private CustomerRepository customerRepository;


    @Bean
    public FlatFileItemReader<CustomerDetail> reader(){
//        FlatFileItemReader itemReader = new FlatFileItemReader<>();
//        itemReader.setResource(new FileSystemResource("src/main/resources/customer.csv"));
//        itemReader.setName("csvReader");
//        itemReader.setLinesToSkip(1);
//        itemReader.setLineMapper(lineMapper());
//        return itemReader;
        return new FlatFileItemReaderBuilder<CustomerDetail>().name("itemReader")
                .resource(new ClassPathResource("customer.csv")).delimited()
                .names(new String[] {"id2","firstName","lastName","email","gender","contactNo","country","dob"})
                .fieldSetMapper(new BeanWrapperFieldSetMapper<CustomerDetail>(){
            {
                setTargetType(CustomerDetail.class);
            }
        }).build();

    }
    @Bean
    public MongoItemWriter<Customer> writer(MongoTemplate mongoTemplate) {
        return new MongoItemWriterBuilder<Customer>().template(mongoTemplate).collection("Customer")
                .build();
    }

//    private LineMapper<CustomerDetail> lineMapper() {
//        DefaultLineMapper<CustomerDetail> lineMapper = new DefaultLineMapper<>();
//
//        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
//        lineTokenizer.setDelimiter(",");
//        lineTokenizer.setStrict(false);
//        lineTokenizer.setNames("id","firstName","lastName","email","gender","contactNo","country","dob");
//
//
//        BeanWrapperFieldSetMapper<CustomerDetail> fieldSetMapper= new BeanWrapperFieldSetMapper<>();
//        fieldSetMapper.setTargetType(CustomerDetail.class);
//
//        lineMapper.setLineTokenizer(lineTokenizer);
//        lineMapper.setFieldSetMapper(fieldSetMapper);
//        return lineMapper;
//    }
    @Bean
    public CustomerProcessor processor(){
        return new CustomerProcessor();
    }

//    public RepositoryItemWriter<CustomerDetail> writer(){
//        RepositoryItemWriter<CustomerDetail> writer = new RepositoryItemWriter<>();
//        writer.setRepository(customerRepository);
//        writer.setMethodName("save");
//        return writer;
//    }

//    @Bean
//    public Step step1(){
//        return  stepBuilderFactory.get("csv-step")
//                .<CustomerDetail, CustomerDetail>chunk(10)
//                .reader(reader())
//                .processor(processor())
//                .writer(writer())
//                .taskExecutor(taskExecutor())
//                .build();
//    }
    @Bean
    public Step step1(FlatFileItemReader<CustomerDetail> itemReader, MongoItemWriter<Customer> itemWriter) {

        return this.stepBuilderFactory.get("step1").<CustomerDetail, Customer>chunk(10).reader(itemReader)
                .processor(processor()).writer(itemWriter).build();
    }

    @Bean
    public Job updateUserJob(JobCompletionNotificationListener listener,Step step1) {

        return this.jobBuilderFactory.get("updateUserJob").incrementer(new RunIdIncrementer())
                .listener(listener)
                .start(step1)
                .build();
    }




//    @Bean
//    public Job runJob(){
//        return jobBuilderFactory.get("importCustomers")
//                .flow(step1()).end().build();
//    }

//    @Bean
//    public TaskExecutor taskExecutor(){
//        SimpleAsyncTaskExecutor simpleAsyncTaskExecutor = new SimpleAsyncTaskExecutor();
//        simpleAsyncTaskExecutor.setConcurrencyLimit(10);
//        return simpleAsyncTaskExecutor;
//    }
}
