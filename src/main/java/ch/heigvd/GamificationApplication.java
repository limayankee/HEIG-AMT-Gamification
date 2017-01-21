package ch.heigvd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@Import({ SecurityConfiguration.class })
@SpringBootApplication
@EnableSwagger2
public class GamificationApplication {

	@Autowired
	private Environment env;

	@Bean(name = "dataSource")
	public DriverManagerDataSource dataSource() {

		DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
		driverManagerDataSource.setDriverClassName("com.mysql.jdbc.Driver");
		driverManagerDataSource.setUrl(env.getProperty("spring.datasource.url"));
		driverManagerDataSource.setUsername(env.getProperty("spring.datasource.username"));
		driverManagerDataSource.setPassword(env.getProperty("spring.datasource.password"));
		return driverManagerDataSource;
	}

	public static void main(String[] args) {
		SpringApplication.run(GamificationApplication.class, args);
	}
}
