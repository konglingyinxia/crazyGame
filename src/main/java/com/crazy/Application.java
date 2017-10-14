package com.crazy;

import com.crazy.config.web.StartedEventListener;
import com.crazy.core.BMDataContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableAutoConfiguration
@SpringBootApplication
@EnableScheduling
@EnableAsync
@EnableJpaRepositories("com.crazy.web.service.repository.jpa")
@EnableElasticsearchRepositories("com.crazy.web.service.repository.es")
public class Application {
    
	public static void main(String[] args) {
		System.setProperty("hazelcast.local.localAddress", "10.80.114.163");
		SpringApplication springApplication = new SpringApplication(Application.class) ;
		//Listener会自动加载的~ springApplication.addListeners(new StartedEventListener());
		BMDataContext.setApplicationContext(springApplication.run(args));
	}
	
}
