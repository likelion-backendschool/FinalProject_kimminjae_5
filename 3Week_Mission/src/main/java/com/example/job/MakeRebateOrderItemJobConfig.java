package com.example.job;


import com.example.admin.RebateOrderItem;
import com.example.admin.RebateOrderItemRepository;
import com.example.order.OrderItem;
import com.example.order.OrderItemRepository;
import com.example.util.Ut;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class MakeRebateOrderItemJobConfig {
//    private final JobBuilderFactory jobBuilderFactory;
//    private final StepBuilderFactory stepBuilderFactory;
//
//    private final OrderItemRepository orderItemRepository;
//    private final RebateOrderItemRepository rebateOrderItemRepository;
//
//
//    @Bean
//    public Job makeRebateOrderItemJob(Step makeRebateOrderItemStep1) {
//
//        return jobBuilderFactory.get("makeRebateOrderItemJob")
//                .start(makeRebateOrderItemStep1)
//                .build();
//    }
//
//    @Bean
//    @JobScope
//    public Step makeRebateOrderItemStep1(
//            ItemReader orderItemReader,
//            ItemProcessor orderItemToRebateOrderItemProcessor,
//            ItemWriter rebateOrderItemWriter
//    ) {
//        return stepBuilderFactory.get("makeRebateOrderItemStep1")
//                .<OrderItem, RebateOrderItem>chunk(100)
//                .reader(orderItemReader)
//                .processor(orderItemToRebateOrderItemProcessor)
//                .writer(rebateOrderItemWriter)
//                .build();
//    }
//
//    @StepScope
//    @Bean
//    public RepositoryItemReader<OrderItem> orderItemReader() {
//        LocalDateTime now = LocalDateTime.now();
//        int year = now.getYear();
//        int month = now.getMonthValue();
//        int endDay = Ut.date.getEndDayOf(year, month);
//        LocalDateTime startDayTime = Ut.date.parse(year + "-" + month + "-" + 1 + " " + "00" + ":" + "00");
//        LocalDateTime endDayTime = Ut.date.parse(year + "-" + month + "-" + endDay + " " + "23" + ":" + "59");
//        System.out.println(startDayTime);
//        System.out.println(endDayTime);
//        return new RepositoryItemReaderBuilder<OrderItem>()
//                .name("orderItemReader")
//                .repository(orderItemRepository)
//                .methodName("findAllByIsPaidBetween")
//                .pageSize(100)
//                .arguments(Arrays.asList(startDayTime, endDayTime))
//                .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
//                .build();
//    }
//
//    @StepScope
//    @Bean
//    public ItemProcessor<OrderItem, RebateOrderItem> orderItemToRebateOrderItemProcessor() {
//        return orderItem -> new RebateOrderItem(orderItem);
//    }
//
//    @StepScope
//    @Bean
//    public ItemWriter<RebateOrderItem> rebateOrderItemWriter() {
//        return items -> items.forEach(item -> {
//            RebateOrderItem oldRebateOrderItem = rebateOrderItemRepository.findByOrderItemId(item.getOrderItem().getId()).orElse(null);
//
//            if (oldRebateOrderItem != null) {
//                rebateOrderItemRepository.delete(oldRebateOrderItem);
//            }
//
//            rebateOrderItemRepository.save(item);
//        });
//    }
}

