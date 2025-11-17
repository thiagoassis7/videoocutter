package com.thiago.videoocutter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;


@SpringBootApplication
@EnableAsync
public class VideoocutterApplication {

	public static void main(String[] args) {
		SpringApplication.run(VideoocutterApplication.class, args);
	}

}
