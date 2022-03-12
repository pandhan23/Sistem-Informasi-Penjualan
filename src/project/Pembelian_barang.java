/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project;
import static groovyjarjarantlr.build.ANTLR.root;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author HP
 */
public class Pembelian_barang extends javax.swing.JInternalFrame {
private Connection kon = new Koneksi().connect();
private DefaultTableModel tabmode;
 public void kode_pembelian(){
      String kode="BL-000";
      int i=0;
    try{
        
        Connection kon=DriverManager.getConnection("jdbc:mysql://localhost:3306/penjualan_makanan","root","");
        Statement stat = kon.createStatement();
        ResultSet rs = stat.executeQuery("select id_pembelian from pembelian");
        
        while(rs.next()){
            kode = rs.getString("id_pembelian");
        }
        kode = kode.substring(4);
        i = Integer.parseInt(kode)+1;
        kode = "00"+i;
        kode = "BL-"+kode.substring(kode.length()-3);
        tIp.setText(kode);
       
    }catch(SQLException e){
        System.out.println(e.getMessage());
    }
        
    }
    /**
     * Creates new form Supplier
     */
    public Pembelian_barang() {
        initComponents();
        datatable();
        supplier1();
        barang1();
        aktif();
        kode_pembelian();
        
    }
    protected void datatable() {
        Object[] Baris ={"id_pembelian","id_supplier","nama_supplier","id_barang","nama_barang","quantity","harga","tanggal_m"};
        tabmode = new DefaultTableModel(null, Baris);
        tabel2.setModel(tabmode);
        String sql = "select * from pembelian  inner join supplier on pembelian.id_supplier = supplier.id_supplier inner join barang on pembelian.id_barang = barang.id_barang"; 
        try {
            java.sql.Statement stat = kon.createStatement();
            ResultSet hasil = stat.executeQuery(sql);
            while(hasil.next()){
                String a = hasil.getString("id_pembelian");
                String b = hasil.getString("id_supplier");
                String c = hasil.getString("nama_supplier");
                String d = hasil.getString("id_barang");
                String e = hasil.getString("nama_barang");
                String f = hasil.getString("quantity");
                String g = hasil.getString("harga");
                String h = hasil.getString("tanggal_m");
                
              String[] data={a,b,c,d,e,f,g,h};
                tabmode.addRow(data);
            }
        } catch (Exception e){}
    }
   protected void cari(){
        java.util.Date utilStartDate = tgl_awal.getDate();
        java.sql.Date sqlStartDate = new java.sql.Date(utilStartDate.getTime());
        java.util.Date utilStartDate2 = tgl_akhir.getDate();
        java.sql.Date sqlStartDate2 = new java.sql.Date(utilStartDate2.getTime());
        jLabel14.setText(sqlStartDate.toString() +" s/d "+sqlStartDate2.toString());
        Object[] Baris ={"id_pembelian","id_supplier","nama_supplier","id_barang","nama_barang","quantity","harga","tanggal_m"};
        tabmode = new DefaultTableModel(null, Baris);
        tabel2.setModel(tabmode);
        try {
        String sql = "select * from pembelian  inner join supplier on pembelian.id_supplier = supplier.id_supplier inner join barang on pembelian.id_barang = barang.id_barang"
                + " WHERE tanggal_m BETWEEN '" + sqlStartDate.toString() + "' AND '" + sqlStartDate2.toString() + "'"
                + " ORDER BY tanggal_m ASC";
            PreparedStatement pst = kon.prepareStatement(sql);
            ResultSet hasil = pst.executeQuery(sql);
            while(hasil.next()){
                 String a = hasil.getString("id_pembelian");
                String b = hasil.getString("id_supplier");
                String c = hasil.getString("nama_supplier");
                String d = hasil.getString("id_barang");
                String e = hasil.getString("nama_barang");
                String f = hasil.getString("quantity");
                String g = hasil.getString("harga");
                String h = hasil.getString("tanggal_m");
                
              String[] data={a,b,c,d,e,f,g,h};
                tabmode.addRow(data);
            }
        } catch (Exception e){
        JOptionPane.showMessageDialog(null,e);}
    }
       private void supplier1(){
        try{
          String sql="select * from supplier";
          PreparedStatement pst = kon.prepareStatement(sql);
          ResultSet rs=pst.executeQuery(sql);          
      while(rs.next()){   
          String name =rs.getString("id_supplier");
          supplier.addItem(name);
      }
        }catch(Exception e){
        JOptionPane.showMessageDialog(null,e);
        }
    }
        private void supplier2(){
        try{
          String sql="select * from supplier where id_supplier='"+supplier.getSelectedItem()+"';";
          PreparedStatement pst = kon.prepareStatement(sql);
          ResultSet rs=pst.executeQuery(sql); 
          while(rs.next()){   
          String name =rs.getString("nama_supplier");
          tNm.setText(name);}
            }catch(Exception e){
        JOptionPane.showMessageDialog(null,e);
        }
    }
        private void barang1(){
        try{
          String sql="select * from barang";
          PreparedStatement pst = kon.prepareStatement(sql);
          ResultSet rs=pst.executeQuery(sql);          
      while(rs.next()){   
          String name =rs.getString("id_barang");
          barang.addItem(name);
      }
        }catch(Exception e){
        JOptionPane.showMessageDialog(null,e);
        }
    }
        private void barang2(){
        try{
     String sql="select * from barang where id_barang='"+barang.getSelectedItem()+"';";
          PreparedStatement pst = kon.prepareStatement(sql);
          ResultSet rs=pst.executeQuery(sql); 
          while(rs.next()){   
          String name =rs.getString("nama_barang");
          tNb.setText(name);}
            }catch(Exception e){
        JOptionPane.showMessageDialog(null,e);
        }
    }
        
        protected void barang_stock(){
    String sql = "select * from barang where id_barang='"+barang.getSelectedItem().toString()+"';";
    try {        
            PreparedStatement pst = kon.prepareStatement(sql);
//            java.sql.Statement stat = conn.createStatement();
            ResultSet hasil = pst.executeQuery(sql);
            while(hasil.next()){                
                String a = hasil.getString("stok");
                stokk.setText(a);
            }
        } catch (Exception e){
        JOptionPane.showMessageDialog(null,e);}
    }
        
         protected void restok(){
    try{    
        String sql = "update barang set stok=? where id_barang=?";
         PreparedStatement stat = kon.prepareStatement(sql);
              int tam = Integer.parseInt(tambah.getText());
              int jum = Integer.parseInt(stokk.getText());
              int hasil = jum-tam+Integer.parseInt(tQ.getText());
              String bubah = String.valueOf(hasil);
              stat.setString(1, bubah);
              stat.setString(2, barang.getSelectedItem().toString());                        
            stat.executeUpdate();
            kosong();
            datatable();
            //JOptionPane.showMessageDialog(null, "Data Berhasil Diubah");
        }catch (SQLException e){
            //JOptionPane.showMessageDialog(null, "Data Gagal Diubah"+e);
    }
    }
        
                
    
public void aktif() {
    tIp.setEnabled(true);
    tNm.setEnabled(true);
    tNb.setEnabled(true);
    tQ.setEnabled(true);
  tH.setEnabled(true);
  tanggal.setEnabled(true);  
    }
public void kosong() {
      tIp.setText("");
       tNm.setText("");
       tNb.setText("");
       tQ.setText("");
       tambah.setText("");
       tH.setText("");
       tanggal.setCalendar(null);
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabel2 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        tIp = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        supplier = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        tNm = new javax.swing.JTextField();
        tNb = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        tQ = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        tH = new javax.swing.JTextField();
        tanggal = new com.toedter.calendar.JDateChooser();
        jLabel4 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        barang = new javax.swing.JComboBox<>();
        jPanel3 = new javax.swing.JPanel();
        bEd = new javax.swing.JButton();
        bTm = new javax.swing.JButton();
        bHp = new javax.swing.JButton();
        bRst = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        tgl_awal = new com.toedter.calendar.JDateChooser();
        tgl_akhir = new com.toedter.calendar.JDateChooser();
        jLabel10 = new javax.swing.JLabel();
        jSc = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        tambah = new javax.swing.JLabel();
        stokk = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(204, 204, 204));
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Pembelian Barang");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8_Buying_80px.png"))); // NOI18N
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Tabel"));

        tabel2.setModel(new javax.swing.table.DefaultTableModel(
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
        tabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabel2MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tabel2);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(102, 102, 102)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 911, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(92, 92, 92)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 440, 1120, 210));

        jPanel2.setBackground(new java.awt.Color(204, 204, 204));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Input"));

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel8.setText("Id_Pembelian");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setText("Id_Supplier");

        supplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supplierActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setText("Nama");

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel6.setText("Nama Barang");

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel9.setText("Quantity");

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel5.setText("Harga");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel4.setText("Tanggal Masuk");

        jLabel15.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel15.setText("Id Barang");

        barang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                barangActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(tNm, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(18, 18, 18)
                        .addComponent(tIp, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(31, 31, 31)
                        .addComponent(supplier, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(81, 81, 81)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel15)
                            .addComponent(jLabel6))
                        .addGap(51, 51, 51)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(barang, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(tNb, javax.swing.GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(91, 91, 91)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 70, Short.MAX_VALUE)
                        .addComponent(tQ, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(69, 69, 69)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(tH, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 61, Short.MAX_VALUE)
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addComponent(tanggal, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(58, 58, 58))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5)
                        .addComponent(tH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel8)
                        .addComponent(tIp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel15)
                        .addComponent(barang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(tNb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(tQ, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel4))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(supplier))
                                .addGap(25, 25, 25)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(tNm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel9))
                                    .addComponent(jLabel3)))
                            .addComponent(tanggal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 160, 1120, -1));

        jPanel3.setBackground(new java.awt.Color(204, 204, 204));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Navigasi"));

        bEd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8_edit_file_20px.png"))); // NOI18N
        bEd.setText(" Edit");
        bEd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bEdActionPerformed(evt);
            }
        });

        bTm.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8_save_close_20px_5.png"))); // NOI18N
        bTm.setText(" Simpan");
        bTm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bTmActionPerformed(evt);
            }
        });

        bHp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8_delete_folder_20px_1.png"))); // NOI18N
        bHp.setText(" Hapus");
        bHp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bHpActionPerformed(evt);
            }
        });

        bRst.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8_synchronize_20px_1.png"))); // NOI18N
        bRst.setText("Reset");
        bRst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bRstActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(bTm)
                .addGap(35, 35, 35)
                .addComponent(bEd, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22)
                .addComponent(bHp)
                .addGap(33, 33, 33)
                .addComponent(bRst, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(60, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bRst)
                    .addComponent(bHp)
                    .addComponent(bEd)
                    .addComponent(bTm))
                .addContainerGap(29, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 330, 530, 100));

        jPanel4.setBackground(new java.awt.Color(204, 204, 204));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Search"));

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel10.setText("   -");

        jSc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8_search_20px_1.png"))); // NOI18N
        jSc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jScActionPerformed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel11.setText("Cari Tanggal");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(tgl_awal, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tgl_akhir, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 60, Short.MAX_VALUE)
                .addComponent(jSc, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 421, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11)
                    .addComponent(tgl_awal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tgl_akhir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSc)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, 19, Short.MAX_VALUE)
                .addContainerGap())
        );

        getContentPane().add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 320, 580, 110));
        getContentPane().add(tambah, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 130, -1, -1));
        getContentPane().add(stokk, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 90, -1, -1));

        jPanel5.setBackground(new java.awt.Color(153, 153, 153));

        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8_buy_for_cash_80px_1.png"))); // NOI18N

        jLabel13.setFont(new java.awt.Font("Californian FB", 0, 36)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Data Pembelian Barang");

        jLabel17.setFont(new java.awt.Font("Californian FB", 0, 36)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("Form");
        jLabel17.setToolTipText("");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(369, 369, 369)
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(110, 110, 110)
                        .addComponent(jLabel17))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel13)))
                .addContainerGap(295, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        getContentPane().add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1120, 150));

        getAccessibleContext().setAccessibleName(" Pembelian Barang");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bTmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bTmActionPerformed
String sql = "insert into pembelian (id_pembelian,id_supplier,id_barang,quantity,harga,tanggal_m) values (?,?,?,?,?,?)";
        try {
            PreparedStatement stat = kon.prepareStatement(sql);
            
            stat.setString(1, tIp.getText());
            stat.setString(2, supplier.getSelectedItem().toString());
             stat.setString(3, barang.getSelectedItem().toString());
            stat.setString(4, tQ.getText());
             stat.setString(5, tH.getText());
            java.util.Date utilStartDate = tanggal.getDate();
        java.sql.Date sqlStartDate = new java.sql.Date(utilStartDate.getTime());
        stat.setDate(6, sqlStartDate);
           
           
            
            stat.executeUpdate();
            kosong();
           kode_pembelian();
            barang_stock();
            datatable();
            JOptionPane.showMessageDialog(null, "Data Berhasil Disimpan");
           
        }catch (SQLException e){
            JOptionPane.showMessageDialog(null, "Data Gagal Disimpan"+e);
        }
    }//GEN-LAST:event_bTmActionPerformed

    private void bEdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bEdActionPerformed
try{    
        String sql = "update pembelian set id_supplier=?, id_barang=?, quantity=?, harga=?, tanggal_m=? where id_pembelian=?";
            PreparedStatement stat = kon.prepareStatement(sql);
            
            stat.setString(1, supplier.getSelectedItem().toString());
            
         stat.setString(2, barang.getSelectedItem().toString());
         
            stat.setString(3, tQ.getText());
             stat.setString(4, tH.getText());
             java.util.Date utilStartDate = tanggal.getDate();
        java.sql.Date sqlStartDate = new java.sql.Date(utilStartDate.getTime());
        stat.setDate(5, sqlStartDate);
      
           
            stat.setString(6, tIp.getText());
            
                        
            stat.executeUpdate();
            restok();
            kosong();    
            kode_pembelian();
            datatable();
            JOptionPane.showMessageDialog(null, "Data Berhasil Diubah");
           
        }catch (SQLException e){
            JOptionPane.showMessageDialog(null, "Data Gagal Diubah"+e);
        }
    }//GEN-LAST:event_bEdActionPerformed

    private void bHpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bHpActionPerformed
int ok = JOptionPane.showConfirmDialog(null, "Yakin untuk di hapus ?","Konfirmasi Dialog", JOptionPane.YES_NO_OPTION);
       if (ok==0){
           String sql = "delete from pembelian where id_pembelian ='"+tIp.getText()+"'";
           try {
               PreparedStatement stat = kon.prepareStatement(sql);
               stat.executeUpdate();
               JOptionPane.showMessageDialog(null, "Data berhasil dihapus");
               datatable();
               kosong();
               kode_pembelian();
           }catch (SQLException e){
               JOptionPane.showMessageDialog(null, "Data gagal dihapus"+e);
           }
       }
    }//GEN-LAST:event_bHpActionPerformed

    private void tabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabel2MouseClicked
     int bar = tabel2.getSelectedRow();
      String a = tabmode.getValueAt (bar, 0) .toString();
      String b = tabmode.getValueAt (bar, 1) .toString();
      String c = tabmode.getValueAt (bar, 2) .toString();
      String d = tabmode.getValueAt (bar, 3) .toString();
    String e = tabmode.getValueAt (bar, 4) .toString();
     try {
        java.util.Date date = new SimpleDateFormat("yyyy-MM-dd").parse((String)tabmode.getValueAt(bar, 7));
        tanggal.setDate(date);
    } catch (ParseException ex) {
        Logger.getLogger(Pembelian_barang.class.getName()).log(Level.SEVERE, null, ex);
    }
    String f = tabmode.getValueAt (bar, 5) .toString();
      String g = tabmode.getValueAt (bar, 6) .toString();
      tIp.setText(a);
      supplier.setSelectedItem(b);
      tNm.setText(c);
        barang.setSelectedItem(d);
      tNb.setText(e);
      tQ.setText(f);
      tambah.setText(f);
      tH.setText(g);
       barang_stock();
    }//GEN-LAST:event_tabel2MouseClicked

    private void supplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supplierActionPerformed
     supplier2();
    }//GEN-LAST:event_supplierActionPerformed

    private void jScActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jScActionPerformed
  cari();
    }//GEN-LAST:event_jScActionPerformed

    private void bRstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bRstActionPerformed
kosong();    
 kode_pembelian();
    }//GEN-LAST:event_bRstActionPerformed

    private void barangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_barangActionPerformed
barang2();        // TODO add your handling code here:
    }//GEN-LAST:event_barangActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bEd;
    private javax.swing.JButton bHp;
    private javax.swing.JButton bRst;
    private javax.swing.JButton bTm;
    private javax.swing.JComboBox<String> barang;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JButton jSc;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel stokk;
    private javax.swing.JComboBox<String> supplier;
    private javax.swing.JTextField tH;
    private javax.swing.JTextField tIp;
    private javax.swing.JTextField tNb;
    private javax.swing.JTextField tNm;
    private javax.swing.JTextField tQ;
    private javax.swing.JTable tabel2;
    private javax.swing.JLabel tambah;
    private com.toedter.calendar.JDateChooser tanggal;
    private com.toedter.calendar.JDateChooser tgl_akhir;
    private com.toedter.calendar.JDateChooser tgl_awal;
    // End of variables declaration//GEN-END:variables
}
