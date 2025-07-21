package com.app;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

@Configuration
class I18NConfig implements WebMvcConfigurer{
	@Bean(name = "messageSource")
	public MessageSource getMessageSource() {
		ReloadableResourceBundleMessageSource ms = new ReloadableResourceBundleMessageSource();
		ms.setDefaultEncoding("utf-8");
		ms.setBasename("classpath:i18n/global");
		return ms;
	}
	@Bean(name = "localeResolver")
	public LocaleResolver getLocaleResolver() {
		CookieLocaleResolver resolver = new CookieLocaleResolver();
		resolver.setCookiePath("/");
		resolver.setCookieMaxAge(60*60);
		resolver.setDefaultLocale(Locale.ENGLISH);
		return resolver;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
		lci.setParamName("lang");
		registry.addInterceptor(lci)
		.addPathPatterns("/**")
		.excludePathPatterns("/images/**");
	}
}
