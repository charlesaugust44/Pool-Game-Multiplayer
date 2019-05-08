/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import game.Game;
import java.awt.Point;
import java.awt.Rectangle;
import model.MatchTableModel;
import model.Match;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import net.Addr;
import net.Request;
import net.Response;
import net.SocketControl;
import threads.MatchListThread;

public class MatchList extends javax.swing.JFrame {

    private JFrame parent;
    private MatchTableModel model;
    public MatchListThread matchListThread;
    private String selected;

    public MatchList(JFrame parent) {

        ArrayList<Match> matches = new ArrayList<>();
        model = new MatchTableModel(matches);

        initComponents();
        this.setLocationRelativeTo(null);
        this.parent = parent;
        this.setTitle(Game.me.user);

        matchListThread = new MatchListThread(this);
        matchListThread.start();
    }

    public void updateModel() {
        boolean exist = false;
        for (Match m : Match.matches)
            if (m.getGroupIp().equals(selected)) {
                exist = true;
                break;
            }

        if (!exist) {
            selected = null;
            btnEnterMatch.setText("Enter");
            btnEnterMatch.setEnabled(false);
        }

        model = new MatchTableModel(Match.matches);

        matchTable.setModel(model);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        matchTable = new javax.swing.JTable();
        btnCreateMatch = new javax.swing.JButton();
        btnEnterMatch = new javax.swing.JToggleButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        matchTable.setModel(model);
        matchTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                matchTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(matchTable);

        btnCreateMatch.setText("Create Match");
        btnCreateMatch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreateMatchActionPerformed(evt);
            }
        });

        btnEnterMatch.setText("Enter");
        btnEnterMatch.setEnabled(false);
        btnEnterMatch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEnterMatchActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 410, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnEnterMatch, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCreateMatch, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 479, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCreateMatch, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEnterMatch, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        parent.setVisible(true);
    }//GEN-LAST:event_formWindowClosing

    private void btnCreateMatchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateMatchActionPerformed
        SocketControl s = new SocketControl(Addr.SERVER_ADDRESS, Addr.SERVER_PORT);
        s.sendInt(Request.CREATE_MATCH);
        s.waitResponse();

        int port = s.receiveInt();

        s.close();
        s.sleep(50);

        s = new SocketControl(Addr.SERVER_ADDRESS, port);

        s.sendUTF(Game.me.user);

        s.waitResponse();

        int groupId = s.receiveInt();
        Game.group = "224.0.0." + groupId;

        MatchRoom matchRoom = new MatchRoom(this, groupId);
        matchListThread.setMatchRoom(matchRoom);

        matchRoom.setVisible(true);
        Rectangle bds = getBounds();
        Rectangle ths = matchRoom.getBounds();
        matchRoom.setBounds(bds.x, bds.y, ths.width, ths.height);
        this.setVisible(false);
    }//GEN-LAST:event_btnCreateMatchActionPerformed

    private void matchTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_matchTableMouseClicked
        JTable source = (JTable) evt.getSource();
        Point point = evt.getPoint();
        int row = source.rowAtPoint(point);

        String group = (String) source.getValueAt(row, 0);
        for (Match m : Match.matches)
            if (m.getGroupIp().equals(group)) {
                selected = m.getGroupIp();
                btnEnterMatch.setEnabled(true);
                btnEnterMatch.setText("Enter " + selected);
                break;
            }
    }//GEN-LAST:event_matchTableMouseClicked

    private void btnEnterMatchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEnterMatchActionPerformed
        SocketControl control = new SocketControl(Addr.SERVER_ADDRESS, Addr.SERVER_PORT);

        control.sendInt(Request.ENTER_MATCH);
        control.waitResponse();

        int port = control.receiveInt();
        control.close();
        control.sleep(50);

        control = new SocketControl(Addr.SERVER_ADDRESS, port);

        control.sendUTF(Game.me.user);
        control.sendUTF(selected);
        control.waitResponse();

        switch (control.receiveInt()) {
            case Response.ENTERED:
                String[] r = selected.split("\\.");
                MatchRoom matchRoom = new MatchRoom(this, Integer.parseInt(r[3]));
                matchListThread.setMatchRoom(matchRoom);
                Rectangle bds = getBounds();
                Rectangle ths = matchRoom.getBounds();
                matchRoom.setBounds(bds.x, bds.y, ths.width, ths.height);
                matchRoom.setVisible(true);
                this.setVisible(false);
                break;
            case Response.MATCH_DOES_NOT_EXIST:
                JOptionPane.showMessageDialog(this, "The group `" + selected + "` does not exists anymore.");
                btnEnterMatch.setText("Enter");
                btnEnterMatch.setEnabled(false);
                break;
        }
        control.close();
    }//GEN-LAST:event_btnEnterMatchActionPerformed

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
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels())
                if ("Windows".equals(info.getName()) || "GTK+".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MatchList.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MatchList.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MatchList.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MatchList.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MatchList(null).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCreateMatch;
    private javax.swing.JToggleButton btnEnterMatch;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable matchTable;
    // End of variables declaration//GEN-END:variables
}
