package tpe;


import tpe.utils.*;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;


/**
 * NO modificar la interfaz de esta clase ni sus métodos públicos.
 * Sólo se podrá adaptar el nombre de la clase "Tarea" según sus decisiones
 * de implementación.
 */
public class Servicio {
	private HashMap<String, Procesador> procesadores;
	private HashMap<String, Tarea> tareas;
	private List<Tarea> notCritica;
	private List<Tarea> sonCritica;
	private ArrayList<Tarea> tareasPendientes;
	private final int MAXPRIORIDAD = 0;
	private final int MINPRIORIDAD = 0;

	/*
     * Expresar la complejidad temporal del constructor.
     * La complejidad es de O(n + m*2) donde n son los procesadores y m son las tareas, * 2 porque son invocadas
     * de dos maneras distintas, por hashmap y por arraylist
     * El resto de los componentes es de 0(1)
     */
	public Servicio(String pathProcesadores, String pathTareas)
	{
		CSVReader reader = new CSVReader();
		this.procesadores = new HashMap<>();
		this.tareas = new HashMap<>();
		this.notCritica = new ArrayList<>();
		this.sonCritica = new ArrayList<>();
		tareas = reader.readTasks(pathTareas);
		procesadores = reader.readProcessors(pathProcesadores);
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
			else {
				sonCritica.add(tarea);
			}
		}
	}
	
	/*
     * Expresar la complejidad temporal del servicio 1.
     * El servicio1 tiene una complejidad O(1) ya que accede directamente al
     * espacio de memoria del ID (Hashmap)
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
     * El servicio2 tiene una complejidad O(1) ya que la lista se arma en el constructor
     */
	public List<Tarea> servicio2(boolean esCritica) {
		if(esCritica){
			return sonCritica;
		}
		else if(!esCritica){
			return notCritica;
		}
		return null;
	}

    /*
     * Expresar la complejidad temporal del servicio 3.
     * El servicio3 tiene una complejidad de O(n) donde n son todas las tareas que recorre
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
	}

	//devuelve las tareas
	public ArrayList<Tarea> getTareas() {
		return new ArrayList<Tarea>(tareas.values());
	}

	//devuelve los procesadores
	public ArrayList<Procesador> getProcesadores() {
		return new ArrayList<Procesador>(procesadores.values());
	}
}
