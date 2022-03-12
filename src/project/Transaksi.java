/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Toolkit;
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
import java.util.HashMap;
import javax.swing.table.DefaultTableCellRenderer;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
/**
 *
 * @author HP
 */
public final class Transaksi extends javax.swing.JInternalFrame {
private Connection kon = new Koneksi().connect();
//private DefaultTableModel tabmode;
String harga_barang;
String kode="TR-0000";
DefaultTableModel tabmode = new DefaultTableModel();
    /**
     * Creates new form Transaksi
     */

    public Transaksi() {
        initComponents();
        //datatable();
        aktif();
        kosong();
        kode_transaksi();
        if(!kode2.getText().equals("0")){jID.setText(kode2.getText());}
        pembeli();
        //barang2();
        totalnya();
        Tgl.setDate(java.sql.Date.valueOf(java.time.LocalDate.now()));
        table2.setModel(tabmode);
        tabmode.addColumn("ID");
        tabmode.addColumn("Nama Barang");
        tabmode.addColumn("Harga");
        tabmode.addColumn("Jumlah");
        tabmode.addColumn("Total Harga");
        tabmode.addColumn("");
        table2.removeColumn(table2.getColumnModel().getColumn(5));
        tampilData();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
    }
//    protected void datatable() {
//        Object[] Baris ={"id_transaksi","id_barang","nama_barang","id_pembeli","nama_pembeli","tanggal","Jumlah","Total_Harga"};
//        tabmode = new DefaultTableModel(null, Baris);
//        table2.setModel(tabmode);
//        //String sql = "select * from transaksi inner join pembeli on transaksi.id_pembeli=pembeli.id_pembeli"; 
//        String sql = "select * from transaksi inner join pembeli on transaksi.id_pembeli = pembeli.id_pembeli inner join barang on transaksi.id_barang = barang.id_barang";      
//        try {
//            java.sql.Statement stat = kon.createStatement();
//            ResultSet hasil = stat.executeQuery(sql);
//            while(hasil.next()){
//                String a = hasil.getString("id_transaksi");
//                String b = hasil.getString("id_barang");
//                String c = hasil.getString("nama_barang");
//                String d = hasil.getString("id_pembeli");
//                String e = hasil.getString("nama_pembeli");
//                String f = hasil.getString("tanggal");
//                String g = hasil.getString("jumlah_barang");
//                String h = hasil.getString("Total_Harga");
//               
//                           
//                String[] data={a,b,c,d,e,f,g,h};
//                tabmode.addRow(data);
//            }
//        } catch (Exception e){}
//        }
    public void kode_transaksi(){
      int i=0;
    try{
        Statement stat = kon.createStatement();
        //ResultSet rs = stat.executeQuery("select * from transaksi where tanggal = (SELECT MAX(tanggal) from transaksi) LIMIT 1");
        ResultSet rs = stat.executeQuery("select * from transaksi where no = (SELECT MAX(no) from transaksi where tanggal = (SELECT MAX(tanggal) from transaksi))");         
        while(rs.next()){
            kode = rs.getString("id_transaksi");
        }
//        kode = kode.substring(3);
//            i = Integer.parseInt(kode)+1;
//            String formatValue = String.format("%04d", i);
//            kode = "TR-"+String.valueOf(formatValue);
//            jID.setText(kode);
    if(kode.equals("TR-0000") && kode2.getText().equals("0")){
            //kode = kode.substring(3);
            //kode = "TR-00"+i.toString();
            //kode = "TR-"+kode;
            //kode = "TR-"+kode.substring(kode.length()-3);\
            kode = "TR-0001";
            jID.setText(kode);
        } else if (!kode.equals("TR-0000") && kode2.getText().equals("0")) {
            kode = kode.substring(3);
            i = Integer.parseInt(kode)+1;
            String formatValue = String.format("%04d", i);
            kode = "TR-"+String.valueOf(formatValue);
            jID.setText(kode);
        } else {
       jID.setText(kode2.getText());}
       
    }catch(SQLException e){
        System.out.println(e.getMessage());
    }
        
}
    
    private void totalnya(){
        String procedures = "CALL `total_harga_transaksi`()";
        
        try{
            Statement sttmnt = kon.createStatement();//membuat statement
            ResultSet rslt = sttmnt.executeQuery(procedures);//menjalanakn query\
                while(rslt.next()){
                    String a = rslt.getString("total_harga");
                    tHar.setText(a);
                }
                
        }catch(Exception e){
            System.out.println(e);
        }
        
        
    }
    
    private void tampilData(){
        //untuk mengahapus baris setelah input
        int row = table2.getRowCount();
        for(int a = 0 ; a < row ; a++){
            tabmode.removeRow(0);
        }
        
        String query = "SELECT * FROM `keranjang` ";
        String procedures = "CALL `total_harga_transaksi`()";
        
        try{
            Connection connect = kon;//memanggil koneksi
            Statement sttmnt = connect.createStatement();//membuat statement
            ResultSet rslt = sttmnt.executeQuery(query);//menjalanakn query
            
            while (rslt.next()){
                //menampung data sementara
                   
                    String no = rslt.getString("no");
                    String kode = rslt.getString("id_transaksi");
                    String nama = rslt.getString("nama_barang");
                    String harga = rslt.getString("harga");
                    String jumlah = rslt.getString("jumlah_barang");
                    String total = rslt.getString("Total_harga");
                    
                //masukan semua data kedalam array
                String[] data = {kode,nama,harga,jumlah,total,no};
                //menambahakan baris sesuai dengan data yang tersimpan diarray
                tabmode.addRow(data);
            }
                //mengeset nilai yang ditampung agar muncul di table
                table2.setModel(tabmode);
            
        }catch(Exception e){
            System.out.println(e);
        }
        totalnya();
       
    }
    
    private void kembalian(){
       if(bayaran.getText() != null || tHar.getText() != null || bayaran.getText() != "" || tHar.getText() != "") {
        String total = bayaran.getText();
        String uang = tHar.getText();
        int totals = Integer.parseInt(total);
            try{
                int uangs = Integer.parseInt(uang);     
                int kembali = (totals-uangs);
                if(kembali >= 0) {
                String fix = Integer.toString(kembali);
                kembaliannih.setText(fix);
                    try{
                        String file = "/Laporan/struk.jasper";
            
                        Class.forName("com.mysql.jdbc.Driver").newInstance();
                        HashMap param = new HashMap();
            
                        param.put("total",tHar.getText());
                        param.put("uang",bayaran.getText());
                        param.put("kembalian",kembaliannih.getText());
            
                        JasperPrint print = JasperFillManager.fillReport(getClass().getResourceAsStream(file),param,kon);
                        JasperViewer.viewReport(print, false);
            
                    }catch(ClassNotFoundException | InstantiationException | IllegalAccessException | JRException e){
                        System.out.println(e);
                    }
                    try{
                        String clear = "TRUNCATE `keranjang`";
                        //String clear = "DELETE FROM keranjang WHERE id_transaksi = '"+jID.getText()+"'";
                        PreparedStatement ps = (PreparedStatement) kon.prepareStatement(clear);
                        ps.execute();
//                      keranjang();            
                    }catch(Exception e){
                        System.out.println(e);
                    }finally{
                        tampilData();
                        kosong2();
                        totalnya();
                        this.dispose();
                    }
                JOptionPane.showMessageDialog(null, "Transaksi Berhasil!");
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid Payment(Duitnya kurang)");
                }
            }catch(NumberFormatException | HeadlessException e){
                JOptionPane.showMessageDialog(null, "Invalid Payment");
            }
       } else {
           JOptionPane.showMessageDialog(null, "Invalid Payment(Cek Total harga atau uang bayar)");
       }
    }
    
     private void hapusData(){
        //ambill data no pendaftaran
        int i = table2.getSelectedRow();
        
        String kode = tabmode.getValueAt(i, 0).toString();
        
        Connection connect = kon;
        
        String query = "DELETE FROM `keranjang` WHERE `keranjang`.`id_transaksi` = '"+kode+"' ";
        try{
            PreparedStatement ps = (PreparedStatement) connect.prepareStatement(query);
            ps.execute();
        }catch(SQLException | HeadlessException e){
            System.out.println(e);
            JOptionPane.showMessageDialog(null, "Data Gagal Dihapus");
        }finally{
            tampilData();
            //clear();
        }
        totalnya();
    }
    
//    protected void cari(){
//        java.util.Date utilStartDate = tgl_awal.getDate();
//        java.sql.Date sqlStartDate = new java.sql.Date(utilStartDate.getTime());
//        java.util.Date utilStartDate2 = tgl_akhir.getDate();
//        java.sql.Date sqlStartDate2 = new java.sql.Date(utilStartDate2.getTime());
//        jLabel9.setText(sqlStartDate.toString() +" s/d "+sqlStartDate2.toString());
//        Object[] Baris ={"id_transaksi","id_barang","nama_barang","id_pembeli","nama_pembeli","tanggal","Jumlah","Total_Harga"};
//        tabmode = new DefaultTableModel(null, Baris);
//        table2.setModel(tabmode);
//        try {
//        String sql = "select * from transaksi inner join pembeli on transaksi.id_pembeli = pembeli.id_pembeli inner join barang on transaksi.id_barang = barang.id_barang"
//                + " WHERE tanggal BETWEEN '" + sqlStartDate.toString() + "' AND '" + sqlStartDate2.toString() + "'"
//                + " ORDER BY tanggal ASC";
//            PreparedStatement pst = kon.prepareStatement(sql);
////            java.sql.Statement stat = conn.createStatement();
//            ResultSet hasil = pst.executeQuery(sql);
//            while(hasil.next()){
//                String a = hasil.getString("id_transaksi");
//                String b = hasil.getString("id_barang");
//                String c = hasil.getString("nama_barang");
//                String d = hasil.getString("id_pembeli");
//                String e = hasil.getString("nama_pembeli");
//                String f = hasil.getString("tanggal");
//                String g = hasil.getString("jumlah_barang");
//                String h = hasil.getString("Total_Harga");
//               
//                String[] data={a,b,c,d,e,f,g,h};
//                tabmode.addRow(data);
//            }
//        } catch (Exception e){
//        JOptionPane.showMessageDialog(null,e);}
//    }
//    
//    protected void barang(){
//    String sql = "select * from barang where id_barang="+barang.getSelectedItem().toString()+";";
//    try {        
//            PreparedStatement pst = kon.prepareStatement(sql);
////            java.sql.Statement stat = conn.createStatement();
//            ResultSet hasil = pst.executeQuery(sql);
//            while(hasil.next()){                
//                String a = hasil.getString("stok");
//                stuk.setText(a);
//            }
//        } catch (Exception e){
//        JOptionPane.showMessageDialog(null,e);}
//    }
    
//    protected void stok(){
//    try{    
//        String sql = "update barang set stok=? where id_barang=?";
//         PreparedStatement stat = kon.prepareStatement(sql);
//              int jum = Integer.parseInt(stuk.getText());
//              int hasil = jum-Integer.parseInt(jJum.getText());
//              String bubah = String.valueOf(hasil);
//              stat.setString(1, bubah);
//              stat.setString(2, barang.getSelectedItem().toString());                        
//            stat.executeUpdate();
//            kosong();
//            //datatable();
//            //JOptionPane.showMessageDialog(null, "Data Berhasil Diubah");
//        }catch (SQLException e){
//            //JOptionPane.showMessageDialog(null, "Data Gagal Diubah"+e);
//    }
//    }
    
// protected void restok(){
//    try{    
//        String sql = "update barang set stok=? where id_barang=?";
//         PreparedStatement stat = kon.prepareStatement(sql);
//              int tam = Integer.parseInt(tambah.getText());
//              int jum = Integer.parseInt(stuk.getText());
//              int hasil = jum+tam-Integer.parseInt(jJum.getText());
//              String bubah = String.valueOf(hasil);
//              stat.setString(1, bubah);
//              stat.setString(2, barang.getSelectedItem().toString());                        
//            stat.executeUpdate();
//            kosong();
//            //datatable();
//            //JOptionPane.showMessageDialog(null, "Data Berhasil Diubah");
//        }catch (SQLException e){
//            //JOptionPane.showMessageDialog(null, "Data Gagal Diubah"+e);
//    }
//    }
    
    private void pembeli(){
        try{
          String sql="select * from pembeli";
          PreparedStatement pst = kon.prepareStatement(sql);
          ResultSet rs=pst.executeQuery(sql);          
          while(rs.next()){
          String name =rs.getString("id_pembeli");
          pembeli.addItem(name);
          }          
        }catch(Exception e){
        JOptionPane.showMessageDialog(null,e);
        }
    }
    
    private void pembeli2(){
        //if(pembeli.getSelectedItem() != null){
        try{
          String sql="select * from pembeli where id_pembeli='"+pembeli.getSelectedItem()+"';";
          PreparedStatement pst = kon.prepareStatement(sql);
          ResultSet rs=pst.executeQuery(sql);          
          while(rs.next()){
          String name =rs.getString("nama_pembeli");
          tIp.setText(name);
        
          //if (member.equals("Member")) diskon.setText("2%");
          //else diskon.setText("0%");
          }
          //trans2();
        }catch(Exception e){
        JOptionPane.showMessageDialog(null,e);
        }
        //}
    }
    
//    private void barang2(){
//        try{
//          String sql="select * from barang";
//          PreparedStatement pst = kon.prepareStatement(sql);
//          ResultSet rs=pst.executeQuery(sql);          
//          while(rs.next()){
//          String name =rs.getString("id_barang");
//          barang.addItem(name);
//          }          
//        }catch(Exception e){
//        JOptionPane.showMessageDialog(null,e);
//        }
//    }
    
//    private void barang3(){
//        try{
//          String sql="select * from barang where id_barang="+barang.getSelectedItem()+";";
//          PreparedStatement pst = kon.prepareStatement(sql);
//          ResultSet rs=pst.executeQuery(sql);          
//          while(rs.next()){
//          String name =rs.getString("nama_barang");
//          String stok =rs.getString("stok");
//          nb.setText(name);
//          stuk.setText(stok);
//          }          
//        }catch(Exception e){
//        JOptionPane.showMessageDialog(null,e);
//        }
//    }
   
    private void keranjang(){
        String kode = jIb.getText();
        String nama = nb.getText();
        String harga = hg.getText();
        String jumlah = jJum.getText();
        String total = tHar.getText();
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        String tanggal = String.valueOf(date.format(Tgl.getDate()));
     
        //query untuk memasukan data
        String query = "INSERT INTO `transaksi` (`id_transaksi`,`tanggal`, `id_barang`, `id_pembeli`, `jumlah_barang`, `Total_Harga`) "
                + "VALUES ('"+jID.getText()+"','"+tanggal+"', '"+kode+"', '"+pembeli.getSelectedItem().toString()+"','"+jumlah+"', '"+harga+"')";
        String query2 = "SELECT SUM(Total_harga) tHar FROM `keranjang`";
        
        try{
            //menyiapkan statement untuk di eksekusi
            PreparedStatement ps = (PreparedStatement) kon.prepareStatement(query);
            PreparedStatement pst = kon.prepareStatement(query2);
            ps.executeUpdate(query);
            ResultSet rs=pst.executeQuery(query2);
            while(rs.next()){
            String thar =rs.getString("tHar");
            tHar.setText(thar);
            }
            JOptionPane.showMessageDialog(null,"Data Masuk Ke-Keranjang"); 
            
        }catch(SQLException | HeadlessException e){
            System.out.println(e);
            JOptionPane.showMessageDialog(null,"Data Gagal Disimpan");
            
        }finally{
            tampilData();
        
            
        }
        totalnya();
    }
    
    private void trans2(){
          if (!jJum.getText().equals("")) {
           String n22 = harga_barang;
          int a = Integer.parseInt(jJum.getText());
          int b = Integer.parseInt(n22);
          int c = a*b;
          hg.setText(String.valueOf(c));
        } else {
          hg.setText("");
        }
//          if (asd.equals("Member")){
//          diskon.setText("2%");
//          Double d = c*0.02;
//          Double e = c-d;
//          tHar.setText(String.valueOf(e));
//          }else if(!asd.equals("Member")){
//          diskon.setText("0%");
//          int e = c;
//          tHar.setText(String.valueOf(e));}            
    }
    
    private void checked() {
        if(!jJum.getText().equals("")){
        String stuks = stuk.getText();
        int a = Integer.parseInt(stuks);
        int b = Integer.parseInt(jJum.getText());
        if(a < b){
            JOptionPane.showMessageDialog(null,"Out of stock");
            jJum.setText(String.valueOf(a));
        }
        }
        
    }
    
    public void aktif() {
        jID.setEnabled(true);
        jIb.setEnabled(true);
        tIp.setEnabled(true);
        Tgl.setEnabled(true);
        
        
    }
    
    public void kosong() {
        //jID.setText("");
        jIb.setText("");
        tIp.setText("");
        jJum.setText("");
        stock.setText("");
        Tgl.setCalendar(null);
        
        pembeli.setSelectedItem(null);
        mm.setText("");
        tHar.setText("");
       hg.setText("");
       nb.setText("");
        tambah.setText("");
        bayaran.setText("");
        kembaliannih.setText("");
        
    }
    
    public void kosong2() {
        //jID.setText("");
        jIb.setText("");
        jJum.setText("");
        stock.setText("");
        stuk.setText("");
       
       hg.setText("");
       nb.setText("");
        tambah.setText("");
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        stock = new javax.swing.JTextField();
        mm = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        tambah = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        table2 = new javax.swing.JTable();
        jLabel8 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jID = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jIb = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        nb = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jJum = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        stuk = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        hg = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        tIp = new javax.swing.JTextField();
        pembeli = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        bayaran = new javax.swing.JTextField();
        kembaliannih = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        harga = new javax.swing.JLabel();
        tHar = new javax.swing.JLabel();
        Tgl = new com.toedter.calendar.JDateChooser();
        jLabel4 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        bRt = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        kode2 = new javax.swing.JTextField();
        no = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();

        setBackground(new java.awt.Color(153, 153, 153));
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Transaksi\n");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8_transaction_list_15px.png"))); // NOI18N
        setPreferredSize(new java.awt.Dimension(1207, 807));
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameClosing(evt);
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
            }
        });

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Tabel"));

        table2.setModel(new javax.swing.table.DefaultTableModel(
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
        table2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                table2MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(table2);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(tambah, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(62, 62, 62)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 897, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 84, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addComponent(tambah, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jLabel8.setFont(new java.awt.Font("Californian FB", 0, 36)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(51, 255, 51));
        jLabel8.setText("Form");

        jPanel2.setBackground(new java.awt.Color(204, 204, 204));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Input"));

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setText("Id_Transaksi");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setText("Barang");

        jIb.setEditable(false);

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8_checked_20px_1.png"))); // NOI18N
        jButton4.setText("Pilih");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        nb.setEditable(false);

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel10.setText("Jumlah Barang");

        jJum.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jJumKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jJumKeyReleased(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel11.setText("Stok");

        jLabel13.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel13.setText("Harga");

        hg.setEditable(false);

        jButton1.setText("Add");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel6.setText("Pembeli ");

        pembeli.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " " }));
        pembeli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pembeliActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel7.setText("Nama Barang");

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel9.setText("Nama Pembeli");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(jLabel2)
                            .addGap(31, 31, 31))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                            .addComponent(jLabel10)
                            .addGap(18, 18, 18)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(nb, javax.swing.GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE)
                    .addComponent(jID)
                    .addComponent(jIb)
                    .addComponent(jJum, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(154, 154, 154)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addGap(18, 18, 18))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addGap(45, 45, 45)))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(pembeli, 0, 157, Short.MAX_VALUE)
                            .addComponent(tIp)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jButton4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel13)
                                .addGap(54, 54, 54)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(stuk, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(hg, javax.swing.GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE))))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(255, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(247, 247, 247))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel2)
                                .addComponent(jID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel3)
                                .addComponent(jIb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButton4))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel13)
                                .addComponent(hg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(stuk, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(nb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel7))
                    .addComponent(pembeli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tIp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jJum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel10)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addComponent(jButton1)
                .addContainerGap())
        );

        jPanel3.setBackground(new java.awt.Color(204, 204, 204));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Transaksi"));

        jLabel5.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel5.setText("Bayar");

        jLabel12.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel12.setText("Kembalian");

        jButton2.setText("Payment");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        harga.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        harga.setText("Total Harga");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(81, 81, 81))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12)
                    .addComponent(harga))
                .addGap(28, 28, 28)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(bayaran)
                        .addContainerGap())
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tHar, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(kembaliannih, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 19, Short.MAX_VALUE))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tHar, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(harga))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(bayaran, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(kembaliannih, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22))
        );

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel4.setText("Tanggal");

        jPanel4.setBackground(new java.awt.Color(204, 204, 204));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Navigasi"));

        bRt.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8_synchronize_20px_1.png"))); // NOI18N
        bRt.setText("Reset");
        bRt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bRtActionPerformed(evt);
            }
        });

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8_delete_folder_20px_1.png"))); // NOI18N
        jButton3.setText("Delete");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap(113, Short.MAX_VALUE)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(57, 57, 57)
                .addComponent(bRt, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(112, 112, 112))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bRt))
                .addGap(0, 17, Short.MAX_VALUE))
        );

        kode2.setEditable(false);
        kode2.setText("0");
        kode2.setPreferredSize(new java.awt.Dimension(0, 0));

        no.setEditable(false);
        no.setPreferredSize(new java.awt.Dimension(0, 0));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8_transaction_approved_80px_3.png"))); // NOI18N

        jLabel15.setFont(new java.awt.Font("Californian FB", 0, 36)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(51, 255, 51));
        jLabel15.setText("Daftar Transaksi");

        jButton5.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8_back_arrow_20px_1.png"))); // NOI18N
        jButton5.setText("Back");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 72, Short.MAX_VALUE)
                        .addComponent(stock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(95, 95, 95)
                                .addComponent(kode2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(820, 820, 820)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(no, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel4))
                                .addGap(42, 42, 42)
                                .addComponent(Tgl, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(389, 389, 389)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jLabel15))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(79, 79, 79)
                                .addComponent(jLabel8)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(138, 138, 138)))
                .addGap(6, 6, 6))
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39))
            .addGroup(layout.createSequentialGroup()
                .addGap(327, 327, 327)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(kode2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(no, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel15))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(jButton5)
                        .addGap(75, 75, 75)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(Tgl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(stock, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void table2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_table2MouseClicked
     int bar = table2.getSelectedRow();
      String a = tabmode.getValueAt (bar, 0) .toString();
      String b = tabmode.getValueAt (bar, 1) .toString();
      String c = tabmode.getValueAt (bar, 2) .toString();
      String d = tabmode.getValueAt (bar, 3) .toString();
       String f = tabmode.getValueAt (bar, 4) .toString();
       String g = table2.getModel().getValueAt(bar,5) .toString();
//       try {
//        java.util.Date date = new SimpleDateFormat("yyyy-MM-dd").parse((String)tabmode.getValueAt(bar, 5));
//        Tgl.setDate(date);
//    } catch (ParseException ex) {
//        Logger.getLogger(Transaksi.class.getName()).log(Level.SEVERE, null, ex);
//    }
      //String g = tabmode.getValueAt (bar, 6) .toString();
      //String h = tabmode.getValueAt (bar, 7) .toString();
   
      jID.setText(a);
      jIb.setText(b);
      nb.setText(c);
      pembeli.setSelectedItem(d);
      tIp.setText(f);
      //jJum.setText(g);
      no.setText(g);
      //tHar.setText(h);
      trans2();
      
      //barang();
    }//GEN-LAST:event_table2MouseClicked

    private void bRtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bRtActionPerformed
      try{
            //String clear = "TRUNCATE `keranjang`";
            String clear = "DELETE FROM keranjang WHERE id_transaksi = '"+jID.getText()+"'";
            String clear2 = "DELETE FROM transaksi WHERE id_transaksi = '"+jID.getText()+"'";
            PreparedStatement ps = (PreparedStatement) kon.prepareStatement(clear);
            ps.execute();
            PreparedStatement ps2 = (PreparedStatement) kon.prepareStatement(clear2);
            ps2.execute();
//            keranjang();
            
            
        }catch(Exception e){
            System.out.println(e);
        }finally{
            tampilData();
            kosong2();
            totalnya();
        }
    }//GEN-LAST:event_bRtActionPerformed

    private void pembeliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pembeliActionPerformed
        pembeli2();
    }//GEN-LAST:event_pembeliActionPerformed

    private void jJumKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jJumKeyReleased
        trans2();
       checked();
    }//GEN-LAST:event_jJumKeyReleased

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (jID.getText().equals("") || jIb.getText().equals("") || tIp.getText().equals("") ||
        jJum.getText().equals("") || stuk.getText().equals("") || Tgl.getCalendar().equals(null) ||
        pembeli.getSelectedItem().toString().equals(null) || hg.getText().equals("") || nb.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Silahkan Isi Semua Field Terlebih Dahulu");
        } else {
            keranjang();
            kosong2();
        }
//kode_transaksi();        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        kembalian();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        try{
            //String clear = "TRUNCATE `keranjang`";
            String clear = "DELETE FROM keranjang WHERE no = '"+no.getText()+"'";
            PreparedStatement ps = (PreparedStatement) kon.prepareStatement(clear);
            ps.execute();
//            keranjang();
            
            
        }catch(Exception e){
            System.out.println(e);
        }finally{
            tampilData();
            kosong2();
            totalnya();
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        
        belanja bela = new belanja();
        bela.jT.setText(jID.getText());
        if(tIp.getText() != null && pembeli.getSelectedItem() != null){
        bela.np.setText(tIp.getText());
        bela.idp.setText(pembeli.getSelectedItem().toString());}
        //menu.add(bela);
        this.getParent().add(bela);
        bela.toFront();
        bela.setVisible(true);
//        this.setVisible(false);
        dispose();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
       try{
            //String clear = "TRUNCATE `keranjang`";
            String clear = "DELETE FROM keranjang WHERE id_transaksi = '"+jID.getText()+"'";
            PreparedStatement ps = (PreparedStatement) kon.prepareStatement(clear);
            ps.execute();
//            keranjang();
            
            
        }catch(Exception e){
            System.out.println(e);
        }finally{
            tampilData();
            kosong2();
            totalnya();
        }
    }//GEN-LAST:event_formInternalFrameClosing

    private void jJumKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jJumKeyPressed
        
    }//GEN-LAST:event_jJumKeyPressed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_jButton5ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.toedter.calendar.JDateChooser Tgl;
    private javax.swing.JButton bRt;
    private javax.swing.JTextField bayaran;
    private javax.swing.JLabel harga;
    public javax.swing.JTextField hg;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    public javax.swing.JTextField jID;
    public javax.swing.JTextField jIb;
    private javax.swing.JTextField jJum;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField kembaliannih;
    public javax.swing.JTextField kode2;
    private javax.swing.JLabel mm;
    public javax.swing.JTextField nb;
    private javax.swing.JTextField no;
    public javax.swing.JComboBox<String> pembeli;
    private javax.swing.JTextField stock;
    public javax.swing.JLabel stuk;
    private javax.swing.JLabel tHar;
    public javax.swing.JTextField tIp;
    private javax.swing.JTable table2;
    private javax.swing.JTextField tambah;
    // End of variables declaration//GEN-END:variables
}
