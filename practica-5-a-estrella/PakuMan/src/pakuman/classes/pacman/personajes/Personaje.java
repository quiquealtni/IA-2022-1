package pacman.personajes;

import javafx.scene.shape.Shape;

public class Personaje {
    protected Shape forma; // Ojo, no cambiarla o tendré poblemas con quién es graficada.
    protected Movimiento movimientoActual;
    protected int ren;
    protected int col;
    protected int maxRen;
    protected int maxCol;
	
	protected Personaje(int ren, int col, int maxRen, int maxCol) {
		this.ren = ren;
        this.col = col;
        this.maxRen = maxRen;
        this.maxCol = maxCol;
	}

    /**
     * Devuelve la forma que representa a este personaje.
     *
     * @return
     */
    public Shape getShape() {
        return forma;
    }

    /**
     * Indica el estado actual de movimiento del personaje.
	 * @return direcció en la que se moverá.
     */
    public Movimiento getMovimientoActual() {
        return movimientoActual;
    }

    /**
     * Renglón de la celda donde está el personaje.
     */
    public int ren() {
        return ren;
    }

    /**
     * Columna de la celda donde está el personaje.
     */
    public int col() {
        return col;
    }

    /**
     * Asigna el renglón.
     *
     * @param ren
     */
    public void ren(int ren) {
        this.ren = ren;
    }

    /**
     * Asigna la columna.
     * @param col
     */
    public void col(int col) {
        this.col = col;
    }

    /**
     * Desplaza sus coordenadas según el estado actual de movimiento.
     * Este método sólo debe ser llamado cuando el movimiento es posible,
     * de otro modo creará un estado inconsistente.
     */
    public void mueve() {
        switch (this.movimientoActual) {
            case ABAJO:
                ren = (ren + 1) % maxRen;
                return;
            case ARRIBA:
                ren = (ren != 0) ? ren - 1 : maxRen - 1;
                return;
            case DERECHA:
                col = (col + 1) % maxCol;
                return;
            case IZQUIERDA:
                col = (col != 0) ? col - 1 : maxCol - 1;
                return;
        }
    }
}
