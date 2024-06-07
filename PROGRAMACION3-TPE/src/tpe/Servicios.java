package tpe;


import tpe.utils.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.HashMap;


/**
 * NO modificar la interfaz de esta clase ni sus métodos públicos.
 * Sólo se podrá adaptar el nombre de la clase "Tarea" según sus decisiones
 * de implementación.
 */
public class Servicios {
	private HashMap<String, Procesador> procesadores;
	private HashMap<String, Tarea> tareas;
	private List<Tarea> notCritica;
	private List<Tarea> sonCritica;
	private ArrayList<Tarea> tareasPendientes;
	private final int MAXPRIORIDAD = 0;
	private final int MINPRIORIDAD = 0;
	private int estadosGenerados;
	private Solucion mejorSolucion;
	private int candidatosConsiderados;
	private Comparator<Tarea> comparador;
	private Comparator<Procesador> comparadorProcesadores;
	private boolean solucionBack;
	/*
     * Expresar la complejidad temporal del constructor.
     *
     * La complejidad es de O(n + m*2) donde n son los procesadores y m son las tareas, *2 porque son señaladas de dos maneras distintas, por hashmap y por arraylist
     * El resto de los componentes es de 0(1) asique es despreciable
     */
	public Servicios(String pathProcesadores, String pathTareas)
	{
		CSVReader reader = new CSVReader();
		this.procesadores = new HashMap<>();
		this.tareas = new HashMap<>();
		this.notCritica = new ArrayList<>();
		this.sonCritica = new ArrayList<>();
		this.comparador = new ComparadorTiempoEjecucion();
		this.comparadorProcesadores = new ComparadorTiempoTotal();
		tareas = reader.readTasks(pathTareas);
		procesadores = reader.readProcessors(pathProcesadores);
		this.tareasPendientes = new ArrayList<>(tareas.values());
		tareasPendientes.sort(comparador);
		if (this.tareas == null || this.tareas.isEmpty()) {
			throw new IllegalArgumentException("No se pudieron cargar las tareas desde el archivo.");
		}
		if (this.procesadores == null || this.procesadores.isEmpty()) {
			throw new IllegalArgumentException("No se pudieron cargar los procesadores desde el archivo.");
		}

		for(Tarea tarea: tareas.values()) {
			if(!tarea.isCritica()) {
				notCritica.add(tarea);
			}
		}
		for(Tarea tarea: tareas.values()) {
			if(tarea.isCritica()) {
				sonCritica.add(tarea);
			}
		}

		this.estadosGenerados = 0;
		this.mejorSolucion = null;
		this.candidatosConsiderados = 0;
		this.solucionBack = false;

		
	}
	
	/*
     * Expresar la complejidad temporal del servicio 1.
     *
     * El servicio1 tiene una complejidad de O(1) ya que gracias al hashmap accede directamente al espacio de memoria de ID
     *
     */
	public Tarea servicio1(String ID) {	
		if(tareas.containsKey(ID)) {
			Tarea tarea = tareas.get(ID);
			return new Tarea(tarea.getId(),
							tarea.getNombre(),
							tarea.getTiempo(),
							tarea.isCritica(),
							tarea.getPrioridad());
		}
		return null;
	}
    /*
     * Expresar la complejidad temporal del servicio 2.
     *
     * El servicio2 tiene una complejidad de O(1) ya que la lista esta prearmada en el constructor
     *
     *
     *
     */
	public List<Tarea> servicio2(boolean esCritica) {
		if(esCritica){
			return sonCritica;
		}
		else if(esCritica==false){
			return notCritica;
		}

		return null;
	}
    /*
     * Expresar la complejidad temporal del servicio 3.
     * El servicio2 tiene una complejidad de O(n) donde n son todas las tareas que recorrer
     */
	public List<Tarea> servicio3(int prioridadInferior, int prioridadSuperior) {
		if (prioridadInferior < prioridadSuperior && prioridadInferior > MINPRIORIDAD && prioridadSuperior < MAXPRIORIDAD) {

			return null;
		}
		List<Tarea> tareasPorPrioridad = new ArrayList<>();
		for(Tarea tarea: tareas.values()) {
			if(tarea.getPrioridad()>=prioridadInferior &&tarea.getPrioridad()<=prioridadSuperior) {
				tareasPorPrioridad.add(tarea);
			}
		}
		return tareasPorPrioridad;
	} /*
	Entre la funcion backtracking (publica) y la funcion backtrack(privada) tienen una complejidad
	de O(n!) entre la recursion del backtracking y el ciclo for
	*/
	public void backtracking(int tiempoMax){


		 //Genero listas para verificar que no queden
		ArrayList<Procesador> procesadoresPendientes = new ArrayList<>(procesadores.values());
		boolean solucionEncontrada = backtrack(tiempoMax, tareasPendientes, procesadoresPendientes, mejorSolucion);
		if(solucionEncontrada) {
			System.out.println("Proceso terminado");
			mostrarResultadoBacktracking();

		}
		else{
			System.out.println("Solucion no valida");
		}

	}

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

		procPendientes.sort(comparadorProcesadores);

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


	private boolean puedeAsignarTarea(Procesador procesador, Tarea tarea, int tiempoMax){ //Complejidad O(1) solo hay que hacer una cuenta
		if(tarea == null){
			return false;
		}
		int tiempoTotal = procesador.tiempoTotal() + tarea.getTiempo();
		if(tiempoTotal> tiempoMax && !procesador.isRefrigerado()){
			return false;
		}
		else{
			return true;
		}
	}

	private Solucion actualizarMejorSolucion() { //Complejidad O(n) donde son todos los procesadores
		int tiempoMaximo = calcularTiempoMaximo();
		List<List<Tarea>> asignaciones = new ArrayList<>();

		for (Procesador procesador : procesadores.values()) {
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
		for(Procesador procesador: procesadores.values()){
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
	public void greedy(int tiempoMax){
		ArrayList<Tarea> tareasTotales= new ArrayList<>(this.tareas.values());
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



	private Procesador obtenerMejorProcesador(int tiempoMax, Tarea tarea){
		Procesador mejorProc = null;
		int menorTiempoTotal = Integer.MAX_VALUE;

		for(Procesador procesador: procesadores.values()){
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
