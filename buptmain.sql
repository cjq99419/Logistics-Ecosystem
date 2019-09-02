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
-- Table `buptmail`.`Address`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `buptmail`.`Address` ;

CREATE TABLE IF NOT EXISTS `buptmail`.`Address` (
  `id` INT UNSIGNED NOT NULL,
  `country` VARCHAR(45) NOT NULL,
  `province` VARCHAR(45) NOT NULL,
  `city` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `buptmail`.`User`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `buptmail`.`User` ;

CREATE TABLE IF NOT EXISTS `buptmail`.`User` (
  `id` INT UNSIGNED NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `password` VARCHAR(45) NOT NULL,
  `tel` VARCHAR(45) NOT NULL,
  `email` VARCHAR(45) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `buptmail`.`Mail`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `buptmail`.`Mail` ;

CREATE TABLE IF NOT EXISTS `buptmail`.`Mail` (
  `id` INT UNSIGNED NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `tel` VARCHAR(20) NOT NULL,
  `Address_id` INT UNSIGNED NOT NULL,
  `User_id` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_Mail_Address1_idx` (`Address_id` ASC),
  INDEX `fk_Mail_User1_idx` (`User_id` ASC),
  CONSTRAINT `fk_Mail_Address1`
    FOREIGN KEY (`Address_id`)
    REFERENCES `buptmail`.`Address` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Mail_User1`
    FOREIGN KEY (`User_id`)
    REFERENCES `buptmail`.`User` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `buptmail`.`Driver`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `buptmail`.`Driver` ;

CREATE TABLE IF NOT EXISTS `buptmail`.`Driver` (
  `id` INT UNSIGNED NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `tel` VARCHAR(20) NOT NULL,
  `Address_id` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_Driver_Address1_idx` (`Address_id` ASC),
  CONSTRAINT `fk_Driver_Address1`
    FOREIGN KEY (`Address_id`)
    REFERENCES `buptmail`.`Address` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `buptmail`.`Car`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `buptmail`.`Car` ;

CREATE TABLE IF NOT EXISTS `buptmail`.`Car` (
  `id` INT UNSIGNED NOT NULL,
  `location` VARCHAR(45) NOT NULL,
  `Driver_id` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_Car_Driver1_idx` (`Driver_id` ASC),
  CONSTRAINT `fk_Car_Driver1`
    FOREIGN KEY (`Driver_id`)
    REFERENCES `buptmail`.`Driver` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `buptmail`.`Box`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `buptmail`.`Box` ;

CREATE TABLE IF NOT EXISTS `buptmail`.`Box` (
  `id` INT UNSIGNED NOT NULL,
  `manufacture` DATE NOT NULL,
  `length` INT UNSIGNED NOT NULL,
  `width` INT UNSIGNED NOT NULL,
  `height` INT UNSIGNED NOT NULL,
  `weight` INT UNSIGNED NOT NULL,
  `password` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `buptmail`.`Staff`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `buptmail`.`Staff` ;

CREATE TABLE IF NOT EXISTS `buptmail`.`Staff` (
  `id` INT UNSIGNED NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `password` VARCHAR(45) NOT NULL,
  `tel` VARCHAR(45) NOT NULL,
  `email` VARCHAR(45) NULL,
  `salary` INT UNSIGNED NOT NULL,
  `position` VARCHAR(45) NOT NULL,
  `Address_id` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_Staff_Address1_idx` (`Address_id` ASC),
  CONSTRAINT `fk_Staff_Address1`
    FOREIGN KEY (`Address_id`)
    REFERENCES `buptmail`.`Address` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `buptmail`.`Order`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `buptmail`.`Order` ;

CREATE TABLE IF NOT EXISTS `buptmail`.`Order` (
  `id` INT UNSIGNED NOT NULL,
  `weight` INT UNSIGNED NOT NULL,
  `type` VARCHAR(45) NOT NULL,
  `price` INT UNSIGNED NOT NULL,
  `mode_payment` VARCHAR(45) NOT NULL,
  `Ordercol` VARCHAR(45) NOT NULL,
  `time` DATE NOT NULL,
  `Car_id` INT UNSIGNED NOT NULL,
  `Box_id` INT UNSIGNED NOT NULL,
  `sender_id` INT UNSIGNED NOT NULL,
  `recipient_id` INT UNSIGNED NOT NULL,
  `Staff_id` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_Order_Car_idx` (`Car_id` ASC),
  INDEX `fk_Order_Box1_idx` (`Box_id` ASC),
  INDEX `fk_Order_Mail1_idx` (`sender_id` ASC),
  INDEX `fk_Order_Mail2_idx` (`recipient_id` ASC),
  INDEX `fk_Order_Staff1_idx` (`Staff_id` ASC),
  CONSTRAINT `fk_Order_Car`
    FOREIGN KEY (`Car_id`)
    REFERENCES `buptmail`.`Car` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Order_Box1`
    FOREIGN KEY (`Box_id`)
    REFERENCES `buptmail`.`Box` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Order_Mail1`
    FOREIGN KEY (`sender_id`)
    REFERENCES `buptmail`.`Mail` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Order_Mail2`
    FOREIGN KEY (`recipient_id`)
    REFERENCES `buptmail`.`Mail` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Order_Staff1`
    FOREIGN KEY (`Staff_id`)
    REFERENCES `buptmail`.`Staff` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
