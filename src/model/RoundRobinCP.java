/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.List;
import util.Helper;

/**
 *
 * @author Hugo
 */
public class RoundRobinCP implements PoliticaPlanificacion {

    @Override
    public void entradaNuevoProceso(PCB pcb, long hora, List<PCB> listos, List<PCB> ejecucion) {
        pcb.setEstado(EstadoPCB.LISTO);

        if (!ejecucion.isEmpty() && pcb.compareTo(ejecucion.get(0)) < 0) {
            ejecucion.get(0).setConsumidoUsoContCPU(ejecucion.get(0).getConsumidoUsoContCPU() + ejecucion.get(0).getConsParUsoContCPU());
            ejecucion.get(0).setConsParUsoContCPU(0);
            ejecucion.get(0).setEstado(EstadoPCB.LISTO);
            Helper.insertarOrdenadoPorPrioridad(listos, ejecucion.get(0));
            ejecucion.remove(0);
        }
        Helper.insertarOrdenadoPorPrioridad(listos, pcb);
    }

    @Override
    public void finQuantum(PCB pcb, long hora, List<PCB> listos, List<PCB> ejecucion) {
        pcb.setConsumidoUsoContCPU(pcb.getConsumidoUsoContCPU() + pcb.getConsParUsoContCPU());
        pcb.setConsParUsoContCPU(0);
        pcb.setEstado(EstadoPCB.LISTO);
        Helper.insertarOrdenadoPorPrioridad(listos, pcb);
        ejecucion.remove(0);
    }

    @Override
    public void finProcesoActivo(PCB pcb, List<PCB> ejecucion, long hora) {
        PCB procesoEnEjecucion = ejecucion.get(0);
        procesoEnEjecucion.setConsumidoUsoContCPU(0);
        procesoEnEjecucion.setConsParUsoContCPU(0);
        procesoEnEjecucion.setHoraSalida(hora);
        ejecucion.remove(0);
    }

    @Override
    public void bloqueoPorEYS(PCB pcb, long hora, List<PCB> ejecucion, List<PCB> bloqueados) {
        pcb.setConsumidoUsoContCPU(0);
        pcb.setConsParUsoContCPU(0);
        pcb.setEstado(EstadoPCB.BLOQUEADO);
        ejecucion.get(0).setConsumidoEYS(0);
        bloqueados.add(ejecucion.get(0));
        ejecucion.remove(0);
    }

    @Override
    public void finEYS(PCB pcb, long hora, List<PCB> enEYS, List<PCB> listos, List<PCB> ejecucion) {
        pcb.setConsumidoEYS(0);
        enEYS.remove(0);
        
        if (!ejecucion.isEmpty() && pcb.compareTo(ejecucion.get(0)) < 0) {
            ejecucion.get(0).setConsumidoUsoContCPU(ejecucion.get(0).getConsumidoUsoContCPU() + ejecucion.get(0).getConsParUsoContCPU());
            ejecucion.get(0).setConsParUsoContCPU(0);
            ejecucion.get(0).setEstado(EstadoPCB.LISTO);
            Helper.insertarOrdenadoPorPrioridad(listos, ejecucion.get(0));
            ejecucion.remove(0);
        }
        
        Helper.insertarOrdenadoPorPrioridad(listos, pcb);
    }
}
