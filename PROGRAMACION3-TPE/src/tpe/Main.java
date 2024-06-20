package tpe;

import tpe.utils.Solucion;
import tpe.utils.Tarea;

import java.util.List;
public class Main {
	public static void main(String[] args) {

		Servicio servicio = new Servicio("./src/tpe/datasets/Procesadores.csv", "./src/tpe/datasets/Tareas.csv");
		int tiempoMaximo = 110;

		System.out.println("Prueba Servicio 1:");
		Tarea tarea1;
		tarea1 = servicio.servicio1("T1");
		System.out.println(tarea1 + "\n");

		System.out.println("Prueba Servicio 2:");
		boolean criticidad = true;
		List<Tarea> tareasCriticas = servicio.servicio2(criticidad);
		System.out.println("Tareas encontradas con criticidad: " + criticidad);
		for (Tarea tarea : tareasCriticas) {
			System.out.println(tarea.getNombre() + " - Criticidad: " + tarea.isCritica());
		}
		System.out.println("\n");

		System.out.println("Prueba Servicio 3:");
		int prioridadInferior = 30;
		int prioridadSuperior = 70;

		List<Tarea> tareasEnPrioridad = servicio.servicio3(prioridadInferior, prioridadSuperior); {
			if (tareasEnPrioridad != null) {
				System.out.println("Tareas encontradas entre las prioridades " + prioridadInferior + " y " + prioridadSuperior);
				for (Tarea tarea : tareasEnPrioridad) {
					System.out.println(tarea.getNombre() + " - Prioridad: " + tarea.getPrioridad());
				}
			}
			System.out.println("\n");
		}


		//Prueba seleccion por Backtracing y Greedy
		Seleccion seleccion1 = new Seleccion(servicio.getProcesadores(), servicio.getTareas());
		Seleccion seleccion2 = new Seleccion(servicio.getProcesadores(), servicio.getTareas());

		Solucion solb = new Solucion(seleccion1.backtracking(tiempoMaximo));
		solb.mostrarResultado();
		System.out.println("\n");
		Solucion solg = new Solucion(seleccion2.greedy(tiempoMaximo));
		solg.mostrarResultado();

	}
//FIN MAIN
}
