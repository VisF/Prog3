package tpe.utils;

public class Tarea {
	private String id;
    private String nombre;
    private int tiempo;
    private boolean critica;
    private int prioridad;
	private boolean asignada;

    public Tarea(String id, String nombre, int tiempo, boolean critica, int prioridad) {
        this.id = id;
        this.nombre = nombre;
        this.tiempo = tiempo;
        this.critica = critica;
        this.prioridad = prioridad;
		this.asignada = false;
    }
    public void asignar(){
		this.asignada = true;
	}
	public void desasignar(){
		this.asignada = false;
	}
	public boolean estaAsignada(){
		return this.asignada;
	}
	public String toString() {
		return "Tarea{id='" + id + "', nombre='" + nombre + "', tiempo=" + tiempo +
				", critica=" + critica + ", prioridad=" + prioridad + "}";
	}
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getTiempo() {
		return tiempo;
	}

	public void setTiempo(int tiempo) {
		this.tiempo = tiempo;
	}

	public boolean isCritica() {
		return critica;
	}

	public void setCritica(boolean critica) {
		this.critica = critica;
	}

	public int getPrioridad() {
		return prioridad;
	}

	public void setPrioridad(int prioridad) {
		this.prioridad = prioridad;
	}

	public String getId() {
		return id;
	}
}
