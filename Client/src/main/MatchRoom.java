package main;

import game.Game;
import game.objects.Player;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import model.PlayerTableModel;
import net.Addr;
import net.Cast;
import net.SocketControl;
import net.Request;
import net.Response;
import processing.core.PApplet;
import threads.MatchThread;

public class MatchRoom extends javax.swing.JFrame {

    private JFrame parent;
    private int matchId;
    private PlayerTableModel playerModel;
    private MatchThread matchThread;
    public game.Match game;

    public MatchRoom(JFrame parent, int match) {
        updateModel();
        initComponents();
        this.setLocationRelativeTo(null);
        this.parent = parent;
        this.matchId = match;
        Game.group = "224.0.0." + matchId;
    }

    public void startMatch() {
        setVisible(false);
        model.Match match = null;

        for (model.Match m : model.Match.matches)
            if (m.getId() == matchId) {
                match = m;
                break;
            }

        game = new game.Match(match.getPlayers(), this);

        Game.currentPlayer = Game.players.get(0);

        for (Player p : Game.players)
                if (p.getUser().equals(Game.me.user))
                Game.me = p;

        Rectangle bds = getBounds();

        String[] AppletArgs = {"--location=" + getLocation().x + "," + getLocation().y, "PoolGame (" + Game.me.user + ")"};
        PApplet.runSketch(AppletArgs, game);
    }

    public void updateModel() {
        for (model.Match m : model.Match.matches)
            if (m.getId() == matchId)
                playerModel = new PlayerTableModel(m.getPlayers());

        if (playerModel == null)
            playerModel = new PlayerTableModel(new ArrayList<>());

        if (playersTable != null) {
            playersTable.setModel(playerModel);
            if (playersTable.getRowCount() > 0)
                if (playersTable.getValueAt(0, 0).equals(Game.me.user)) {
                    btnStart.setVisible(true);
                    if (matchThread != null)
                        matchThread.stop();
                } else {
                    btnStart.setVisible(false);
                    if (matchThread == null) {
                        matchThread = new MatchThread(this);
                        matchThread.start();
                    }
                }

        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        playersTable = new javax.swing.JTable();
        btnLeave = new javax.swing.JButton();
        btnStart = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Match Room");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        playersTable.setModel(playerModel);
        jScrollPane1.setViewportView(playersTable);

        btnLeave.setText("Leave");
        btnLeave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLeaveActionPerformed(evt);
            }
        });

        btnStart.setText("Start");
        btnStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStartActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnLeave, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 203, Short.MAX_VALUE)
                .addComponent(btnStart, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnLeave, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnStart, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        btnStart.getAccessibleContext().setAccessibleDescription("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStartActionPerformed
        Cast.send(Response.MATCH_START.getBytes(), Game.group, Addr.MATCH_PORT);
        startMatch();
    }//GEN-LAST:event_btnStartActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        btnLeaveActionPerformed(null);
        parent.setVisible(true);
    }//GEN-LAST:event_formWindowClosing

    private void btnLeaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLeaveActionPerformed
        if (matchThread != null)
            matchThread.stop();

        ((MatchList) parent).matchListThread.setMatchRoom(null);

        SocketControl control = new SocketControl(Addr.SERVER_ADDRESS, Addr.SERVER_PORT);
        control.sendInt(Request.LEAVE_MATCH);

        control.waitResponse();
        int port = control.receiveInt();
        control.close();
        control.sleep(50);

        control = new SocketControl(Addr.SERVER_ADDRESS, port);

        control.sendUTF(Game.me.user);
        control.sendInt(matchId);
        control.waitResponse();

        if (control.receiveInt() == Response.LEFT && evt != null) {
            parent.setVisible(true);
            this.dispose();
        }

        control.close();
    }//GEN-LAST:event_btnLeaveActionPerformed

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
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MatchRoom.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MatchRoom.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MatchRoom.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MatchRoom.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MatchRoom(null, -1).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLeave;
    private javax.swing.JButton btnStart;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable playersTable;
    // End of variables declaration//GEN-END:variables
}
