/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Hugo
 */
public class DiagramaGantt extends JPanel {

    public static final int PADDIND_EJE_X = 40;
    public static final int PADDIND_EJE_Y = 20;
    public static final int UNIDAD_TIEMPO = 30;
    public static final int DIST_PROCESOS = 40;
    public int pos_x;

    public DiagramaGantt() {
        pos_x = PADDIND_EJE_X;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        dibujarEjes(g);
    }

    private void dibujarEjes(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Stroke stroke = new BasicStroke(2.0f);
        g2.setStroke(stroke);
        g.drawLine(PADDIND_EJE_X, PADDIND_EJE_Y, PADDIND_EJE_X, this.size().height - PADDIND_EJE_Y);
        g.drawLine(PADDIND_EJE_X - 20, this.size().height - PADDIND_EJE_Y * 2, this.size().width, this.size().height - PADDIND_EJE_Y * 2);
        int j = 0;
        System.out.println("w: " + this.size().width);
        for (int i = pos_x; i < this.size().width; i = i + UNIDAD_TIEMPO) {
            g.drawLine(i, this.size().height - PADDIND_EJE_Y * 2 - 5, i, this.size().height - PADDIND_EJE_Y * 2 + 5);
            g.drawString(j + "", i - 5, this.size().height - PADDIND_EJE_Y * 2 + 20);
            j++; 
        }

        System.out.println("h: " + this.size().height);
        int acu = this.size().height - PADDIND_EJE_Y * 2;
        for (int i = 0; i < 10; i++) {
            acu -= DIST_PROCESOS;
            System.out.println("gra");
            g.drawLine(PADDIND_EJE_X - 5, acu, PADDIND_EJE_X + 5, acu);
        }
    }

    public static void main(String[] args) {
        JFrame v = new JFrame();
        v.setVisible(true);
        v.getContentPane().add(new DiagramaGantt());
        v.setSize(500, 540);
        v.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
