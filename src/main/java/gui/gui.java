package gui;

import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;

import economie_exec.*;
import com.toedter.calendar.JCalendar;

public class gui extends JFrame{
    private JPanel panel1, panel2, panel1_1, panel1_2, panel1_2_1, panel1_2_2, stv;
    private JButton add_btn, del_btn;
    private JTable all_table, sel_table;
    private exec Exec;
    private Vector<cb> all_list, sel_list;
    private JCalendar calendar;
    private JTabbedPane tabbedPane1;
    private JTextArea nv;
    private final ImageIcon icon0 = new ImageIcon(getClass().getResource("/add.png"));
    private final ImageIcon icon1 = new ImageIcon(getClass().getResource("/del.png"));
    public gui() {
        //create_db();
        initComponents();
    }
    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Economie");
        panel1 = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel1_1 = new JPanel(new BorderLayout());
        calendar = new JCalendar();
        calendar.setPreferredSize(new Dimension(400, 300));
        calendar.setMinimumSize(new Dimension(400, 300)); // Set minimum size
        calendar.setMaximumSize(new Dimension(400, 300));
        calendar.addPropertyChangeListener("calendar", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                Date day_ = calendar.getDate();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                String day = format.format(day_);
                load_sel_table(day);
            }
        });
        panel1_1.add(calendar, BorderLayout.CENTER);
        panel1.add(panel1_1, gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        //
        panel1_2_1 = new JPanel(new GridBagLayout());
        del_btn = new JButton(icon1);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String day = format.format(new Date());
        load_sel_table(day);

        panel1_2_2 = new JPanel(new BorderLayout());

        // 임시
        JCalendar calendar2 = new JCalendar();
        calendar2.setPreferredSize(new Dimension(400, 300));
        calendar2.setMinimumSize(new Dimension(400, 300)); // Set minimum size
        calendar2.setMaximumSize(new Dimension(400, 300));
        panel1_2_2.add(calendar2);
        //

        JTabbedPane tabbedPane1 = new JTabbedPane();
        tabbedPane1.addTab("日別の内訳", panel1_2_1);
        tabbedPane1.addTab("内訳入力", panel1_2_2);


        JPanel panel1_2 = new JPanel(new BorderLayout());
        panel1_2.add(tabbedPane1, BorderLayout.CENTER);
        //
        panel1.add(panel1_2, gbc);
        panel2 = new JPanel(new BorderLayout());
        load_all_table();
        setLayout(new GridLayout(2, 1));
        add(panel1);
        add(panel2);
        setSize(800, 640);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    private void load_all_table(){
        Exec = new exec();
        all_list = Exec.load_all();
        String[] columns = {"日付", "入/出", "内容", "金額", "残額"};
        int l = all_list.size();
        int t = 0;
        Object[][] data = new Object[l][columns.length];
         for (int i = 0; i < l; i++) {
            data[l-1-i][0] = all_list.get(i).day;
            data[l-1-i][1] = all_list.get(i).nsf;
            data[l-1-i][2] = all_list.get(i).naiyou;
            data[l-1-i][3] = all_list.get(i).atai + " ¥";
            t += all_list.get(i).atai;
            data[l-1-i][4] = t + " ¥";
        }
        DefaultTableModel tuple = new DefaultTableModel(data, columns);
        all_table = new JTable(tuple);
        all_table.getColumn("日付").setPreferredWidth(140);
        all_table.getColumn("入/出").setPreferredWidth(60);
        all_table.getColumn("内容").setPreferredWidth(300);
        all_table.getColumn("金額").setPreferredWidth(150);
        all_table.getColumn("残額").setPreferredWidth(150);
        panel2.add(new JScrollPane(all_table), BorderLayout.CENTER);
    }
    private void load_sel_table(String day){
        if (stv != null){
            stv.removeAll();
        } else {
            stv = new JPanel(new GridBagLayout());
        }
        Exec = new exec();
        sel_list = Exec.load_sel(day);
        String[] columns = {"日付", "入/出", "金額"};
        int l = sel_list.size();
        Object[][] data = new Object[l][columns.length];
        for (int i = 0; i < sel_list.size(); i++) {
            data[l-1-i][0] = sel_list.get(i).day;
            data[l-1-i][1] = sel_list.get(i).nsf;
            data[l-1-i][2] = sel_list.get(i).atai + " ¥";
        }
        DefaultTableModel tuple = new DefaultTableModel(data, columns);
        sel_table = new JTable(tuple);
        sel_table.getColumn("日付").setPreferredWidth(60);
        sel_table.getColumn("入/出").setPreferredWidth(60);
        sel_table.getColumn("金額").setPreferredWidth(60);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        stv.add(new JScrollPane(sel_table), gbc);
        gbc.gridx = 4;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        // del_btn
        stv.add(del_btn, gbc);
        nv = new JTextArea(5, 20);
        for (int i = 0; i < l; i++){
            if ((i+1)==l) {
                nv.append(sel_list.get(l - 1 - i).naiyou);
            } else {
                nv.append(sel_list.get(l - 1 - i).naiyou + "\n");
            }

        }
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 5;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        stv.add(new JScrollPane(nv), gbc);
        stv.revalidate();
        panel1_2_1.add(stv, gbc);
        panel1_2_1.revalidate();
    }
}