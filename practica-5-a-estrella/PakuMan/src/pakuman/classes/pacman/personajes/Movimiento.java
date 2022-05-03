package pacman.personajes;

/**
 * Dirección en la cual se está moviendo el personaje.
 * @author blackzafiro
 * @author baruch
 */
public enum Movimiento {
    ARRIBA(10),
    ABAJO(10),
    DERECHA(10),
    IZQUIERDA(10);
	
	private final int costo;
	
	/**
	 * Asigna un costo a cada acción.
	 * @param costo 
	 */
    Movimiento(int costo) {
		this.costo = costo;
    }
	
	/**
	 * Permite conocer el costo asociado a este movimiento.
	 * @return costo
	 */
    public int costo() { return costo; }
}
