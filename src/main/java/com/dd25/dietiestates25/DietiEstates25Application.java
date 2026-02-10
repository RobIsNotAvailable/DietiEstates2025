package com.dd25.dietiestates25;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.dd25.dietiestates25.model.SurroundingInfo;
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
    public CommandLineRunner testSurroundingInfo(GeoapifyService geoapifyService) 
    {
        return args -> 
        {
            System.out.println("\n--- [DEBUG GEOAPIFY] Test recupero POI ---");

            String rawAddress= "via salvador dalì 111 napoli";
            Address addr = geoapifyService.normalizeAddress(rawAddress);
            
            System.out.println("Indirizzo normalizzato: " + addr.getStreet() + " " + addr.getHouseNumber() + ", " + addr.getCity() + ", " + addr.getProvince() + ", " + addr.getCountry());
            try 
            {
                System.out.println("Interrogando Geoapify per Lat: " + addr.getLatitude() + ", Lon: " + addr.getLongitude() + "...");
                
                SurroundingInfo info = geoapifyService.fetchSurroundingInfo(addr.getLatitude(), addr.getLongitude());

                System.out.println("\n--- RISULTATI DINTORNI ---");
                System.out.println("Fermate mezzi pubblici: " + (info.isNearStops() ? "✅ TROVATE" : "❌ NON TROVATE"));
                System.out.println("Parchi/Verde:          " + (info.isNearParks() ? "✅ TROVATI" : "❌ NON TROVATI"));
                System.out.println("Scuole:                " + (info.isNearSchools() ? "✅ TROVATE" : "❌ NON TROVATE"));
                System.out.println("---------------------------\n");

            } 
            catch (Exception e) 
            {
                System.err.println("Errore durante il test Geoapify: " + e.getMessage());
                e.printStackTrace();
            }
        };
    }
}
