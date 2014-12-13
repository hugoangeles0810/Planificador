/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Hugo
 */
public class Proceso {
    
    private long horaLlegada;
    private long usoCPU;
    private long usoContCPU;
    private int tamañoMemoria;
    private TipoProceso tipo;

    public Proceso() {
    }

    public Proceso(long horaLlegada, long usoCPU, long usoContCPU) {
        this.horaLlegada = horaLlegada;
        this.usoCPU = usoCPU;
        this.usoContCPU = usoContCPU;
    }

    public Proceso(long horaLlegada, long usoCPU, long usoContCPU, TipoProceso tipo) {
        this.horaLlegada = horaLlegada;
        this.usoCPU = usoCPU;
        this.usoContCPU = usoContCPU;
        this.tipo = tipo;
    }

    public Proceso(long horaLlegada, long usoCPU, long usoContCPU, int tamañoMemoria, TipoProceso tipo) {
        this.horaLlegada = horaLlegada;
        this.usoCPU = usoCPU;
        this.usoContCPU = usoContCPU;
        this.tamañoMemoria = tamañoMemoria;
        this.tipo = tipo;
    }

    public long getHoraLlegada() {
        return horaLlegada;
    }

    public void setHoraLlegada(long horaLlegada) {
        this.horaLlegada = horaLlegada;
    }

    public long getUsoCPU() {
        return usoCPU;
    }

    public void setUsoCPU(long usoCPU) {
        this.usoCPU = usoCPU;
    }

    public long getUsoContCPU() {
        return usoContCPU;
    }

    public void setUsoContCPU(long usoContCPU) {
        this.usoContCPU = usoContCPU;
    }

    public int getTamañoMemoria() {
        return tamañoMemoria;
    }

    public void setTamañoMemoria(int tamañoMemoria) {
        this.tamañoMemoria = tamañoMemoria;
    }

    public TipoProceso getTipo() {
        return tipo;
    }

    public void setTipo(TipoProceso tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return "H: " + this.horaLlegada + " CPU: " + this.usoCPU 
                + " CPUCont: " + this.usoContCPU +" Tipo: " + this.tipo + " TMem: " + this.tamañoMemoria;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (int) (this.horaLlegada ^ (this.horaLlegada >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Proceso other = (Proceso) obj;
        return true;
    }
    
}
