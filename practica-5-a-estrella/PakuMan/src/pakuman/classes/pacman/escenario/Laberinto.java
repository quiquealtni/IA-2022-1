package pacman.escenario;

import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.IntegerPropertyBase;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import pacman.Temporizador;
import pacman.comida.Bolita;
import pacman.comida.Comida;
import pacman.comida.Galleta;
import pacman.personajes.Movimiento;
import pacman.personajes.PacMan;
import pacman.personajes.Fantasma;
import pacman.personajes.Sombra;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.stream.Stream;

/**
 * Escena donde se despliega el laberinto.
 * @author blackzafiro
 * @author baruch
 */
public class Laberinto extends Scene implements EventHandler<KeyEvent> {
	
	private final static Logger LOGGER = Logger.getLogger("pacman.escenario.Laberinto");
	static { LOGGER.setLevel(Level.OFF); }

    private IntegerProperty puntos;
    private Label etiquetaPuntos;
    private Label etiquetaListo;

    private static final int EXTRA_TAM_CELDAS = 3;
    private GridPane nodoLaberinto;
    private char[][] laberintoEnCodigo;
    private Celda[][] celdas;
    private int alto;
    private int ancho;
    private PacMan pacman;
	private Fantasma[] fantasmas = new Fantasma[1];
    private Temporizador temporizador;

    /**
     * Construye una escena con el laberinto indicado por el archivo de configuración
     * @param root Nodo raíz montado en el <code>stage</code>.
     * @param alto Número de celdas de alto.
     * @param ancho Número de celdas de ancho.
     * @param archivoNivel
     */
    public Laberinto(Parent root, int alto, int ancho, String archivoNivel) {
        super(root, (ancho + EXTRA_TAM_CELDAS) * Celda.TAM, (alto + EXTRA_TAM_CELDAS) * Celda.TAM);
        GridPane grid = (GridPane) this.getRoot();
        this.alto = alto;
        this.ancho = ancho;

        // Laberinto
        cargaNivel(archivoNivel);
        grid.add(nodoLaberinto, 1,1, 3, 1);
        temporizador = new Temporizador(this);

        // Puntos
        puntos = new IntegerPropertyBase() {
            @Override
            public Object getBean() {
                return Laberinto.this;
            }

            @Override
            public String getName() {
                return "puntos";
            }
        };
        puntos.set(0);
        etiquetaPuntos = new Label();
        etiquetaPuntos.textProperty().bindBidirectional(puntos, new DecimalFormat());
        grid.add(etiquetaPuntos, 3, 0);
        Label nombrePuntos = new Label("Puntos: ");
        nombrePuntos.setLabelFor(etiquetaPuntos);
        grid.add(nombrePuntos, 2, 0);

        // ¡Listo!
        etiquetaListo = new Label("¡Listo! Presiona S para comenzar");
        etiquetaListo.setFont(new Font(25));

        Stop[] stops = new Stop[] { new Stop(0, Color.GREENYELLOW), new Stop(1, Color.RED)};
        LinearGradient lg1 = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, stops);
        etiquetaListo.setTextFill(lg1);

        etiquetaListo.setAlignment(Pos.CENTER);
        nodoLaberinto.add(etiquetaListo, 0, 0, ancho, alto);
        this.setOnKeyPressed(this);
    }

    /*
     * Devuelve la celda del tipo correcto según su código de caracter.
     * @param c
     * @return Celda
     */
    private Celda creaCelda(int ren, int col) {
        char c = laberintoEnCodigo[ren][col];
        switch (c) {
            case '.':
                return new Pasillo(ren, col, new Bolita());
            case 'o':
                return new Pasillo(ren, col, new Galleta());
            case '-':
			case 'T':
                return new Pared(ren, col, c);	
            case 'p':
                return new Pared(ren, col, this.laberintoEnCodigo);
			case 'C':
                pacman = new PacMan(ren, col, alto, ancho);
                return new Pasillo(ren, col, pacman);
			case 'F':
				fantasmas[0] = new Sombra(ren, col, this);
				return new Pasillo(ren, col, fantasmas[0]);
            case ' ':
            case '_':
            default:
                return new Pasillo(ren, col);
        }
    }

    /*
     * Lee la información del archivo de configuración, crea y llena los arreglos
     * de códigos y de celdas.  Agrega las figuras de cada celda en el grid de javafx.
     */
    private void cargaNivel(String archivoNivel) {
        laberintoEnCodigo = new char[alto][ancho];
        celdas = new Celda[alto][ancho];

        // Lee el código de caracteres desde el archivo del nivel
        try(BufferedReader bf = new BufferedReader(new FileReader(archivoNivel))) {
            String l;
            int ren = 0;
            while((l = bf.readLine()) != null) {
				LOGGER.log(Level.FINE, l);
                int col = 0;
                for(char c: l.toCharArray()) {
                    laberintoEnCodigo[ren][col] = c;
                    col++;
                }
                ren++;
            }
        } catch (FileNotFoundException e) {
            System.err.println("No debiera llegar aquí.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Crea celdas a partir de los códigos
        nodoLaberinto = new GridPane();
        BackgroundFill[] fills = new BackgroundFill [] { new BackgroundFill(Color.BLACK,
                                                                            new CornerRadii(Celda.TAM/3),
                                                                            new Insets(0)
                )};
        nodoLaberinto.setBackground(new Background(fills));
        for(int ren = 0; ren < alto; ren++) {
            nodoLaberinto.getRowConstraints().add(new RowConstraints(Celda.TAM));
        }
        for(int col = 0; col < ancho; col++) {
            nodoLaberinto.getColumnConstraints().add(new ColumnConstraints(Celda.TAM));
        }

        Celda cel;
        for(int ren = 0; ren < alto; ren++) {
            for(int col = 0; col < ancho; col++) {
                cel = creaCelda(ren, col);
                celdas[ren][col] = cel;
                nodoLaberinto.add(cel.getNode(), col, ren);
            }
        }
		
		// Las celdas pasillo configuran las conexiones con sus vecinas.
		for(int ren = 0; ren < alto; ren++) {
            for(int col = 0; col < ancho; col++) {
				if (celdas[ren][col] instanceof Pasillo) {
					((Pasillo)celdas[ren][col]).configuraVecinos(this.celdas);
				}
			}
		}
		
		for(Fantasma f : this.fantasmas) {
			f.inicializaNavegacion();
		}
    }
	
	/**
     * Función para obtener el alto del laberinto.
     * @return Valor de la altura del laberinto.
     */
    public int alto(){
        return this.alto;
    }
    
    /**
     * Función para obtener el ancho del laberinto.
     * @return Valor de la anchura del laberinto.
     */
    public int ancho(){
        return this.ancho;
    }

	/**
     * Función para obtener una celda determinada.
     * @param ren Renglon donde se encuentra la celda.
     * @param col Columna donde se encuentra la celda.
     * @return Celda solicitada.
     */
    public Celda obtenerCelda(int ren, int col){
        return celdas[ren][col];
    }
	
    /*
     * Mueve a PacMan.
     */
    private void mueveAPacMan() {
        Pasillo actual = (Pasillo)celdas[pacman.ren()][pacman.col()];
        Pasillo siguiente = actual.obtenVecino(pacman.getMovimientoActual());
        
        if(siguiente != null && siguiente.recibePersonaje(pacman)) {
            actual.despidePersonaje(pacman);
            pacman.mueve();
            Comida comida = siguiente.alimentaAPacMan();
            if (comida instanceof Bolita) {
                this.puntos.set(puntos.get() + Bolita.PUNTOS_BOLITA);
            }
        }
    }
	
	/*
	 * Mueve un fantasma.
	 */
	private void mueveFantasma(Fantasma fantasma) {
		Pasillo actual = (Pasillo)celdas[fantasma.ren()][fantasma.col()];
		fantasma.eligeMovimiento(pacman);
		Pasillo siguiente = actual.obtenVecino(fantasma.getMovimientoActual());
		
		if(siguiente != null && siguiente.recibePersonaje(fantasma)) {
            actual.despidePersonaje(fantasma);
            fantasma.mueve();
        }
	}

    /**
     * Mueve a los personajes según el tick del reloj.
     */
    public synchronized void muevePersonajes() {
        mueveAPacMan();
		for(int i = 0; i < fantasmas.length; i++) {
			mueveFantasma(fantasmas[i]);
		}
    }

    /**
     * Responde a los eventos de teclado del usuario.
     * @param keyEvent
     */
    @Override
    public void handle(KeyEvent keyEvent) {
        KeyCode keyCode = keyEvent.getCode();
        //System.out.println("Código " + keyCode);
        switch (keyCode) {
            case S:
                nodoLaberinto.getChildren().removeAll(this.etiquetaListo);
                temporizador.start();
                return;
            case UP:
            case KP_UP:
                pacman.setMovimientoActual(Movimiento.ARRIBA);
                return;
            case DOWN:
            case KP_DOWN:
                pacman.setMovimientoActual(Movimiento.ABAJO);
                return;
            case LEFT:
            case KP_LEFT:
                pacman.setMovimientoActual(Movimiento.IZQUIERDA);
                return;
            case RIGHT:
            case KP_RIGHT:
                pacman.setMovimientoActual(Movimiento.DERECHA);
                return;
        }
    }

    /**
     * Función para crear un escena Laberinto en el nodo indicado.
     * @param root
     * @param archivoNivel
     * @return El laberinto según lo indicado en el archivo.
     */
    public static Laberinto fabricaLaberinto(Parent root, String archivoNivel) {
        int ancho = 0, alto = 0;

        // El alto está dado por el número de línea en el archivo.
        try(BufferedReader bf = new BufferedReader(new FileReader(archivoNivel))) {
            Stream<String> stream = bf.lines();
            alto = (int)stream.count();
        } catch (FileNotFoundException e) {
            System.err.println("Ahí no está " + archivoNivel);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // El ancho corresponde al número de caracteres por renglón.
        try(BufferedReader bf = new BufferedReader(new FileReader(archivoNivel))) {
            String l = bf.readLine();
            ancho = l.length();
        } catch (FileNotFoundException e) {
            System.err.println("Ahí no está " + archivoNivel);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Laberinto(root, alto, ancho, archivoNivel);
    }
}
