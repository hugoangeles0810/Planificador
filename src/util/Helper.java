/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.Iterator;
import java.util.List;
import model.PCB;

/**
 *
 * @author Hugo
 */
public class Helper {

    public static void insertarOrdenadoPorPrioridad(List<PCB> collection, PCB obj) {
        Iterator it = collection.iterator();
        int i = 0;
        if (!collection.isEmpty()) {
            while (it.hasNext()) {
                PCB element = (PCB) it.next();
                if (obj.compareTo(element) < 0) {
                    collection.add(i, obj);
                    return;
                }
                i++;
            }
        }
        collection.add(obj);
    }

    public static void insertarOrdenadoPorUsoCPU(List<PCB> collection, PCB obj) {
        Iterator it = collection.iterator();
        int i = 0;
        if (!collection.isEmpty()) {
            while (it.hasNext()) {
                PCB element = (PCB) it.next();
                if (obj.compareToUsoCPU(element) < 0) {
                    collection.add(i, obj);
                    return;
                }
                i++;
            }
        }
        collection.add(obj);
    }
    
    public static void insertarOrdenadoPorHoraLLegada(List<PCB> collection, PCB obj) {
        Iterator it = collection.iterator();
        int i = 0;
        if (!collection.isEmpty()) {
            while (it.hasNext()) {
                PCB element = (PCB) it.next();
                if (obj.compareToHoraLlegada(element) < 0) {
                    collection.add(i, obj);
                    return;
                }
                i++;
            }
        }
        collection.add(obj);
    }

    public static void incrementaEsperaListo(List<PCB> collection) {
        for (Iterator<PCB> it = collection.iterator(); it.hasNext();) {
            PCB pcb = it.next();
            pcb.setTiempoEsperaListo(pcb.getTiempoEsperaListo() + 1);
        }
    }

    public static void incrementaEsperaBloqueado(List<PCB> collection) {
        for (Iterator<PCB> it = collection.iterator(); it.hasNext();) {
            PCB pcb = it.next();
            pcb.setTiempoEsperaBloqueado(pcb.getTiempoEsperaBloqueado() + 1);
        }
    }
    
    public static double calculaSTDListo(List<PCB> pcbs, double mean, int n){
        double acu = 0;
        for (PCB pcb : pcbs) {
            acu += Math.pow(mean-pcb.getTiempoEsperaListo(),2);
        }
        
        return Math.sqrt(acu/n);
        
    }
    
    public static double calculaSTDBloqueado(List<PCB> pcbs, double mean, int n){
        double acu = 0;
        for (PCB pcb : pcbs) {
            acu += Math.pow(mean-pcb.getTiempoEsperaBloqueado(),2);
        }
        
        return Math.sqrt(acu/n);
        
    }
    
    public static double calculaSTDCPU(List<PCB> pcbs, double mean, int n){
        double acu = 0;
        for (PCB pcb : pcbs) {
            acu += Math.pow(mean-pcb.getUsoCPU(),2);
        }
        
        return Math.sqrt(acu/n);
        
    }
}
