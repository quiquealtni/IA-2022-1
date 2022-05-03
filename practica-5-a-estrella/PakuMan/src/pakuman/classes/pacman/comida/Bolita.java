package pacman.comida;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import pacman.escenario.Celda;

/**
 * Bolitas que come PacMan.
 */
public class Bolita extends Comida {

    public static final int PUNTOS_BOLITA = 10;

    public Bolita() {
        this.figuraDeComida = new Circle(Celda.TAM/10, Color.YELLOW);
    }
}
