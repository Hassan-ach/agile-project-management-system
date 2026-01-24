package com.ensa.agile.seed;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

// @Component
@RequiredArgsConstructor
@Slf4j
public class TestDataSeeder implements CommandLineRunner {

    private final List<Seeder> seeders;

    @Override
    @Transactional
    public void run(String... args) {
        log.info("Starting Data Seeding...");

        seeders.stream()
            .sorted(Comparator.comparingInt(Seeder::getOrder))
            .forEach(seeder -> {
                String seederName = seeder.getClass().getSimpleName();
                log.info("Running {}", seederName);
                try {
                    seeder.seed();
                    log.info("{} completed successfully.", seederName);
                } catch (Exception e) {
                    log.error("Failed to run {}", seederName, e);
                    // Decide if you want to stop or continue
                    throw new RuntimeException("Seeding failed at " + seederName, e); 
                }
            });

        log.info("Data Seeding Finished.");
    }
}
