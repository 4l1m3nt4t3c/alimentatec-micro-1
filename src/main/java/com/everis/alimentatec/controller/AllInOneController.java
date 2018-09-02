package com.everis.alimentatec.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.everis.alimentatec.domain.Persona;
import com.everis.alimentatec.domain.Saludo;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@RefreshScope
@EnableCircuitBreaker
@RestController
public class AllInOneController {

	@Value("${customprop.vercambiosdeconfig}")
	private String vercambiosdeconfig;
	@Autowired
	private DiscoveryClient discoveryClient; // No específico de Eureka, nos serviría para otros servicios de
												// descubrimiento
	@Autowired
	Environment environment;

	@RequestMapping(value = "/hola", method = RequestMethod.POST)
	// Aquí podemos meter props de hystrix o en la config para aislar en otro hilo
	// las peticiones fallback o forzar al mismo hilo (semáforo) y si se quiere
	// compartir el contexto de seguridad shareSecurityContext
	@HystrixCommand(fallbackMethod = "fallbackMethodHolaMundo")
	public Saludo holaMundo(@RequestBody Persona persona) throws IOException {
		if (persona.getNombre().equals("error")) {
			throw new IOException("Esto es un error provocado para probar un fallback de hystrix");
		}
		Saludo saludo = new Saludo();
		saludo.setFrase("" + vercambiosdeconfig + " PUERTO: " + environment.getProperty("local.server.port"));
		saludo.setPersona(persona);
		return saludo;
	}

	public Saludo fallbackMethodHolaMundo(@RequestBody Persona persona) {
		Saludo saludo = new Saludo();
		saludo.setFrase("Esto es el método fallback " + environment.getProperty("local.server.port"));
		Persona fallbackPersona = new Persona();
		fallbackPersona.setNombre("FALLBACK");
		fallbackPersona.setApellidos("FALLBACK");
		saludo.setPersona(fallbackPersona);
		return saludo;
	}

	@RequestMapping("/service-instances/{applicationName}")
	public List<ServiceInstance> serviceInstancesByApplicationName(@PathVariable String applicationName) {
		return this.discoveryClient.getInstances(applicationName);
	}

}
