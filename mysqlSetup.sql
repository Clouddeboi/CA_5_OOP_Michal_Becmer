-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Apr 20, 2024 at 04:22 PM
-- Server version: 10.4.27-MariaDB
-- PHP Version: 8.2.0

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `dark_souls_ca5_oop`
--

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
                         `ID` int(11) NOT NULL,
                         `username` char(40) DEFAULT NULL,
                         `password` char(40) DEFAULT NULL,
                         `displayName` char(40) DEFAULT NULL,
                         `isAdmin` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`ID`, `username`, `password`, `displayName`, `isAdmin`) VALUES
                                                                                 (1, 'Michal', 'Michal123', 'Michal', 1),
                                                                                 (2, 'Stephen', 'Stephen123', 'Stephano', 1),
                                                                                 (3, 'Jeff', 'Jeff123', 'Jeff', 1),
                                                                                 (4, 'User1', 'User123', 'User', 0);

-- --------------------------------------------------------

--
-- Table structure for table `weapons`
--

CREATE TABLE `weapons` (
                           `ID` int(11) NOT NULL,
                           `Name` varchar(255) NOT NULL,
                           `Attack` int(11) DEFAULT NULL,
                           `Weight` float DEFAULT NULL,
                           `Location` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `weapons`
--

INSERT INTO `weapons` (`ID`, `Name`, `Attack`, `Weight`, `Location`) VALUES
                                                                         (1, 'Club', 87, 3, 'Anor Londo'),
                                                                         (2, 'Broadsword', 205, 3, 'Purchased'),
                                                                         (3, 'Silver Knight Straight Sword', 262, 6, 'Anor Londo'),
                                                                         (4, 'Greatsword of Artorias', 180, 10, 'Darkroot Garden'),
                                                                         (5, 'Chaos Blade', 216, 6, 'Soul of Quelaag'),
                                                                         (6, 'Ricard\'s Rapier', 175, 2, 'Sen\'s Fortress'),
                                                                         (7, 'Hammer of Vamos', 115, 5, 'The Catacombs'),
                                                                         (8, 'Demons Great Hammer', 138, 22, 'Traded for sack of Snuggly the Crow'),
                                                                         (9, 'Grant', 130, 24, 'Tomb of the Giants'),
                                                                         (10, 'Great Club', 135, 12, 'Blighttwon'),
                                                                         (11, 'Smoughs Hammer', 300, 28, 'Created'),
                                                                         (12, 'Battle Axe', 95, 4, 'Undead Burg'),
                                                                         (13, 'Golem Axe', 170, 16, 'Anor Londo'),
                                                                         (14, 'Black Knight Greataxe', 229, 16, 'The Catacombs'),
                                                                         (15, 'Dragon King Greataxe', 380, 24, 'Reward'),
                                                                         (16, 'Priscillas Dagger', 80, 1, 'Reward'),
                                                                         (17, 'Ghost Blade', 110, 0.5, 'New Londo Ruins'),
                                                                         (18, 'Estoc', 75, 3, 'New Londo Ruins'),
                                                                         (19, 'Rapier', 73, 1.5, 'Purchased'),
                                                                         (20, 'Dark Sword', 82, 6, 'Earned'),
                                                                         (21, 'Drake Swrod', 200, 6, 'Purchased'),
                                                                         (22, 'Claymore', 103, 6, 'Undead Burg'),
                                                                         (23, 'Flamberge', 100, 6, 'Sens Fortress'),
                                                                         (24, 'Stone Greatsword', 148, 18, 'Purchased'),
                                                                         (25, 'Greatsword', 130, 12, 'Purchased'),
                                                                         (26, 'Black Knight Greatsword', 220, 14, 'Undead Parish watchtower '),
                                                                         (27, 'Iaito Sword', 88, 5, 'Blighttown '),
                                                                         (28, 'Washing Pole', 90, 8, ' Purchased'),
                                                                         (29, 'Falchion', 82, 2.5, 'Blighttown '),
                                                                         (30, 'Server', 107, 10, 'Blighttown ');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `users`
--
ALTER TABLE `users`
    ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `weapons`
--
ALTER TABLE `weapons`
    ADD PRIMARY KEY (`ID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
    MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `weapons`
--
ALTER TABLE `weapons`
    MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=36;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;