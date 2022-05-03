package pacman.comida;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import pacman.escenario.Celda;

/**
 * Galletas que come PacMan para poder perseguir fantasmas.
 */
public class Galleta extends Comida {
    public Galleta() {
        this.figuraDeComida = new Circle(Celda.TAM/5, Color.ORANGE);
    }
}
