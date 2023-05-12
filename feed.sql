CREATE TABLE IF NOT EXISTS `user` (
                        `id` bigint(20) NOT NULL,
                        `attempts` int(11) NOT NULL,
                        `first_login` tinyint(1) NOT NULL,
                        `password` varchar(255) DEFAULT NULL,
                        `role` varchar(255) DEFAULT NULL,
                        `username` varchar(255) NOT NULL,
                        PRIMARY KEY (`id`),
                        UNIQUE KEY `UK_sb8bbouer5wak8vyiiy4pf2bx` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

SET @password = 'YOURPASSWORD';

DELETE FROM lucreziasPresent.user WHERE id = 1;

INSERT INTO lucreziasPresent.user (id, username, attempts, first_login, password, role)
VALUES (999, 'admin', 0, 0, @password, 'admin');

