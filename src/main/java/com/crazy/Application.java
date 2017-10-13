package com.crazy;

import com.crazy.config.web.StartedEventListener;
import com.crazy.core.BMDataContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;


@EnableAutoConfiguration
@SpringBootApplication
@EnableAsync
@EnableJpaRepositories("com.crazy.web.service.repository.jpa")
@EnableElasticsearchRepositories("com.crazy.web.service.repository.es")
public class Application {
    
	public static void main(String[] args) {
		System.setProperty("hazelcast.local.localAddress", "127.0.0.1");
		SpringApplication springApplication = new SpringApplication(Application.class) ;
		springApplication.addListeners(new StartedEventListener());
		BMDataContext.setApplicationContext(springApplication.run(args));
	}
	
}
