package economie_exec;

import java.sql.*;
import java.util.Vector;

public class exec {
    private Vector<cb> all_list;
    private Vector<cb> sel_list;
    private Connection conn = null;
    private ResultSet rs = null;
    public Vector<cb> load_all(){
        all_list = new Vector<cb>();
        PreparedStatement pstmt = null;
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:book");
            String sql = "SELECT day, nsf, naiyou, atai FROM chobo ORDER BY day ASC;";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                cb t = new cb();
                t.day = rs.getString("day");
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
    public Vector<cb> load_sel(String day){
        sel_list = new Vector<cb>();
        PreparedStatement pstmt = null;
        try{
            conn = DriverManager.getConnection("jdbc:sqlite:book");
            String sql = "SELECT day, nsf, naiyou, atai FROM chobo WHERE day=?;";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, day);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                cb t = new cb();
                t.day = rs.getString("day");
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
            pstmt.setString(1, in.day);
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
    public void create_book(){
        PreparedStatement pstmt = null;
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:book");
            String sql = "CREATE TABLE chobo (" +
                    "day CHAR(20), " +
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