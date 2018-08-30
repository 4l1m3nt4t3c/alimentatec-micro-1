package com.everis.alimentatec.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Configuraciones {
	
	
	@ConfigurationProperties(prefix = "customprop")
	public static class Customprop
	{
		String vercambiosdeconfig;
	}
	

}
