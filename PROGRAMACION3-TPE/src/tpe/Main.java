package tpe;

import tpe.Servicios;
import tpe.utils.CSVReader;
import tpe.utils.Procesador;
import tpe.utils.Tarea;
import java.util.ArrayList;
import java.util.List;
public class Main {
	public static void main(String args[]) {

		Servicios servicio = new Servicios("./src/tpe/datasets/Procesadores.csv", "./src/tpe/datasets/Tareas.csv");
		int tiempoMaximo = 110;

		//Prueba Servicio 1
		Tarea tarea1;
		tarea1 = servicio.servicio1("T1");
		System.out.println(tarea1);

		//Prueba Servicio 2
		boolean criticidad = false;
		List<Tarea> tareasCriticas = servicio.servicio2(criticidad);
		System.out.println("Tareas encontradas con criticidad: " + criticidad);
		for (Tarea tarea : tareasCriticas) {
			System.out.println(tarea.getNombre() + " - Criticidad: " + tarea.isCritica());
		}

		//Prueba Servicio 3
		int prioridadInferior = 30;
		int prioridadSuperior = 70;

		List<Tarea> tareasEnPrioridad = servicio.servicio3(prioridadInferior, prioridadSuperior);
		{
			if (tareasEnPrioridad != null) {
				System.out.println("Tareas encontradas entre las prioridades " + prioridadInferior + " y " + prioridadSuperior);
				for (Tarea tarea : tareasEnPrioridad) {
					System.out.println(tarea.getNombre() + " - Prioridad: " + tarea.getPrioridad());
				}
			}

			//Prueba seleccion por Backtracing y Greedy
			Seleccion seleccion = new Seleccion(servicio.getProcesadores(), servicio.getTareas());

			seleccion.backtracking(tiempoMaximo);
			seleccion.greedy(tiempoMaximo);
		}
	}
}//FIN MAIN
