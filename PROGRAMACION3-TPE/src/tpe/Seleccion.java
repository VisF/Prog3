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
    private final Comparator<Tarea> comparador;
    private boolean solucionBack;

    public Seleccion(ArrayList<Procesador> procesadores, ArrayList<Tarea> tareas) {
        this.procesadoresPendientes = new ArrayList<>(procesadores);
        this.tareasPendientes = new ArrayList<>(tareas);
        this.estadosGenerados = 0;
        this.mejorSolucion = null;
        this.candidatosConsiderados = 0;
        this.solucionBack = false;
        this.comparador = new ComparadorTiempoEjecucion();
        //tareasPendientes.sort(comparador);
    }


    /*
	* Método principal de backtracking, se encarga de iniciar el proceso.
    * Genera listas para verificar que no queden tareas sin asignar.
    * Complejidad de O(n!) debido a la recursión del backtracking y el ciclo for.
    *
    * Luego, invoca al método recursivo `backtrack`, el cual intenta encontrar una solución
    * dentro del tiempo máximo permitido. Si retorna true, se ha encontrado una solución válida.
    * Se actualiza el estado de la mejor solución encontrada y se retorna dicha solución.
    * Si retorna false, significa que no se ha encontrado ninguna solución válida.
	*/
    public Solucion backtracking(int tiempoMax){
        //Genero listas para verificar que no queden tareas sin asignar
        boolean solucionEncontrada = backtrack(tiempoMax, tareasPendientes, procesadoresPendientes, mejorSolucion);
        if(!solucionEncontrada) {
            System.out.println("Solucion no valida");
        }
        else{
            System.out.println("Proceso backtracking terminado");
            mejorSolucion.setEstado(estadosGenerados);
            return mejorSolucion;
        }
        return null;
    }

    /*
    * Método privado para la búsqueda de soluciones con algoritmo backtracking.
    * Intenta asignar tareas a procesadores de manera que el tiempo de ejecución total no exceda el tiempo máximo permitido.
    * - tiempoMax El tiempo máximo permitido para procesar las tareas.
    * - tareasExtra Lista de tareas que aún no han sido asignadas a un procesador.
    * - procPendientes Lista de procesadores disponibles para asignar tareas.
    * - solucionActual La solución actual que se está construyendo.
    *
    * Si la lista de tareas pendientes (`tareasExtra`) está vacía, significa que todas las tareas han sido asignadas:
    *   Se actualiza la mejor solución encontrada hasta el momento.
    *   Si no hay una mejor solución previamente registrada o si la solución actual es mejor que la registrada,
    *      se actualiza `mejorSolucion`.
    *   Retorna true indicando que se ha encontrado una solución válida.
    *
    * Se selecciona la primera tarea de la lista y se genera una nueva lista (`tareasRestantes`) con el
    *   resto de tareas pendientes.
    * Se intenta asignar la tarea seleccionada a cada procesador disponible:
    * Si el procesador puede asignar la tarea dentro del tiempo máximo permitido (`puedeAsignarTarea`) y su cantidad de
    *   tareas criticas es menor a 2 la tarea se asigna al procesador.
    *   Se incrementa el contador de estados generados (`estadosGenerados`).
    *   Se realiza una llamada recursiva a `backtrack` con la lista actualizada de tareas y la solución actual.
    *
    * Si la llamada retorna true, se ha encontrado una solución válida y se actualiza la variable `seEncontroSolucion`.
    * Si la llamada retorna false, se interrumpe el ciclo ya que no se puede encontrar una solución válida con la
    *     asignación actual.
    *   La tarea se remueve del procesador para intentar con otros procesadores.
    *   Retorna true si se encuentra una solución válida en alguna de las llamadas o false en caso contrario.
     */
    private boolean backtrack(int tiempoMax, ArrayList<Tarea> tareasExtra, ArrayList<Procesador> procPendientes, Solucion solucionActual) {
        if (tareasExtra.isEmpty()) {
            solucionActual = actualizarMejorSolucion();
            if (mejorSolucion == null || esMejorSolucion(solucionActual)) {
                mejorSolucion = solucionActual;
            }
            return true;
        }
        ArrayList<Tarea> tareasRestantes = null;
        Tarea tarea = tareasExtra.get(0);
        //Reestructuracion del arraylist para que quede el siguiente elemento en la posicion 0
        tareasRestantes = new ArrayList<>(tareasExtra.subList(1, tareasExtra.size()));
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
                else {
                    break;
                }
                procesador.removerTarea(tarea);
            }
        }
        return seEncontroSolucion;
    }

    //Devuelve true si la solucion por parametro es mejor que la solucion anterior
    private boolean esMejorSolucion(Solucion actual){
        return actual.getTiempoMaximo()<mejorSolucion.getTiempoMaximo();
    }

    // Si la tarea cumple con las restricciones, devuelvo true para poder asignarla
    //Complejidad O(1)
    private boolean puedeAsignarTarea(Procesador procesador, Tarea tarea, int tiempoMax){
        if(tarea == null){
            return false;
        }
        int tiempoTotal = procesador.tiempoTotal() + tarea.getTiempo();
        return (!tarea.isCritica() || (procesador.tareasCriticas() < 2)) && (tiempoTotal <= tiempoMax || procesador.isRefrigerado());
    }

    //Complejidad O(n) donde n son todos los procesadores
    //Este metodo se encarga de generar un objeto "Solucion"
    private Solucion actualizarMejorSolucion() {
        int tiempoMaximo = calcularTiempoMaximo();
        List<List<Tarea>> asignaciones = new ArrayList<>();
        for (Procesador procesador : procesadoresPendientes) {
            asignaciones.add(new ArrayList<>(procesador.getTareas()));
        }
        Solucion sol = new Solucion(asignaciones, tiempoMaximo, estadosGenerados);
        if(mejorSolucion == null || esMejorSolucion(sol)){
            mejorSolucion = sol;
        }
        return sol;
    }

    //Complejidad O(n) donde n son todos los procesadores
    private int calcularTiempoMaximo(){
        int maxTiempo = 0;
        for(Procesador procesador: procesadoresPendientes){
            int tiempoProc = Math.max(maxTiempo, procesador.tiempoTotal());
            if(tiempoProc > maxTiempo){
                maxTiempo = tiempoProc;
            }
        }
        return maxTiempo;
    }

    //----------------------------------------------------------------------------------------------------------

    /*
     * Método principal para la asignación de tareas usando el algoritmo Greedy.
     * Selecciona siempre la mejor opción disponible en el momento, sin considerar todas las combinaciones posibles.
     * Estrategia: Se ordenan todas las tareas de menor a mayor. Se toma y remueve la primera y se asigna al mejor
     * procesador disponible, si no se imprime un mensaje.
     * Una vez asignadas todas las tareas, se actualiza la mejor solucion encontrada
     */
    public Solucion greedy(int tiempoMax){
        ArrayList<Tarea> tareasTotales= new ArrayList<>(this.tareasPendientes);
        tareasTotales.sort(comparador);
        if(tareasTotales.isEmpty()){
            System.out.println("Proceso Greedy terminado");
            return null;
        }
        estadosGenerados = 0;
        while(!(tareasTotales.isEmpty())){
            estadosGenerados++;
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
        return mejorSolucion;
    }

    /*
    * Método para obtener el mejor procesador disponible para una tarea específica.
    * Considera el tiempo total de ejecución y si el procesador está refrigerado.
    */
    private Procesador obtenerMejorProcesador(int tiempoMax, Tarea tarea){
        Procesador mejorProc = null;
        int menorTiempoTotal = Integer.MAX_VALUE;

        for(Procesador procesador: procesadoresPendientes){
            estadosGenerados++;
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

}
