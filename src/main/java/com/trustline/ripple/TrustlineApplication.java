package com.trustline.ripple;

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.web.context.support.StandardServletEnvironment;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class TrustlineApplication {
	   private static final Logger log = LoggerFactory.getLogger(TrustlineApplication.class);
	    

		public static void main(String[] args) {
	        SpringApplication springApplication = new SpringApplication(TrustlineApplication.class);
	        ApplicationContext ctx = springApplication.run(args);

	        MutablePropertySources propertySources = ((StandardServletEnvironment) ctx.getEnvironment())
	                .getPropertySources();
	        Iterator<org.springframework.core.env.PropertySource<?>> iterator = propertySources.iterator();
	        while (iterator.hasNext())
	        {
	            Object propertySourceObject = iterator.next();
	            if ( propertySourceObject instanceof org.springframework.core.env.PropertySource )
	            {
	                org.springframework.core.env.PropertySource<?> propertySource = (org.springframework.core.env.PropertySource<?>) propertySourceObject;
	            }
	        }
	    }
		
		@Bean
		public Docket api() {
			return new Docket(DocumentationType.SWAGGER_2).select()
					.apis(RequestHandlerSelectors.basePackage("com.ge.dcs.api"))
					.paths(PathSelectors.ant("/.well-known/est/**")).build().apiInfo(apiInfo());
		}

		private ApiInfo apiInfo() {
			ApiInfo apiInfo = new ApiInfo("DCS API", "Device Certificate Service", "", "", "", "", "");
			return apiInfo;
		}
}