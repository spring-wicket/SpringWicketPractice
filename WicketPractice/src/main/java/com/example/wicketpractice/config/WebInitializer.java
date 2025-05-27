package com.example.wicketpractice.config;

import java.util.Set;

import org.apache.wicket.protocol.http.WicketFilter;
import org.apache.wicket.spring.SpringWebApplicationFactory;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Configuration;

import jakarta.servlet.FilterRegistration;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.SessionTrackingMode;

// Wicketでのweb.xmlのfilter設定の代わりを行う
@Configuration
public class WebInitializer implements ServletContextInitializer {

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		FilterRegistration filter = servletContext.addFilter("wicket-filter", WicketFilter.class);
	    filter.setInitParameter(WicketFilter.APP_FACT_PARAM, SpringWebApplicationFactory.class.getName());
	    filter.setInitParameter("applicationBean", "wicketPracticeApplication");
	    filter.setInitParameter(WicketFilter.FILTER_MAPPING_PARAM, "/*");
	    filter.addMappingForUrlPatterns(null, false, "/*");
	    filter.setInitParameter("configuration", "development");
	    
	    servletContext.setSessionTrackingModes(Set.of(SessionTrackingMode.COOKIE));

	}

}
