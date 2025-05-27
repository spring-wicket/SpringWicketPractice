package com.example.wicketpractice;

import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.util.crypt.CharEncoding;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.example.wicketpractice.page.Practice;

/**
 * 
 * Employeeテーブル(int id, String name)を表示するプログラム
 *
 */
@SpringBootApplication
public class WicketPracticeApplication extends WebApplication {
	
	private final ApplicationContext context;
	
//	@Autowired
	public WicketPracticeApplication(ApplicationContext context) {
		this.context = context;
	}

	public static void main(String[] args) {
		SpringApplication.run(WicketPracticeApplication.class, args);
	}

	@Override
	public Class<? extends Page> getHomePage() {
		return Practice.class;
	}
	@Override
	protected void init() {
		super.init();
		getRequestCycleSettings().setResponseRequestEncoding(CharEncoding.UTF_8);
		getMarkupSettings().setDefaultMarkupEncoding(CharEncoding.UTF_8);
		
		getComponentInstantiationListeners().add(new SpringComponentInjector(this, context));
		mountPage("/top", Practice.class);
	}

}
