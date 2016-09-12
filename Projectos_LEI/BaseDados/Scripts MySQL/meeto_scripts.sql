-- phpMyAdmin SQL Dump
-- version 4.2.7.1
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: 15-Dez-2014 às 20:02
-- Versão do servidor: 5.6.20
-- PHP Version: 5.5.15

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `meeto`
--

DELIMITER $$
--
-- Procedures
--
CREATE DEFINER=`idiota`@`%` PROCEDURE `add_action_item`(IN `i_desc` VARCHAR(256), IN `id_reuniao` INT, IN `user_id` INT)
    NO SQL
BEGIN

DECLARE 
newid INT;

INSERT into action_item(name,is_done) VALUES (i_desc,0);

SET newid = LAST_INSERT_ID();

INSERT into action_item_user(id_action_item,id_meeting,id_user) VALUES (newid,id_reuniao,user_id);

END$$

--
-- Functions
--
CREATE DEFINER=`idiota`@`%` FUNCTION `add_item`(`id_reuniao` INT, `item_desc` VARCHAR(256), `user_id` INT) RETURNS int(11)
    NO SQL
BEGIN

DECLARE 
result INT;

IF (exists(SELECT * FROM meeting_user m WHERE m.id_meeting = id_reuniao AND m.id_user = user_id AND accepted = 1)) THEN
	
	INSERT into item(name) VALUES (item_desc);
	SET result = LAST_INSERT_ID();

	INSERT into meeting_item(id_meeting,id_item) VALUES (id_reuniao,result);
ELSE
	SET result = -1;
end if;

return result;

END$$

CREATE DEFINER=`idiota`@`%` FUNCTION `add_meeting`(`m_title` VARCHAR(256), `m_outcome` VARCHAR(256), `m_date` TIMESTAMP, `m_duration` VARCHAR(256), `m_location` VARCHAR(256), `m_leader` INT) RETURNS int(11)
    NO SQL
    DETERMINISTIC
BEGIN

DECLARE 
newid INT;

INSERT into meeting(title,outcome,date,duration,location,leader_id,finished) VALUES (m_title,m_outcome,m_date,m_duration,m_location,m_leader,0);

SET newid = LAST_INSERT_ID();

INSERT into meeting_user(id_meeting,id_user,accepted) VALUES (newid,m_leader,1);

return newid;

END$$

CREATE DEFINER=`idiota`@`%` FUNCTION `create_account`(`u_username` VARCHAR(256), `u_password` VARCHAR(256)) RETURNS int(11)
    NO SQL
BEGIN

DECLARE 
result INT;

IF (exists (SELECT * from user where username=u_username FOR UPDATE)) THEN
	set result = -1;
ELSE
	INSERT into user(username,password) VALUES (u_username,u_password);
	SET result = LAST_INSERT_ID();
END IF;

return result;

END$$

CREATE DEFINER=`idiota`@`%` FUNCTION `delete_item`(`id_reuniao` INT, `a_id_item` INT, `user_id` INT) RETURNS int(11)
    NO SQL
BEGIN

DECLARE 
result INT;

IF (exists(SELECT * FROM meeting_user m WHERE m.id_meeting = id_reuniao AND m.id_user = user_id AND accepted = 1) AND exists(SELECT * FROM meeting_item m WHERE m.id_meeting = id_reuniao AND m.id_item = a_id_item)) THEN
	
	DELETE FROM item WHERE id = a_id_item;
    DELETE FROM meeting_item WHERE id_item = a_id_item ;
    SET result = 1;
ELSE
	SET result = -1;
end if;

return result;

END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Estrutura da tabela `action_item`
--

CREATE TABLE IF NOT EXISTS `action_item` (
`id` int(11) NOT NULL,
  `name` varchar(256) NOT NULL,
  `is_done` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estrutura da tabela `action_item_user`
--

CREATE TABLE IF NOT EXISTS `action_item_user` (
  `id_action_item` int(11) NOT NULL,
  `id_meeting` int(11) NOT NULL,
  `id_user` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estrutura da tabela `item`
--

CREATE TABLE IF NOT EXISTS `item` (
`id` int(11) NOT NULL,
  `name` varchar(256) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estrutura da tabela `key_decisions`
--

CREATE TABLE IF NOT EXISTS `key_decisions` (
`id` int(11) NOT NULL,
  `id_item` int(11) NOT NULL,
  `decision` varchar(256) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estrutura da tabela `meeting`
--

CREATE TABLE IF NOT EXISTS `meeting` (
`id` int(11) NOT NULL,
  `title` varchar(256) NOT NULL,
  `outcome` varchar(256) NOT NULL,
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `duration` varchar(256) NOT NULL,
  `location` varchar(256) NOT NULL,
  `leader_id` int(11) NOT NULL,
  `finished` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Acionadores `meeting`
--
DELIMITER //
CREATE TRIGGER `add_default_meeting_item` AFTER INSERT ON `meeting`
 FOR EACH ROW INSERT INTO meeting_item (id_meeting,id_item) VALUES (NEW.id,(SELECT id FROM item WHERE name = 'Any other business'))
//
DELIMITER ;
DELIMITER //
CREATE TRIGGER `insert_first_item` BEFORE INSERT ON `meeting`
 FOR EACH ROW IF ((SELECT COUNT(*) from item) = 0 AND (SELECT COUNT(*) from meeting) = 0) THEN
		INSERT INTO item (name) VALUES ("Any other business");
END IF
//
DELIMITER ;

-- --------------------------------------------------------

--
-- Estrutura da tabela `meeting_item`
--

CREATE TABLE IF NOT EXISTS `meeting_item` (
  `id_meeting` int(11) NOT NULL,
  `id_item` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estrutura da tabela `meeting_user`
--

CREATE TABLE IF NOT EXISTS `meeting_user` (
  `id_meeting` int(11) NOT NULL,
  `id_user` int(11) NOT NULL,
  `accepted` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estrutura da tabela `user`
--

CREATE TABLE IF NOT EXISTS `user` (
`id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(256) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `action_item`
--
ALTER TABLE `action_item`
 ADD PRIMARY KEY (`id`);

--
-- Indexes for table `action_item_user`
--
ALTER TABLE `action_item_user`
 ADD PRIMARY KEY (`id_action_item`,`id_meeting`,`id_user`);

--
-- Indexes for table `item`
--
ALTER TABLE `item`
 ADD PRIMARY KEY (`id`);

--
-- Indexes for table `key_decisions`
--
ALTER TABLE `key_decisions`
 ADD PRIMARY KEY (`id`,`id_item`);

--
-- Indexes for table `meeting`
--
ALTER TABLE `meeting`
 ADD PRIMARY KEY (`id`);

--
-- Indexes for table `meeting_item`
--
ALTER TABLE `meeting_item`
 ADD PRIMARY KEY (`id_meeting`,`id_item`);

--
-- Indexes for table `meeting_user`
--
ALTER TABLE `meeting_user`
 ADD PRIMARY KEY (`id_meeting`,`id_user`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
 ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `action_item`
--
ALTER TABLE `action_item`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `item`
--
ALTER TABLE `item`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `key_decisions`
--
ALTER TABLE `key_decisions`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `meeting`
--
ALTER TABLE `meeting`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
