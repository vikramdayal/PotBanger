/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package potbanger.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import potbanger.play.Bar;

/**
 *
 * @author vdayal
 */
public class BarScreen extends javax.swing.JDialog {

    Bar _bar = null;
    int _NumDevices;
    int _NumSteps;
    JTextField jTextFieldArray[][];
    JButton BtnPlayAll = new JButton("Play All");
    JButton BtnNext = new JButton("Next >>");
    JButton BtnCopyNext = new JButton("Copy to Next");
    JButton BtnRunFrame = new JButton("Play <>");
    JButton BtnPrev = new JButton("<< Prev");
    JButton BtnExit = new JButton("Close");
    JLabel name = new JLabel(" ");
    /**
     * Creates new form BarScreen
     * @param parent
     * @param modal
     */
    public BarScreen(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    /**
     *
     * @param parent
     * @param modal
     * @param bar
     */
    public BarScreen(java.awt.Frame parent, boolean modal, Bar bar) {
        super(parent, modal);
        //super("Bar");
        _bar = bar;
        _NumDevices = Bar.NUM_DEVICES;
        _NumSteps = bar.getNumCells();
        jTextFieldArray = new JTextField[_NumDevices][_NumSteps];
        initComponents();
        addDynamicComponents();
        updateScreen();
    }

    private void EditNext(){
        Bar b = StartScreen.PlaySheet.getBarAt(_bar.getSeq()+1);
        if (b == null){
            JOptionPane.showMessageDialog(this, "No next");
            return;
        }
        updateBar();
        _bar = b;
        updateScreen();
    }
    
    private void CopyNext(){
        Bar b = StartScreen.PlaySheet.getBarAt(_bar.getSeq()+1);
        if (b == null){
            JOptionPane.showMessageDialog(this, "No next");
            return;
        }
        updateBar();
        b.update(_bar);
        _bar = b;
        updateScreen();
    }
    
    private void EditPrev(){
        Bar b = StartScreen.PlaySheet.getBarAt(_bar.getSeq()-1);
        if (b == null){
            JOptionPane.showMessageDialog(this, "No Prev");
            return;
        }
        updateBar();
        _bar = b;
        updateScreen();
    }
    
    
    private void updateScreen(){
        name.setText("Beats for Bar:"  + _bar.getSeq());
        for (int i = 0; i < _NumDevices; i++) {
            for (int j = 0; j < _NumSteps; j++) {
                jTextFieldArray[i][j].setText("" + _bar.getPulse(i, j));
            }
        }
    }
    
    
    public void updateBar() {
        for (int i = 0; i < _NumSteps; i++) {
            for (int j = 0; j < _NumDevices; j++) {
                _bar.setPulse(j, i, Integer.parseInt(jTextFieldArray[j][i].getText().trim()));
            }
        }
    }

    private void RunCmd() {
        potbanger.play.Sheet.init();
        StartScreen.PlaySheet.play(_bar);
    }
    private void RunAll() {
        potbanger.play.Sheet.init();
        StartScreen.PlaySheet.play();
    }

    private void addDynamicComponents() {
        int gridX=0;
        int gridY=0;
        BtnRunFrame.addActionListener((ActionEvent e) -> {
            updateBar();
            RunCmd();
        });
        BtnExit.addActionListener((ActionEvent e) -> {
            updateBar();
            this.dispose();
        });
        BtnCopyNext.addActionListener((ActionEvent e) -> {
            updateBar();
            CopyNext();
        });
        BtnNext.addActionListener((ActionEvent e) -> {
            updateBar();
            EditNext();
        });
        BtnPrev.addActionListener((ActionEvent e) -> {
            EditPrev();
        });
        BtnPlayAll.addActionListener((ActionEvent e) -> {
            RunAll();
        });
        setBounds(0, 0, 800, 359);
        System.out.println("adding bar=" + _bar.getSeq());
        //PanelDynamic =new javax.swing.JPanel();
        //this.setLayout(new BorderLayout());
        JPanel north = new JPanel();
        north.add(name);
        PanelDynamic.add("North", north);
        JPanel center = new JPanel();
        center.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = gridX;
        gridX++;
        c.gridy = gridY;
        center.add(new Label("Pin#"),c);
        for (int j = 0; j < _NumSteps; j++) {
            if ((j % _bar.getNumFractions())== 0) {
                c.gridx = gridX;
                c.gridy = gridY;
                gridX++;
                center.add(new Label("          |          "),c);
            }
            c.gridy = gridY;
            c.gridx = gridX;
            gridX++;
            center.add(new Label(j % _bar.getNumFractions() +"/" + (j) / (_bar.getNumBeats() )),c);
        }
        gridY++;
        for (int i = 0; i < _NumDevices; i++) {
            gridY++;
            c.gridy = gridY;
            gridX = 0;
            c.gridx=gridX;
            gridX++;
            center.add(new Label("#" + (i + 1)), c);
            for (int j = 0; j < _NumSteps; j++) {
                if ((j % _bar.getNumFractions())== 0) {
                    c.gridx = gridX;
                    gridX++;
                    c.gridy = gridY;
                    center.add(new Label("          |          "),c);
                }
                c.gridy = gridY;
                c.gridx = gridX;
                gridX++;
                jTextFieldArray[i][j] = new JTextField(3);
                jTextFieldArray[i][j].setText("" + _bar.getPulse(i, j));
                center.add(jTextFieldArray[i][j], c);
            }
        }
        PanelDynamic.add("Center", center);
        JPanel south = new JPanel();
        south.add(BtnPlayAll);
        south.add(BtnPrev);
        south.add(BtnRunFrame);
        south.add(BtnNext);
        south.add(BtnCopyNext);
        south.add(BtnExit);
        PanelDynamic.add("South", south);
        pack();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        PanelDynamic = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Enter Beats for Bar");

        PanelDynamic.setLayout(new java.awt.BorderLayout());
        getContentPane().add(PanelDynamic, java.awt.BorderLayout.CENTER);

        setBounds(0, 0, 623, 359);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(BarScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(BarScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(BarScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BarScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                BarScreen dialog = new BarScreen(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        dialog.dispose();
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PanelDynamic;
    // End of variables declaration//GEN-END:variables
}
