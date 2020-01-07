/**
 * 
 */
package com.kratonsolution.skrip.leon;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.jsondb.JsonDBTemplate;

/**
 * @author Leon
 *
 */
@Configuration
public class DBConfig {

	@Bean 
	public JsonDBTemplate dbTemplat() {
		
		return new JsonDBTemplate("diagnosadb", "com.kratonsolution.skrip.leon.model");
	}
}
