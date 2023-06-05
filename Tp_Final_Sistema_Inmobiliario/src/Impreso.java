import java.util.LinkedList;

public class Impreso<T> {

    private LinkedList<T> fila;

    public Impreso() {
        fila = new LinkedList<>();
    }


    public void agregar(T elemento){
        fila.addLast(elemento);
    }

    public T imprimir(){
        return fila.getFirst();
    }
}

