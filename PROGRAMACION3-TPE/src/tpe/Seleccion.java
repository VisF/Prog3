package tpe;

import tpe.utils.ComparadorTiempoEjecucion;
import tpe.utils.Procesador;
import tpe.utils.Solucion;
import tpe.utils.Tarea;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Seleccion {
    private ArrayList<Tarea> tareasPendientes;
    private ArrayList<Procesador> procesadoresPendientes;
    private int estadosGenerados;
    private Solucion mejorSolucion;
    private int candidatosConsiderados;
    private Comparator<Tarea> comparador;
    private boolean solucionBack;

    public Seleccion(ArrayList<Procesador> procesadores, ArrayList<Tarea> tareas) {
        this.procesadoresPendientes = new ArrayList<>(procesadores);
        this.tareasPendientes = new ArrayList<>(tareas);
        this.estadosGenerados = 0;
        this.mejorSolucion = null;
        this.candidatosConsiderados = 0;
        this.solucionBack = false;
        this.comparador = new ComparadorTiempoEjecucion();
        tareasPendientes.sort(comparador);
    }


    /*
	* Método principal de backtracking, se encarga de iniciar el proceso.
    * Genera listas para verificar que no queden tareas sin asignar.
    * Complejidad de O(n!) debido a la recursión del backtracking y el ciclo for.
	*/
    public void backtracking(int tiempoMax){
        //Genero listas para verificar que no queden

        boolean solucionEncontrada = backtrack(tiempoMax, tareasPendientes, procesadoresPendientes, mejorSolucion);
        if(solucionEncontrada) {
            System.out.println("Proceso terminado");
            mostrarResultadoBacktracking();

        }
        else{
            System.out.println("Solucion no valida");
        }

    }

    /*
    * Método privado recursivo para la búsqueda de soluciones usando backtracking.
    * Intenta asignar tareas a procesadores de manera que el tiempo de ejecución total no exceda el tiempo máximo permitido.
    */
    private boolean backtrack(int tiempoMax, ArrayList<Tarea> tareasExtra, ArrayList<Procesador> procPendientes, Solucion solucionActual) {
        if (tareasExtra.isEmpty()) {
            solucionActual = actualizarMejorSolucion();
            if (mejorSolucion == null || esMejorSolucion(solucionActual)) {
                mejorSolucion = solucionActual;
            }
            return true;
        }
        Tarea tarea = null;
        ArrayList<Tarea> tareasRestantes = null;
        tarea = tareasExtra.get(0);
        tareasRestantes = new ArrayList<>(tareasExtra.subList(1, tareasExtra.size())); //Reestructuracion del arraylist para que quede el siguiente elemento en la posicion 0
        //procPendientes.sort(comparadorProcesadores);//ACA CAMBIAR, NO ORDENAR SINO ELEGIR
        boolean seEncontroSolucion = false;
        for (int i = 0; i < procPendientes.size(); i++) {
            Procesador procesador = procPendientes.get(i);
            if (puedeAsignarTarea(procesador, tarea, tiempoMax)) {
                procesador.asignarTarea(tarea);
                estadosGenerados++;
                solucionBack = backtrack(tiempoMax, tareasRestantes, procPendientes, solucionActual);
                if (solucionBack) {
                    seEncontroSolucion = true;
                }
                procesador.removerTarea(tarea);
            }
        }
        return seEncontroSolucion;
    }

    private boolean esMejorSolucion(Solucion actual){
        return actual.getTiempoMaximo()<mejorSolucion.getTiempoMaximo();
    }

    // ESTE METODO CONTROLA LA POSIBILIDAD DE ASIGNAR UNA TAREA SI NO ESTA RESTRINGIDA
    private boolean puedeAsignarTarea(Procesador procesador, Tarea tarea, int tiempoMax){ //Complejidad O(1) solo hay que hacer una cuenta
        if(tarea == null){
            return false;
        }
        int tiempoTotal = procesador.tiempoTotal() + tarea.getTiempo();
        if (tarea.isCritica() && (procesador.tareasCriticas()>=2) || (tiempoTotal > tiempoMax && !procesador.isRefrigerado())) {
            return false;
        }
        else {
            return true;
        }
    }

    private Solucion actualizarMejorSolucion() { //Complejidad O(n) donde son todos los procesadores
        int tiempoMaximo = calcularTiempoMaximo();
        List<List<Tarea>> asignaciones = new ArrayList<>();

        for (Procesador procesador : procesadoresPendientes) {
            asignaciones.add(new ArrayList<>(procesador.getTareas()));
        }
        Solucion  sol = new Solucion(asignaciones, tiempoMaximo);
        if(mejorSolucion== null ||esMejorSolucion(sol)){
            mejorSolucion = sol;
        }
        return sol;

    }

    private int calcularTiempoMaximo(){ //Complejidad O(n) donde n son todos los procesadores
        int maxTiempo = 0;
        for(Procesador procesador: procesadoresPendientes){
            int tiempoProc = Math.max(maxTiempo, procesador.tiempoTotal());
            if(tiempoProc>maxTiempo){
                maxTiempo = tiempoProc;
            }
        }
        return maxTiempo;
    }

    public void mostrarResultadoBacktracking(){ //Complejidad O(n + m) donde n son los procesadores y m las tareas, solo una vez ya que las tareas no se repiten
        if(mejorSolucion == null){
            System.out.println("No se encontró una solución valida.");


        }
        System.out.println("Solucion obtenida");
        List<List<Tarea>> asignaciones =  mejorSolucion.getAsignaciones();
        for(int i=0;i<asignaciones.size();i++) {
            List<Tarea> asignacion = asignaciones.get(i);
            for(Tarea tarea : asignacion) {
                System.out.println("Procesador " + (i + 1) + " : " + tarea);
            }

        }
        System.out.println("Tiempo maximo de ejecucion: " + mejorSolucion.getTiempoMaximo());
        System.out.println("Cantidad de estados generados: " + estadosGenerados);
    }

    /*
     * Método principal para la asignación de tareas usando el algoritmo Greedy.
     * Selecciona siempre la mejor opción disponible en el momento, sin considerar todas las combinaciones posibles.
     */
    public void greedy(int tiempoMax){
        ArrayList<Tarea> tareasTotales= new ArrayList<>(this.tareasPendientes);
        tareasTotales.sort(comparador);
        if(tareasTotales.isEmpty()){
            System.out.println("Proceso terminado");
            return;
        }
        candidatosConsiderados = 0;
        while(!(tareasTotales.size() ==0)){
            candidatosConsiderados++;
            Tarea tarea = tareasTotales.remove(0);
            Procesador mejorProc = obtenerMejorProcesador(tiempoMax, tarea);
            if (mejorProc != null){
                mejorProc.asignarTarea(tarea);
            }
            else{
                System.out.println("No se pudo asignar la tarea:" + tarea);
            }
        }
        actualizarMejorSolucion();
        mostrarResultadoGreedy();
    }

    /*
    * Método para obtener el mejor procesador disponible para una tarea específica.
    * Considera el tiempo total de ejecución y si el procesador está refrigerado.
    */
    private Procesador obtenerMejorProcesador(int tiempoMax, Tarea tarea){
        Procesador mejorProc = null;
        int menorTiempoTotal = Integer.MAX_VALUE;

        for(Procesador procesador: procesadoresPendientes){
            candidatosConsiderados++;
            int tiempoTotal = procesador.tiempoTotal() + tarea.getTiempo();
            if(tiempoTotal<=tiempoMax  ||  procesador.isRefrigerado()){
                if(tiempoTotal< menorTiempoTotal){
                    menorTiempoTotal = tiempoTotal;
                    mejorProc = procesador;
                }
            }
        }
        return mejorProc;
    }

    public void mostrarResultadoGreedy() {
        if (mejorSolucion == null) {
            System.out.println("No se encontró una solución válida.");
            return;
        }
        System.out.println("Solución obtenida (Greedy):");
        List<List<Tarea>> asignaciones = mejorSolucion.getAsignaciones();
        for (int i = 0; i < asignaciones.size(); i++) {
            List<Tarea> asignacion = asignaciones.get(i);
            for (Tarea tarea : asignacion) {
                System.out.println("Procesador " + (i + 1) + ": " + tarea);
            }
        }
        System.out.println("Tiempo máximo de ejecución: " + mejorSolucion.getTiempoMaximo());
        System.out.println("Cantidad de candidatos considerados: " + candidatosConsiderados);
    }
}
