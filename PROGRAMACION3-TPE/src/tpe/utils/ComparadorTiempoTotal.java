package tpe.utils;

import java.util.Comparator;

public class ComparadorTiempoTotal implements Comparator<Procesador> {
    @Override
    public int compare(Procesador p1, Procesador p2) {
        return p1.tiempoTotal() - p2.tiempoTotal();
    }
}

