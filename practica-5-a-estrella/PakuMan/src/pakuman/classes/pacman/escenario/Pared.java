package pacman.escenario;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;

public class Pared extends Celda {
	
	private boolean noHayParedALaDerecha(int ren, int col, char[][] laberinto) {
		return col == laberinto[ren].length - 1 || !esPared(laberinto[ren][col + 1]);
	}
	
	private boolean noHayParedALaIzquierda(int ren, int col, char[][] laberinto) {
		return col == 0 || !esPared(laberinto[ren][col-1]);
	}
	
	private boolean noHayParedArriba(int ren, int col, char[][] laberinto) {
		return ren == 0 || !esPared(laberinto[ren-1][col]);
	}
	
	private boolean noHayParedAbajo(int ren, int col, char[][] laberinto) {
		return ren == laberinto.length - 1 || !esPared(laberinto[ren+1][col]);
	}
	
    /*
     * Indica si el caracter indicado es código para un tipo de pared.
     */
    private boolean esPared(char c) {
        return c == 'p' || c == '-' || c == '|' || c == 'T';
    }
	
	/**
     * Si ya se sabe qué tipo de pared es, la crea directamente.
     * @param ren
     * @param col
     * @param code
     */
    public Pared(int ren, int col, char code) {
		super(ren, col);
		nodo = new Pane();
        asignaDibujo(code);
    }

    /**
     * Crea una pared con un dibujo dependiendo de su rol en el laberinto.
     * __     __       |      ___      ___    ___    ___     __|__
     * |       |     __|__     |       |__    | |    ___       |
     *
     * |       |     |__     __|      ___     | |    | |      __
     * |__   __|     |         |      __|     |_|    | |     |_|
     * @param laberinto
     */
    public Pared(int ren, int col, char[][] laberinto) {
		super(ren, col);
		nodo = new Pane();

        if (noHayParedArriba(ren, col, laberinto)) {
            if(noHayParedAbajo(ren, col, laberinto)) {
                if(noHayParedALaIzquierda(ren, col, laberinto)) {
                    if(noHayParedALaDerecha(ren, col, laberinto)) {
                        asignaDibujo('o');  // Cuadrito
                    } else {
						asignaDibujo('c');
					}
                } else {
                    // Hay pared a la izquierda
                    if(noHayParedALaDerecha(ren, col, laberinto)) {
                        asignaDibujo('b');  // c inversa
                    } else {
                        // Hay pared a la derecha
                        // Dibujar pared horizontal
                        asignaDibujo('-');
                    }
                }
            } else {
				// Hay pared abajo
				if(noHayParedALaIzquierda(ren, col, laberinto)) {
					if(noHayParedALaDerecha(ren, col, laberinto)) {
                        asignaDibujo('n');
					} else {
						// Esquina superior izquierda
						asignaDibujo('e');
					}
				} else {
					// Hay pared a la izquierda
					if(noHayParedALaDerecha(ren, col, laberinto)) {
						// Esquina superior derecha
						asignaDibujo('f');
					} else {
						// Hay pared a la derecha
						asignaDibujo('T');
					}
					
				}
            }
        } else {
            // Hay pared arriba
            if(noHayParedAbajo(ren, col, laberinto)) {
				if(noHayParedALaIzquierda(ren, col, laberinto)) {
					if(noHayParedALaDerecha(ren, col, laberinto)) {
						asignaDibujo('u');
					} else {
						// Hay pared a la derecha
						// Esquina superior izquierda
						asignaDibujo('g');
					}
				} else {
					// Hay pared a la izquierda
					if(noHayParedALaDerecha(ren, col, laberinto)) {
						// Esquina inferior izquierda
						asignaDibujo('h');
					} else {
						// Hay pared a la derecha
						asignaDibujo('w');
					}
				}
            } else {
                // Hay pared abajo
                if(noHayParedALaIzquierda(ren, col, laberinto)) {
                    if(noHayParedALaDerecha(ren, col, laberinto)) {
                        // Dibujar pared vertical
                        asignaDibujo('|');
                    } else {
                        // Hay pared a la derecha
						asignaDibujo('}');
                    }
                } else {
                    // Hay pared a la izquierda
					if(noHayParedALaDerecha(ren, col, laberinto)) {
						asignaDibujo('{');
					} else {
						// Hay pared a la derecha
						asignaDibujo('t');
					}
                }
            }
        }
    }

    private void asignaDibujo(char code) {
        // Documentación para los paths
        // https://developer.mozilla.org/en-US/docs/Web/SVG/Tutorial/Paths

        int THIRD = Celda.TAM/3;
		int SIXTH = Celda.TAM/6;
        String HIAR = "0," + THIRD; // Horizontal, izquierda, arriba
        String HIAB = "0," + 2*THIRD; // Horizontal, izquierda, abajo
        String HDAR = "" + Celda.TAM + "," + THIRD; // Horizontal, derecha, arriba
        String HDAB = "" + Celda.TAM + "," + 2*THIRD; // Horizontal, derecha, abajo
        String VIAR = "" + THIRD + ",0"; // Vertical, izquierda, arriba
		String VIAB = "" + THIRD + "," + Celda.TAM; // Vertical, izquierda, arriba
        String VDAR = "" + 2*THIRD + ",0"; // Vertical, derecha, arriba
		String VDAB = "" + 2*THIRD + "," + Celda.TAM; // Vertical, derecha, arriba
		String CIAR = "" + THIRD + "," + THIRD; // Centro, izquierda, arriba
		String CIAB = "" + THIRD + "," + 2*THIRD; // Centro, izquierda, abajo
		String CDAR = "" + 2*THIRD + "," + THIRD; // Centro, derecha, arriba
		String CDAB = "" + 2*THIRD + "," + 2*THIRD; // Centro, derecha, abajo
        SVGPath svg = new SVGPath();
        svg.setStroke(Color.BLUEVIOLET);
        svg.setStrokeWidth(2.0);
        svg.setStrokeType(StrokeType.CENTERED);
        svg.setStrokeLineCap(StrokeLineCap.ROUND);
        svg.setStrokeLineJoin(StrokeLineJoin.ROUND);

        switch (code) {
            case '-':
                // Dibujar pared horizontal
                svg.setContent("M" + HIAB + " " + HDAB + "M" + HIAR + " " + HDAR);
                break;
            case '|':
                svg.setContent("M" + VIAR + " V" + Celda.TAM + " M" + VDAR + " V" + Celda.TAM);
                break;
            case 'e':
                // Esquina superior izquierda
                svg.setContent("M" + VDAB + " A " + THIRD + " " + THIRD + " 0 0 1 " + HDAB +
						       "M" + VIAB + " A " + 2*THIRD + " " + 2*THIRD + " 0 0 1 " + HDAR);
				break;
			case 'f':
				// Esquina superior 
				svg.setContent("M" + HIAB + " A " + THIRD + " " + THIRD + " 0 0 1 " + VIAB +
						       "M" + HIAR + " A " + 2*THIRD + " " + 2*THIRD + " 0 0 1 " + VDAB);
				break;
			case 'g':
				// Esquina inferior izquieda
				svg.setContent("M" + HDAR + " A " + THIRD + " " + THIRD + " 0 0 1 " + VDAR +
						       "M" + HDAB + " A " + 2*THIRD + " " + 2*THIRD + " 0 0 1 " + VIAR);
				break;
			case 'h':
				// Esquina inferior derecha
				svg.setContent("M" + VIAR + " A " + THIRD + " " + THIRD + " 0 0 1 " + HIAR +
						       "M" + VDAR + " A " + 2*THIRD + " " + 2*THIRD + " 0 0 1 " + HIAB);
				break;
			case 'c':
				svg.setContent("M" + HDAR + " " + CIAR +
						       " A " + SIXTH + " " + SIXTH + " 0 0 0 " + CIAB +  // 0 - rotación del eje x; 0 - ángulo corto o largo; 0 - manecillas del reloj o contra.
						       " L" + CIAB + " " + HDAB);
				break;
			case 'b':
				svg.setContent("M" + HIAR + " " + CDAR +
						       " A " + SIXTH + " " + SIXTH + " 0 0 1 " + CDAB +
						       " L" + CDAB + " " + HIAB);
				break;
			case 'u':
				svg.setContent("M" + VIAR + " " + CIAB +
						       " A " + SIXTH + " " + SIXTH + " 0 0 0 " + CDAB +
						       " L" + CDAB + " " + VDAR);
				break;
			case 'n':
				svg.setContent("M" + VIAB + " " + CIAR +
						       " A " + SIXTH + " " + SIXTH + " 0 0 1 " + CDAR +
						       " L" + CDAR + " " + VDAB);
				break;
			case 'T':
				// T
				svg.setContent("M" + HIAR + " " + HDAR + " M" + HIAB + " " + CIAB + " " + VIAB + " M" + VDAB + " " + CDAB + " " + HDAB);
                break;
			case '}':
				svg.setContent("M" + VIAR + " " + VIAB + " M" + VDAR + " " + CDAR + " " + HDAR + " M" + VDAB + " " + CDAB + " " + HDAB);
                break;
			case '{':
				svg.setContent("M" + VDAR + " " + VDAB + " M" + VIAR + " " + CIAR + " " + HIAR + " M" + VIAB + " " + CIAB + " " + HIAB);
                break;
			case 'w':
				// T hacia arriba
				svg.setContent("M" + HIAB + " " + HDAB + " M" + HIAR + " " + CIAR + " " + VIAR + " M" + VDAR + " " + CDAR + " " + HDAR);
                break;
			case 't':
				svg.setContent("M" + HIAB + " " + CIAB + " " + VIAB + " M" + VDAB + " " + CDAB + " " + HDAB + " M" + HIAR + " " + CIAR + " " + VIAR + " M" + VDAR + " " + CDAR + " " + HDAR);
                break;
			case 'o':
				// Un cuadrito solitario
				svg.setContent("M" + CIAR + " " + CDAR + " " + CDAB + " " + CIAB + " " + CIAR);
                break;
			default:
				svg.setContent("M0,0 L 0,0 " + Celda.TAM + "," + Celda.TAM);
        }

        nodo.getChildren().add(svg);
    }
}
