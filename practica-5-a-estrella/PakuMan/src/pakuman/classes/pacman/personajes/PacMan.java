package pacman.personajes;

import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import pacman.escenario.Celda;

public class PacMan extends Personaje {

    /*
     * Tiempo que le queda a PacMan con efecto galleta.
     */
    private long quedaGalleta;

    /**
     * Crea un PacMan que conoce sus coordenadas y los límites del laberinto.
     * @param ren
     * @param col
     * @param maxRen
     * @param maxCol
     */
    public PacMan(int ren, int col, int maxRen, int maxCol) {
        super(ren, col, maxRen, maxCol);
        this.quedaGalleta = 0;
        Arc pacman = new Arc();
        this.forma = pacman;
        pacman.setRadiusX(Celda.TAM/2);
        pacman.setRadiusY(Celda.TAM/2);
        pacman.setStartAngle(45.0f);
        pacman.setLength(270.0f);
        pacman.setFill(Color.YELLOW);
        pacman.setStroke(Color.ORANGE);
        pacman.setType(ArcType.ROUND);
        movimientoActual = Movimiento.DERECHA;
    }

    /**
     * Asigna el nuevo estado de movimiento de PacMan.
     * @param m
     */
    public void setMovimientoActual(Movimiento m) {
        this.movimientoActual = m;
        Arc pacman = (Arc)forma;
        switch (m) {
            case DERECHA:
                pacman.setStartAngle(45.0f);
                return;
            case IZQUIERDA:
                pacman.setStartAngle(225.0f);
                return;
            case ARRIBA:
                pacman.setStartAngle(135.0f);
                return;
            case ABAJO:
                pacman.setStartAngle(315.0f);
                return;
        }
    }
}
