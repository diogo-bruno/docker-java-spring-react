package com.br.zallpyws.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.fasterxml.classmate.TypeResolver;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig extends WebMvcConfigurationSupport {

  private static final Set<String> DEFAULT_PRODUCES_CONSUMES = new HashSet<String>(Arrays.asList("application/json"));

  @Value("${default.token.test}") public String defaultValueTest;

  @Autowired private TypeResolver typeResolver;

  @Bean
  public Docket greetingApi() {
    // http://localhost:8010/swagger-ui.html

    ParameterBuilder parameterBuilder = new ParameterBuilder();
    parameterBuilder.name("token").modelRef(new ModelRef("string")).parameterType("header").description("Token API").required(true).defaultValue(defaultValueTest).build();
    List<Parameter> parameters = new ArrayList<>();
    parameters.add(parameterBuilder.build());

    return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.basePackage("com.br.zallpyws.ws")).build().apiInfo(metaData()).useDefaultResponseMessages(false)
        .additionalModels(typeResolver.resolve(ExceptionDTO.class)).produces(DEFAULT_PRODUCES_CONSUMES).consumes(DEFAULT_PRODUCES_CONSUMES).globalResponseMessage(RequestMethod.GET, responseMessages())
        .globalResponseMessage(RequestMethod.POST, responseMessages()).globalOperationParameters(parameters);
  }

  private ApiInfo metaData() {

    return new ApiInfoBuilder().title("ZALLPY APIs").description("Test Spring Boot ZALLPY").version("1.0.0").license("Apache License Version 2.0")
        .licenseUrl("https://www.apache.org/licenses/LICENSE-2.0\"").contact(new Contact("Diogo Bruno de Almeida", "", "diogo.bruno2@gmail.com")).build();
  }

  @SuppressWarnings("serial")
  private List<ResponseMessage> responseMessages() {
    return new ArrayList<ResponseMessage>() {
      {
        add(new ResponseMessageBuilder().code(500).message("Foi gerada uma exceção no servidor (Erro interno na API)").responseModel(new ModelRef("ExceptionDTO")).build());
        add(new ResponseMessageBuilder().code(403).message("É necessário adicionar o TOKEN").responseModel(new ModelRef("ExceptionDTO")).build());
        add(new ResponseMessageBuilder().code(400).message("Validação para entrada de dados com mensagem de retorno").responseModel(new ModelRef("ExceptionDTO")).build());
        add(new ResponseMessageBuilder().code(401).message("TOKEN não é valido").responseModel(new ModelRef("ExceptionDTO")).build());
      }
    };
  }

  @Override
  protected void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
    registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
  }

}