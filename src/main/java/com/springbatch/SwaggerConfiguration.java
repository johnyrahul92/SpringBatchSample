package com.springbatch;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.annotations.Api;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    private static final String HEADER = "header";
    private static final String STRING = "string";

    @Bean
    public ApiInfo beneficiaryApiInfo() {
        final ApiInfoBuilder builder = new ApiInfoBuilder()
                .title("API for Spring batch program for KYC updation")
                .version("1.0")
                .license("(C) Copyright")
                .description("KYC Queue API");
        return builder.build();
    }

    @Bean
    public Docket beneficiaryApiDocket() {
        List<Parameter> internalAPI = new ArrayList<>();
        ParameterBuilder params = new ParameterBuilder();

        internalAPI.add(params.name("content-type").modelRef(new ModelRef(STRING)).parameterType(HEADER).required(false).description("Content type").build());

        internalAPI.add(params.name("accept").modelRef(new ModelRef(STRING)).parameterType(HEADER).required(false).description("Accept Type").build());

        internalAPI.add(params.name("cif").modelRef(new ModelRef(STRING)).parameterType(HEADER).required(true).description("CIF").build());

        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .paths(PathSelectors.regex("/kyc/queue.*"))
                .build().pathMapping("/")
                .apiInfo(beneficiaryApiInfo())
                .groupName("kycQueue")
                .useDefaultResponseMessages(false)
                .globalOperationParameters(internalAPI);
    }
}