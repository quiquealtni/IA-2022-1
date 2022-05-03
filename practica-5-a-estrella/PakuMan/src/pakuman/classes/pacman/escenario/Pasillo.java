package pacman.escenario;

import java.util.HashMap;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import pacman.comida.Comida;
import pacman.personajes.Movimiento;
import pacman.personajes.Personaje;

/**
 * Tipo especial de celda por donde puede pasar Pacman.
 * Cada pasillo es el nodo de un grafo, pues mantiene conexiones a los pasillos
 * vecinos.
 * @author blackzafiro
 */
public class Pasillo extends Celda {

    /**
     * Si hay algo que comer en esta celda, esto no será null.
     */
    private Comida comida;
    private Personaje personaje;
	
	private HashMap<Movimiento, Pasillo> vecinos = new HashMap<>();

	/**
	 * Crea una celda por donde se puede pasar en el renglón y columna del
	 * laberinto indicados.
	 * @param ren
	 * @param col 
	 */
    public Pasillo(int ren, int col) {
		super(ren, col);
		nodo = new StackPane();
        this.nodo.getChildren().add(new Rectangle(Celda.TAM, Celda.TAM));
    }

    /**
     * Además de la figura que representa el fondo de esta celda,
     * un pasillo puede tener comida encima.
     * @param comida
     */
    public Pasillo(int ren, int col, Comida comida) {
        this(ren, col);
        this.comida = comida;
        this.nodo.getChildren().add(comida.getShape());
    }

	/**
     * Además de la figura que representa el fondo de esta celda,
     * un pasillo puede tener un personaje en él.
     * @param personaje Pacman o un fantasma
     */
    public Pasillo(int ren, int col, Personaje personaje) {
        this(ren, col);
        this.personaje = personaje;
        this.nodo.getChildren().add(personaje.getShape());
    }
	
	/**
	 * Asigna las referencias a las celdas en las posibles direcciones de
	 * movimiento.
	 * @param celdas Celdas del laberinto.
	 */
	void configuraVecinos(Celda[][] celdas) {
		Pasillo siguiente = null;
		int alto = celdas.length;
		int ancho = celdas[0].length;
		
		// Derecha
		if (columna < ancho - 1 && celdas[renglon][columna+1] instanceof Pasillo) {
			siguiente = (Pasillo)celdas[renglon][columna+1];
		} else if (columna == ancho - 1 && celdas[renglon][0] instanceof Pasillo) {
			siguiente = (Pasillo)celdas[renglon][0];
		}
		vecinos.put(Movimiento.DERECHA, siguiente);
		
		// Izquierda
		siguiente = null;
		if (columna > 0 && celdas[renglon][columna-1] instanceof Pasillo) {
			siguiente = (Pasillo)celdas[renglon][columna-1];
		} else if (columna == 0 && celdas[renglon][ancho-1] instanceof Pasillo) {
			siguiente = (Pasillo)celdas[renglon][ancho-1];
		}
		vecinos.put(Movimiento.IZQUIERDA, siguiente);
		
		// Arriba
		siguiente = null;
		if (renglon > 0 && celdas[renglon-1][columna] instanceof Pasillo) {
			siguiente = (Pasillo)celdas[renglon-1][columna];
		} else if (renglon == 0 && celdas[alto-1][columna] instanceof Pasillo) {
			siguiente = (Pasillo)celdas[alto-1][columna];
		}
		vecinos.put(Movimiento.ARRIBA, siguiente);
		
		// Abajo
		siguiente = null;
		if (renglon < alto - 1 && celdas[renglon+1][columna] instanceof Pasillo) {
			siguiente = (Pasillo)celdas[renglon+1][columna];
		} else if (renglon == alto-1 && celdas[0][columna] instanceof Pasillo) {
			siguiente = (Pasillo)celdas[0][columna];
		}
		vecinos.put(Movimiento.ABAJO, siguiente);
	}
	
	/**
	 * Devuelve el pasillo en la dirección de movimiento indicada.
	 * @param mov Movimiento que se realizará a partir de este pasillo.
	 * @return Pasillo o <code>null</code> si no se puede avanzar en esa
	 *         dirección.
	 */
	public Pasillo obtenVecino(Movimiento mov) {
		return vecinos.get(mov);
	}

    /**
     * Devuelve verdadero si el pasillo estaba vacío y pudo recibir al personaje,
     * falso si ya había alguien ahí.
     * @param personaje
     * @return
     */
    public synchronized boolean recibePersonaje(Personaje personaje){
        if(this.personaje != null) return false;
        else {
            this.personaje = personaje;
            this.nodo.getChildren().add(personaje.getShape());
            return true;
        }
    }

	/**
	 * Se manda llamar cuando el personaje sale de esta celda.
	 * @param p personaje
	 */
    public synchronized void despidePersonaje(Personaje p) {
        if(this.personaje == null) throw new IllegalStateException("No había nadie aquí.");
        this.nodo.getChildren().removeAll(p.getShape());
        this.personaje = null;
    }
	
	/**
	 * Pinta la celda con el color de fondo indicado.
	 * @param color Color del que se pintará el piso del pasillo.
	 */
	@Override
	public void pintaFondo(Color color) {
		Rectangle r = (Rectangle)this.nodo.getChildren().get(0);
		r.setFill(color);
	}

    /**
     * Si este pasillo tenía comida la devuelve, si no devuelve <code>null</code>.
     * Si había comida la elimina de este nodo.
     * @return la comida.
     */
    public Comida alimentaAPacMan() {
        if (this.comida == null) return null;
        this.nodo.getChildren().removeAll(comida.getShape());
        Comida c = this.comida;
        this.comida = null;
        return c;
    }
}
