package com.alfonsoristorato.lucreziaspresentbackend.integrationTests;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Component
public class DatabaseInitializer {

    @Value("${spring.datasource.url}")
    private String dataSourceUrl;

    private final PasswordEncoder passwordEncoder;

    private final List<String> dbFeedQuery = List.of(
            """
                    CREATE TABLE IF NOT EXISTS user (
                    id bigint(20) NOT NULL,
                    attempts int(11) NOT NULL,
                    first_login tinyint(1) NOT NULL,
                    password varchar(255) DEFAULT NULL,
                    role varchar(255) DEFAULT NULL,
                    username varchar(255) NOT NULL,
                    PRIMARY KEY (id),
                    UNIQUE KEY UK_sb8bbouer5wak8vyiiy4pf2bx (username)
                    ) ENGINE=InnoDB DEFAULT CHARSET=latin1;
                    """,
                    """
                    CREATE TABLE IF NOT EXISTS entry (
                    id bigint NOT NULL AUTO_INCREMENT,
                    color varchar(255) DEFAULT NULL,
                    content longtext,
                    date date DEFAULT NULL,
                    file longblob,
                    icon int DEFAULT NULL,
                    name varchar(255) DEFAULT NULL,
                    owner varchar(255) DEFAULT NULL,
                    title varchar(255) DEFAULT NULL,
                    PRIMARY KEY (`id`)
                    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
                    """,
            "DELETE FROM entry WHERE id BETWEEN 1 AND 2;",
            "INSERT INTO entry (color, content, date, icon, name, owner, title) VALUES ('red','content1','2023-10-10',1,'name1','user','title1');",
            "INSERT INTO entry (color, content, date, icon, name, owner, title) VALUES ('red','content2','2023-02-10',1,'name2','admin','title2');",
            "DELETE FROM user WHERE id BETWEEN 101 AND 110;",
            "INSERT INTO user (id, username, attempts, first_login, password, role) VALUES (101,'admin', 0, 0, @password, 'admin');",
            "INSERT INTO user (id, username, attempts, first_login, password, role) VALUES (102, 'user', 0, 0, @password, 'user');",
            "INSERT INTO user (id, username, attempts, first_login, password, role) VALUES (103, 'userPassChange', 0, 1, @password, 'user');",
            "INSERT INTO user (id, username, attempts, first_login, password, role) VALUES (104, 'userRoleChange', 0, 0, @password, 'user');",
            "INSERT INTO user (id, username, attempts, first_login, password, role) VALUES (105, 'adminFirstLogin', 0, 1, @password, 'admin')",
            "INSERT INTO user (id, username, attempts, first_login, password, role) VALUES (106, 'userFirstLogin', 0, 1, @password, 'user');",
            "INSERT INTO user (id, username, attempts, first_login, password, role) VALUES (107, 'adminLocked', 4, 0, @password, 'admin');",
            "INSERT INTO user (id, username, attempts, first_login, password, role) VALUES (108, 'userLocked', 4, 0, @password, 'user');",
            "INSERT INTO user (id, username, attempts, first_login, password, role) VALUES (109, 'userToLock', 0, 0, @password, 'user');",
            "INSERT INTO user (id, username, attempts, first_login, password, role) VALUES (110, 'userResetPassword', 0, 0, @password, 'user');"
    );

    public DatabaseInitializer(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    private void populateDatabase() {
        String defaultPass = passwordEncoder.encode("defaultPass");
        try (Connection connection = DriverManager.getConnection(dataSourceUrl);
                Statement statement = connection.createStatement()) {
            String passwordVariableQuery = String.format("SET @password = '%s';", defaultPass);
            statement.execute(passwordVariableQuery);
            for (String query : dbFeedQuery) {
                statement.execute(query);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
