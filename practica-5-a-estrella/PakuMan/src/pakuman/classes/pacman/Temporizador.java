package pacman;

import javafx.animation.AnimationTimer;
import pacman.escenario.Laberinto;

public class Temporizador extends AnimationTimer {
    private final long INTER_FRAME_TIME = 500000000;

    private Laberinto laberinto;
    private long lastTime = 0;

    public Temporizador(Laberinto laberinto) {
        this.laberinto = laberinto;
    }

    @Override
    public void handle(long l) {
        if (l - lastTime > INTER_FRAME_TIME) {
            lastTime = l;
            laberinto.muevePersonajes();
        }
    }
}
