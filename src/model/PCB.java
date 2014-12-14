/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Hugo
 */
public class PCB implements Comparable<PCB>{

    public static long NEXT_PID;
    private long PID;
    private EstadoPCB estado;
    private TipoProceso tipoProceso;
    private int memoria;
    private int direccion;
    private long horaLlegada;
    private long horaSalida;
    private long usoCPU;
    private long usoContCPU;
    private long consumidoUsoCPU;
    private long consumidoUsoContCPU;
    private long consParUsoContCPU;
    private long tiempoEYS;
    private long consumidoEYS;
    private long tiempoEsperaListo;
    private long tiempoEsperaBloqueado;
    private long tiempoTotalEYS;


    static {
        NEXT_PID = 0;
    }

    public PCB(){
    }

    public PCB(Proceso proceso) {
        NEXT_PID++;
        this.PID = NEXT_PID;
        this.estado = EstadoPCB.LISTO;
        this.tipoProceso = proceso.getTipo();
        this.horaLlegada = proceso.getHoraLlegada();
        this.usoCPU = proceso.getUsoCPU();
        this.usoContCPU = proceso.getUsoContCPU();
        this.memoria = proceso.getTamañoMemoria();
    }

    public PCB(EstadoPCB estado, long horaLlegada, long usoCPU, long usoContCPU) {
        this();
        this.estado = estado;
        this.horaLlegada = horaLlegada;
        this.usoCPU = usoCPU;
        this.usoContCPU = usoContCPU;
    }

    public PCB(TipoProceso tipoProceso, long horaLlegada, long usoCPU, long usoContCPU) {
        this();
        this.tipoProceso = tipoProceso;
        this.horaLlegada = horaLlegada;
        this.usoCPU = usoCPU;
        this.usoContCPU = usoContCPU;
    }

    public PCB(EstadoPCB estado, TipoProceso tipoProceso, int memoria, int direccion, long horaLlegada, long usoCPU, long usoContCPU) {
        this();
        this.estado = estado;
        this.tipoProceso = tipoProceso;
        this.memoria = memoria;
        this.direccion = direccion;
        this.horaLlegada = horaLlegada;
        this.usoCPU = usoCPU;
        this.usoContCPU = usoContCPU;
    }

    public long getPID() {
        return PID;
    }

    public void setPID(long PID) {
        this.PID = PID;
    }

    public EstadoPCB getEstado() {
        return estado;
    }

    public void setEstado(EstadoPCB estado) {
        this.estado = estado;
    }

    public TipoProceso getTipoProceso() {
        return tipoProceso;
    }

    public void setTipoProceso(TipoProceso tipoProceso) {
        this.tipoProceso = tipoProceso;
    }

    public int getMemoria() {
        return memoria;
    }

    public void setMemoria(int memoria) {
        this.memoria = memoria;
    }

    public int getDireccion() {
        return direccion;
    }

    public void setDireccion(int direccion) {
        this.direccion = direccion;
    }

    public long getHoraLlegada() {
        return horaLlegada;
    }

    public void setHoraLlegada(long horaLlegada) {
        this.horaLlegada = horaLlegada;
    }

    public long getHoraSalida() {
        return horaSalida;
    }

    public void setHoraSalida(long horaSalida) {
        this.horaSalida = horaSalida;
    }

    public long getUsoCPU() {
        return usoCPU;
    }

    public void setUsoCPU(long usoCPU) {
        this.usoCPU = usoCPU;
    }

    public long getConsumidoUsoCPU() {
        return consumidoUsoCPU;
    }

    public void setConsumidoUsoCPU(long consumidoUsoCPU) {
        this.consumidoUsoCPU = consumidoUsoCPU;
    }

    public long getUsoContCPU() {
        return usoContCPU;
    }

    public void setUsoContCPU(long usoContCPU) {
        this.usoContCPU = usoContCPU;
    }

    public long getConsumidoUsoContCPU() {
        return consumidoUsoContCPU;
    }

    public void setConsumidoUsoContCPU(long consumidoUsoContCPU) {
        this.consumidoUsoContCPU = consumidoUsoContCPU;
    }

    public long getTiempoEsperaListo() {
        return tiempoEsperaListo;
    }

    public void setTiempoEsperaListo(long tiempoEsperaListo) {
        this.tiempoEsperaListo = tiempoEsperaListo;
    }

    public long getConsParUsoContCPU() {
        return consParUsoContCPU;
    }

    public void setConsParUsoContCPU(long consParUsoContCPU) {
        this.consParUsoContCPU = consParUsoContCPU;
    }

    public long getTiempoEYS() {
        return tiempoEYS;
    }

    public void setTiempoEYS(long tiempoEYS) {
        this.tiempoEYS = tiempoEYS;
    }

    public long getConsumidoEYS() {
        return consumidoEYS;
    }

    public long getTiempoEsperaBloqueado() {
        return tiempoEsperaBloqueado;
    }

    public void setTiempoEsperaBloqueado(long tiempoEsperaBloqueado) {
        this.tiempoEsperaBloqueado = tiempoEsperaBloqueado;
    }

    public long getTiempoTotalEYS() {
        return tiempoTotalEYS;
    }

    public void setTiempoTotalEYS(long tiempoTotalEYS) {
        this.tiempoTotalEYS = tiempoTotalEYS;
    }

    public void setConsumidoEYS(long consumidoEYS) {
        this.consumidoEYS = consumidoEYS;
    }
    
    public boolean estaTerminado() {
        return this.consumidoUsoCPU >= this.usoCPU;
    }
    
    public boolean requiereEYS(){
        return this.consumidoUsoContCPU + this.consParUsoContCPU >= this.usoContCPU;
    }
    

    @Override
    public String toString() {
        return "PID: " + this.PID
                + " HL: " + this.horaLlegada
                + " CPU: " + this.usoCPU
                + " ContCPU: " + this.usoContCPU
                + " ConsCPU: " + this.consumidoUsoCPU
                + " ConsContCPU: " + this.consumidoUsoContCPU
                + " HS: " + this.horaSalida;
    }

    @Override
    public boolean equals(Object obj) {
        PCB other = (PCB) obj;
        return this.PID == other.PID; //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int compareTo(PCB o) {
        return this.tipoProceso.compareTo(o.tipoProceso);
    }
    
    public int compareToUsoCPU(PCB o){
        Long a = this.usoCPU-this.consumidoUsoCPU;
        Long b = o.usoCPU - o.consumidoUsoCPU;
        return a.compareTo(b);
    }
    
    public int compareToHoraLlegada(PCB o){
        Long a = this.horaLlegada;
        Long b = o.horaLlegada;
        return a.compareTo(b);
    }
    
    public void setProceso(Proceso proceso){
        NEXT_PID++;
        this.PID = NEXT_PID;
        this.estado = EstadoPCB.LISTO;
        this.tipoProceso = proceso.getTipo();
        this.horaLlegada = proceso.getHoraLlegada();
        this.usoCPU = proceso.getUsoCPU();
        this.usoContCPU = proceso.getUsoContCPU();
        this.memoria = proceso.getTamañoMemoria();
    }
    
    public void resetData(){
        this.PID = 0;
        this.consParUsoContCPU = 0;
        this.consumidoEYS = 0;
        this.consumidoUsoCPU = 0;
        this.consumidoUsoContCPU = 0;
        this.direccion = 0;
        this.estado = EstadoPCB.LIBRE;
        this.horaLlegada = 0;
        this.horaSalida = 0;
        this.memoria = 0;
        this.tiempoEYS = 0;
        this.tipoProceso = null;
        this.usoCPU = 0;
        this.usoContCPU = 0;
    }
    
    public PCB clone(){
        PCB pcb = new PCB();
        pcb.PID = this.PID;
        pcb.consParUsoContCPU = this.consParUsoContCPU;
        pcb.consumidoEYS = this.consumidoEYS;
        pcb.consumidoUsoCPU = this.consumidoUsoCPU ;
        pcb.consumidoUsoContCPU = this.consumidoUsoContCPU;
        pcb.direccion = this.direccion;
        pcb.estado = this.estado;
        pcb.horaLlegada = this.horaLlegada;
        pcb.horaSalida = this.horaSalida;
        pcb.memoria = this.memoria;
        pcb.tiempoEYS = this.tiempoEYS;
        pcb.tipoProceso = this.tipoProceso;
        pcb.usoCPU = this.usoCPU;
        pcb.usoContCPU = this.usoContCPU;
        return pcb;
    }
}
