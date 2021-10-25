-- phpMyAdmin SQL Dump
-- version 4.9.0.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- 생성 시간: 21-10-25 09:44
-- 서버 버전: 10.3.16-MariaDB
-- PHP 버전: 7.3.7

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- 데이터베이스: `yscoin`
--

-- --------------------------------------------------------

--
-- 테이블 구조 `coin_type`
--

CREATE TABLE `coin_type` (
  `id` varchar(50) NOT NULL,
  `last_price` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 테이블의 덤프 데이터 `coin_type`
--

INSERT INTO `coin_type` (`id`, `last_price`) VALUES
('양디코인', 735);

-- --------------------------------------------------------

--
-- 테이블 구조 `hash`
--

CREATE TABLE `hash` (
  `hash` varchar(32) NOT NULL,
  `user_id` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- 테이블 구조 `history_minute`
--

CREATE TABLE `history_minute` (
  `coin_id` varchar(50) NOT NULL,
  `start` int(11) NOT NULL,
  `close_or_mp` int(11) NOT NULL,
  `high` int(11) NOT NULL,
  `low` int(11) NOT NULL,
  `time` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 테이블의 덤프 데이터 `history_minute`
--

INSERT INTO `history_minute` (`coin_id`, `start`, `close_or_mp`, `high`, `low`, `time`) VALUES
('양디코인', 836, 559, 989, 180, '2021-10-25 16:42:00'),
('양디코인', 656, 349, 996, 109, '2021-10-25 16:43:00'),
('양디코인', 184, 735, 994, 103, '2021-10-25 16:44:00');

-- --------------------------------------------------------

--
-- 테이블 구조 `history_second`
--

CREATE TABLE `history_second` (
  `coin_id` varchar(50) NOT NULL,
  `start` int(11) NOT NULL,
  `close_or_mp` int(11) NOT NULL,
  `high` int(11) NOT NULL,
  `low` int(11) NOT NULL,
  `time` time NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 테이블의 덤프 데이터 `history_second`
--

INSERT INTO `history_second` (`coin_id`, `start`, `close_or_mp`, `high`, `low`, `time`) VALUES
('양디코인', 100, 200, 300, 50, '00:14:20');

-- --------------------------------------------------------

--
-- 테이블 구조 `order_info`
--

CREATE TABLE `order_info` (
  `user_id` varchar(20) NOT NULL,
  `count` int(11) NOT NULL,
  `order_time` timestamp NOT NULL DEFAULT current_timestamp(),
  `id` int(11) NOT NULL,
  `order_type` enum('구매','판매') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- 테이블 구조 `users`
--

CREATE TABLE `users` (
  `id` varchar(20) NOT NULL,
  `pw` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 덤프된 테이블의 인덱스
--

--
-- 테이블의 인덱스 `coin_type`
--
ALTER TABLE `coin_type`
  ADD PRIMARY KEY (`id`);

--
-- 테이블의 인덱스 `hash`
--
ALTER TABLE `hash`
  ADD PRIMARY KEY (`hash`);

--
-- 테이블의 인덱스 `order_info`
--
ALTER TABLE `order_info`
  ADD PRIMARY KEY (`id`);

--
-- 테이블의 인덱스 `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- 덤프된 테이블의 AUTO_INCREMENT
--

--
-- 테이블의 AUTO_INCREMENT `order_info`
--
ALTER TABLE `order_info`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
