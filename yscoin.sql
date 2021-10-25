-- phpMyAdmin SQL Dump
-- version 4.9.0.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- 생성 시간: 21-10-25 15:56
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
('양디코인', 287);

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
-- 테이블 구조 `history_date`
--

CREATE TABLE `history_date` (
  `coin_id` varchar(50) NOT NULL,
  `start` int(11) NOT NULL,
  `close_or_mp` int(11) NOT NULL,
  `high` int(11) NOT NULL,
  `low` int(11) NOT NULL,
  `time` int(11) NOT NULL
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
  `time` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 테이블의 덤프 데이터 `history_minute`
--

INSERT INTO `history_minute` (`coin_id`, `start`, `close_or_mp`, `high`, `low`, `time`) VALUES
('양디코인', 107, 514, 998, 107, '2021-10-24 16:17:00'),
('양디코인', 625, 430, 993, 106, '2021-10-24 16:18:00'),
('양디코인', 220, 798, 984, 102, '2021-10-24 16:19:00'),
('양디코인', 863, 129, 985, 117, '2021-10-24 16:20:00'),
('양디코인', 234, 926, 986, 126, '2021-10-24 16:21:00'),
('양디코인', 516, 497, 999, 109, '2021-10-24 16:22:00'),
('양디코인', 288, 282, 986, 102, '2021-10-24 16:23:00'),
('양디코인', 717, 458, 984, 108, '2021-10-24 16:24:00'),
('양디코인', 367, 608, 906, 367, '2021-10-24 16:25:00'),
('양디코인', 882, 788, 998, 117, '2021-10-24 20:59:00'),
('양디코인', 725, 413, 990, 103, '2021-10-24 21:00:00'),
('양디코인', 978, 692, 978, 169, '2021-10-24 21:01:00'),
('양디코인', 345, 917, 977, 340, '2021-10-24 21:04:00'),
('양디코인', 172, 585, 990, 101, '2021-10-24 21:05:00'),
('양디코인', 910, 608, 972, 108, '2021-10-24 21:06:00'),
('양디코인', 282, 734, 997, 111, '2021-10-24 21:07:00'),
('양디코인', 298, 311, 996, 106, '2021-10-24 21:08:00'),
('양디코인', 135, 289, 999, 135, '2021-10-24 21:09:00'),
('양디코인', 208, 929, 991, 101, '2021-10-24 21:10:00'),
('양디코인', 788, 997, 997, 112, '2021-10-24 21:11:00'),
('양디코인', 307, 837, 997, 106, '2021-10-24 21:17:00'),
('양디코인', 620, 428, 932, 104, '2021-10-24 21:18:00'),
('양디코인', 338, 653, 951, 103, '2021-10-24 21:19:00'),
('양디코인', 535, 617, 998, 117, '2021-10-24 21:20:00'),
('양디코인', 831, 530, 995, 149, '2021-10-24 21:21:00'),
('양디코인', 853, 631, 980, 142, '2021-10-24 21:35:00'),
('양디코인', 177, 348, 985, 104, '2021-10-24 21:36:00'),
('양디코인', 746, 250, 993, 106, '2021-10-24 21:37:00'),
('양디코인', 630, 950, 979, 108, '2021-10-24 21:38:00'),
('양디코인', 283, 287, 801, 283, '2021-10-24 21:39:00');

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
-- 테이블의 덤프 데이터 `users`
--

INSERT INTO `users` (`id`, `pw`) VALUES
('-909', '999'),
('123', '123'),
('123121', '123123'),
('123123213', '123123'),
('1234', '1234'),
('213213', '123213'),
('23423', '234'),
('234234', '234234'),
('23432435', '35325325'),
('234324353', '35325325'),
('3535', '35354'),
('3673546', '34234'),
('4534', '3443'),
('56756', '567'),
('655', '8787'),
('6575', '567'),
('657568', '56856'),
('8798', '789987'),
('bngfmgkm', ',jhyy'),
('ff', 'g'),
('hgfh', 'q'),
('sdfbkmi;', '12ed'),
('sdfsdf', 'sdfsdf');

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
