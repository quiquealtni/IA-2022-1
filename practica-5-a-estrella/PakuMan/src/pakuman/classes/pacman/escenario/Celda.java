package pacman.escenario;

import javafx.scene.Node;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Una celda en el laberinto.
 */
public class Celda {

    /**
	 * Ancho de la celda cuadrada.
	 * Luce mejor si es un múltiplo de tres, por la forma en que se trazan
	 * las líneas de las paredes.
	 */
    public  static final int TAM = 21;

    /**
	 * Panel donde se guardarán los gráficos de los objetos en esta celda.
	 * Algunas celdas preferiran StackPane, otras sólo Pane, cada celda debe
	 * designar este elemento antes de realizar cualquier operación.
	 */
    protected Pane nodo;
	
	/**
	 * Renglón del laberinto donde se encuentra.
	 */
	protected int renglon;
	
	/**
	 * Columna del laberinto donde se encuentra.
	 */
	protected int columna;
	
	/**
	 * Constructor con las coordenadas.
	 * @param renglon
	 * @param columna 
	 */
	protected Celda(int ren, int col) {
		this.renglon = ren;
		this.columna = col;
	}

    /**
     * Devuelve la forma que representa a esta celda.
     * @return forma
     */
    Node getNode() {
        return nodo;
    }
	
	/**
	 * Renglón del laberinto donde se encuentra.
	 */
	public int renglon() { return renglon; }
	
	/**
	 * Columna del laberinto donde se encuentra.
	 */
	public int columna() { return columna; }
	
	/**
	 * Pinta la celda con el color de fondo indicado.
	 */
	public void pintaFondo(Color color) {
		nodo.setBackground(new Background(new BackgroundFill(color, null, null)));
	}
	
}
