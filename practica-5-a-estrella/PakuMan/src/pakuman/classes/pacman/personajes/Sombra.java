/*
 * Código utilizado para el curso de Inteligencia Artificial.
 * Se permite consultarlo para fines didácticos en forma personal,
 * pero no esta permitido transferirlo resuelto a estudiantes actuales o potenciales.
 */
package pacman.personajes;


import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.LinkedList;
import javafx.scene.paint.Color;
import pacman.escenario.Laberinto;
import pacman.personajes.navegacion.AEstrella;
import pacman.personajes.navegacion.Estado;

/**
 *
 * @author baruch
 * @author blackzafiro
 */
public class Sombra extends Fantasma {
	
	private final static Logger LOGGER = Logger.getLogger("pacman.personajes.navegacion.Sombra");
	static { LOGGER.setLevel(Level.OFF); }
	
	public Sombra(int ren, int col, Laberinto l) {
		super(ren, col, l);
		this.forma.setFill(Color.RED);
		this.forma.setStroke(Color.YELLOW);
		this.alg = new AEstrella();
	}

	@Override
	public void eligeMovimiento(PacMan pacman) {
		Estado inicial = alg.leeEstado(this.ren, this.col);
		Estado meta = alg.leeEstado(pacman.ren(), pacman.col());
		
		LinkedList<Movimiento> sol = alg.resuelveAlgoritmo(inicial, meta);
		this.movimientoActual = sol.getFirst();
		LOGGER.log(Level.FINE, "Movimiento: " + this.movimientoActual);
		
	}

	@Override
	public void inicializaNavegacion() {
		alg.inicializaEstados(laberinto);
	}
}
