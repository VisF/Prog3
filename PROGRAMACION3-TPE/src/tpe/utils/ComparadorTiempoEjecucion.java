package tpe.utils;
import java.util.Comparator;
import java.util.Collections;

public class ComparadorTiempoEjecucion  implements Comparator<Tarea>{
    @Override
    public int compare(Tarea t1, Tarea t2) {
        return t1.getTiempo() - t2.getTiempo();
    }
}
