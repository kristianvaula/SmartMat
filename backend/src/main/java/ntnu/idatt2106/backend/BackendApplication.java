package ntnu.idatt2106.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * BackendApplication is contains the main method for the REST api
 */
@SpringBootApplication
public class BackendApplication {


	/**
	 * This is the main method of the Spring Boot application.
	 * @param args An array of String arguments passed to the application. These arguments are used to configure
	 */
	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}
}
