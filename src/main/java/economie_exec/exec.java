package economie_exec;

import java.io.*;
import java.sql.*;
import java.util.Scanner;
import java.util.Vector;

public class exec {
    private Vector<cb> all_list;
    private Vector<cb> sel_list;
    private Connection conn = null;
    private ResultSet rs = null;
    public Vector<cb> sagasu(int kara, int made){
        all_list = new Vector<cb>();
        PreparedStatement pstmt = null;
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:book");
            String sql = "SELECT day, nsf, naiyou, atai FROM chobo WHERE day >= ? AND day <= ? ORDER BY day ASC;";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, String.valueOf(kara));
            pstmt.setString(2, String.valueOf(made));
            rs = pstmt.executeQuery();
            while (rs.next()) {
                cb t = new cb();
                t.day = rs.getInt("day");
                t.nsf = rs.getString("nsf");
                t.naiyou = rs.getString("naiyou");
                t.atai = rs.getInt("atai");
                all_list.add(t);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }  finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return all_list;
    }
    public Vector<cb> load_sel(int day){
        sel_list = new Vector<cb>();
        PreparedStatement pstmt = null;
        try{
            conn = DriverManager.getConnection("jdbc:sqlite:book");
            String sql = "SELECT day, nsf, naiyou, atai FROM chobo WHERE day=?;";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, String.valueOf(day));
            rs = pstmt.executeQuery();
            while (rs.next()) {
                cb t = new cb();
                t.day = rs.getInt("day");
                t.nsf = rs.getString("nsf");
                t.naiyou = rs.getString("naiyou");
                t.atai = rs.getInt("atai");
                sel_list.add(t);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }  finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return sel_list;
    }
    public void insert_day(cb in){
        PreparedStatement pstmt = null;
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:book");
            String sql = "INSERT INTO chobo VALUES (?, ?, ?, ?);";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, String.valueOf(in.day));
            pstmt.setString(2, in.nsf);
            pstmt.setString(3, in.naiyou);
            pstmt.setInt(4, in.atai);
            pstmt.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }  finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public void del_day(String day){
        PreparedStatement pstmt = null;
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:book");
            String sql = "DELETE FROM chobo WHERE day=?;";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, day);
            pstmt.executeUpdate();
            pstmt.close();
            String sql_ = "VACUUM;";
            pstmt = conn.prepareStatement(sql_);
            pstmt.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }  finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public void out(final String filename){
        Vector<String> csv_file = new Vector<String>();
        Vector<cb> tmp = sagasu(0, 99999999);
        int size = tmp.size();
        int zangaku = 0;
        for (int i = 0; i < size; i++){
            zangaku += tmp.get(i).atai;
            csv_file.add(tmp.get(i).day + ";" + tmp.get(i).naiyou + ";" + tmp.get(i).atai + ";" + zangaku + '\n');
        }
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(filename, false));
            for (int i = 0; i < size; i++) {
                writer.write(csv_file.get(i));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void import_(final String filename){
        Vector<String> csv_file = new Vector<String>();
        Scanner scanner = null;
        try {
            File file = new File(filename);
            scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                csv_file.add(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
        int size = csv_file.size();
        for(int i = 0; i < size; i++){
            String[] columns = csv_file.get(i).split(";");
            cb t_ = new cb();
            t_.day = Integer.parseInt(columns[0]);
            t_.naiyou = columns[1];
            t_.atai = Integer.parseInt(columns[2]);
            if (t_.atai < 0){
                t_.nsf = "支出";
            } else {
                t_.nsf = "収入";
            }
            insert_day(t_);
        }
    }
    public void create_book(){
        PreparedStatement pstmt = null;
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:book");
            String sql = "CREATE TABLE chobo (" +
                    "day INT, " +
                    "nsf CHAR(20), " +
                    "naiyou CHAR(80), " +
                    "atai INT" +
                    ");";
            pstmt = conn.prepareStatement(sql);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}