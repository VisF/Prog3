package tpe.utils;

import java.util.ArrayList;
import java.util.List;
public class Procesador {
	private String id;
	private String codigo;
	private boolean refrigerado;
	private int anio;
	private List<Tarea> tareas;
	private int tareasCriticas;
	
	public Procesador(String id, String codigo, boolean refrigerado, int anio) {
		this.id = id;
		this.codigo = codigo;
		this.refrigerado = refrigerado;
		this.anio = anio;
		this.tareas = new ArrayList<>();
		tareasCriticas = 0;
	}
	public boolean asignarTarea(Tarea tarea){
		if(tarea.isCritica() && tareasCriticas>2){

			return false;
		}

		tareas.add(tarea);
		tarea.asignar();
		if(tarea.isCritica()){
			tareasCriticas++;
		}
		return true;

	}
	public void removerTarea(Tarea tarea){
		tareas.remove(tarea);
		tarea.desasignar();
		if(tarea.isCritica()){
			tareasCriticas--;
		}
	}
	public int tiempoTotal(){
		int tiempo = 0;
		for(Tarea tarea: tareas){
			tiempo += tarea.getTiempo();
		}
		return tiempo;
	}
	public String toString() {
		return "Procesador {id='" + id + "', Codigo ='" + codigo + "', Es refrigerado =" + refrigerado +
				", anio =" + anio  + "}";
	}
	public List<Tarea> getTareas() {
		return tareas;
	}
	public boolean isRefrigerado() {
		return refrigerado;
	}

	public void setRefrigerado(boolean refrigerado) {
		this.refrigerado = refrigerado;
	}

	public String getId() {
		return id;
	}

	public String getCodigo() {
		return codigo;
	}

	public int getAnio() {
		return anio;
	}
	
	
}
