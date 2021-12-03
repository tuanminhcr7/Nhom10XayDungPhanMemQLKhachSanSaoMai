package QLKS.Views;

import QLKS.Models.PhongKhachDat_CustomTable_PhongTrong;
import QLKS.Helper.DataValidator;
import QLKS.Models.PhongKhachDat;
import QLKS.Helper.DatabaseHelper;
import QLKS.Dao.PhongKhachDat_Controller;
import QLKS.Dao.PhongKhachDat_KhachHang_Controller;
import QLKS.Dao.PhongKhachDat_PhongTrong_Controller;
import QLKS.Dao.QLPhongDao;
import QLKS.Helper.ShareDataPhongKhachDat;
import QLKS.Models.KhachHang;
import QLKS.Models.NhanVien;
import QLKS.Models.Phong;
import QLKS.Views.AdminMain;
import QLKS.Models.PhongKhachDat_CustomTable;
import QLKS.Models.PhongKhachDat_CustomTable_KhachHang;

import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import java.awt.Button;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ButtonGroup;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class QuanLyDatPhong_Panel extends javax.swing.JPanel {

    private AdminMain ad;
    
    DefaultTableModel modelDatPhong =new DefaultTableModel();
    static PhongKhachDat_Controller nvcontroller = new PhongKhachDat_Controller();
    ArrayList<PhongKhachDat> list = new ArrayList<PhongKhachDat>(); 
    ArrayList<PhongKhachDat> list5= new ArrayList<>();
    
    static PhongKhachDat_PhongTrong_Controller ptcontroller = new PhongKhachDat_PhongTrong_Controller();   
    ArrayList<Phong> list1 = new ArrayList<Phong>();   
    ArrayList<Phong> list2 = new ArrayList<Phong>();
    ArrayList<Phong> list7 = new ArrayList<>();
    
    static PhongKhachDat_KhachHang_Controller khcontroller = new PhongKhachDat_KhachHang_Controller();
    ArrayList<KhachHang> list3 = new ArrayList<KhachHang>(); 
    ArrayList<KhachHang> list4 = new ArrayList<KhachHang>();
    ArrayList<KhachHang> list6 = new ArrayList<>();
    
    static QLPhongDao qlPhongDao= new QLPhongDao();
    int selectedRow = 0;
    int yearNow = Calendar.getInstance().get(Calendar.YEAR);
    
    public String SinhMaTuDong(){
        int coso=0;
        if(list.size()== 0){
            coso=1;
        }
        else if(list.size()==1 && Integer.parseInt(list.get(0).getMaDatPhong().toString().substring(4, 5))==1){
            coso=2;
        }
        else if(list.size()==1 && Integer.parseInt(list.get(0).getMaDatPhong().toString().substring(4, 5))>1){
            coso=1;
        }
        else{
            int i =0;
            for(i = 0; i < list.size()-1; i++){
                if(Integer.parseInt(list.get(i+1).getMaDatPhong().toString().substring(4, 5))-Integer.parseInt(list.get(i).getMaDatPhong().toString().substring(4, 5))>1){
                    coso=Integer.parseInt(list.get(i).getMaDatPhong().toString().substring(4, 5))+1;
                    break;
                }
            }
            if(i == list.size()-1){
                coso=Integer.parseInt(list.get(list.size()-1).getMaDatPhong().toString().substring(4, 5))+1;
            }
        }
        if(coso<10)
            return "HD00"+coso;
        if(coso<100)
            return "HD0"+coso;
        return "HD"+coso;
    }
    public QuanLyDatPhong_Panel() throws SQLException {
        initComponents();
        initCombobox();       
        loadData();
    }
    
    private void initCombobox(){
        // Ngày Đến
        for(int i=2019;i<=yearNow;i++){
            cmbNamDen.addItem(""+i);           
        }cmbNamDen.setSelectedIndex(0);
        for(int i=1;i<=12;i++){
            cmbThangDen.addItem(""+i);
        }cmbThangDen.setSelectedIndex(1);
        for(int i=1;i<=31;i++){
            cmbNgayDen.addItem(""+i);
        }cmbNgayDen.setSelectedIndex(0);
        
        //Ngày Đi
        for(int i=2019;i<=yearNow;i++){cmbNamDi.addItem(""+i);
        }cmbNamDi.setSelectedIndex(0);
        for(int i=1;i<=12;i++){cmbThangDi.addItem(""+i);  
        }cmbThangDi.setSelectedIndex(1);
        for(int i=1;i<=31;i++){
            cmbNgayDi.addItem(""+i);
        }cmbNgayDi.setSelectedIndex(0);
    }   
    public void loadCombobox(){
        try{
            cmbLocLoaiPhong.removeAllItems();
            DatabaseHelper a=new DatabaseHelper();
            Connection conn=a.getConnection();
//             Xử lý Combobox Tình Trạng
            PreparedStatement ps1=conn.prepareStatement("SELECT TinhTrang FROM Phong_Khach_Dat GROUP BY TinhTrang");          
            // Xử lý Combobox Loại phòng
            PreparedStatement ps2=conn.prepareStatement("SELECT LoaiPhong FROM Phong GROUP BY LoaiPhong");
            ResultSet rs2 = ps2.executeQuery();
            while(rs2.next()){
                cmbLocLoaiPhong.addItem(rs2.getString("loaiPhong"));          
            }
            
        }catch(Exception ex){
            System.out.println(ex.toString());
        }          
    }  
    private void ClearTxt(){
        txtMaDatPhong.setText("");
        txtMaPhong.setText("");
        txtSoCMNDKH.setText("");
              
        txtTienCoc.setText("");
        txtGhiChu.setText("");        
              
        txtMaDatPhong.getAction();
//        cmbMaPhong.setSelectedIndex(0);
        cmbSoNguoiO.setSelectedIndex(0);    
        cmbTinhTrang.setSelectedIndex(0);
        
        cmbNgayDen.setSelectedIndex(0);    
        cmbThangDen.setSelectedIndex(0);
        cmbNamDen.setSelectedIndex(0);    
        
        cmbNgayDi.setSelectedIndex(0);
        cmbThangDi.setSelectedIndex(0);    
        cmbNamDi.setSelectedIndex(0);    
        
        txtTimMaDatPhong.setText("");
        cmbLocLoaiPhong.setSelectedIndex(0);
        txtTimCMND.setText("");
        
    }
    
    void loadTimMaDatPhong(ArrayList<PhongKhachDat> a){
        tblPhongKhachDat.setModel(new PhongKhachDat_CustomTable(a) ); 
    }
    void loadTimCMND(ArrayList<KhachHang> a){
        tblKhachHang.setModel(new PhongKhachDat_CustomTable_KhachHang(a) ); 
    }
    void loadTimLoaiPhong(ArrayList<Phong> a){
        tblPhongTrong.setModel(new PhongKhachDat_CustomTable_PhongTrong(a) ); 
    }
    
    public void loadDataKhachHang() throws SQLException{      
        list3 = khcontroller.getAllKH_PKD();     
        tblKhachHang.setModel(new PhongKhachDat_CustomTable_KhachHang(list3));
    } 
    
    public void loadDataPhongTrong() throws SQLException{      
        list1 = ptcontroller.getAllPhongTrong();     
        tblPhongTrong.setModel(new PhongKhachDat_CustomTable_PhongTrong(list1));
        list2 = ptcontroller.getAllPhong();    
    } 
    
    public void loadData() throws SQLException{        
        list = nvcontroller.getAllPhongKhachDat();       
        tblPhongKhachDat.setModel(new PhongKhachDat_CustomTable(list));
        loadCombobox();
        loadDataPhongTrong();
        loadDataKhachHang();
        txtMaDatPhong.setEnabled(false);
    } 
    public void loadData2() throws SQLException{        
        list = nvcontroller.getAllPhongKhachDat();       
        tblPhongKhachDat.setModel(new PhongKhachDat_CustomTable(list));
        loadDataPhongTrong();
        loadDataKhachHang();
//        loadCombobox();
    } 
 
    
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel4 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        jPanel5 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtMaDatPhong = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtTienCoc = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtGhiChu = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        cmbNamDen = new javax.swing.JComboBox<>();
        cmbThangDen = new javax.swing.JComboBox<>();
        cmbNgayDen = new javax.swing.JComboBox<>();
        jLabel11 = new javax.swing.JLabel();
        cmbNamDi = new javax.swing.JComboBox<>();
        cmbThangDi = new javax.swing.JComboBox<>();
        cmbNgayDi = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        cmbTinhTrang = new javax.swing.JComboBox<>();
        jPanel6 = new javax.swing.JPanel();
        btnAdd = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        txtTimMaDatPhong = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtSoCMNDKH = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        cmbSoNguoiO = new javax.swing.JComboBox<>();
        txtMaPhong = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblPhongTrong = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        cmbLocLoaiPhong = new javax.swing.JComboBox<>();
        btnLocLoaiPhong = new javax.swing.JButton();
        jLabel18 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jSeparator5 = new javax.swing.JSeparator();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblKhachHang = new javax.swing.JTable();
        jLabel14 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        txtTimCMND = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblPhongKhachDat = new javax.swing.JTable();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel15 = new javax.swing.JLabel();

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 0, 0));
        jLabel16.setText("Thông tin Đặt phòng");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setText("Mã Đặt Phòng:");

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel12.setText("Tiền Cọc:");

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel13.setText("Ghi Chú:");

        txtGhiChu.setColumns(20);
        txtGhiChu.setRows(5);
        jScrollPane4.setViewportView(txtGhiChu);

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel2.setText("Năm");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel7.setText("Tháng");

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel9.setText("Ngày");

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel10.setText("Ngày Đến:");

        cmbThangDen.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cmbThangDenFocusLost(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel11.setText("Ngày Đi:");

        cmbNamDi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbNamDiActionPerformed(evt);
            }
        });

        cmbThangDi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbThangDiActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel8.setText("Tình Trạng:");

        cmbTinhTrang.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmbTinhTrang.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Chờ thanh toán", "Đã thanh toán", "Đã huỷ" }));
        cmbTinhTrang.setToolTipText("");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(133, 133, 133)
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(50, 50, 50)
                                .addComponent(cmbNamDen, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cmbThangDen, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cmbNgayDen, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(cmbNamDi, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(cmbThangDi, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cmbNgayDi, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(0, 6, Short.MAX_VALUE)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtMaDatPhong, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(txtTienCoc, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cmbTinhTrang, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMaDatPhong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(25, 25, 25)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel7)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbNamDen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(cmbThangDen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbNgayDen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cmbNgayDi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmbThangDi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmbNamDi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel12))
                    .addComponent(txtTienCoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(cmbTinhTrang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(42, 42, 42))
        );

        btnAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/QLKS/icons/add.png"))); // NOI18N
        btnAdd.setText("Thêm");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/QLKS/icons/edit.png"))); // NOI18N
        btnUpdate.setText("Sửa");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/QLKS/icons/exit32.png"))); // NOI18N
        jButton10.setText("Tẩy");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/QLKS/icons/delete.png"))); // NOI18N
        btnDelete.setText("Xoá");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        btnReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/QLKS/icons/find_1.png"))); // NOI18N
        btnReset.setText("Tải lại");
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });

        txtTimMaDatPhong.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txtTimMaDatPhongCaretUpdate(evt);
            }
        });

        jLabel21.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel21.setText("Tìm kiếm:");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnAdd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnUpdate, javax.swing.GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE)
                            .addComponent(jButton10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtTimMaDatPhong)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))))))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAdd)
                    .addComponent(btnUpdate))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton10)
                    .addComponent(btnDelete))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnReset)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel21)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                        .addComponent(txtTimMaDatPhong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(22, 22, 22))
        );

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setText("Số CMND KH:");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setText("Mã Phòng:");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setText("Số Người Ở:");

        cmbSoNguoiO.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4" }));

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(jLabel4)
                    .addComponent(jLabel6))
                .addGap(25, 25, 25)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbSoNguoiO, 0, 125, Short.MAX_VALUE)
                    .addComponent(txtSoCMNDKH)
                    .addComponent(txtMaPhong))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtMaPhong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtSoCMNDKH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(cmbSoNguoiO, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator4)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 335, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tblPhongTrong.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(tblPhongTrong);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(51, 0, 255));
        jLabel1.setText("Danh sách Phòng đang trống:");

        btnLocLoaiPhong.setIcon(new javax.swing.ImageIcon(getClass().getResource("/QLKS/icons/view.png"))); // NOI18N
        btnLocLoaiPhong.setText("Lọc");
        btnLocLoaiPhong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLocLoaiPhongActionPerformed(evt);
            }
        });

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel18.setText("Lọc Loại Phòng:");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbLocLoaiPhong, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel18)
                        .addGap(0, 21, Short.MAX_VALUE))
                    .addComponent(btnLocLoaiPhong, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel18)
                .addGap(18, 18, 18)
                .addComponent(cmbLocLoaiPhong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnLocLoaiPhong)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 544, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 1, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 0, 0));
        jLabel20.setText("Thông tin Đơn đặt");

        tblKhachHang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tblKhachHang);

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(51, 0, 255));
        jLabel14.setText("Danh sách Khách hàng:");

        txtTimCMND.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txtTimCMNDCaretUpdate(evt);
            }
        });

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel19.setText("Tìm Khách hàng:");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtTimCMND)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jLabel19)
                        .addGap(0, 11, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap(45, Short.MAX_VALUE)
                .addComponent(jLabel19)
                .addGap(18, 18, 18)
                .addComponent(txtTimCMND, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane1))
                .addGap(18, 18, 18)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator5)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel20)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel20)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tblPhongKhachDat.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblPhongKhachDat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPhongKhachDatMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tblPhongKhachDat);

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(51, 0, 255));
        jLabel15.setText("Danh sách Phòng khách đặt:");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator2)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel15)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 7, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 11, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void cmbThangDenFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbThangDenFocusLost
    
    }//GEN-LAST:event_cmbThangDenFocusLost

    private void cmbNamDiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbNamDiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbNamDiActionPerformed

    private void cmbThangDiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbThangDiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbThangDiActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed

        StringBuilder sb = new StringBuilder();
        String testma = "Còn trống";

        for(Phong c:list2){
            if(c.getMaPhong().equals(txtMaPhong.getText())&& !c.getTinhTrang().equals(testma)){
                sb.append("Phòng được chọn hiện đang không trống! \n");
                break;
            }
        }
        if (txtMaPhong.getText().equals("")) {
            sb.append("Mã Phòng không được để trống!\n");}
        if (txtSoCMNDKH.getText().equals("")) {
            sb.append("Số CMND không được để trống!\n");}
        if (txtTienCoc.getText().equals("")) {
            sb.append("Tiền cọc không được để trống!\n");}
        if(sb.length()>0){
            JOptionPane.showMessageDialog(null, sb, "Thông báo", JOptionPane.WARNING_MESSAGE);
        }else {
            try {
               String MaDatPhong= SinhMaTuDong();
                System.out.println(list.size());
                System.out.println(MaDatPhong);
                String MaPhong = txtMaPhong.getText();
                String SoCMNDKH = txtSoCMNDKH.getText();
                String SoNguoiO = cmbSoNguoiO.getSelectedItem().toString();

                if(cmbThangDen.getSelectedItem().equals("4")
                    ||cmbThangDen.getSelectedItem().equals("6")
                    ||cmbThangDen.getSelectedItem().equals("9")
                    ||cmbThangDen.getSelectedItem().equals("11")){
                    if(cmbNgayDen.getSelectedItem().equals("31")){
                        sb.append("Các Tháng 4,6,9,11 có 30 ngày! Mời Chọn Lại Ngày!!\n");
                    }
                }
                if(cmbThangDen.getSelectedItem().equals("2")){
                    if(Integer.parseInt(""+cmbNamDen.getSelectedItem())%4==0){
                        if(cmbNgayDen.getSelectedItem().equals("30")||cmbNgayDen.getSelectedItem().equals("31")){
                            sb.append("Năm Nhuận Tháng 2 có 29 ngày! Mời Chọn Lại Ngày!\n");
                        }
                    }else{
                        if(cmbNgayDen.getSelectedItem().equals("29")
                            ||cmbNgayDen.getSelectedItem().equals("30")
                            ||cmbNgayDen.getSelectedItem().equals("31")){
                            sb.append("Tháng 2 có 28 ngày! Mời Chọn Lại Ngày!\n");
                        }
                    }
                }

                if(cmbThangDi.getSelectedItem().equals("4")
                    ||cmbThangDi.getSelectedItem().equals("6")
                    ||cmbThangDi.getSelectedItem().equals("9")
                    ||cmbThangDi.getSelectedItem().equals("11")){
                    if(cmbNgayDi.getSelectedItem().equals("31")){
                        sb.append("Các Tháng 4,6,9,11 có 30 ngày! Mời Chọn Lại Ngày!!\n");
                    }
                }
                if(cmbThangDi.getSelectedItem().equals("2")){
                    if(Integer.parseInt(""+cmbNamDi.getSelectedItem())%4==0){
                        if(cmbNgayDi.getSelectedItem().equals("30")||cmbNgayDi.getSelectedItem().equals("31")){
                            sb.append("Năm Nhuận Tháng 2 có 29 ngày! Mời Chọn Lại Ngày!\n");
                        }
                    }else{
                        if(cmbNgayDi.getSelectedItem().equals("29")
                            ||cmbNgayDi.getSelectedItem().equals("30")
                            ||cmbNgayDi.getSelectedItem().equals("31")){
                            sb.append("Tháng 2 có 28 ngày! Mời Chọn Lại Ngày!\n");
                        }
                    }
                }

                PhongKhachDat pdk2 = new PhongKhachDat();
                pdk2.setNgayDen(cmbNamDen.getSelectedItem()+"-"
                    +cmbThangDen.getSelectedItem()+"-"+cmbNgayDen.getSelectedItem());

                String NgayDen = pdk2.getNgayDen();
                pdk2.setNgayDi(cmbNamDi.getSelectedItem()+"-"
                    +cmbThangDi.getSelectedItem()+"-"+cmbNgayDi.getSelectedItem());
                String NgayDi = pdk2.getNgayDi();

                Float TienCoc = Float.valueOf(txtTienCoc.getText());
                String TinhTrang = cmbTinhTrang.getSelectedItem().toString();
                String GhiChu = txtGhiChu.getText();

                PhongKhachDat kh = new PhongKhachDat(MaDatPhong, MaPhong, SoCMNDKH, SoNguoiO, NgayDen, NgayDi, TienCoc, TinhTrang, GhiChu);
                Phong p = new Phong(MaPhong);
                
                if (
                    txtMaPhong.getText().equals("")
                    || txtSoCMNDKH.getText().equals("")
                    || txtTienCoc.getText().equals("")){
                    JOptionPane.showMessageDialog(null, "Không được để trống dữ liệu!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                }
                try {                    
                    if (nvcontroller.addPhongKhachDat(kh) && nvcontroller.updatePhong_PKD_DangThue(p) ) {
                        JOptionPane.showMessageDialog(null, "Thêm hóa đơn thành công", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                        ClearTxt();
                    }                
                } catch (SQLException ex) {
                    Logger.getLogger(QuanLyDatPhong_Panel.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    loadData();
                } catch (SQLException ex) {
                    Logger.getLogger(QuanLyDatPhong_Panel.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (java.lang.NumberFormatException nfe) {
                JOptionPane.showMessageDialog(null, "Bạn đã nhập sai kiểu dữ liệu!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            } catch (Exception ex) {
                System.out.print(ex);
            }
        }
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        StringBuilder sb = new StringBuilder();

        String testma = "Còn trống";
        
        for(Phong p:list2){
            String maphu = p.getMaPhong();
            if(p.getMaPhong().equals(txtMaPhong.getText())&& !p.getTinhTrang().equals(testma)
                    && p.getMaPhong() != maphu){
                sb.append("Phòng được chọn thay đổi hiện đang không trống! \n");
                break;
            }
        }

        if (txtMaDatPhong.getText().equals("")) {
            sb.append("Vui lòng chọn 1 dòng thông tin cần sửa!\n");
        }
        if (txtMaPhong.getText().equals("")) {
            sb.append("Mã Phòng không được để trống!\n");
        }
        if (txtSoCMNDKH.getText().equals("")) {
            sb.append("Số CMND không được để trống!\n");
        }
        if (txtTienCoc.getText().equals("")) {
            sb.append("Tiền cọc không được để trống!\n");
        }
        if(sb.length()>0){
            JOptionPane.showMessageDialog(null, sb, "Thông báo", JOptionPane.WARNING_MESSAGE);
        }else {
            try {
                String MaDatPhong = txtMaDatPhong.getText();
                String MaPhong = txtMaPhong.getText();
                
                String SoCMNDKH = txtSoCMNDKH.getText();
                String SoNguoiO = cmbSoNguoiO.getSelectedItem().toString();

                if(cmbThangDen.getSelectedItem().equals("4")
                    ||cmbThangDen.getSelectedItem().equals("6")
                    ||cmbThangDen.getSelectedItem().equals("9")
                    ||cmbThangDen.getSelectedItem().equals("11")){
                    if(cmbNgayDen.getSelectedItem().equals("31")){
                        sb.append("Các Tháng 4,6,9,11 có 30 ngày! Mời Chọn Lại Ngày!!\n");
                    }
                }
                if(cmbThangDen.getSelectedItem().equals("2")){
                    if(Integer.parseInt(""+cmbNamDen.getSelectedItem())%4==0){
                        if(cmbNgayDen.getSelectedItem().equals("30")||cmbNgayDen.getSelectedItem().equals("31")){
                            sb.append("Năm Nhuận Tháng 2 có 29 ngày! Mời Chọn Lại Ngày!\n");
                        }
                    }else{
                        if(cmbNgayDen.getSelectedItem().equals("29")
                            ||cmbNgayDen.getSelectedItem().equals("30")
                            ||cmbNgayDen.getSelectedItem().equals("31")){
                            sb.append("Tháng 2 có 28 ngày! Mời Chọn Lại Ngày!\n");
                        }
                    }
                }

                if(cmbThangDi.getSelectedItem().equals("4")
                    ||cmbThangDi.getSelectedItem().equals("6")
                    ||cmbThangDi.getSelectedItem().equals("9")
                    ||cmbThangDi.getSelectedItem().equals("11")){
                    if(cmbNgayDi.getSelectedItem().equals("31")){
                        sb.append("Các Tháng 4,6,9,11 có 30 ngày! Mời Chọn Lại Ngày!!\n");
                    }
                }
                if(cmbThangDi.getSelectedItem().equals("2")){
                    if(Integer.parseInt(""+cmbNamDi.getSelectedItem())%4==0){
                        if(cmbNgayDi.getSelectedItem().equals("30")||cmbNgayDi.getSelectedItem().equals("31")){
                            sb.append("Năm Nhuận Tháng 2 có 29 ngày! Mời Chọn Lại Ngày!\n");
                        }
                    }else{
                        if(cmbNgayDi.getSelectedItem().equals("29")
                            ||cmbNgayDi.getSelectedItem().equals("30")
                            ||cmbNgayDi.getSelectedItem().equals("31")){
                            sb.append("Tháng 2 có 28 ngày! Mời Chọn Lại Ngày!\n");
                        }
                    }
                }

                PhongKhachDat pdk2 = new PhongKhachDat();
                pdk2.setNgayDen(cmbNamDen.getSelectedItem()+"-"
                    +cmbThangDen.getSelectedItem()+"-"+cmbNgayDen.getSelectedItem());
                String NgayDen = pdk2.getNgayDen();

                pdk2.setNgayDi(cmbNamDi.getSelectedItem()+"-"
                    +cmbThangDi.getSelectedItem()+"-"+cmbNgayDi.getSelectedItem());
                String NgayDi = pdk2.getNgayDi();

                Float TienCoc = Float.valueOf(txtTienCoc.getText()) ;

                String TinhTrang = cmbTinhTrang.getSelectedItem().toString();
                String GhiChu = txtGhiChu.getText();

                PhongKhachDat kh = new PhongKhachDat(MaDatPhong, MaPhong, SoCMNDKH, SoNguoiO, NgayDen, NgayDi, TienCoc, TinhTrang, GhiChu);
                
                Phong p = new Phong(MaPhong); // Tạo Contructor  bên Class Phong như cái này

                
//                Phong pTam = new Phong(MaPhongTamThoi);
                try {
                    if (nvcontroller.updatePhongKhachDat(kh) 
                            && nvcontroller.updatePhong_PKD_DangThue(p)) {
                        JOptionPane.showMessageDialog(null, "Cập nhật thông tin phòng khách đặt thành công", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                        ClearTxt();
                    }
                    
                } catch (SQLException ex) {
                    Logger.getLogger(QuanLyDatPhong_Panel.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    loadData();
                } catch (SQLException ex) {
                    Logger.getLogger(QuanLyDatPhong_Panel.class.getName()).log(Level.SEVERE, null, ex);
                }
                ClearTxt();
            } catch (java.lang.NumberFormatException nfe) {
                JOptionPane.showMessageDialog(null, "Không được để trống dữ liệu\n");
            } catch (Exception ex) {
                System.out.print(ex);
            }
        }
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        ClearTxt();
    }//GEN-LAST:event_jButton10ActionPerformed
    
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
  
        // Xoá Type 1
            boolean check=false;
            for(PhongKhachDat nv:list)
            {
                if(nv.getMaDatPhong().equals(txtMaDatPhong.getText())){
                    check=true;
                    break;
                }
            } 
            if(txtMaDatPhong.getText().trim().equals("")){
                JOptionPane.showMessageDialog(null, "Vui lòng chọn 1 dòng thông tin để xóa", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
            if(check){
                int xn = JOptionPane.showConfirmDialog(null,
                "Bạn có chắc muốn xoá bản ghi này không?",
                "Xác nhận",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
                if (xn == JOptionPane.YES_OPTION) {
                    try {
                        PhongKhachDat pkd= new PhongKhachDat();
                       for(PhongKhachDat pk : list){
                           if(pk.getMaDatPhong().equals(txtMaDatPhong.getText())){
                               pkd=pk;
                           }
                       }
                        qlPhongDao.upDatePhongKD(pkd.getMaDatPhong());
                        nvcontroller.delPhongKhachDat(txtMaDatPhong.getText());
                        JOptionPane.showMessageDialog(null, "Xóa thành công", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                        loadData();
                        ClearTxt();
                    }
                    catch (SQLException ex) {
                        Logger.getLogger(QuanLyDatPhong_Panel.class.getName()).log(Level.SEVERE,null, ex);
                    }
                }
            }
            else{
                JOptionPane.showMessageDialog(this,"Mã đặt phòng không tồn tại hoặc bị trống!");
                txtMaDatPhong.setText("");
                txtMaDatPhong.requestFocus();
            }

    }//GEN-LAST:event_btnDeleteActionPerformed

    private void tblPhongKhachDatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPhongKhachDatMouseClicked
        int selectedRow = tblPhongKhachDat.getSelectedRow();
        int i = 0;
        for (PhongKhachDat e : list) {
            if (e.getMaDatPhong().equals(tblPhongKhachDat.getValueAt(selectedRow, 0))) {
                i = list.indexOf(e);
            }
        }
        PhongKhachDat h = list.get(i);

        txtMaDatPhong.setText(h.getMaDatPhong());
        txtMaPhong.setText(h.getMaPhong());
        txtSoCMNDKH.setText(h.getSoCMNDKH());

        if (h.getSoNguoiO().equals("1")) {
            cmbSoNguoiO.setSelectedItem("1");
        }
        else if (h.getSoNguoiO().equals("2")) {
            cmbSoNguoiO.setSelectedItem("2");
        }
        else {
            cmbSoNguoiO.setSelectedItem("3");
        }

        String[] s = h.getNgayDen().split("-");
        cmbNamDen.setSelectedItem(""+Integer.parseInt(s[0]));
        cmbThangDen.setSelectedItem(""+Integer.parseInt(s[1]));
        String tam = ""+s[2].charAt(0)+s[2].charAt(1);
        cmbNgayDen.setSelectedItem(""+Integer.parseInt(tam));

        String[] ss = h.getNgayDi().split("-");
        cmbNamDi.setSelectedItem(""+Integer.parseInt(ss[0]));
        cmbThangDi.setSelectedItem(""+Integer.parseInt(ss[1]));
        String tam1 = ""+ss[2].charAt(0)+ss[2].charAt(1);
        cmbNgayDi.setSelectedItem(""+Integer.parseInt(tam1));

        txtTienCoc.setText(String.valueOf(h.getTienCoc()));

        if (h.getTinhTrang().equals("Chờ thanh toán")) {
            cmbTinhTrang.setSelectedItem("Chờ thanh toán");
        }
        else if (h.getTinhTrang().equals("Đã thanh toán")) {
            cmbTinhTrang.setSelectedItem("Đã thanh toán");
        }
        else {
            cmbTinhTrang.setSelectedItem("Đã huỷ");
        }
        txtGhiChu.setText(h.getGhiChu());
    }//GEN-LAST:event_tblPhongKhachDatMouseClicked

    private void txtTimMaDatPhongCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txtTimMaDatPhongCaretUpdate
        list5.clear();
        for(PhongKhachDat h: list){
            if(h.getSoCMNDKH().contains(txtTimMaDatPhong.getText())||h.getMaPhong().contains(txtTimMaDatPhong.getText())||h.getMaDatPhong().contains(txtTimMaDatPhong.getText())){
                list5.add(h);
                if(h.getSoCMNDKH().equals(txtTimMaDatPhong.getText())||h.getMaPhong().equals(txtTimMaDatPhong.getText())||h.getMaDatPhong().equals(txtTimMaDatPhong.getText())){
                    
                    txtMaDatPhong.setText(h.getMaDatPhong());
                    txtMaPhong.setText(h.getMaPhong());
                    txtSoCMNDKH.setText(h.getSoCMNDKH());

                    if (h.getSoNguoiO().equals("1")) {
                        cmbSoNguoiO.setSelectedItem("1");
                    }
                    else if (h.getSoNguoiO().equals("2")) {
                        cmbSoNguoiO.setSelectedItem("2");
                    }
                    else {
                        cmbSoNguoiO.setSelectedItem("3");
                    }

                    String[] s = h.getNgayDen().split("-");
                    cmbNamDen.setSelectedItem(""+Integer.parseInt(s[0]));
                    cmbThangDen.setSelectedItem(""+Integer.parseInt(s[1]));
                    String tam = ""+s[2].charAt(0)+s[2].charAt(1);
                    cmbNgayDen.setSelectedItem(""+Integer.parseInt(tam));

                    String[] ss = h.getNgayDi().split("-");
                    cmbNamDi.setSelectedItem(""+Integer.parseInt(ss[0]));
                    cmbThangDi.setSelectedItem(""+Integer.parseInt(ss[1]));
                    String tam1 = ""+ss[2].charAt(0)+ss[2].charAt(1);
                    cmbNgayDi.setSelectedItem(""+Integer.parseInt(tam1));

                    txtTienCoc.setText(String.valueOf(h.getTienCoc()));

                    if (h.getTinhTrang().equals("Chờ thanh toán")) {
                        cmbTinhTrang.setSelectedItem("Chờ thanh toán");
                    }
                    else if (h.getTinhTrang().equals("Đã thanh toán")) {
                        cmbTinhTrang.setSelectedItem("Đã thanh toán");
                    }
                    else {
                        cmbTinhTrang.setSelectedItem("Đã huỷ");
                    }
                    txtGhiChu.setText(h.getGhiChu());

                }
                else{
                    txtMaDatPhong.setText("");
                    txtMaPhong.setText("");
                    txtSoCMNDKH.setText("");

                    txtTienCoc.setText("");
                    txtGhiChu.setText("");        

                    txtMaDatPhong.getAction();
                    cmbSoNguoiO.setSelectedIndex(0);    
                    cmbTinhTrang.setSelectedIndex(0);

                    cmbNgayDen.setSelectedIndex(0);    
                    cmbThangDen.setSelectedIndex(0);
                    cmbNamDen.setSelectedIndex(0);    

                    cmbNgayDi.setSelectedIndex(0);
                    cmbThangDi.setSelectedIndex(0);    
                    cmbNamDi.setSelectedIndex(0);          
                }
            }
        }
        loadTimMaDatPhong(list5);
        
        
    }//GEN-LAST:event_txtTimMaDatPhongCaretUpdate

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        ClearTxt();
        try {
            loadData2();
            ClearTxt();
        } catch (SQLException ex) {
            Logger.getLogger(QuanLyDatPhong_Panel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnResetActionPerformed

    private void btnLocLoaiPhongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLocLoaiPhongActionPerformed
        list7.clear();
        for(Phong h: list1){
 
            if(h.getLoaiPhong().contains(cmbLocLoaiPhong.getSelectedItem().toString())){
                list7.add(h);        
            }
        }
        loadTimLoaiPhong(list7);
    }//GEN-LAST:event_btnLocLoaiPhongActionPerformed

    private void txtTimCMNDCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txtTimCMNDCaretUpdate
        list6.clear();
        for(KhachHang h: list3){
            if(h.getSoDT().contains(txtTimCMND.getText())
                    ||h.getHoTenKH().contains(txtTimCMND.getText())
                    ||h.getSoCMNDKH().contains(txtTimCMND.getText())){
                list6.add(h);
//                tblModel.addRow(new Object[]{a.getMaNV(),a.getHoTenNV(),a.getSoCMND(),a.getNgaySinh(),a.getGioiTinh(),a.getEmail(),a.getSoDienThoai(),a.getDiaChi(),a.getChucVu()});
                           
            }
        }
        loadTimCMND(list6);
    }//GEN-LAST:event_txtTimCMNDCaretUpdate

 

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnLocLoaiPhong;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JComboBox<String> cmbLocLoaiPhong;
    private javax.swing.JComboBox<String> cmbNamDen;
    private javax.swing.JComboBox<String> cmbNamDi;
    private javax.swing.JComboBox<String> cmbNgayDen;
    private javax.swing.JComboBox<String> cmbNgayDi;
    private javax.swing.JComboBox<String> cmbSoNguoiO;
    private javax.swing.JComboBox<String> cmbThangDen;
    private javax.swing.JComboBox<String> cmbThangDi;
    private javax.swing.JComboBox<String> cmbTinhTrang;
    private javax.swing.JButton jButton10;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JTable tblKhachHang;
    private javax.swing.JTable tblPhongKhachDat;
    private javax.swing.JTable tblPhongTrong;
    private javax.swing.JTextArea txtGhiChu;
    private javax.swing.JTextField txtMaDatPhong;
    private javax.swing.JTextField txtMaPhong;
    private javax.swing.JTextField txtSoCMNDKH;
    private javax.swing.JTextField txtTienCoc;
    private javax.swing.JTextField txtTimCMND;
    private javax.swing.JTextField txtTimMaDatPhong;
    // End of variables declaration//GEN-END:variables

}
