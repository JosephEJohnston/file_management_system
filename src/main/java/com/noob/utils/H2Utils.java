package com.noob.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class H2Utils {
    private static final String JDBC_DRIVER = "org.h2.Driver";
    private static final String DB_URL = "jdbc:h2:file:~/.file_management/file_management";
    private static final String USER = "sa";
    private static final String PASS = "";

    public static void createTableIfNotExist() {
        try {
            Class.forName(JDBC_DRIVER);

            try (Connection conn = DriverManager.getConnection(DB_URL,USER,PASS);
                 Statement stmt = conn.createStatement()) {

                String sql = """
                        CREATE TABLE IF NOT EXISTS file (
                              `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                              `name` VARCHAR(256) NOT NULL,
                              `parent_id` BIGINT NOT NULL default -1,
                              `full_path` VARCHAR(288) NOT NULL,
                              `size` BIGINT NOT NULL ,
                              `type` INT NOT NULL DEFAULT 1,
                              `status` INT NOT NULL DEFAULT 0,
                              `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                              `modified_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
                        );

                        CREATE INDEX IF NOT EXISTS file_name_index ON file(`name`);

                        CREATE TABLE IF NOT EXISTS tag (
                            `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                            `name` VARCHAR(256) NOT NULL,
                            `status` INT NOT NULL DEFAULT 0,
                            `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            `modified_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
                        );

                        CREATE INDEX IF NOT EXISTS tag_name_index ON tag(`name`);

                        CREATE TABLE IF NOT EXISTS tag_relation (
                            `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                            `tag_id` BIGINT NOT NULL,
                            `entity_id` BIGINT NOT NULL ,
                            `type` INT NOT NULL DEFAULT 1,
                            `status` INT NOT NULL DEFAULT 0,
                            `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            `modified_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
                        );

                        CREATE INDEX IF NOT EXISTS tag_relation_tid_eid_index ON tag_relation(`tag_id`, `entity_id`);

                        """;

                stmt.executeUpdate(sql);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
