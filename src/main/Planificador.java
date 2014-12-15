/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import gui.PlanificadorGUI;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import model.EstadoPCB;
import model.Evento;
import model.PCB;
import model.Parametros;
import model.PoliticaPlanificacion;
import model.Proceso;
import model.TipoEvento;
import model.TipoProceso;
import util.Helper;

/**
 *
 * @author Hugo
 */
public class Planificador {

    public long hora;
    public Proceso procesoFlag;
    public long tiempoOcio;
    public long tiempoEYS;
    public ArrayList<PCB> ejecucion;
    public ArrayList<PCB> enEYS;
    public ArrayList<PCB> listos;
    public ArrayList<PCB> bloqueados;
    public ArrayList<PCB> libres;
    public ArrayList<PCB> finalizados;
    public PoliticaPlanificacion politica;
    public BufferedReader lectorProcesos;
    public File archivoCarga;
    double minWait, maxWait, meanWait, stdWait, acuWait;
    double minBlock, maxBlock, meanBlock, stdBlock, acuBlock;
    double minExec, maxExec, meanExec, stdExec, acuExec;
    int cont;

    PlanificadorGUI view;

    public Planificador(PlanificadorGUI view) {
        this.view = view;
        view.planf = this;
        this.ejecucion = new ArrayList();
        this.enEYS = new ArrayList();
        this.listos = new ArrayList();
        this.bloqueados = new ArrayList();
        this.finalizados = new ArrayList();
        this.libres = generarPCBsLibres(Parametros.MAX_PCB);
        this.hora = 0L;
        this.tiempoEYS = 0L;
        this.tiempoOcio = 0L;
        this.procesoFlag = null;
        minWait = maxWait = meanWait = stdWait = acuWait = 0;
        minBlock = maxBlock = meanBlock = stdBlock = acuBlock = 0;
        minExec = maxExec = meanExec = stdExec = acuExec = 0;
        cont = 0;

        archivoCarga = new File(Parametros.NOMBRE_ARCHIVO_CARGA);
        iniciarLectura();

    }

    private void iniciarLectura() {
        try {
            lectorProcesos = new BufferedReader(new FileReader(archivoCarga));
        } catch (FileNotFoundException ex) {
            System.out.println("No se encontro el archivo de carga");
            System.exit(0);
        }
    }

    public void planificar() {
        boolean salir;
        Evento evento;
        procesoFlag = leerProceso();
        System.out.println("NULL: " + procesoFlag);
        salir = false;
        System.out.println("--------------Inicio Simulacion---------------");
        view.jTextAreaLog.setText("--------------Inicio Simulacion---------------" + "\n");
        while (!salir) {
            evento = siguienteEvento();

            switch (evento.getTipo()) {
                case ENTRADA_NUEVO_PROCESO:
                    PCB pcb;
                    if (!libres.isEmpty()) {
                        pcb = libres.get(0);
                        libres.remove(0);
                        pcb.setProceso(procesoFlag);
                        politica.entradaNuevoProceso(pcb, hora, listos, ejecucion);
                        view.diagrama.dibujaEntradaProceso(pcb);
                        System.out.println("[T:" + hora + "]" + " Entrada nuevo Proceso ==> " + pcb);
                        view.jTextAreaLog.setText(view.jTextAreaLog.getText() + "[T:" + hora + "]" + " Entrada nuevo Proceso ==> " + pcb + "\n");
                        procesoFlag = leerProceso();
                    } else {
                        System.out.println("[T:" + hora + "]" + " Entrada nuevo Proceso ==> " + " RECHAZADO, no hay PCBs libres ");
                        view.jTextAreaLog.setText(view.jTextAreaLog.getText() + "[T:" + hora + "]" + " Entrada nuevo Proceso ==> " + " RECHAZADO, no hay PCBs libres " + "\n");
                    }

                    break;
                case FIN_QUANTUM:
                    System.out.println("[T:" + hora + "]" + " Fin de Quantum ==> " + evento.getPcb());
                    view.jTextAreaLog.setText(view.jTextAreaLog.getText() + "[T:" + hora + "]" + " Fin de Quantum ==> " + evento.getPcb() + "\n");

                    politica.finQuantum(evento.getPcb(), hora, listos, ejecucion);
                    break;
                case FIN_PROCESO_ACTIVO:
                    politica.finProcesoActivo(evento.getPcb(), ejecucion, hora);
                    view.diagrama.dibujaFinalProceso(evento.getPcb());
                    System.out.println("[T:" + hora + "]" + " Fin de Proceso ==> " + evento.getPcb());
                    view.jTextAreaLog.setText(view.jTextAreaLog.getText() + "[T:" + hora + "]" + " Fin de Proceso ==> " + evento.getPcb() + "\n");
                    finalizados.add(evento.getPcb().clone());
                    procesaEstadisticas();
                    evento.getPcb().resetData();
                    libres.add(evento.getPcb());
                    break;
                case BLOQUEO_ES:
                    politica.bloqueoPorEYS(evento.getPcb(), hora, ejecucion, bloqueados);
                    System.out.println("[T:" + hora + "]" + " Bloqueo E/S ==> " + evento.getPcb());
                    view.jTextAreaLog.setText(view.jTextAreaLog.getText() + "[T:" + hora + "]" + " Bloqueo E/S ==> " + evento.getPcb() + "\n");

                    break;
                case FIN_ES:
                    PCB flag = evento.getPcb();
                    flag.setTiempoTotalEYS(flag.getTiempoTotalEYS() + flag.getTiempoEYS());
                    politica.finEYS(evento.getPcb(), hora, enEYS, listos, ejecucion);
                    System.out.println("[T:" + hora + "]" + " Fin E/S ==> " + evento.getPcb());
                    view.jTextAreaLog.setText(view.jTextAreaLog.getText() + "[T:" + hora + "]" + " Fin E/S ==> " + evento.getPcb() + "\n");

                    break;
                case FIN_SIMULACION:
                    try {
                        lectorProcesos.close();
                    } catch (IOException ex) {
                    }
                    System.out.println("--------------Fin Simulacion---------------");
                    view.jTextAreaLog.setText(view.jTextAreaLog.getText() + "--------------Fin Simulacion---------------" + "\n");
                    this.view.diagrama.pintar = false;
                    salir = true;
                    break;
            }

            if (ejecucion.isEmpty() && !listos.isEmpty()) {
                listos.get(0).setEstado(EstadoPCB.EJECUCION);
                ejecucion.add(listos.get(0));
                listos.remove(0);
                System.out.println("[T:" + hora + "]" + " Ejecuta Proceso ==> " + ejecucion.get(0));
                view.jTextAreaLog.setText(view.jTextAreaLog.getText() + "[T:" + hora + "]" + " Ejecuta Proceso ==> " + ejecucion.get(0) + "\n");
            }

            if (!bloqueados.isEmpty() && enEYS.isEmpty()) {
                bloqueados.get(0).setEstado(EstadoPCB.EYS);
                enEYS.add(bloqueados.get(0));
                bloqueados.remove(0);
                System.out.println("[T:" + hora + "]" + " Inicio E/S ==> " + enEYS.get(0));
                view.jTextAreaLog.setText(view.jTextAreaLog.getText() + "[T:" + hora + "]" + " Inicio E/S ==> " + enEYS.get(0) + "\n");

            }
        }

    }

    public Evento siguienteEvento() {
        Evento evento;
//        ProcStat();
        while (true) {
            // Entrada de proceso
            if (procesoFlag != null && hora >= procesoFlag.getHoraLlegada()) {
                evento = new Evento();
                evento.setTipo(TipoEvento.ENTRADA_NUEVO_PROCESO);
                evento.setProceso(procesoFlag);
                evento.setReloj(hora);
                return evento;
            }

            // Fin de proceso
            if (!ejecucion.isEmpty()
                    && ejecucion.get(0).estaTerminado()) {
                evento = new Evento();
                evento.setTipo(TipoEvento.FIN_PROCESO_ACTIVO);
                evento.setPcb(ejecucion.get(0));
                evento.setReloj(hora);
                return evento;
            }

            // Peticion de E/S
            if (!ejecucion.isEmpty()
                    && !ejecucion.get(0).estaTerminado()
                    && ejecucion.get(0).requiereEYS()) {
                evento = new Evento();
                evento.setTipo(TipoEvento.BLOQUEO_ES);
                evento.setPcb(ejecucion.get(0));
                evento.setReloj(hora);
                return evento;
            }

            // Tiempo excedido
            if (!ejecucion.isEmpty()
                    && ejecucion.get(0).getConsParUsoContCPU() >= Parametros.QUANTUM) {
                evento = new Evento();
                evento.setTipo(TipoEvento.FIN_QUANTUM);
                evento.setPcb(ejecucion.get(0));
                evento.setReloj(hora);
                return evento;
            }

            // Fin de E/S
            if (!enEYS.isEmpty()
                    && enEYS.get(0).getConsumidoEYS() >= Parametros.TIO) {
                evento = new Evento();
                evento.setTipo(TipoEvento.FIN_ES);
                evento.setPcb(enEYS.get(0));
                evento.setReloj(hora);
                return evento;
            }
            // Fin simulacion
            if (procesoFlag == null
                    && ejecucion.isEmpty() && listos.isEmpty()
                    && bloqueados.isEmpty() && enEYS.isEmpty()) {
                evento = new Evento();
                evento.setTipo(TipoEvento.FIN_SIMULACION);
                evento.setProceso(procesoFlag);
                evento.setReloj(hora);
                return evento;
            }

            hora++;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
            }
            view.jLabelTime.setText(hora + "");

            view.diagrama.tiempo = hora;
            view.diagrama.pcbs = obtenerPCBs();
            view.diagrama.dibujarEstados();
            view.diagrama.repaint();
//            view.diagrama.repaint();
            if (!ejecucion.isEmpty()) {
                PCB procesoEjecucion = ejecucion.get(0);
                procesoEjecucion.setConsumidoUsoCPU(procesoEjecucion.getConsumidoUsoCPU() + 1);
                procesoEjecucion.setConsParUsoContCPU(procesoEjecucion.getConsParUsoContCPU() + 1);
                Helper.incrementaEsperaListo(listos);
            } else {
                tiempoOcio++;
            }

            if (!enEYS.isEmpty()) {
                PCB procesoEYS = enEYS.get(0);
                procesoEYS.setConsumidoEYS(procesoEYS.getConsumidoEYS() + 1);
                tiempoEYS++;
                Helper.incrementaEsperaBloqueado(bloqueados);
            }
            view.jLabelIdle.setText(tiempoOcio + "");
            view.jLabelBusy.setText((hora - tiempoOcio) + "");

        }
    }

    public ArrayList<PCB> obtenerPCBs() {
        ArrayList<PCB> pcbs = new ArrayList();

        pcbs.addAll(listos);
        pcbs.addAll(bloqueados);
        pcbs.addAll(ejecucion);
        pcbs.addAll(enEYS);

        return pcbs;
    }

    public Proceso leerProceso() {
        Proceso p;
        String data[];

        p = null;

        try {
            data = lectorProcesos.readLine().split(" ");

            if (data != null) {
                long horaEntra = Long.parseLong(data[0]);
                long totalCPU = Long.parseLong(data[1]);
                long usoContinuoCPU = Long.parseLong(data[2]);

                p = new Proceso(horaEntra, totalCPU, usoContinuoCPU, TipoProceso.valueOf(data[3]));
            }
        } catch (IndexOutOfBoundsException ex) {
            System.out.println("Error en formato de archivo de carga");
        } catch (NullPointerException | IOException ex) {
            p = null;
        }
        return p;
    }

    public int[] obtenerEstados() {
        ArrayList<PCB> pcbs;
        pcbs = new ArrayList();

        for (PCB pcb : listos) {
            Helper.insertarOrdenadoPorHoraLLegada(pcbs, pcb);
        }

        for (PCB pcb : bloqueados) {
            Helper.insertarOrdenadoPorHoraLLegada(pcbs, pcb);
        }

        for (PCB pcb : ejecucion) {
            Helper.insertarOrdenadoPorHoraLLegada(pcbs, pcb);
        }

        for (PCB pcb : enEYS) {
            Helper.insertarOrdenadoPorHoraLLegada(pcbs, pcb);
        }

        return extraerEstados(pcbs);
    }

    private int[] extraerEstados(ArrayList<PCB> pcbs) {
        int[] estados = new int[Parametros.MAX_PCB];

        for (int i = 0; i < pcbs.size(); i++) {
            estados[i] = pcbs.get(i).getEstado();
        }

        for (int i = pcbs.size(); i < Parametros.MAX_PCB; i++) {
            estados[i] = -1;
        }

        return estados;
    }

    public void clearData() {
        PCB.NEXT_PID = 0;
        listos.clear();
        bloqueados.clear();
        ejecucion.clear();
        enEYS.clear();
        finalizados.clear();
        hora = 0;
        tiempoEYS = 0;
        tiempoOcio = 0;
        try {
            lectorProcesos.close();
        } catch (IOException ex) {
            lectorProcesos = null;
        }
        iniciarLectura();
    }

    public static void main(String[] args) {
//        Proceso p1 = new Proceso(0, 10, 5, TipoProceso.TR);
//        Proceso p2 = new Proceso(5, 15, 10, TipoProceso.TR);
//        Proceso p3 = new Proceso(10, 60, 5, TipoProceso.SYS);
//        Proceso p4 = new Proceso(20, 1699, 5, TipoProceso.BAT);
//        Proceso p5 = new Proceso(30, 40, 5, TipoProceso.TR);
//        Proceso p6 = new Proceso(30, 10, 5, TipoProceso.SYS);
//        Proceso p7 = new Proceso(30, 1000, 5, TipoProceso.INT);
//        Proceso p8 = new Proceso(30, 100, 5, TipoProceso.BAT);
//        
//        PCB pcb1 =  new PCB(p1);
//        PCB pcb2 =  new PCB(p2);
//        PCB pcb3 =  new PCB(p3);
//        PCB pcb4 =  new PCB(p4);
//        PCB pcb5 =  new PCB(p5);
//        PCB pcb6 =  new PCB(p6);
//        PCB pcb7 =  new PCB(p7);
//        PCB pcb8 =  new PCB(p8);
//        pcb7.setConsumidoUsoCPU(998);
//        
//        ArrayList<PCB> pcbs = new ArrayList();
//        
//        Helper.insertarOrdenadoPorUsoCPU(pcbs, pcb1);
//        Helper.insertarOrdenadoPorUsoCPU(pcbs, pcb2);
//        Helper.insertarOrdenadoPorUsoCPU(pcbs, pcb3);
//        Helper.insertarOrdenadoPorUsoCPU(pcbs, pcb4);
//        
//        Helper.insertarOrdenadoPorUsoCPU(pcbs, pcb5);
//        Helper.insertarOrdenadoPorUsoCPU(pcbs, pcb6);
////        Helper.insertarOrdenadoPorUsoCPU(pcbs, pcb7);
//        Helper.insertarOrdenadoPorUsoCPU(pcbs, pcb8);
//        
//        mostrar_elementos(pcbs);
//        
//        System.out.println("TR TR " + TipoProceso.TR.compareTo(TipoProceso.TR));
//        System.out.println("TR SYS " + TipoProceso.SYS.compareTo(TipoProceso.TR));
//        
//        Parametros.QUANTUM = Integer.MAX_VALUE;
        Planificador p = new Planificador(new PlanificadorGUI());

//        ArrayList<PCB> pcbs = new ArrayList(Parametros.MAX_PCB);
//        System.out.println(pcbs.get(0));
    }

    public static void mostrar_elementos(Collection coleccion) {
        Iterator iterador = coleccion.iterator();
        while (iterador.hasNext()) {
            PCB elemento = (PCB) iterador.next();
            System.out.println("\tPID: " + elemento.getPID());
        }
        System.out.println();
    }

    private ArrayList<PCB> generarPCBsLibres(int MAX_PCB) {
        ArrayList<PCB> pcbs = new ArrayList();
        for (int i = 0; i < MAX_PCB; i++) {
            pcbs.add(new PCB());
        }
        return pcbs;
    }

    public void ProcStat() {
        System.out.println("PCBs libres: " + libres.size());
        System.out.println("CPU: ");
        if (!ejecucion.isEmpty()) {
            System.out.println(ejecucion.get(0));
        } else {
            System.out.println("IDLE");
        }
        System.out.println("\nLIST:");
        if (!listos.isEmpty()) {
            mostrar_elementos(listos);
        } else {
            System.out.println("NULL");
        }
        System.out.println("\nBLQ:");
        if (!bloqueados.isEmpty()) {
            mostrar_elementos(bloqueados);
        } else {
            System.out.println("NULL");
        }
        System.out.println("");

    }

    public void procesaEstadisticas() {
        PCB p = finalizados.get(finalizados.size()-1);
        cont++;
        acuWait+=p.getTiempoEsperaListo();
        meanWait = acuWait/cont;
        
        acuBlock+=p.getTiempoEsperaBloqueado();
        meanBlock = acuBlock/cont;
        
        acuExec+=p.getUsoCPU();
        meanExec = acuExec/cont;
        
        
        stdWait = Helper.calculaSTDListo(finalizados, meanWait, cont);
        stdBlock = Helper.calculaSTDBloqueado(finalizados, meanBlock, cont);
        stdExec= Helper.calculaSTDCPU(finalizados, meanExec, cont);
        
        if (minWait > p.getTiempoEsperaListo()) {
            minWait = p.getTiempoEsperaListo();
        }
        
        if (maxWait < p.getTiempoEsperaListo()) {
            maxWait = p.getTiempoEsperaListo();
        }
        
        if (minBlock > p.getTiempoEsperaBloqueado()) {
            minBlock = p.getTiempoEsperaBloqueado();
        }
        
        if (maxBlock < p.getTiempoEsperaBloqueado()) {
            maxBlock = p.getTiempoEsperaBloqueado();
        }
        
        if (minExec > p.getUsoCPU()) {
            minExec = p.getUsoCPU();
        }
        
        if (maxExec < p.getUsoCPU()) {
            maxExec = p.getUsoCPU();
        }
        
        view.jLabelWaitMin.setText(minWait + "");
        view.jLabelWaitMax.setText(maxWait + "");
        view.jLabelWaitMean.setText(Math.rint(meanWait * 100) / 100 + "");
        view.jLabelWaitStd.setText(Math.rint(stdWait * 100) / 100 + "");
        
        view.jLabelBlockedMin.setText(minBlock + "");
        view.jLabelBlockedMax.setText(maxBlock + "");
        view.jLabelBlockedMean.setText(Math.rint(meanBlock * 100) / 100 + "");
        view.jLabelBlockedStd.setText(Math.rint(stdBlock * 100) / 100 + "");
        
        view.jLabelExecMin.setText(minExec + "");
        view.jLabelExecMax.setText(maxExec + "");
        view.jLabelExecMean.setText(Math.rint(meanExec * 100) / 100 + "");
        view.jLabelExecStd.setText(Math.rint(stdExec * 100) / 100 + "");

    }
}
