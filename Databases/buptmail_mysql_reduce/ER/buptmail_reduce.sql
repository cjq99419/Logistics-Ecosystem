-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema buptmail
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `buptmail` ;

-- -----------------------------------------------------
-- Schema buptmail
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `buptmail` DEFAULT CHARACTER SET utf8 ;
USE `buptmail` ;

-- -----------------------------------------------------
-- Table `buptmail`.`Orders`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `buptmail`.`Orders` ;

CREATE TABLE IF NOT EXISTS `buptmail`.`Orders` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `sender_name` VARCHAR(45) NOT NULL,
  `sender_tel` VARCHAR(45) NOT NULL,
  `sender_address` VARCHAR(45) NOT NULL,
  `recipient_name` VARCHAR(45) NOT NULL,
  `recipient_tel` VARCHAR(45) NOT NULL,
  `recipient_address` VARCHAR(45) NOT NULL,
  `type` VARCHAR(45) NOT NULL,
  `price` INT UNSIGNED NOT NULL,
  `mode_payment` VARCHAR(45) NOT NULL,
  `weight` INT UNSIGNED NOT NULL,
  `status` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `buptmail`.`User`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `buptmail`.`User` ;

CREATE TABLE IF NOT EXISTS `buptmail`.`User` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_name` VARCHAR(45) NOT NULL,
  `password` VARCHAR(20) NOT NULL,
  `tel` VARCHAR(20) NOT NULL,
  `email` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `buptmail`.`Staff`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `buptmail`.`Staff` ;

CREATE TABLE IF NOT EXISTS `buptmail`.`Staff` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `staff_name` VARCHAR(45) NOT NULL,
  `password` VARCHAR(20) NOT NULL,
  `tel` VARCHAR(45) NOT NULL,
  `email` VARCHAR(45) NULL,
  `address_region` VARCHAR(45) NOT NULL,
  `salary` INT UNSIGNED NOT NULL,
  `position` VARCHAR(20) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
