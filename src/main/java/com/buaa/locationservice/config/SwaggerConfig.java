package com.buaa.locationservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
@Profile({"dev","test"})
public class SwaggerConfig {
	/**
	 * 创建swagger ui的摘要
	 * @return
	 */
	@Bean
	public Docket docket() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo())
				.select()
				// 扫描的class的包路径
				.apis(RequestHandlerSelectors.basePackage("com.buaa.locationservice.controller"))
				// 只扫描类上有API注解的class
                // .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
				// 只扫描方法上有ApiOperation注解的方法
                //.apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
				.paths(PathSelectors.any())
				.build();
	}

	/**
	 * swagger ui的标题信息
	 * @return
	 */
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("卫星定位系统")
				.description("API文档")
				.version("1.0")
				.build();
	}
}
