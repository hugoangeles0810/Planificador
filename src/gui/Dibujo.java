/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import javax.swing.JComponent;
import javax.swing.JFrame;
import model.EstadoPCB;
import model.PCB;
import model.Parametros;

/**
 *
 * @author Brayan
 */
public class Dibujo extends JComponent {

    public static final int PADDIND_EJE_X = 40;
    public static final int PADDIND_EJE_Y = 20;
    public static final int UNIDAD_TIEMPO = 30;
    public static int DIST_PROCESOS = 33;

    Color colorBloq = Color.RED;
    Color colorList = Color.GREEN;
    Color colorEjec = Color.BLUE;
    Color colorEYS = Color.YELLOW;
    Point puntoOrigenFlag = new Point(PADDIND_EJE_X, this.getHeight() - PADDIND_EJE_Y + 20);

    public ArrayList<Color> colors;
    public ArrayList<Shape> shapes;
    public ArrayList<Shape> entradas;
    public ArrayList<Shape> etiquetas;
    public ArrayList<Shape> finales;

    public ArrayList<PCB> pcbs;
    public long tiempo;
    public boolean pintar = true;

    public Dibujo() {
        shapes = new ArrayList();
        colors = new ArrayList();
        entradas = new ArrayList();
        etiquetas = new ArrayList();
        finales = new ArrayList();
        this.tiempo = 0;
        setPreferredSize(new Dimension(300 * UNIDAD_TIEMPO + 2 * PADDIND_EJE_X, DIST_PROCESOS * Parametros.MAX_PCB + 3 * PADDIND_EJE_Y));
    }

    public long getTiempo() {
        return tiempo;
    }

    public void setTiempo(long tiempo) {
        this.tiempo = tiempo;
    }

    public ArrayList<PCB> getPcbs() {
        return pcbs;
    }

    public void setPcbs(ArrayList<PCB> pcbs) {
        this.pcbs = pcbs;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.WHITE);
        g2.drawRect(0, 0, this.getWidth(), this.getHeight());
        g2.setColor(Color.BLACK);
        dibujarEjeX(g2);
        dibujarEjeY(g2);
        Stroke stroke = new BasicStroke(3.0f);
        g2.setStroke(stroke);
        System.out.println("Size: " + shapes.size());
        System.out.println("Size: " + colors.size());
        for (int i = 0; i < shapes.size(); i++) {
            Color color = colors.get(i);
            Shape shape = shapes.get(i);
            g2.setColor(color);
            g2.draw(shape);
        }

        g2.setColor(Color.MAGENTA);
        for (Shape s : entradas) {
            g2.fill(s);
        }
        
        g2.setColor(Color.CYAN);
        for (Shape s : finales) {
            g2.fill(s);
        }
        
        g2.setStroke(new BasicStroke(0.2f));
        g2.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
        g2.setColor(Color.BLACK);
        for (Shape s : etiquetas) {
            g2.draw(s);
        }

    }

    private void dibujarEjeY(Graphics2D g2) {
        if (tiempo > 0) {
            g2.setStroke(new BasicStroke(2.0f));
            g2.drawLine(PADDIND_EJE_X, PADDIND_EJE_Y, PADDIND_EJE_X, this.getHeight() - PADDIND_EJE_Y);
            int acu = this.getHeight() - PADDIND_EJE_Y * 2;
            for (int i = 0; i < 10; i++) {
                acu -= DIST_PROCESOS;
                g2.setStroke(new BasicStroke(0.1f));
                g2.drawLine(PADDIND_EJE_X, acu, (int) (tiempo) * UNIDAD_TIEMPO + PADDIND_EJE_X, acu);
                g2.setStroke(new BasicStroke(2.0f));
                g2.drawLine(PADDIND_EJE_X - 5, acu, PADDIND_EJE_X + 5, acu);
            }
        }

    }

    private void dibujarEjeX(Graphics2D g2) {

        if (tiempo > 0) {
            Stroke stroke = new BasicStroke(2.0f);
            g2.setStroke(stroke);
            puntoOrigenFlag.y = this.getHeight() - PADDIND_EJE_Y * 2;
            g2.drawLine(puntoOrigenFlag.x, puntoOrigenFlag.y, (int) (tiempo) * UNIDAD_TIEMPO + PADDIND_EJE_X, puntoOrigenFlag.y);
            System.out.println("w: " + this.getWidth());
            int j = 0;
            for (int i = puntoOrigenFlag.x; i <= (int) (tiempo) * UNIDAD_TIEMPO + PADDIND_EJE_X; i = i + UNIDAD_TIEMPO) {
                g2.setStroke(new BasicStroke(0.1f));
                g2.drawLine(i, PADDIND_EJE_Y, i, this.getHeight() - 2 * PADDIND_EJE_Y);
                g2.setStroke(new BasicStroke(2.0f));
                g2.drawLine(i, this.getHeight() - PADDIND_EJE_Y * 2 - 5, i, this.getHeight() - PADDIND_EJE_Y * 2 + 5);
                g2.drawString(j + "", i - 5, this.getHeight() - PADDIND_EJE_Y * 2 + 20);
                j++;
            }
        }
    }

    public void dibujarEstados() {
        Graphics2D g2 = (Graphics2D) getGraphics();
        int pos_y = this.getHeight() - PADDIND_EJE_Y * 2;
        for (PCB pcb : pcbs) {
            if (pcb.getEstado() != EstadoPCB.LIBRE) {
                System.out.println("Estado : " + pcb.getEstado());
                Color color = getColorEstados(pcb.getEstado());
                Line2D line = crearLinea((int) (tiempo - 1) * UNIDAD_TIEMPO + PADDIND_EJE_X, pos_y - (int) pcb.getNRO() * DIST_PROCESOS, (int) (tiempo) * UNIDAD_TIEMPO + PADDIND_EJE_X, pos_y - (int) pcb.getNRO() * DIST_PROCESOS);
                colors.add(color);
                shapes.add(line);
            }
        }
    }

    public void dibujaEntradaProceso(PCB pcb) {
        Graphics2D g2 = (Graphics2D) getGraphics();
        Font f = new Font(g2.getFont().getName(), Font.PLAIN, 10);
        int pos_y = this.getHeight() - PADDIND_EJE_Y * 2;
        Rectangle2D rect = crearRect((int) (tiempo) * UNIDAD_TIEMPO + PADDIND_EJE_X - 10, pos_y - (int) pcb.getNRO() * DIST_PROCESOS - 10, 20, 20);
        entradas.add(rect);
        Shape etiqueta = f.createGlyphVector(getFontMetrics(f).getFontRenderContext(), pcb.getPID() + "").getOutline((int) (tiempo) * UNIDAD_TIEMPO + PADDIND_EJE_X, pos_y - (int) pcb.getNRO() * DIST_PROCESOS);
        etiquetas.add(etiqueta);
    }
    
    public void dibujaFinalProceso(PCB pcb) {
        Graphics2D g2 = (Graphics2D) getGraphics();
        int pos_y = this.getHeight() - PADDIND_EJE_Y * 2;
        Rectangle2D rect = crearRect((int) (tiempo) * UNIDAD_TIEMPO + PADDIND_EJE_X - 10, pos_y - (int) pcb.getNRO() * DIST_PROCESOS - 10, 20, 20);
        finales.add(rect);
    }

    public Color getColorEstados(int estado) {

        switch (estado) {
            case EstadoPCB.LISTO:
                return colorList;
            case EstadoPCB.BLOQUEADO:
                return colorBloq;
            case EstadoPCB.EJECUCION:
                return colorEjec;
            case EstadoPCB.EYS:
                return colorEYS;
            default:
                return null;
        }

    }

    private Line2D.Float crearLinea(int x1, int y1, int x2, int y2) {
        return new Line2D.Float(x1, y1, x2, y2);

    }

    private Rectangle2D.Float crearRect(int x, int y, int w, int h) {
        return new Rectangle2D.Float(x, y, w, h);
    }
}
