/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import javax.swing.JComponent;
import javax.swing.JFrame;
import model.EstadoPCB;
import model.Parametros;

/**
 *
 * @author Brayan
 */
public class Dibujo extends JComponent {

    public static final int PADDIND_EJE_X = 40;
    public static final int PADDIND_EJE_Y = 20;
    public static final int UNIDAD_TIEMPO = 30;
    public static final int DIST_PROCESOS = 35;

    Color colorBloq = Color.RED;
    Color colorList = Color.GREEN;
    Color colorEjec = Color.BLUE;
    Point puntoOrigenFlag = new Point(PADDIND_EJE_X, this.getHeight() - PADDIND_EJE_Y + 20);

    public ArrayList<Color> colors;
    public ArrayList<Shape> shapes;
    public EstadoPCB[] actual, anterior;
    Point[] ptsFlag;
    public long tiempo;
    public Dibujo() {
        this.actual = new EstadoPCB[Parametros.MAX_PCB];
        this.anterior = new EstadoPCB[Parametros.MAX_PCB];
        ptsFlag = iniciarPtsFlags();
        shapes = new ArrayList();
        colors = new ArrayList();
        this.tiempo = 0;
    }

    public long getTiempo() {
        return tiempo;
    }

    public void setTiempo(long tiempo) {
        this.tiempo = tiempo;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        dibujarEjeX(g2);
        dibujarEjeY(g2);
        Stroke stroke = new BasicStroke(2.0f);
        g2.setStroke(stroke);
        System.out.println("Size: " + shapes.size());
        for (int i = 0; i < shapes.size(); i++) {
            Color color = colors.get(i);
            Shape shape = shapes.get(i);
            g2.setColor(color);
            g2.draw(shape);
        }

    }

    private void dibujarEjeY(Graphics2D g2) {
        Stroke stroke = new BasicStroke(2.0f);
        g2.setStroke(stroke);
        g2.drawLine(PADDIND_EJE_X, PADDIND_EJE_Y, PADDIND_EJE_X, this.getHeight() - PADDIND_EJE_Y);
        int acu = this.getHeight() - PADDIND_EJE_Y * 2;
        for (int i = 0; i < 10; i++) {
            acu -= DIST_PROCESOS;
            g2.drawLine(PADDIND_EJE_X - 5, acu, PADDIND_EJE_X + 5, acu);
        }
    }

    private void dibujarEjeX(Graphics2D g2) {

        Stroke stroke = new BasicStroke(2.0f);
        g2.setStroke(stroke);
        puntoOrigenFlag.y = this.getHeight() - PADDIND_EJE_Y * 2;
        System.out.println("Punto 1: " + puntoOrigenFlag.x + "," + puntoOrigenFlag.y + " Punto 2: " + (int) tiempo * UNIDAD_TIEMPO + PADDIND_EJE_X + ", " + puntoOrigenFlag.y);
        g2.drawLine(puntoOrigenFlag.x, puntoOrigenFlag.y, (int) (tiempo) * UNIDAD_TIEMPO + PADDIND_EJE_X, puntoOrigenFlag.y);
        System.out.println("w: " + this.getWidth());
        int j = 0;
        for (int i = puntoOrigenFlag.x; i <= (int) (tiempo) * UNIDAD_TIEMPO + PADDIND_EJE_X; i = i + UNIDAD_TIEMPO) {
            System.out.println("aca esta");
            g2.drawLine(i, this.getHeight() - PADDIND_EJE_Y * 2 - 5, i, this.getHeight() - PADDIND_EJE_Y * 2 + 5);
            g2.drawString(j + "", i - 5, this.getHeight() - PADDIND_EJE_Y * 2 + 20);
            j++;
        }
    }

    public void dibujarEstadosPorTiempo(Graphics2D g2) {
        Stroke stroke = new BasicStroke(2.0f);
        g2.setStroke(stroke);
        int acu = puntoOrigenFlag.y;
        for (int i = 0; i < actual.length; i++) {
            acu -= DIST_PROCESOS;
            if (actual[i] != null) {
                Color c = getColorEstados(actual[i]);
                Line2D.Float line = new Line2D.Float((tiempo-1)*UNIDAD_TIEMPO+PADDIND_EJE_X, acu, (tiempo)*UNIDAD_TIEMPO+PADDIND_EJE_X, acu);
                colors.add(c);
                shapes.add(line);
                g2.setColor(c);
                g2.draw(line);
            }
//            if (actual[i] != null) {
//                if (anterior[i] != null) {
//                    if (actual[i].equals(anterior[i])) {
//                        Color c = getColorEstados(actual[i]);
//                        
//                        System.out.println(actual[i]);
//                        g2.setColor(c);
//                        Line2D.Float line = new Line2D.Float(ptsFlag[i], new Point((int) tiempo * UNIDAD_TIEMPO + PADDIND_EJE_X, ptsFlag[i].y));
//                        shapes.add(line);
//                        colors.add(c);
//                        g2.draw(line);
//                    }
//                } else {
//                    ptsFlag[i].x = (int) tiempo * UNIDAD_TIEMPO + PADDIND_EJE_X;
//                    ptsFlag[i].y = acu;
//                    anterior = actual;
//                }
//            } else {
//                if (anterior[i] != null) {
//                    Color c = getColorEstados(anterior[i]);
//                    g2.setColor(c);
//                    Line2D.Float line = new Line2D.Float(ptsFlag[i], new Point((int) tiempo * UNIDAD_TIEMPO + PADDIND_EJE_X, ptsFlag[i].y));
//                    shapes.add(line);
//                    colors.add(c);
//                    g2.draw(line);
//                }
//        }
//
//        acu -= DIST_PROCESOS;
//    }
        }
    }

    public Color getColorEstados(EstadoPCB estado) {

        switch (estado) {
            case LISTO:
                return colorList;
            case BLOQUEADO:
                return colorBloq;
            case EJECUCION:
                return colorEjec;
            default:
                return null;
        }

    }

    public Point[] iniciarPtsFlags() {
        Point[] points;
        points = new Point[Parametros.MAX_PCB];

        for (int i = 0; i < points.length; i++) {
            points[i] = new Point(0, 0);

        }

        return points;
    }

    public static void main(String[] args) throws InterruptedException {
        JFrame v = new JFrame("Prueba");
        Dibujo d = new Dibujo();
        v.getContentPane().add(d);
        v.setBounds(200, 200, 500, 500);
        v.setVisible(true);
        for (int i = 0; i < 20; i++) {
            System.out.println("entro");
            d.setTiempo(i);
            d.repaint();
            Thread.sleep(100);
        }
        Thread.sleep(1000);

        for (int i = 20; i < 40; i++) {
            System.out.println("entro");
            d.setTiempo(i);
            d.repaint();
            Thread.sleep(100);
        }
    }

}
