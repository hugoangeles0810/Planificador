/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Hugo
 */
public class Evento {
    
    
    private TipoEvento tipo;
    private Proceso proceso;
    private PCB pcb;
    private long reloj;
    
    public Evento(){}

    public TipoEvento getTipo() {
        return tipo;
    }

    public void setTipo(TipoEvento tipo) {
        this.tipo = tipo;
    }

    public Proceso getProceso() {
        return proceso;
    }

    public void setProceso(Proceso proceso) {
        this.proceso = proceso;
    }

    
    public long getReloj() {
        return reloj;
    }

    public void setReloj(long reloj) {
        this.reloj = reloj;
    }

    public PCB getPcb() {
        return pcb;
    }

    public void setPcb(PCB pcb) {
        this.pcb = pcb;
    }
    
}
