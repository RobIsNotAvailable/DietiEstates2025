package com.dd25.dietiestates25;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import com.dd25.dietiestates25.model.Address;

import com.dd25.dietiestates25.service.GeoapifyService;

@SpringBootApplication
public class DietiEstates25Application 
{

	public static void main(String[] args)
	{
		SpringApplication.run(DietiEstates25Application.class, args);
	}	


	@Bean
    public CommandLineRunner testGeoapify(GeoapifyService geoapifyService) {
        return args -> {
            System.out.println("--- INIZIO TEST GEOAPIFY ---");
            try {
                Address addr = geoapifyService.normalizeAddress("PUT AN ADDRESS HERE FOR TESTING");
                System.out.println("Città trovata: " + addr.getCity());
                System.out.println("Indirizzo completo: " + addr.getFormattedAddress());
                System.out.println("Lat/Lon: " + addr.getLatitude() + " / " + addr.getLongitude());
            } catch (Exception e) {
                System.err.println("Errore durante il test: " + e.getMessage());
            }
            System.out.println("--- FINE TEST ---");
        };
    }
}
