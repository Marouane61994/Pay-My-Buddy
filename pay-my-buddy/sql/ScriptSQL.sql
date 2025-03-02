-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema Users
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema Users
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `Users` DEFAULT CHARACTER SET utf8 ;
-- -----------------------------------------------------
-- Schema User
-- -----------------------------------------------------
-- This schema was created for a stub table

-- -----------------------------------------------------
-- Schema User
--
-- This schema was created for a stub table
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `User` ;
USE `Users` ;

-- -----------------------------------------------------
-- Table `Users`.`Users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Users`.`Users` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(255) NOT NULL,
  `email` VARCHAR(255) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  INDEX `username` (`username` ASC) VISIBLE,
  INDEX `email` (`email` ASC) VISIBLE,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Users`.`Transactions`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Users`.`Transactions` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `sender_id` INT NOT NULL,
  `receiver_id` INT NOT NULL,
  `description` TEXT NULL,
  `amount` DOUBLE NOT NULL,
  INDEX `sender_id` (`sender_id` ASC) VISIBLE,
  INDEX `receiver_id` (`receiver_id` ASC) VISIBLE,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_sender_id`
    FOREIGN KEY (`sender_id`)
    REFERENCES `Users`.`Users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_receiver_id`
    FOREIGN KEY (`receiver_id`)
    REFERENCES `Users`.`Users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Users`.`Users_Connections`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Users`.`Users_Connections` (
  `users_id` INT NOT NULL,
  `connection_id` INT NOT NULL,
  INDEX `users_id _idx` (`users_id` ASC) VISIBLE,
  INDEX `connection_id _idx` (`connection_id` ASC) VISIBLE,
  CONSTRAINT `users_id `
    FOREIGN KEY (`users_id`)
    REFERENCES `Users`.`Users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `connection_id `
    FOREIGN KEY (`connection_id`)
    REFERENCES `Users`.`Users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

USE `User` ;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
