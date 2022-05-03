/*
 * Código utilizado para el curso de Inteligencia Artificial.
 * Se permite consultarlo para fines didácticos en forma personal,
 * pero no esta permitido transferirlo resuelto a estudiantes actuales o potenciales.
 */
package pacman.personajes.navegacion;

import java.util.LinkedList;
import pacman.escenario.Celda;
import pacman.escenario.Laberinto;
import pacman.escenario.Pasillo;
import pacman.personajes.Movimiento;

/**
 * Superclase para cualquier algoritmo de navegación para personajes no
 * jugables.
 * @author blackzafiro
 */
public abstract class Algoritmo {
	
	private Laberinto laberinto;
	private Estado[][] estados;
	
	
	public void inicializaEstados(Laberinto l) {
		this.laberinto = l;
		estados = new Estado[laberinto.alto()][laberinto.alto()];
		
		int alto = l.alto(), ancho = l.ancho();
		for(int ren = 0; ren < alto; ren++){
			for(int col = 0; col < ancho; col++) {
				Celda c = laberinto.obtenerCelda(ren, col);
				if (c instanceof Pasillo) {
					estados[ren][col] = new Estado(this, (Pasillo)c);
				}
			}
		}
	}
	
	public Estado leeEstado(int ren, int col) {
		return estados[ren][col];
	}
	
	
	
	/**
     * Función que realiza el algoritmo hasta que se encuentre una solución.
     */
    public abstract LinkedList<Movimiento> resuelveAlgoritmo(Estado estadoInicial, Estado estadoFinal);
}
