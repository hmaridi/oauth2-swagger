package com.oauth.web.swagger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.CrossOrigin;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ApiKeyVehicle;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hibernate.validator.internal.util.CollectionHelper.newArrayList;

@Configuration
@EnableSwagger2
@CrossOrigin
public class SwaggerConfig {

	@Value("${config.oauth2.accessTokenUri}")
	private String accessTokenUri;

	@Bean
	public Docket UserApi() {

		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.oauth.web.controllers"))
				.paths(PathSelectors.any())
				.build()
				.securityContexts(Collections.singletonList(securityContext()))
		        .securitySchemes(Arrays.asList(securitySchema())).apiInfo(getApiInfo());

	}

	private OAuth securitySchema() {

		List<AuthorizationScope> authScopeList = newArrayList();
		authScopeList.add(new AuthorizationScope("read", "read all"));
		authScopeList.add(new AuthorizationScope("write", "read all"));
		authScopeList.add(new AuthorizationScope("trust", "access all"));

		List<GrantType> grantTypes = newArrayList();
		GrantType pwdCredentialsGrant = new ResourceOwnerPasswordCredentialsGrant(accessTokenUri);
		grantTypes.add(pwdCredentialsGrant);

		return new OAuth("oauth2", authScopeList, grantTypes);
	}

	private SecurityContext securityContext() {
		return SecurityContext.builder().securityReferences(defaultAuth()).build();
	}

	private List<SecurityReference> defaultAuth() {

		final AuthorizationScope[] authScopes = new AuthorizationScope[3];
		authScopes[0] = new AuthorizationScope("read", "read all");
		authScopes[1] = new AuthorizationScope("trust", "trust all");
		authScopes[2] = new AuthorizationScope("write", "write all");

		return Collections.singletonList(new SecurityReference("oauth2", authScopes));
	}

	@SuppressWarnings("deprecation")
	@Bean
	public SecurityConfiguration security() {
		return new SecurityConfiguration("client", "secret", "", "", "Bearer access token",
				ApiKeyVehicle.HEADER, "Authorization", ": Bearer");
	}

	private ApiInfo getApiInfo() {
		Contact contact = new Contact("HARI KISHORE", "mhari.dropbox@gmail.com", null);
		return new ApiInfoBuilder().title("Spring Boot 2.0 REST API")
				.description("SpringBoot With RESTful Using User-Registration").version("2.0.1").license("Apache 2.0")
				.contact(contact).build();
	}

}
