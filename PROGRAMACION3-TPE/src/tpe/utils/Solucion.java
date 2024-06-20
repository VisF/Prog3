package tpe.utils;
import java.util.List;
public class Solucion {
    private int estado;
    private List<List<Tarea>> asignaciones;
    private int tiempoMaximo;

    public Solucion(List<List<Tarea>> asignaciones, int tiempoMaximo, int estadosGenerados) {
        this.asignaciones = asignaciones;
        this.tiempoMaximo = tiempoMaximo;
        this.estado = estadosGenerados;
    }

    public Solucion (Solucion solucion) {
        this.asignaciones = solucion.getAsignaciones();
        this.tiempoMaximo = solucion.getTiempoMaximo();
        this.estado = solucion.getEstado();
    }

    public List<List<Tarea>> getAsignaciones() {
        return asignaciones;
    }

    public int getTiempoMaximo() {
        return tiempoMaximo;
    }

    public int getEstado () {
        return estado;
    }

    public void setEstado (int n) {
        this.estado = n;
    }

    public void setAsignaciones(List<List<Tarea>> asignaciones) {
        this.asignaciones = asignaciones;
    }

    public void setTiempoMaximo(int tiempoMaximo) {
        this.tiempoMaximo = tiempoMaximo;
    }

    @Override
    public String toString() {
        String result = "Tiempo máximo de ejecución: " + tiempoMaximo + "\n";
        for (int i = 0; i < asignaciones.size(); i++) {
            result += "Procesador " + (i + 1) + ": " + asignaciones.get(i) + "\n";
        }
        return result;
    }

    public void mostrarResultado() {
        //Controlo que haya asignaciones para devolver
        if (this.getAsignaciones() == null) {
            System.out.println("No se encontró una solución valida.");
        }
        System.out.println("Solucion obtenida: ");
        List<List<Tarea>> asignaciones =  this.getAsignaciones();
        for(int i=0;i<asignaciones.size();i++) {
            List<Tarea> asignacion = asignaciones.get(i);
            for(Tarea tarea : asignacion) {
                System.out.println("Procesador " + (i + 1) + " : " + tarea);
            }
        }
        System.out.println("Tiempo maximo de ejecucion: " + this.getTiempoMaximo());
        System.out.println("Cantidad de estados generados / candidatos considerados: " + estado);
    }
}
