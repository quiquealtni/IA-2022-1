/**
 * Recreación del videojuego PacMan para fines educativos.
 * Documentación de JavaFX:
 * https://openjfx.io/index.html
 * https://openjfx.io/javadoc/11/
 * Layouts en https://docs.oracle.com/javafx/2/layout/builtin_layouts.htm
 */
package pacman;

import java.util.logging.Level;
import java.util.logging.Logger;

import java.net.URL;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import pacman.escenario.Laberinto;


public class MainPacMan extends Application {
	
	private final static Logger LOG_RAIZ = Logger.getLogger("pacman");

    @Override
    public void start(Stage primaryStage) throws Exception{
		LogConsola handler = new LogConsola();
		handler.setLevel(Level.ALL);
		handler.setFormatter(new Formateador());
		LOG_RAIZ.addHandler(handler);
		LOG_RAIZ.setUseParentHandlers(false);
		LOG_RAIZ.setLevel(Level.ALL);
		//LOG_RAIZ.setLevel(Level.OFF);
		
        Parent root = FXMLLoader.load(getClass().getResource("data/sample.fxml"));
        primaryStage.setTitle("PacMan");
		primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("data/paku.png")));
		
		URL ubicaciónNivel = getClass().getResource("data/Nivel1.txt");
		String s = java.net.URLDecoder.decode( ubicaciónNivel.getPath(), "UTF-8" );
        Laberinto l = Laberinto.fabricaLaberinto(root, s);
		
		
        primaryStage.setScene(l);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
