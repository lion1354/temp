package com.popular.common.swagger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.mangofactory.swagger.configuration.SpringSwaggerConfig;
import com.mangofactory.swagger.models.dto.ApiInfo;
import com.mangofactory.swagger.plugin.EnableSwagger;
import com.mangofactory.swagger.plugin.SwaggerSpringMvcPlugin;

/*import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;*/

/**
 * 
 * @author Aidan
 *
 * @date 2016/7/21
 *
 */
@Configuration
@EnableWebMvc
@EnableSwagger
//@ComponentScan("com.popular.rest.api")
public class SwaggerConfig {
	private SpringSwaggerConfig springSwaggerConfig;
	
	/**
	 * Required to autowire SpringSwaggerConfig
	 */
	@Autowired
	public void setSpringSwaggerConfig(SpringSwaggerConfig springSwaggerConfig) {
		this.springSwaggerConfig = springSwaggerConfig;
	}

	/**
	 * Every SwaggerSpringMvcPlugin bean is picked up by the swagger-mvc
	 * framework - allowing for multiple swagger groups i.e. same code base
	 * multiple swagger resource listings.
	 */
	@Bean
	public SwaggerSpringMvcPlugin customImplementation() {
		return new SwaggerSpringMvcPlugin(this.springSwaggerConfig).apiInfo(
				apiInfo()).includePatterns(".*1.*");
	}
	
	private ApiInfo apiInfo() {
		ApiInfo apiInfo = new ApiInfo("Popular API INFO", // title
				"popular api info", // description
				"", // termsOfServiceUrl
				"", // contact
				"", // license
				""// licenseUrl
		);
		return apiInfo;
	}
	
}
