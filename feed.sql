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

INSERT INTO lucreziasPresent.user (id, username, attempts, first_login, password, role)
VALUES (1,'admin',0,0,@password,'admin');
INSERT INTO lucreziasPresent.user (id, username, attempts, first_login, password, role)
VALUES (2, 'user', 0, 0, @password, 'user');
INSERT INTO lucreziasPresent.user (id, username, attempts, first_login, password, role)
VALUES (3, 'userPassChange', 0, 1, @password, 'user');
INSERT INTO lucreziasPresent.user (id, username, attempts, first_login, password, role)
VALUES (4, 'userRoleChange', 0, 0, @password, 'user');
INSERT INTO lucreziasPresent.user (id, username, attempts, first_login, password, role)
VALUES (5, 'adminFirstLogin', 0, 1, @password, 'admin');
INSERT INTO lucreziasPresent.user (id, username, attempts, first_login, password, role)
VALUES (6, 'userFirstLogin', 0, 1, @password, 'user');
INSERT INTO lucreziasPresent.user (id, username, attempts, first_login, password, role)
VALUES (7, 'adminLocked', 4, 0, @password, 'admin');
INSERT INTO lucreziasPresent.user (id, username, attempts, first_login, password, role)
VALUES (8, 'userLocked', 4, 0, @password, 'user');
INSERT INTO lucreziasPresent.user (id, username, attempts, first_login, password, role)
VALUES (9, 'userToLock', 0, 0, @password, 'user');
INSERT INTO lucreziasPresent.user (id, username, attempts, first_login, password, role)
VALUES (10, 'userResetPassword', 0, 0, @password, 'user');

