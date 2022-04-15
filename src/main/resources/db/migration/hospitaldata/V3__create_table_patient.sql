CREATE TABLE IF NOT EXISTS migrations.PATIENT (
                                        id INT4,
                                        name TEXT NOT NULL,
                                        age TEXT NOT NULL,
                                        last_visit_date DATE NOT NULL,
                                        PRIMARY KEY (id)
    );


