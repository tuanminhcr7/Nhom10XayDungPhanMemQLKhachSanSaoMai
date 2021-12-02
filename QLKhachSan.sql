if(exists (select name from sysdatabases where name='QLKhachSan'))
drop database QLKhachSan
go
create database QLKhachSan
go
use QLKhachSan
go
create table NhanVien(
	MaNV nvarchar(10) not null primary key,
	HoTenNV nvarchar(30),
	SoCMND nvarchar(20),
	NgaySinh datetime,
	GioiTinh nvarchar(20),
	Email nvarchar(50),
	SoDienThoai nvarchar(15),
	DiaChi nvarchar(100),
	ChucVu nvarchar(30)
	)
go
create table TaiKhoan(
	TenDangNhap nvarchar(50) not null primary key,
	MatKhau nvarchar(50),
	IsAdmin bit,
	MaNV nvarchar(10),
	constraint fk_nv foreign key(MaNV) 
		references NhanVien(MaNV)
	)

create table KhachHang(
	SoCMNDKH nvarchar(20) not null primary key,
	HoTenKH nvarchar(30),
	NgaySinh datetime,
	GioiTinh nvarchar(3),
	SoDT nvarchar(15),
	Email nvarchar(50),
	DiaChi nvarchar(100),
	GhiChu nvarchar(1000)
	)
create table Phong(
	MaPhong nvarchar(10) not null primary key,
	LoaiPhong nvarchar(30),
	GiaPhong float,
	TinhTrang nvarchar(50),
	GhiChu nvarchar(1000)
	)

create table Phong_Khach_Dat(
	MaDatPhong nvarchar(10) not null primary key,
	MaPhong nvarchar(10) not null,
	SoCMNDKH nvarchar(20),
	SoNguoiO int,
	NgayDen datetime,
	NgayDi datetime,
	TienCoc float,
	TinhTrang nvarchar(30),
	GhiChu nvarchar(1000),
	constraint fk1 foreign key(MaPhong)
		references Phong(MaPhong),
	constraint fk2 foreign key(SoCMNDKH)
		references KhachHang(SoCMNDKH),
)
--SELECT CONCAT('NV', RIGHT(CONCAT('00',ISNULL(right(max(MaNV),2),0) + 1),2)) from NhanVien where MaNV like 'NV%'
--insert Nhan vien
insert into NhanVien values('admin',N'Đậu Thị Huyền','893283','07/12/2000',N'Nữ',N'huyendau@gmail.com','03892738',N'Nghệ An',N'Quản lý')
insert into NhanVien values('NV01',N'Lưu Quỳnh Nga','193828','05/26/2000',N'Nữ',N'ngaluu@gmail.com','083727632',N'Nghệ An',N'Nhân viên')

--insert TaiKhoan
insert into TaiKhoan values('admin','admin123',1,'admin')
insert into TaiKhoan values('luuquynhnga','ngalq',0,'NV01')

--insert Khach hang
insert into KhachHang values('1830',N'Cao Thị Linh','02/11/1998',N'Nữ','073872387','linhcaothi@gmail.com',N'Hà Nội',N'Không')
insert into KhachHang values('2179',N'Nguyễn Thị Anh','06/02/1999',N'Nữ','083787323','anhnguyenthi@gmail.com',N'Hà Nội',N'Không')

-- insert Phong
insert into Phong values('P01',N'Thường đơn',180,N'Còn trống',N'Không')
insert into Phong values('P02',N'Vip đơn',350,N'Còn trống',N'Không')
insert into Phong values('P03',N'Thường đôi',280,N'Đang sử dụng',N'Không')
insert into Phong values('P04',N'Vip đôi',580,N'Đang sử dụng',N'Không')

insert into Phong values('P05',N'Thường đơn',180,N'Còn trống',N'Không')
insert into Phong values('P06',N'Vip đơn',350,N'Còn trống',N'Không')
insert into Phong values('P07',N'Thường đôi',280,N'Còn trống',N'Không')
insert into Phong values('P08',N'Vip đôi',580,N'Còn trống',N'Không')


--insert PhongKhachDat
insert into Phong_Khach_Dat values('HD001','P01','1830',1,'06/06/2021','06/10/2021',350,N'Đã thanh toán',N'Không')
insert into Phong_Khach_Dat values('HD002','P02','2179',1,'06/30/2021','07/03/2021',350,N'Đã thanh toán',N'Không')
insert into Phong_Khach_Dat values('HD005','P03','1830',3,'07/14/2021','07/16/2021',350,N'Đã thanh toán',N'Không')
insert into Phong_Khach_Dat values('HD003','P03','1830',3,'08/14/2021','08/16/2021',350,N'Chờ thanh toán',N'Không')
insert into Phong_Khach_Dat values('HD004','P04','2179',4,'08/14/2021','08/17/2021',350,N'Chờ thanh toán',N'Không')
go
select * from NhanVien
select *from TaiKhoan
select * from KhachHang
select * from Phong
select * from Phong_Khach_Dat
