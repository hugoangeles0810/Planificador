/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.List;

/**
 *
 * @author Hugo
 */
public interface PoliticaPlanificacion {

    public void entradaNuevoProceso(PCB pcb, long hora, List<PCB> listos, List<PCB> ejecucion);

    public void finQuantum(PCB pcb, long hora, List<PCB> listos, List<PCB> ejecucion);

    public void finProcesoActivo(PCB pcb, List<PCB> ejecucion, long hora);

    public void bloqueoPorEYS(PCB pcb, long hora, List<PCB> ejecucion, List<PCB> bloqueados);
    
    public void finEYS(PCB pcb, long hora, List<PCB> enEYS, List<PCB> listos,List<PCB> ejecucion);
    
}
