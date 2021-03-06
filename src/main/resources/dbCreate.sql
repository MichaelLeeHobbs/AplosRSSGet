-- MySQL Script generated by MySQL Workbench
-- Mon 01 Jun 2015 01:05:58 PM KST
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema AplosRSS
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `AplosRSS` ;

-- -----------------------------------------------------
-- Schema AplosRSS
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `AplosRSS` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `AplosRSS` ;

-- -----------------------------------------------------
-- Table `AplosRSS`.`RSS_Feed`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `AplosRSS`.`RSS_Feed` (
  `idRSS_Feed` INT NOT NULL,
  `Author` MEDIUMTEXT NULL,
  `Copyright` MEDIUMTEXT NULL,
  `Desc` MEDIUMTEXT NULL,
  `DescEx` MEDIUMTEXT NULL,
  `Type` VARCHAR(50) NULL,
  `ImgTitle` MEDIUMTEXT NULL,
  `ImgDesc` MEDIUMTEXT NULL,
  `ImgLink` MEDIUMTEXT NULL,
  `ImgURI` MEDIUMTEXT NULL,
  `Lang` VARCHAR(50) NULL,
  `PubDate` DATE NULL,
  `Title` MEDIUMTEXT NULL,
  `TitleEx` MEDIUMTEXT NULL,
  `URI` MEDIUMTEXT NULL,
  `Link` MEDIUMTEXT NULL,
  PRIMARY KEY (`idRSS_Feed`))
ENGINE = InnoDB;

CREATE UNIQUE INDEX `idRSS_Feed_UNIQUE` ON `AplosRSS`.`RSS_Feed` (`idRSS_Feed` ASC);


-- -----------------------------------------------------
-- Table `AplosRSS`.`Authors`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `AplosRSS`.`Authors` (
  `idAuthors` INT NOT NULL AUTO_INCREMENT,
  `Author` VARCHAR(255) NULL,
  PRIMARY KEY (`idAuthors`))
ENGINE = InnoDB;

CREATE UNIQUE INDEX `Author_UNIQUE` ON `AplosRSS`.`Authors` (`Author` ASC);

CREATE UNIQUE INDEX `idAuthors_UNIQUE` ON `AplosRSS`.`Authors` (`idAuthors` ASC);


-- -----------------------------------------------------
-- Table `AplosRSS`.`RSS_Feed_has_Authors`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `AplosRSS`.`RSS_Feed_has_Authors` (
  `RSS_Feed_idRSS_Feed` INT NOT NULL,
  `Authors_idAuthors` INT NOT NULL,
  PRIMARY KEY (`RSS_Feed_idRSS_Feed`, `Authors_idAuthors`),
  CONSTRAINT `fk_RSS_Feed_has_Authors_RSS_Feed`
    FOREIGN KEY (`RSS_Feed_idRSS_Feed`)
    REFERENCES `AplosRSS`.`RSS_Feed` (`idRSS_Feed`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_RSS_Feed_has_Authors_Authors1`
    FOREIGN KEY (`Authors_idAuthors`)
    REFERENCES `AplosRSS`.`Authors` (`idAuthors`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_RSS_Feed_has_Authors_Authors1_idx` ON `AplosRSS`.`RSS_Feed_has_Authors` (`Authors_idAuthors` ASC);


-- -----------------------------------------------------
-- Table `AplosRSS`.`Categories`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `AplosRSS`.`Categories` (
  `idCategories` INT NOT NULL AUTO_INCREMENT,
  `Category` VARCHAR(255) NULL,
  PRIMARY KEY (`idCategories`))
ENGINE = InnoDB;

CREATE UNIQUE INDEX `idCategories_UNIQUE` ON `AplosRSS`.`Categories` (`idCategories` ASC);

CREATE UNIQUE INDEX `Category_UNIQUE` ON `AplosRSS`.`Categories` (`Category` ASC);


-- -----------------------------------------------------
-- Table `AplosRSS`.`RSS_Feed_has_Categories`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `AplosRSS`.`RSS_Feed_has_Categories` (
  `RSS_Feed_idRSS_Feed` INT NOT NULL,
  `Categories_idCategories` INT NOT NULL,
  PRIMARY KEY (`RSS_Feed_idRSS_Feed`, `Categories_idCategories`),
  CONSTRAINT `fk_RSS_Feed_has_Categories_RSS_Feed1`
    FOREIGN KEY (`RSS_Feed_idRSS_Feed`)
    REFERENCES `AplosRSS`.`RSS_Feed` (`idRSS_Feed`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_RSS_Feed_has_Categories_Categories1`
    FOREIGN KEY (`Categories_idCategories`)
    REFERENCES `AplosRSS`.`Categories` (`idCategories`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_RSS_Feed_has_Categories_Categories1_idx` ON `AplosRSS`.`RSS_Feed_has_Categories` (`Categories_idCategories` ASC);

CREATE INDEX `fk_RSS_Feed_has_Categories_RSS_Feed1_idx` ON `AplosRSS`.`RSS_Feed_has_Categories` (`RSS_Feed_idRSS_Feed` ASC);


-- -----------------------------------------------------
-- Table `AplosRSS`.`Contributors`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `AplosRSS`.`Contributors` (
  `idContributors` INT NOT NULL AUTO_INCREMENT,
  `Contributor` VARCHAR(255) NULL,
  PRIMARY KEY (`idContributors`))
ENGINE = InnoDB;

CREATE UNIQUE INDEX `idContributors_UNIQUE` ON `AplosRSS`.`Contributors` (`idContributors` ASC);

CREATE UNIQUE INDEX `Contributor_UNIQUE` ON `AplosRSS`.`Contributors` (`Contributor` ASC);


-- -----------------------------------------------------
-- Table `AplosRSS`.`RSS_Feed_has_Contributors`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `AplosRSS`.`RSS_Feed_has_Contributors` (
  `RSS_Feed_idRSS_Feed` INT NOT NULL,
  `Contributors_idContributors` INT NOT NULL,
  PRIMARY KEY (`RSS_Feed_idRSS_Feed`, `Contributors_idContributors`),
  CONSTRAINT `fk_RSS_Feed_has_Contributors_RSS_Feed1`
    FOREIGN KEY (`RSS_Feed_idRSS_Feed`)
    REFERENCES `AplosRSS`.`RSS_Feed` (`idRSS_Feed`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_RSS_Feed_has_Contributors_Contributors1`
    FOREIGN KEY (`Contributors_idContributors`)
    REFERENCES `AplosRSS`.`Contributors` (`idContributors`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_RSS_Feed_has_Contributors_Contributors1_idx` ON `AplosRSS`.`RSS_Feed_has_Contributors` (`Contributors_idContributors` ASC);

CREATE INDEX `fk_RSS_Feed_has_Contributors_RSS_Feed1_idx` ON `AplosRSS`.`RSS_Feed_has_Contributors` (`RSS_Feed_idRSS_Feed` ASC);


-- -----------------------------------------------------
-- Table `AplosRSS`.`Links`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `AplosRSS`.`Links` (
  `idLinks` INT NOT NULL AUTO_INCREMENT,
  `Link` MEDIUMTEXT NULL,
  PRIMARY KEY (`idLinks`))
ENGINE = InnoDB;

CREATE UNIQUE INDEX `idLinks_UNIQUE` ON `AplosRSS`.`Links` (`idLinks` ASC);


-- -----------------------------------------------------
-- Table `AplosRSS`.`RSS_Feed_has_Links`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `AplosRSS`.`RSS_Feed_has_Links` (
  `RSS_Feed_idRSS_Feed` INT NOT NULL,
  `Links_idLinks` INT NOT NULL,
  PRIMARY KEY (`RSS_Feed_idRSS_Feed`, `Links_idLinks`),
  CONSTRAINT `fk_RSS_Feed_has_Links_RSS_Feed1`
    FOREIGN KEY (`RSS_Feed_idRSS_Feed`)
    REFERENCES `AplosRSS`.`RSS_Feed` (`idRSS_Feed`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_RSS_Feed_has_Links_Links1`
    FOREIGN KEY (`Links_idLinks`)
    REFERENCES `AplosRSS`.`Links` (`idLinks`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_RSS_Feed_has_Links_Links1_idx` ON `AplosRSS`.`RSS_Feed_has_Links` (`Links_idLinks` ASC);

CREATE INDEX `fk_RSS_Feed_has_Links_RSS_Feed1_idx` ON `AplosRSS`.`RSS_Feed_has_Links` (`RSS_Feed_idRSS_Feed` ASC);


-- -----------------------------------------------------
-- Table `AplosRSS`.`Entries`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `AplosRSS`.`Entries` (
  `idEntries` INT NOT NULL AUTO_INCREMENT,
  `Feed_Ent_Num` INT NOT NULL,
  `Title` MEDIUMTEXT NULL,
  `Author` MEDIUMTEXT NULL,
  `TitleEx` MEDIUMTEXT NULL,
  `Desc` MEDIUMTEXT NULL,
  `URI` MEDIUMTEXT NULL,
  `Link` MEDIUMTEXT NULL,
  `PubDate` DATE NULL,
  `UpdateDate` DATE NULL,
  PRIMARY KEY (`idEntries`))
ENGINE = InnoDB;

CREATE UNIQUE INDEX `idEntries_UNIQUE` ON `AplosRSS`.`Entries` (`idEntries` ASC);


-- -----------------------------------------------------
-- Table `AplosRSS`.`RSS_Feed_has_Entries`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `AplosRSS`.`RSS_Feed_has_Entries` (
  `RSS_Feed_idRSS_Feed` INT NOT NULL,
  `Entries_idEntries` INT NOT NULL,
  PRIMARY KEY (`RSS_Feed_idRSS_Feed`, `Entries_idEntries`),
  CONSTRAINT `fk_RSS_Feed_has_Entries_RSS_Feed1`
    FOREIGN KEY (`RSS_Feed_idRSS_Feed`)
    REFERENCES `AplosRSS`.`RSS_Feed` (`idRSS_Feed`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_RSS_Feed_has_Entries_Entries1`
    FOREIGN KEY (`Entries_idEntries`)
    REFERENCES `AplosRSS`.`Entries` (`idEntries`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_RSS_Feed_has_Entries_Entries1_idx` ON `AplosRSS`.`RSS_Feed_has_Entries` (`Entries_idEntries` ASC);

CREATE INDEX `fk_RSS_Feed_has_Entries_RSS_Feed1_idx` ON `AplosRSS`.`RSS_Feed_has_Entries` (`RSS_Feed_idRSS_Feed` ASC);


-- -----------------------------------------------------
-- Table `AplosRSS`.`Entries_has_Categories`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `AplosRSS`.`Entries_has_Categories` (
  `Entries_idEntries` INT NOT NULL,
  `Categories_idCategories` INT NOT NULL,
  PRIMARY KEY (`Entries_idEntries`, `Categories_idCategories`),
  CONSTRAINT `fk_Entries_has_Categories_Entries1`
    FOREIGN KEY (`Entries_idEntries`)
    REFERENCES `AplosRSS`.`Entries` (`idEntries`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Entries_has_Categories_Categories1`
    FOREIGN KEY (`Categories_idCategories`)
    REFERENCES `AplosRSS`.`Categories` (`idCategories`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_Entries_has_Categories_Categories1_idx` ON `AplosRSS`.`Entries_has_Categories` (`Categories_idCategories` ASC);

CREATE INDEX `fk_Entries_has_Categories_Entries1_idx` ON `AplosRSS`.`Entries_has_Categories` (`Entries_idEntries` ASC);


-- -----------------------------------------------------
-- Table `AplosRSS`.`Entries_has_Authors`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `AplosRSS`.`Entries_has_Authors` (
  `Entries_idEntries` INT NOT NULL,
  `Authors_idAuthors` INT NOT NULL,
  PRIMARY KEY (`Entries_idEntries`, `Authors_idAuthors`),
  CONSTRAINT `fk_Entries_has_Authors_Entries1`
    FOREIGN KEY (`Entries_idEntries`)
    REFERENCES `AplosRSS`.`Entries` (`idEntries`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Entries_has_Authors_Authors1`
    FOREIGN KEY (`Authors_idAuthors`)
    REFERENCES `AplosRSS`.`Authors` (`idAuthors`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_Entries_has_Authors_Authors1_idx` ON `AplosRSS`.`Entries_has_Authors` (`Authors_idAuthors` ASC);

CREATE INDEX `fk_Entries_has_Authors_Entries1_idx` ON `AplosRSS`.`Entries_has_Authors` (`Entries_idEntries` ASC);


-- -----------------------------------------------------
-- Table `AplosRSS`.`Entries_has_Contributors`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `AplosRSS`.`Entries_has_Contributors` (
  `Entries_idEntries` INT NOT NULL,
  `Contributors_idContributors` INT NOT NULL,
  PRIMARY KEY (`Entries_idEntries`, `Contributors_idContributors`),
  CONSTRAINT `fk_Entries_has_Contributors_Entries1`
    FOREIGN KEY (`Entries_idEntries`)
    REFERENCES `AplosRSS`.`Entries` (`idEntries`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Entries_has_Contributors_Contributors1`
    FOREIGN KEY (`Contributors_idContributors`)
    REFERENCES `AplosRSS`.`Contributors` (`idContributors`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_Entries_has_Contributors_Contributors1_idx` ON `AplosRSS`.`Entries_has_Contributors` (`Contributors_idContributors` ASC);

CREATE INDEX `fk_Entries_has_Contributors_Entries1_idx` ON `AplosRSS`.`Entries_has_Contributors` (`Entries_idEntries` ASC);


-- -----------------------------------------------------
-- Table `AplosRSS`.`Entries_has_Links`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `AplosRSS`.`Entries_has_Links` (
  `Entries_idEntries` INT NOT NULL,
  `Links_idLinks` INT NOT NULL,
  PRIMARY KEY (`Entries_idEntries`, `Links_idLinks`),
  CONSTRAINT `fk_Entries_has_Links_Entries1`
    FOREIGN KEY (`Entries_idEntries`)
    REFERENCES `AplosRSS`.`Entries` (`idEntries`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Entries_has_Links_Links1`
    FOREIGN KEY (`Links_idLinks`)
    REFERENCES `AplosRSS`.`Links` (`idLinks`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_Entries_has_Links_Links1_idx` ON `AplosRSS`.`Entries_has_Links` (`Links_idLinks` ASC);

CREATE INDEX `fk_Entries_has_Links_Entries1_idx` ON `AplosRSS`.`Entries_has_Links` (`Entries_idEntries` ASC);


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
