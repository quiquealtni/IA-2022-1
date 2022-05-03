package pacman.comida;

import javafx.scene.shape.Shape;

/**
 * Objeto comestible.
 */
public class Comida {

    /**
     * Forma en que será dibujado este alimento.
     * Nota: se puede modificar la figura, pero no reasignar esta variable
     * o habrá conflictos con la gráfica de la escena.
     */
    protected Shape figuraDeComida;

    /**
     * Permite tener una referencia a la forma en que se dibuja este alimento.
     * @return
     */
    public Shape getShape() {
        return figuraDeComida;
    }
}
