-- phpMyAdmin SQL Dump
-- version 5.0.3
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jul 06, 2021 at 01:34 PM
-- Server version: 10.4.14-MariaDB
-- PHP Version: 7.4.11

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `penjualan_makanan`
--

DELIMITER $$
--
-- Procedures
--
CREATE DEFINER=`root`@`localhost` PROCEDURE `total_harga_transaksi` ()  BEGIN
SELECT 
SUM(keranjang.jumlah_barang*keranjang.harga) AS total_harga
FROM keranjang;
END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `admin`
--

CREATE TABLE `admin` (
  `id_admin` int(4) NOT NULL,
  `username` varchar(25) NOT NULL,
  `nama` varchar(15) NOT NULL,
  `password` varchar(25) NOT NULL,
  `telepon` varchar(15) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `admin`
--

INSERT INTO `admin` (`id_admin`, `username`, `nama`, `password`, `telepon`) VALUES
(1, 'AdminJy', 'Md_jaya', 'qwerty123', '089128972389'),
(15, 'Admin', 'Admin', 'qwerty', '089182913');

-- --------------------------------------------------------

--
-- Table structure for table `barang`
--

CREATE TABLE `barang` (
  `id_barang` varchar(7) NOT NULL,
  `nama_barang` varchar(25) NOT NULL,
  `merek` varchar(15) NOT NULL,
  `jenis` varchar(15) NOT NULL,
  `harga` int(15) NOT NULL,
  `stok` int(15) NOT NULL,
  `tanggal_k` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `barang`
--

INSERT INTO `barang` (`id_barang`, `nama_barang`, `merek`, `jenis`, `harga`, `stok`, `tanggal_k`) VALUES
('OR-001', 'Oreo', 'Oreo', 'Biskut', 15000, 12, '2021-10-25');

-- --------------------------------------------------------

--
-- Table structure for table `keranjang`
--

CREATE TABLE `keranjang` (
  `no` int(11) NOT NULL,
  `id_transaksi` varchar(8) NOT NULL,
  `id_barang` varchar(8) NOT NULL,
  `nama_barang` varchar(20) NOT NULL,
  `harga` int(15) NOT NULL,
  `jumlah_barang` int(5) NOT NULL,
  `Total_harga` int(15) NOT NULL,
  `tggl` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Triggers `keranjang`
--
DELIMITER $$
CREATE TRIGGER `cancel` AFTER DELETE ON `keranjang` FOR EACH ROW BEGIN
UPDATE barang SET
stok = stok + OLD.jumlah_barang
WHERE id_barang = OLD.id_barang;
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `cancel_2` AFTER DELETE ON `keranjang` FOR EACH ROW DELETE FROM transaksi WHERE id_transaksi = OLD.id_transaksi
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `pembeli`
--

CREATE TABLE `pembeli` (
  `id_pembeli` varchar(8) NOT NULL,
  `nama_pembeli` varchar(30) NOT NULL,
  `jk` varchar(20) NOT NULL,
  `no_telp` varchar(20) NOT NULL,
  `alamat` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `pembeli`
--

INSERT INTO `pembeli` (`id_pembeli`, `nama_pembeli`, `jk`, `no_telp`, `alamat`) VALUES
('PB-001', 'Fatar', 'Laki-laki', '089221322', 'Jl.Mekarsari');

-- --------------------------------------------------------

--
-- Table structure for table `pembelian`
--

CREATE TABLE `pembelian` (
  `id_pembelian` varchar(8) NOT NULL,
  `id_supplier` varchar(8) NOT NULL,
  `id_barang` varchar(8) NOT NULL,
  `quantity` int(10) NOT NULL,
  `harga` int(15) NOT NULL,
  `tanggal` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `supplier`
--

CREATE TABLE `supplier` (
  `id_supplier` varchar(8) NOT NULL,
  `nama_supplier` varchar(25) NOT NULL,
  `no_telp` varchar(15) NOT NULL,
  `alamat` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `supplier`
--

INSERT INTO `supplier` (`id_supplier`, `nama_supplier`, `no_telp`, `alamat`) VALUES
('SP-001', 'Supri', '08187776322', 'Jl.Mekarsari');

-- --------------------------------------------------------

--
-- Table structure for table `transaksi`
--

CREATE TABLE `transaksi` (
  `no` int(11) NOT NULL,
  `id_transaksi` varchar(8) NOT NULL,
  `id_barang` varchar(8) NOT NULL,
  `id_pembeli` varchar(8) NOT NULL,
  `tanggal` date NOT NULL,
  `jumlah_barang` int(15) NOT NULL,
  `Total_Harga` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `transaksi`
--

INSERT INTO `transaksi` (`no`, `id_transaksi`, `id_barang`, `id_pembeli`, `tanggal`, `jumlah_barang`, `Total_Harga`) VALUES
(5, 'TR-0001', 'OR-001', 'PB-001', '2021-06-19', 2, 30000);

--
-- Triggers `transaksi`
--
DELIMITER $$
CREATE TRIGGER `keranjang` AFTER INSERT ON `transaksi` FOR EACH ROW BEGIN
INSERT INTO keranjang SET
id_transaksi = NEW.id_transaksi,
id_barang = NEW.id_barang,
nama_barang = (SELECT nama_barang FROM barang WHERE id_barang = NEW.id_barang),
harga = (SELECT harga FROM barang WHERE id_barang = NEW.id_barang),
jumlah_barang = NEW.jumlah_barang,
Total_harga = NEW.Total_harga,
tggl = NEW.tanggal;
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `transaksi` AFTER INSERT ON `transaksi` FOR EACH ROW BEGIN
UPDATE barang SET
stok = stok - NEW.jumlah_barang
WHERE id_barang = NEW.id_barang;
END
$$
DELIMITER ;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `admin`
--
ALTER TABLE `admin`
  ADD PRIMARY KEY (`id_admin`);

--
-- Indexes for table `barang`
--
ALTER TABLE `barang`
  ADD PRIMARY KEY (`id_barang`);

--
-- Indexes for table `keranjang`
--
ALTER TABLE `keranjang`
  ADD PRIMARY KEY (`no`) USING BTREE;

--
-- Indexes for table `pembeli`
--
ALTER TABLE `pembeli`
  ADD PRIMARY KEY (`id_pembeli`);

--
-- Indexes for table `pembelian`
--
ALTER TABLE `pembelian`
  ADD PRIMARY KEY (`id_pembelian`);

--
-- Indexes for table `supplier`
--
ALTER TABLE `supplier`
  ADD PRIMARY KEY (`id_supplier`);

--
-- Indexes for table `transaksi`
--
ALTER TABLE `transaksi`
  ADD PRIMARY KEY (`no`) USING BTREE;

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `admin`
--
ALTER TABLE `admin`
  MODIFY `id_admin` int(4) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- AUTO_INCREMENT for table `keranjang`
--
ALTER TABLE `keranjang`
  MODIFY `no` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT for table `transaksi`
--
ALTER TABLE `transaksi`
  MODIFY `no` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
