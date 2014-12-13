/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import model.PCB;

/**
 *
 * @author Hugo
 */
public class DiagramaGantt extends JPanel {

    public static final int PADDIND_EJE_X = 40;
    public static final int PADDIND_EJE_Y = 20;
    public static final int UNIDAD_TIEMPO = 30;
    public static final int DIST_PROCESOS = 35;
    public int pos_x;
    ArrayList<PCB> procesos;
    public long tiempo;
    public Point puntoOrigenFlag;
    boolean firstTime = true; 

    public DiagramaGantt() {
        pos_x = PADDIND_EJE_X;
        this.tiempo = 0;
        puntoOrigenFlag = new Point(PADDIND_EJE_X, this.size().height - PADDIND_EJE_Y + 20);
    }

    public DiagramaGantt(int pos_x, ArrayList<PCB> procesos, long tiempo) {
        this.pos_x = pos_x;
        this.procesos = procesos;
        this.tiempo = tiempo;
    }

    public int getPos_x() {
        return pos_x;
    }

    public void setPos_x(int pos_x) {
        this.pos_x = pos_x;
    }

    public ArrayList<PCB> getProcesos() {
        return procesos;
    }

    public void setProcesos(ArrayList<PCB> procesos) {
        this.procesos = procesos;
    }

    public long getTiempo() {
        return tiempo;
    }

    public void setTiempo(long tiempo) {
        this.tiempo = tiempo;
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        dibujarEjes(g2);
    }

    private void dibujarEjes(Graphics2D g2) {
        
        Stroke stroke = new BasicStroke(2.0f);
        g2.setStroke(stroke);
        dibujarEjeY(g2);
        puntoOrigenFlag.y = this.size().height - PADDIND_EJE_Y*2;
        System.out.println("Punto 1: " + puntoOrigenFlag.x +  "," + puntoOrigenFlag.y + " Punto 2: " + (int)tiempo*UNIDAD_TIEMPO + pos_x + ", " + puntoOrigenFlag.y );
        g2.drawLine(puntoOrigenFlag.x, puntoOrigenFlag.y, (int)tiempo*UNIDAD_TIEMPO + pos_x, puntoOrigenFlag.y);
        System.out.println("w: " + this.size().width);
        int j = 0;
        for (int i = puntoOrigenFlag.x; i < (int)tiempo*UNIDAD_TIEMPO; i = i + UNIDAD_TIEMPO) {
            System.out.println("aca esta");
            g2.drawLine(i, this.size().height - PADDIND_EJE_Y * 2 - 5, i, this.size().height - PADDIND_EJE_Y * 2 + 5);
            g2.drawString(j + "", i - 5, this.size().height - PADDIND_EJE_Y * 2 + 20);
            j++; 
        }
    }
    
    public void dibujarEjeY(Graphics2D g2){
        Stroke stroke = new BasicStroke(2.0f);
        g2.setStroke(stroke);
        g2.drawLine(PADDIND_EJE_X, PADDIND_EJE_Y, PADDIND_EJE_X, this.size().height - PADDIND_EJE_Y);
        int acu = this.size().height - PADDIND_EJE_Y * 2;
        for (int i = 0; i < 10; i++) {
            acu -= DIST_PROCESOS;
            g2.drawLine(PADDIND_EJE_X - 5, acu, PADDIND_EJE_X + 5, acu);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        JFrame v = new JFrame();
        v.setVisible(true);
        DiagramaGantt diagrama = new DiagramaGantt();
        v.getContentPane().add(diagrama);
        v.setSize(500, 540);
        v.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        
    }
}
