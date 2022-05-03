/*
 * Código utilizado para el curso de Inteligencia Artificial.
 * Se permite consultarlo para fines didácticos en forma personal,
 * pero no esta permitido transferirlo resuelto a estudiantes actuales o potenciales.
 */
package pacman.personajes;

import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import pacman.escenario.Celda;
import pacman.escenario.Laberinto;
import pacman.personajes.navegacion.Algoritmo;

/**
 *
 * @author baruch
 */
public abstract class Fantasma extends Personaje {
	
	/**
	 * Referencia al laberinto donde se mueve el fantasma.
	 */
	protected Laberinto laberinto;
	
	/**
	 * Algoritmo que utilizará para elegir sus movimientos.
	 */
	protected Algoritmo alg;    // Sombra sigue a PacMan con A*
	
	public Fantasma(int ren, int col, Laberinto l){
		super(ren, col, l.alto(), l.ancho());
		this.laberinto = l;
		Arc fantasma = new Arc();
        this.forma = fantasma;
        fantasma.setRadiusX(Celda.TAM/2);
        fantasma.setRadiusY(Celda.TAM/1.2);
        fantasma.setLength(180.0f);
        fantasma.setType(ArcType.ROUND);
        this.movimientoActual = Movimiento.ARRIBA;
	}
	
	/**
	 * Se manda llamar una vez que el laberinto tenga todas sus celdas.
	 */
	public abstract void inicializaNavegacion();
	
	/**
	 * Elige el nuevo valor para <code>movimientoActual</code> dependiendo
	 * de la posición de pacman en el laberinto donde se encuentren.
	 * @param pacman
	 */
	public abstract void eligeMovimiento(PacMan pacman);
}
