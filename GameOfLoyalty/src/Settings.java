import javax.swing.*;
import java.awt.*;

public class Settings extends javax.swing.JPanel {

    private String strUI[] = {"Metal","Nimbus","Motif","Windows","Windows Classic"};
    private javax.swing.UIManager.LookAndFeelInfo looks[] = javax.swing.UIManager.getInstalledLookAndFeels();
    private String strAI;
    /**
     * Creates new form Settings
     */
    public Settings() {
        initComponents();
        jSlider1.setValue(BoardGUI.aiDiff);
        if(jSlider1.getValue()<4) strAI = "Easy";
        else if(jSlider1.getValue()>7) strAI = "Difficult";
        else strAI = "Medium";
        jLabel3.setVisible(false);
        this.repaint();
    }
    
    public void paintComponent(Graphics g)
    {
        BoardGUI.status.setText("Change the settings of the game.");
        g.setColor(Color.black);
        g.fillRect(0,0,getWidth(),getHeight());
    }
    
    public void clear()
    {
        this.removeAll();
        this.initComponents();
        this.validate();
        this.repaint();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jSlider1 = new javax.swing.JSlider();
        jLabel2 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(0, 0, 0));
        setForeground(new java.awt.Color(255, 255, 255));
        setMaximumSize(new java.awt.Dimension(589, 505));
        setMinimumSize(new java.awt.Dimension(589, 505));

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Change AI Difficulty:");

        jSlider1.setBackground(new java.awt.Color(102, 102, 102));
        jSlider1.setForeground(new java.awt.Color(255, 255, 255));
        jSlider1.setMajorTickSpacing(5);
        jSlider1.setMaximum(10);
        jSlider1.setMinorTickSpacing(2);
        jSlider1.setPaintLabels(true);
        jSlider1.setPaintTicks(true);
        jSlider1.setToolTipText("[0 - 4) : Easy   \n[4 - 7] : Medium   \n(7-10] : Hard");
        jSlider1.setValue(BoardGUI.aiDiff);
        jSlider1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider1StateChanged(evt);
            }
        });

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Set Look and Feel");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Metal", "Nimbus", "Motif", "Windows", "Windows Classic" }));
        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText(String.format("Current AI : " + strAI));

        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("[0,4) : Easy");

        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("[4,7] : Medium");

        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("(7,10] : Difficult");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(199, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(jLabel5)
                    .addComponent(jLabel4)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                        .addComponent(jLabel2)
                        .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel1)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING)))
                .addGap(190, 190, 190))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(75, 75, 75)
                .addComponent(jLabel1)
                .addGap(31, 31, 31)
                .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addGap(50, 50, 50)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(141, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    
    private void jSlider1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider1StateChanged
        try
        {
            BoardGUI.aiDiff = jSlider1.getValue();
            if(jSlider1.getValue()<4) strAI = "Easy";
            else if(jSlider1.getValue()>7) strAI = "Difficult";
            else strAI = "Medium";
            jLabel3.setText("Current AI : " + strAI);
            jLabel3.setVisible(true);
        }
        catch(Exception e){JOptionPane.showMessageDialog(null,"Slider cannot exceed 10!","Error!",JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jSlider1StateChanged

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        try
        {
            looks = UIManager.getInstalledLookAndFeels();
            UIManager.setLookAndFeel(looks[jComboBox1.getSelectedIndex()].getClassName());
            SwingUtilities.updateComponentTreeUI(this);
            BoardGUI.status.setText("The theme has been changed.");
        }
        catch(Exception ex) {JOptionPane.showMessageDialog(null,ex.toString(),"Error!",JOptionPane.ERROR_MESSAGE);}
    }//GEN-LAST:event_jComboBox1ItemStateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JSlider jSlider1;
    // End of variables declaration//GEN-END:variables
}
