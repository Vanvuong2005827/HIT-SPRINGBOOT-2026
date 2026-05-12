package com.example.base;

import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import com.example.base.domain.entity.User;
import com.example.base.domain.entity.Role;

import com.example.base.config.UserInfoProperties;
import com.example.base.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@EnableConfigurationProperties(UserInfoProperties.class)
@SpringBootApplication(scanBasePackages = "com.example.base")
public class BaseApplication {

	private final UserRepository userRepository;

	public static void main(String[] args) {
		Environment env = SpringApplication.run(BaseApplication.class, args).getEnvironment();
		String appName = env.getProperty("spring.application.name");
		if (appName != null) {
			appName = appName.toUpperCase();
		}
		String port = env.getProperty("server.port");
		log.info("-------------------------START " + appName
				+ " Application------------------------------");
		log.info("   Application         : " + appName);
		log.info("   Url swagger-ui      : http://localhost:" + port + "/swagger-ui.html");
		log.info("-------------------------START SUCCESS " + appName
				+ " Application------------------------------");
	}

	@Bean
	CommandLineRunner init(UserInfoProperties userInfo, PasswordEncoder passwordEncoder) {
		return arg -> {
			// Init account admin
			if (userRepository.count() == 0) {
				User admin = User.builder()
						.username(userInfo.getUsername())
						.password(passwordEncoder.encode(userInfo.getPassword()))
						.email(userInfo.getEmail())
						.firstName(userInfo.getFirstName())
						.lastName(userInfo.getLastName())
						.role(Role.ADMIN)
						.build();
				userRepository.save(admin);
				log.info("Admin user created successfully: {}", userInfo.getUsername());
			} else {
				log.info("Admin user already exists");
			}
		};
	}
}