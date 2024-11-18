package gui;

import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import economie_exec.*;
import com.toedter.calendar.JCalendar;

public class gui extends JFrame{
    private JPanel panel1, panel2, panel1_1, panel1_2, panel1_2_1, panel1_2_2, stv, sagasu_bar, panel2_;
    private JButton add_btn, modify_btn, del_btn, in_btn, out_btn;
    private JTable all_table, sel_table;
    private exec Exec;
    private Vector<cb> all_list, sel_list;
    private JCalendar calendar;
    private JTabbedPane tabbedPane;
    private JTextArea nv;
    private JSpinner kr, md;
    private final ImageIcon icon0 = new ImageIcon(getClass().getResource("/add.png"));
    private final ImageIcon icon1 = new ImageIcon(getClass().getResource("/del.png"));
    private final ImageIcon icon2 = new ImageIcon(getClass().getResource("/import.png"));
    private final ImageIcon icon3 = new ImageIcon(getClass().getResource("/out.png"));
    private final ImageIcon icon4 = new ImageIcon(getClass().getResource("/modify.png"));
    public gui() {
        create_db();
        initComponents();
    }
    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Economie");
        setIconImage(new ImageIcon(getClass().getResource("/icon.png")).getImage());
        panel1 = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel1_1 = new JPanel(new BorderLayout());
        calendar = new JCalendar();
        calendar.setPreferredSize(new Dimension(400, 310));
        calendar.setMinimumSize(new Dimension(400, 310));
        calendar.setMaximumSize(new Dimension(400, 310));
        calendar.addPropertyChangeListener("calendar", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                Date day_ = calendar.getDate();
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
                load_sel_table(format.format(day_));
            }
        });
        panel1_1.add(calendar, BorderLayout.CENTER);
        panel1.add(panel1_1, gbc);
        panel1_2_1 = new JPanel(new GridBagLayout());
        del_btn = new JButton(icon1);
        del_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                del_day();
            }
        });
        modify_btn = new JButton(icon4);
        modify_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modify();
            }
        });
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String day = format.format(new Date());
        load_sel_table(day);
        panel1_2_2 = new JPanel(new GridBagLayout());
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        panel1_2_2.add(new JLabel("日付:"), gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        JSpinner nd = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor de = new JSpinner.DateEditor(nd, "yyyy.MM.dd");
        nd.setEditor(de);
        Dimension w = nd.getPreferredSize();
        w.width = 162;
        nd.setPreferredSize(w);
        panel1_2_2.add(nd, gbc);
        gbc.gridx = 2;
        gbc.anchor = GridBagConstraints.WEST;
        add_btn = new JButton(icon0);
        panel1_2_2.add(add_btn, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        panel1_2_2.add(new JLabel("内容:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        JTextField nn = new JTextField(20);
        panel1_2_2.add(nn, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        panel1_2_2.add(new JLabel("金額:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        JPanel na = new JPanel(new BorderLayout());
        JSpinner as = new JSpinner(new SpinnerNumberModel(0, Integer.MIN_VALUE, Integer.MAX_VALUE, 1));
        w.width = 215;
        as.setPreferredSize(w);
        na.add(as, BorderLayout.CENTER);
        na.add(new JLabel("¥"), BorderLayout.EAST);
        panel1_2_2.add(na, gbc);
        add_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
                String day = format.format(nd.getValue());
                cb tuple = new cb();
                tuple.day = Integer.parseInt(day);
                tuple.naiyou = nn.getText();
                tuple.atai = (int) as.getValue();
                if (tuple.atai < 0){
                    tuple.nsf = "支出";
                } else {
                    tuple.nsf = "収入";
                }
                Exec = new exec();
                Exec.insert_day(tuple);
                load_all_table(0, 99999999);
                Date day_ = calendar.getDate();
                load_sel_table(format.format(day_));
            }
        });
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("日別の内訳", panel1_2_1);
        tabbedPane.addTab("内訳入力", panel1_2_2);
        tabbedPane.setPreferredSize(new Dimension(400, 310));
        tabbedPane.setMinimumSize(new Dimension(400, 310));
        tabbedPane.setMaximumSize(new Dimension(400, 310));
        panel1_2 = new JPanel(new BorderLayout());
        panel1_2.add(tabbedPane, BorderLayout.CENTER);
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel1.add(panel1_2, gbc);
        panel2 = new JPanel(new BorderLayout());
        load_all_table(0, 99999999);
        sagasu_bar = new JPanel(new GridLayout());
        gbc.gridx = 0;
        gbc.gridy = 0;
        Calendar dd = Calendar.getInstance();
        dd.set(2000, Calendar.JANUARY, 1, 0, 0, 0);
        Date dd_ = dd.getTime();
        SpinnerDateModel dd__ = new SpinnerDateModel(dd_, null, null, Calendar.DAY_OF_MONTH);
        kr = new JSpinner(dd__);
        JSpinner.DateEditor de1 = new JSpinner.DateEditor(kr, "yyyy.MM.dd");
        kr.setEditor(de1);
        sagasu_bar.add(kr, gbc);
        gbc.gridx = 1;
        md = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor de2 = new JSpinner.DateEditor(md, "yyyy.MM.dd");
        md.setEditor(de2);
        sagasu_bar.add(md, gbc);
        kr.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                Date kr_ = (Date)kr.getValue();
                Date md_ = (Date)md.getValue();
                if (kr_.after(md_))
                    md.setValue(kr_);
                sagasu();
            }
        });
        md.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                Date kr_ = (Date)kr.getValue();
                Date md_ = (Date)md.getValue();
                if (md_.before(kr_))
                    kr.setValue(md_);
                sagasu();
            }
        });
        gbc.gridx = 2;
        sagasu_bar.add(new JPanel(), gbc);
        gbc.gridx = 3;
        in_btn = new JButton(icon2);
        in_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                import_csv();
            }
        });
        sagasu_bar.add(in_btn, gbc);
        gbc.gridx = 4;
        out_btn = new JButton(icon3);
        out_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                out_csv();
            }
        });
        sagasu_bar.add(out_btn, gbc);
        setLayout(new GridLayout(2, 1));
        add(panel1);
        panel2_ = new JPanel(new BorderLayout());
        panel2_.add(sagasu_bar, BorderLayout.NORTH);
        panel2_.add(panel2, BorderLayout.CENTER);
        add(panel2_);
        setSize(830, 660);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    private void load_all_table(int kara, int made){
        Exec = new exec();
        all_list = Exec.sagasu(kara, made);
        Vector<cb> tmp = Exec.sagasu(0, kara-1);
        String[] columns = {"日付", "入/出", "内容", "金額", "残額"};
        int l = all_list.size();
        int t = 0;
        for (int i = 0; i < tmp.size(); i++)
            t += tmp.get(i).atai;
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
        sel_list = Exec.load_sel(Integer.parseInt(day));
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
        JPanel t_ = new JPanel(new BorderLayout());
        t_.add(modify_btn, BorderLayout.NORTH);
        t_.add(new JPanel(), BorderLayout.CENTER);
        t_.add(del_btn, BorderLayout.SOUTH);
        stv.add(t_, gbc);
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
    private void modify(){
        int l = sel_table.getRowCount();
        if (l > 0){
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
            String day = format.format(calendar.getDate());
            Vector<cb> in = new Vector<cb>();
            String[] line = nv.getText().split("\n");
            for(int i = 0; i < l; i++){
                cb t_ = new cb();
                t_.day = Integer.parseInt(day);
                String t = (String)sel_table.getValueAt(i, 2);
                if (!t.endsWith("¥")){
                    return;
                }
                t_.atai = Integer.parseInt(t.substring(0, t.length() - 2));
                t_.naiyou = line[i];
                if (t_.atai < 0){
                    t_.nsf = "支出";
                } else {
                    t_.nsf = "収入";
                }
                in.add(t_);
            }
            Exec.del_day(day);
            Exec = new exec();
            for (int i = 0; i < l; i++)
                Exec.insert_day(in.get(l-1-i));
            sagasu();
            load_sel_table(day);
            panel2.revalidate();
        }
    }
    private void del_day(){
        if (sel_table.getRowCount() == 0){
            return;
        }
        int t = JOptionPane.showConfirmDialog(this, "本当に消してもいいですか？", "警告", JOptionPane.YES_NO_OPTION);
        if (t == JOptionPane.YES_OPTION) {
            Date day_ = calendar.getDate();
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
            String day = format.format(day_);
            Exec = new exec();
            Exec.del_day(day);
            load_all_table(0, 99999999);
            load_sel_table(day);
        }
    }
    private void sagasu(){
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        load_all_table(Integer.parseInt(format.format((Date)kr.getValue())), Integer.parseInt(format.format((Date)md.getValue())));
        panel2.revalidate();
    }
    private void create_db(){
        File file = new File("book");
        if (!file.exists()) {
            Exec = new exec();
            Exec.create_book();
        }
    }
    private void import_csv(){
        int q = JOptionPane.showConfirmDialog(this, "既存の内訳を削除しますか？", "お知らせ", JOptionPane.YES_NO_OPTION);
        JFileChooser fd = new JFileChooser();
        int t = fd.showOpenDialog(null);
        if (t == JFileChooser.APPROVE_OPTION) {
            File file = fd.getSelectedFile();
            if (q == JOptionPane.YES_OPTION) {
                File f = new File("book");
                if (f.exists())
                    f.delete();
                create_db();
            }
            Exec = new exec();
            Exec.import_(file.getAbsolutePath());
            load_all_table(0, 99999999);
            panel2.revalidate();
        }
    }
    private void out_csv(){
        JFileChooser fd = new JFileChooser();
        int t = fd.showSaveDialog(null);
        if (t == JFileChooser.APPROVE_OPTION) {
            File file = fd.getSelectedFile();
            Exec = new exec();
            if (!file.getName().endsWith(".csv"))
                Exec.out(file.getAbsolutePath() + ".csv");
            else
                Exec.out(file.getAbsolutePath());
        }
    }
}