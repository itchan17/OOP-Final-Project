-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jul 18, 2024 at 04:49 AM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `pos_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `admin_tb`
--

CREATE TABLE `admin_tb` (
  `admin_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `admin_tb`
--

INSERT INTO `admin_tb` (`admin_id`, `user_id`) VALUES
(115, 1016);

-- --------------------------------------------------------

--
-- Table structure for table `cashier_tb`
--

CREATE TABLE `cashier_tb` (
  `cashier_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `cashier_tb`
--

INSERT INTO `cashier_tb` (`cashier_id`, `user_id`) VALUES
(108, 1015);

-- --------------------------------------------------------

--
-- Table structure for table `daily_sales_tb`
--

CREATE TABLE `daily_sales_tb` (
  `daily_sale_id` int(11) NOT NULL,
  `daily_sale_date` date NOT NULL,
  `total_daily_discountAmount` int(11) DEFAULT NULL,
  `total_daily_sale` decimal(11,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `daily_sale_details_tb`
--

CREATE TABLE `daily_sale_details_tb` (
  `daily_sale_details_id` int(11) NOT NULL,
  `daily_sale_id` int(11) NOT NULL,
  `item_details_id` int(11) NOT NULL,
  `item_total_quantity` int(11) DEFAULT NULL,
  `item_total_sales` decimal(11,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `items_tb`
--

CREATE TABLE `items_tb` (
  `item_id` int(11) NOT NULL,
  `item_name` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `items_tb`
--

INSERT INTO `items_tb` (`item_id`, `item_name`) VALUES
(100, 'Lomi'),
(102, 'Pancit Bihon'),
(103, 'Sotanghon'),
(104, 'Miki Guisado'),
(105, 'Chami'),
(106, 'Pancit Canton'),
(107, 'Palabok'),
(108, 'Lugaw'),
(109, 'Coke'),
(110, 'Sprite'),
(111, 'Iced Tea'),
(112, 'Iced Coffee'),
(113, 'Lemonade'),
(114, 'Mountain Dew'),
(115, 'Orange Juice'),
(116, 'Milk Tea');

-- --------------------------------------------------------

--
-- Table structure for table `item_details_tb`
--

CREATE TABLE `item_details_tb` (
  `item_details_id` int(11) NOT NULL,
  `item_id` int(11) NOT NULL,
  `size_id` int(11) NOT NULL,
  `item_price` decimal(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `item_details_tb`
--

INSERT INTO `item_details_tb` (`item_details_id`, `item_id`, `size_id`, `item_price`) VALUES
(200, 100, 100, 75.00),
(201, 100, 101, 125.00),
(202, 100, 102, 150.00),
(203, 102, 100, 75.00),
(204, 102, 101, 125.00),
(205, 102, 102, 150.00),
(206, 103, 100, 75.00),
(207, 103, 101, 125.00),
(208, 103, 102, 150.00),
(209, 104, 100, 75.00),
(210, 104, 101, 125.00),
(211, 104, 102, 150.00),
(212, 105, 100, 75.00),
(213, 105, 101, 125.00),
(214, 105, 102, 150.00),
(215, 106, 100, 75.00),
(216, 106, 101, 125.00),
(217, 106, 102, 150.00),
(218, 107, 100, 80.00),
(219, 107, 101, 130.00),
(220, 107, 102, 155.00),
(221, 108, 100, 70.00),
(222, 108, 101, 110.00),
(223, 108, 102, 125.00),
(224, 109, 103, 20.00),
(225, 109, 104, 35.00),
(226, 109, 105, 45.00),
(227, 110, 103, 20.00),
(228, 110, 104, 35.00),
(229, 110, 105, 45.00),
(230, 111, 103, 20.00),
(231, 111, 104, 35.00),
(232, 111, 105, 45.00),
(233, 112, 103, 35.00),
(234, 112, 104, 50.00),
(235, 112, 105, 60.00),
(236, 113, 103, 25.00),
(237, 113, 104, 40.00),
(238, 113, 105, 50.00),
(239, 114, 103, 20.00),
(240, 114, 104, 35.00),
(241, 114, 105, 45.00),
(242, 115, 103, 20.00),
(243, 115, 104, 35.00),
(244, 115, 105, 45.00),
(245, 116, 103, 35.00),
(246, 116, 104, 50.00),
(247, 116, 105, 60.00);

-- --------------------------------------------------------

--
-- Table structure for table `orders_tb`
--

CREATE TABLE `orders_tb` (
  `order_id` int(255) NOT NULL,
  `cashier_id` int(11) DEFAULT NULL,
  `admin_id` int(11) DEFAULT NULL,
  `order_total` decimal(11,2) NOT NULL,
  `sub_total` int(11) NOT NULL,
  `vat` decimal(50,2) NOT NULL,
  `order_cash` int(11) NOT NULL,
  `order_change` int(11) NOT NULL,
  `customer_name` varchar(50) DEFAULT NULL,
  `customer_address` varchar(50) DEFAULT NULL,
  `order_discountId` varchar(100) DEFAULT NULL,
  `order_discount_amount` decimal(50,2) DEFAULT NULL,
  `order_date` date NOT NULL,
  `order_time` varchar(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `orders_tb_archive`
--

CREATE TABLE `orders_tb_archive` (
  `orders_tb_archive_id` int(11) NOT NULL,
  `archived_date` datetime NOT NULL DEFAULT current_timestamp(),
  `order_id` int(11) NOT NULL,
  `cashier_id` int(11) DEFAULT NULL,
  `admin_id` int(11) DEFAULT NULL,
  `order_total` int(11) NOT NULL,
  `sub_total` int(11) NOT NULL,
  `vat` decimal(11,2) NOT NULL,
  `order_cash` decimal(10,0) NOT NULL,
  `order_change` decimal(10,0) NOT NULL,
  `customer_name` text DEFAULT NULL,
  `customer_address` text DEFAULT NULL,
  `order_discountId` varchar(50) DEFAULT NULL,
  `order_discount_amount` int(11) DEFAULT NULL,
  `order_date` date NOT NULL,
  `order_time` time NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `order_details_tb`
--

CREATE TABLE `order_details_tb` (
  `order_detail_id` int(11) NOT NULL,
  `order_id` int(11) NOT NULL,
  `item_details_id` int(11) NOT NULL,
  `item_quantity` int(11) NOT NULL,
  `each_total_price` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `order_details_tb_archive`
--

CREATE TABLE `order_details_tb_archive` (
  `order_details_archive_id` int(11) NOT NULL,
  `order_details_id` int(11) NOT NULL,
  `order_id` int(11) NOT NULL,
  `item_details_id` int(11) NOT NULL,
  `item_quantity` int(11) NOT NULL,
  `each_total_price` decimal(10,0) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `size_tb`
--

CREATE TABLE `size_tb` (
  `size_id` int(11) NOT NULL,
  `size_name` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `size_tb`
--

INSERT INTO `size_tb` (`size_id`, `size_name`) VALUES
(100, 'Regular'),
(101, 'Special'),
(102, 'Jumbo'),
(103, 'Small'),
(104, 'Medium'),
(105, 'Large');

-- --------------------------------------------------------

--
-- Table structure for table `users_tb`
--

CREATE TABLE `users_tb` (
  `user_id` int(11) NOT NULL,
  `firstName` varchar(50) NOT NULL,
  `lastName` varchar(50) NOT NULL,
  `password` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users_tb`
--

INSERT INTO `users_tb` (`user_id`, `firstName`, `lastName`, `password`) VALUES
(1015, 'Cashier', 'Cashier', '202cb962ac59075b964b07152d234b70'),
(1016, 'Admin', 'Admin', '202cb962ac59075b964b07152d234b70');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `admin_tb`
--
ALTER TABLE `admin_tb`
  ADD PRIMARY KEY (`admin_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `cashier_tb`
--
ALTER TABLE `cashier_tb`
  ADD PRIMARY KEY (`cashier_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `daily_sales_tb`
--
ALTER TABLE `daily_sales_tb`
  ADD PRIMARY KEY (`daily_sale_id`);

--
-- Indexes for table `daily_sale_details_tb`
--
ALTER TABLE `daily_sale_details_tb`
  ADD PRIMARY KEY (`daily_sale_details_id`),
  ADD KEY `daily_sale_id` (`daily_sale_id`,`item_details_id`),
  ADD KEY `item_details_id` (`item_details_id`);

--
-- Indexes for table `items_tb`
--
ALTER TABLE `items_tb`
  ADD PRIMARY KEY (`item_id`);

--
-- Indexes for table `item_details_tb`
--
ALTER TABLE `item_details_tb`
  ADD PRIMARY KEY (`item_details_id`),
  ADD KEY `item_id` (`item_id`,`size_id`),
  ADD KEY `size_id` (`size_id`);

--
-- Indexes for table `orders_tb`
--
ALTER TABLE `orders_tb`
  ADD PRIMARY KEY (`order_id`),
  ADD KEY `user_id` (`cashier_id`),
  ADD KEY `admin_id` (`admin_id`);

--
-- Indexes for table `orders_tb_archive`
--
ALTER TABLE `orders_tb_archive`
  ADD PRIMARY KEY (`orders_tb_archive_id`),
  ADD UNIQUE KEY `unique_order_id` (`order_id`);

--
-- Indexes for table `order_details_tb`
--
ALTER TABLE `order_details_tb`
  ADD PRIMARY KEY (`order_detail_id`),
  ADD KEY `transaction_id` (`order_id`),
  ADD KEY `order_id` (`order_id`),
  ADD KEY `item_size_id` (`item_details_id`),
  ADD KEY `item_details_id` (`item_details_id`);

--
-- Indexes for table `order_details_tb_archive`
--
ALTER TABLE `order_details_tb_archive`
  ADD PRIMARY KEY (`order_details_archive_id`),
  ADD KEY `order_id` (`order_id`),
  ADD KEY `item_details_id` (`item_details_id`);

--
-- Indexes for table `size_tb`
--
ALTER TABLE `size_tb`
  ADD PRIMARY KEY (`size_id`);

--
-- Indexes for table `users_tb`
--
ALTER TABLE `users_tb`
  ADD PRIMARY KEY (`user_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `admin_tb`
--
ALTER TABLE `admin_tb`
  MODIFY `admin_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=116;

--
-- AUTO_INCREMENT for table `cashier_tb`
--
ALTER TABLE `cashier_tb`
  MODIFY `cashier_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=109;

--
-- AUTO_INCREMENT for table `daily_sales_tb`
--
ALTER TABLE `daily_sales_tb`
  MODIFY `daily_sale_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1032;

--
-- AUTO_INCREMENT for table `daily_sale_details_tb`
--
ALTER TABLE `daily_sale_details_tb`
  MODIFY `daily_sale_details_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=272;

--
-- AUTO_INCREMENT for table `items_tb`
--
ALTER TABLE `items_tb`
  MODIFY `item_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=117;

--
-- AUTO_INCREMENT for table `item_details_tb`
--
ALTER TABLE `item_details_tb`
  MODIFY `item_details_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=248;

--
-- AUTO_INCREMENT for table `orders_tb`
--
ALTER TABLE `orders_tb`
  MODIFY `order_id` int(255) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=100264;

--
-- AUTO_INCREMENT for table `orders_tb_archive`
--
ALTER TABLE `orders_tb_archive`
  MODIFY `orders_tb_archive_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1075;

--
-- AUTO_INCREMENT for table `order_details_tb`
--
ALTER TABLE `order_details_tb`
  MODIFY `order_detail_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=788;

--
-- AUTO_INCREMENT for table `order_details_tb_archive`
--
ALTER TABLE `order_details_tb_archive`
  MODIFY `order_details_archive_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=490;

--
-- AUTO_INCREMENT for table `size_tb`
--
ALTER TABLE `size_tb`
  MODIFY `size_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=106;

--
-- AUTO_INCREMENT for table `users_tb`
--
ALTER TABLE `users_tb`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1017;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `admin_tb`
--
ALTER TABLE `admin_tb`
  ADD CONSTRAINT `admin_tb_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users_tb` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `cashier_tb`
--
ALTER TABLE `cashier_tb`
  ADD CONSTRAINT `cashier_tb_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users_tb` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `daily_sale_details_tb`
--
ALTER TABLE `daily_sale_details_tb`
  ADD CONSTRAINT `daily_sale_details_tb_ibfk_1` FOREIGN KEY (`item_details_id`) REFERENCES `item_details_tb` (`item_details_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `daily_sale_details_tb_ibfk_2` FOREIGN KEY (`daily_sale_id`) REFERENCES `daily_sales_tb` (`daily_sale_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `item_details_tb`
--
ALTER TABLE `item_details_tb`
  ADD CONSTRAINT `item_details_tb_ibfk_1` FOREIGN KEY (`item_id`) REFERENCES `items_tb` (`item_id`),
  ADD CONSTRAINT `item_details_tb_ibfk_2` FOREIGN KEY (`size_id`) REFERENCES `size_tb` (`size_id`);

--
-- Constraints for table `orders_tb`
--
ALTER TABLE `orders_tb`
  ADD CONSTRAINT `orders_tb_ibfk_2` FOREIGN KEY (`admin_id`) REFERENCES `admin_tb` (`admin_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `orders_tb_ibfk_3` FOREIGN KEY (`cashier_id`) REFERENCES `cashier_tb` (`cashier_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `order_details_tb`
--
ALTER TABLE `order_details_tb`
  ADD CONSTRAINT `order_details_tb_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders_tb` (`order_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `order_details_tb_ibfk_2` FOREIGN KEY (`item_details_id`) REFERENCES `item_details_tb` (`item_details_id`);

--
-- Constraints for table `order_details_tb_archive`
--
ALTER TABLE `order_details_tb_archive`
  ADD CONSTRAINT `order_details_tb_archive_ibfk_2` FOREIGN KEY (`item_details_id`) REFERENCES `item_details_tb` (`item_details_id`),
  ADD CONSTRAINT `order_details_tb_archive_ibfk_3` FOREIGN KEY (`order_id`) REFERENCES `orders_tb_archive` (`order_id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
