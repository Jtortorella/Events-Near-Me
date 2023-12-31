package musicEventsNearMe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import musicEventsNearMe.services.DatabaseService;

@SpringBootApplication
public class MusicEventsNearMeApplication implements CommandLineRunner {

	@Autowired
	private DatabaseService concertDataService;

	public static void main(String[] args) {
		SpringApplication.run(MusicEventsNearMeApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		concertDataService.updateDatabaseWithConcertData();
	}
}
