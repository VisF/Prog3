package tpe.utils;
import java.util.List;
public class Solucion {
    private List<List<Tarea>> asignaciones;
    private int tiempoMaximo;

    public Solucion(List<List<Tarea>> asignaciones, int tiempoMaximo) {
        this.asignaciones = asignaciones;
        this.tiempoMaximo = tiempoMaximo;
    }

    public List<List<Tarea>> getAsignaciones() {
        return asignaciones;
    }

    public int getTiempoMaximo() {
        return tiempoMaximo;
    }

    @Override
    public String toString() {
        String result = "Tiempo máximo de ejecución: " + tiempoMaximo + "\n";
        for (int i = 0; i < asignaciones.size(); i++) {
            result += "Procesador " + (i + 1) + ": " + asignaciones.get(i) + "\n";
        }
        return result;
    }
}
