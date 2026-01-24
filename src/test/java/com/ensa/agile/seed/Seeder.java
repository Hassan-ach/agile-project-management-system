package com.ensa.agile.seed;

public interface Seeder {
    /**
     * seed() executes the logic to populate the database.
     */
    void seed();

    /**
     * getOrder() determines the execution priority.
     * Lower numbers run first.
     */
    int getOrder();
}
