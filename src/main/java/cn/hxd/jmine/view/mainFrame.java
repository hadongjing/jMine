/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * mainFrame.java
 *
 * Created on 2009-12-11, 11:12:24
 */
package cn.hxd.jmine.view;

import cn.hxd.jmine.bean.Mine;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashSet;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author jaux
 */
public class mainFrame extends javax.swing.JFrame {
    private int level;

    /** Creates new form mainFrame */
    public mainFrame() {
        flagIcon = new ImageIcon(getClass().getResource("/cn/hxd/jmine/resources/flag.gif"));
        items = new String[]{"初级","中级","高级"};
        comboBoxModel = new DefaultComboBoxModel(items);
        mines = new ArrayList<Mine>();
        bombs = new HashSet<Integer>();
        level = 9;
        while (bombs.size() < 10) {
            bombs.add((int) (Math.random() * 81));
        }
//    System.out.println((int)(Math.random()*81));
        initComponents();

    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("扫雷");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setResizable(false);

        jPanel2.setPreferredSize(new java.awt.Dimension(270, 30));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jComboBox1.setModel(comboBoxModel);
        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });
        jPanel2.add(jComboBox1, java.awt.BorderLayout.WEST);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("10");
        jLabel2.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jLabel2.setPreferredSize(new java.awt.Dimension(40, 15));
        jPanel2.add(jLabel2, java.awt.BorderLayout.EAST);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cn/hxd/jmine/resources/fresh.png"))); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton1, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        //jPanel1.setSize(270, 270);
        jPanel1.setPreferredSize(new java.awt.Dimension(270, 270));
        jPanel1.setLayout(new java.awt.GridLayout(9, 9));

        for(int i = 0;i<9;i++)
        {
            for(int j=0;j<9;j++)
            {
                Mine mine=new Mine();
                mine.setMargin(new Insets(0, 0, 0, 0));
                if(bombs.contains(i*9+j))
                {
                    mine.setBomb(true);
                    System.out.println(i+1+","+(j+1));
                }
                mine.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent e) {
                        mineClicked(e);
                    }
                });
                mine.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseReleased(java.awt.event.MouseEvent evt) {
                        rightMouseClicked(evt);
                    }
                });
                mines.add(mine);
                jPanel1.add(mine);
            }
        }

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_START);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        // TODO add your handling code here:
        if(comboBoxModel.getSelectedItem().equals("初级")){
            level=9;
            changeJPanel(9);

        }else if(comboBoxModel.getSelectedItem().equals("中级")){
            level=16;
            changeJPanel(16);

        }else{
level=25;
            changeJPanel(25);

        }
    }//GEN-LAST:event_jComboBox1ItemStateChanged

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        changeJPanel(level);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void mineClicked(java.awt.event.ActionEvent e) {
        // TODO add your handling code here:
        Mine mine = (Mine) e.getSource();
        int i = mine.doPress(mines,level);
        if (i == 0) {
//            new Applet().getAudioClip(getClass().getResource("/cn/hxd/jmine/resources/bomb.mid")).loop();
            JOptionPane.showMessageDialog(null,"你输了！");
            
        }
        else if (win()) {
            JOptionPane.showMessageDialog(null,"你赢了！");
        }

    }

    private void rightMouseClicked(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
        Mine mine = (Mine) evt.getSource();
        if (evt.getButton() == MouseEvent.BUTTON3 && (mine.isEnabled() || mine.getIcon() == flagIcon)) {
            if (mine.getIcon() == null) {
                mine.setDisabledIcon(flagIcon);
                mine.setEnabled(false);
                mine.setIcon(flagIcon);
                jLabel2.setText(Integer.parseInt(jLabel2.getText())-1+"");
                if (mine.isBomb()) {
                    flag++;
                }
                if (win()) {
                    JOptionPane.showMessageDialog(null,"你赢了！");
                }
                System.out.println(flag);
            } else {
                mine.setIcon(null);
                mine.setEnabled(true);
                jLabel2.setText(Integer.parseInt(jLabel2.getText())+1+"");
                flag--;
            }
        }

    }

    private void changeJPanel(int level){

         getContentPane().removeAll();
         int size=0;
         switch(level){
             
             case 16:size=40;break;
             case 25:size=120;break;
             default:size=10;
         }
         jLabel2.setText(size+"");
        jPanel2.setPreferredSize(new java.awt.Dimension(level*30, 30));
        jPanel1.removeAll();
        bombs.clear();
        mines.clear();
        jPanel1.setPreferredSize(new java.awt.Dimension(level*30, level*30));
        jPanel1.setLayout(new java.awt.GridLayout(level, level));

        while (bombs.size() < size) {
            bombs.add((int) (Math.random() * level*level));
        }
//        System.out.println(level);
//        System.out.println(bombs.size());
        for(int i = 0;i<level;i++)
        {
            for(int j=0;j<level;j++)
            {
                Mine mine=new Mine();
                mine.setMargin(new Insets(0, 0, 0, 0));
                if(bombs.contains(i*level+j))
                {
                    mine.setBomb(true);
                    System.out.println(i+1+","+(j+1));
                }
                mine.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent e) {
                        mineClicked(e);
                    }
                });
                mine.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        rightMouseClicked(evt);
                    }
                });
                mines.add(mine);
                jPanel1.add(mine);
            }
        }
//        System.out.println(mines.size());
        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);
        getContentPane().add(jPanel1, java.awt.BorderLayout.NORTH);

        pack();
    }

    private boolean win() {
        int o = 0;
        for (Mine mine : mines) {
           /* if (mine.isBomb() && mine.getIcon() == flagIcon) {
                o++;
            }
            if (!mine.isBomb() && !mine.isEnabled()) {
                o++;
            }*/
            if(!mine.isEnabled())
                o++;
        }
        if (o == level*level) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new mainFrame().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    // End of variables declaration//GEN-END:variables
//    private Mine[][] mines ;
    private ArrayList<Mine> mines;
    private HashSet<Integer> bombs;
    private Icon flagIcon;
    private int flag = 0;
    private String[] items;
    private ComboBoxModel comboBoxModel;
}
