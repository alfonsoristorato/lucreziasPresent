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

DELETE FROM lucreziasPresent.user WHERE id BETWEEN 1 AND 10;

INSERT INTO lucreziasPresent.user (id, username, attempts, first_login, password, role)
VALUES (101,'admin', 0, 0, @password, 'admin');
INSERT INTO lucreziasPresent.user (id, username, attempts, first_login, password, role)
VALUES (102, 'user', 0, 0, @password, 'user');
INSERT INTO lucreziasPresent.user (id, username, attempts, first_login, password, role)
VALUES (103, 'userPassChange', 0, 1, @password, 'user');
INSERT INTO lucreziasPresent.user (id, username, attempts, first_login, password, role)
VALUES (104, 'userRoleChange', 0, 0, @password, 'user');
INSERT INTO lucreziasPresent.user (id, username, attempts, first_login, password, role)
VALUES (105, 'adminFirstLogin', 0, 1, @password, 'admin');
INSERT INTO lucreziasPresent.user (id, username, attempts, first_login, password, role)
VALUES (106, 'userFirstLogin', 0, 1, @password, 'user');
INSERT INTO lucreziasPresent.user (id, username, attempts, first_login, password, role)
VALUES (107, 'adminLocked', 4, 0, @password, 'admin');
INSERT INTO lucreziasPresent.user (id, username, attempts, first_login, password, role)
VALUES (108, 'userLocked', 4, 0, @password, 'user');
INSERT INTO lucreziasPresent.user (id, username, attempts, first_login, password, role)
VALUES (109, 'userToLock', 0, 0, @password, 'user');
INSERT INTO lucreziasPresent.user (id, username, attempts, first_login, password, role)
VALUES (110, 'userResetPassword', 0, 0, @password, 'user');

