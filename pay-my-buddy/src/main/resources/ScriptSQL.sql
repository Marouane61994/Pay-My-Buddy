-- -----------------------------------------------------
-- Schema p6
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `p6`;
USE `p6`;

-- -----------------------------------------------------
-- Table `p6`.`Users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `p6`.`Users` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(255) NOT NULL,
  `email` VARCHAR(255) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `balance` double DEFAULT NULL,
  INDEX `idx_username` (`username` ASC) VISIBLE,
  INDEX `idx_email` (`email` ASC) VISIBLE,
  PRIMARY KEY (`id`)
) ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `p6`.`Transactions`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `p6`.`Transactions` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `sender_id` INT NOT NULL,
  `receiver_id` INT NOT NULL,
  `description` TEXT NULL,
  `amount` DOUBLE NOT NULL,
  `timestamp` datetime(6) DEFAULT NULL,
  INDEX `idx_sender_id` (`sender_id` ASC) VISIBLE,
  INDEX `idx_receiver_id` (`receiver_id` ASC) VISIBLE,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_sender_id`
    FOREIGN KEY (`sender_id`)
    REFERENCES `p6`.`Users` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_receiver_id`
    FOREIGN KEY (`receiver_id`)
    REFERENCES `p6`.`Users` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `p6`.`Users_Connections`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `p6`.`Users_Connections` (
   `id` int NOT NULL AUTO_INCREMENT,
   `connection_id` int DEFAULT NULL,
   `users_id` int DEFAULT NULL,
  INDEX `idx_users_id` (`users_id` ASC) VISIBLE,
  INDEX `idx_connection_id` (`connection_id` ASC) VISIBLE,
  CONSTRAINT `fk_users_id`
    FOREIGN KEY (`users_id`)
    REFERENCES `p6`.`Users` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_connection_id`
    FOREIGN KEY (`connection_id`)
    REFERENCES `p6`.`Users` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE = InnoDB;

-- -----------------------------------------------------
-- Données de test - Utilisateurs
-- -----------------------------------------------------
INSERT IGNORE INTO `Users` (`username`, `email`, `password`, `balance`) VALUES
('Alice', 'alice@example.com', '$2a$10$G8U3VdjPdUjNvw0N/M2eS.0fUjo6ENIxsrntZL4wnGzeZqvbuubCG', 100.00),-- Email : alice@example.com / Mot de passe : password123--
('Bob', 'bob@example.com', '$2a$10$G8U3VdjPdUjNvw0N/M2eS.0fUjo6ENIxsrntZL4wnGzeZqvbuubCG', 50.12),-- Email : bob@example.com / Mot de passe : password123--
('Charlie', 'charlie@example.com', '$2a$10$G8U3VdjPdUjNvw0N/M2eS.0fUjo6ENIxsrntZL4wnGzeZqvbuubCG', 24.70),-- Email : charlie@example.com /  Mot de passe : password123--
('David', 'david@example.com', '$2a$10$G8U3VdjPdUjNvw0N/M2eS.0fUjo6ENIxsrntZL4wnGzeZqvbuubCG', 0.00);-- Email : david@example.com / Mot de passe : password123--


