package com.br.zallpyws.config;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.github.bohnman.squiggly.context.provider.SimpleSquigglyContextProvider;
import com.github.bohnman.squiggly.filter.SquigglyPropertyFilter;
import com.github.bohnman.squiggly.filter.SquigglyPropertyFilterMixin;
import com.github.bohnman.squiggly.parser.SquigglyParser;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

@Configuration
public class Beans {

  @Bean
  public ObjectMapper objectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.setSerializationInclusion(Include.NON_EMPTY);
    objectMapper.setSerializationInclusion(Include.NON_NULL);
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    objectMapper.addMixIn(Object.class, SquigglyPropertyFilterMixin.class);
    SquigglyPropertyFilter propertyFilter = new SquigglyPropertyFilter(new SimpleSquigglyContextProvider(new SquigglyParser(), "**"));
    objectMapper.setFilterProvider(new SimpleFilterProvider().addFilter(SquigglyPropertyFilter.FILTER_ID, propertyFilter));
    return objectMapper;

  }

  @Bean
  public Jackson2ObjectMapperBuilder objectMapperBuilder() {
    Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
    builder.serializationInclusion(Include.NON_NULL);
    builder.serializationInclusion(Include.NON_EMPTY);
    builder.failOnUnknownProperties(false);
    return builder;
  }

  @Bean(destroyMethod = "shutdown")
  public CacheManager getCacheManager() {
    return CacheManager.create();
  }

  @Bean(name = "cacheDe3Horas_requestId")
  public Cache getCacheRequestId(CacheManager cacheManager) {
    Cache cache = new Cache("requestId", 0, false, false, 3600 * 3, 0); // fica em memória por 3 horas
    cacheManager.addCache(cache);
    return cache;
  }

  @Bean(name = "cacheDe5Minutos_token")
  public Cache getCacheToken(CacheManager cacheManager) {
    Cache cache = new Cache("token", 0, false, false, 60 * 5, 0); // fica em memória por 5 minutos
    cacheManager.addCache(cache);
    return cache;
  }

  @Bean
  @Primary
  public RestTemplate restTemplate() {
    int timeout = 1000 * 10;
    RestTemplate restTemplate = new RestTemplate();
    restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
    SimpleClientHttpRequestFactory rf = (SimpleClientHttpRequestFactory) restTemplate.getRequestFactory();
    rf.setReadTimeout(timeout);
    rf.setConnectTimeout(timeout);
    return restTemplate;
  }

  @Bean
  public Executor asyncExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(5);
    executor.setMaxPoolSize(50);
    executor.setQueueCapacity(10000);
    executor.setThreadNamePrefix("ThreadAsync-zallpy");
    executor.initialize();
    return executor;
  }

}
