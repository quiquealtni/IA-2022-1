/*
 * No redistribuir.
 */
package gatos;

import java.util.LinkedList;

/**
 * Clase para representar un estado del juego del gato. Cada estado sabe cómo
 * generar a sus sucesores.
 *
 * @author Vero
 */
public class Gato {

    public static final int MARCA1 = 1;             // Número usado en el tablero del gato para marcar al primer jugador.
    public static final int MARCA2 = 4;             // Se usan int en lugar de short porque coincide con el tamaÃ±o de la palabra, el código se ejecuta ligeramente más rápido.

    int[][] tablero = new int[3][3];     // Tablero del juego
    Gato padre;                          // Quién generó este estado.
    LinkedList<Gato> sucesores;          // Posibles jugadas desde este estado.
    boolean jugador1 = false;            // Jugador que tiró en este tablero.
    boolean hayGanador = false;          // Indica si la última tirada produjo un ganador.
    int tiradas = 0;                     // Número de casillas ocupadas.

    /**
     * Constructor del estado inicial.
     */
    Gato() {}

    /**
     * Constructor que copia el tablero de otro gato y el número de tiradas
     */
    Gato(Gato g) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                tablero[i][j] = g.tablero[i][j];
            }
        }
        tiradas = g.tiradas;
    }

    /**
     * Indica si este estado tiene sucesores expandidos.
     */
    int getNumHijos() {
        if (sucesores != null) {
            return sucesores.size();
        } else {
            return 0;
        }
    }

    /* Función auxiliar.
     * Dada la última posición en la que se tiró y la marca del jugador
     * calcula si esta jugada produjo un ganador y actualiza el atributo correspondiente.
     *
     * Esta función debe ser lo más eficiente posible para que la generación del árbol no sea demasiado lenta.
     */
    private void hayGanador(int ren, int col, int marca) {
        // Horizontal
        if (tablero[ren][(col + 1) % 3] == marca && tablero[ren][(col + 2) % 3] == marca) {
            hayGanador = true;
            return;
        }
        // Vertical
        if (tablero[(ren + 1) % 3][col] == marca && tablero[(ren + 2) % 3][col] == marca) {
            hayGanador = true;
            return;
        }
        // Diagonal
        if ((ren != 1 && col == 1) || (ren == 1 && col != 1)) {
			hayGanador = false;  // No debiera ser necesaria.
            return; // No pueden hacer diagonal
        }      // Centro y esquinas
        if (col == 1 && ren == 1) {
            // Diagonal \
            if (tablero[0][0] == marca && tablero[2][2] == marca) {
                hayGanador = true;
                return;
            }
            if (tablero[2][0] == marca && tablero[0][2] == marca) {
                hayGanador = true;
                return;
            }
        } else if (ren == col) {
            // Diagonal \
            if (tablero[(ren + 1) % 3][(col + 1) % 3] == marca && tablero[(ren + 2) % 3][(col + 2) % 3] == marca) {
                hayGanador = true;
                return;
            }
        } else {
            // Diagonal /
            if (tablero[(ren + 2) % 3][(col + 1) % 3] == marca && tablero[(ren + 1) % 3][(col + 2) % 3] == marca) {
                hayGanador = true;
                return;
            }
        }
    }

    /* Función auxiliar.
     * Coloca la marca del jugador en turno para este estado en las coordenadas indicadas.
     * Asume que la casilla está libre.
     * Coloca la marca correspondiente, verifica y asigna la variable si hay un ganador.
     */
    private void tiraEn(int ren, int col) {
        tiradas++;
        int marca = (jugador1) ? MARCA1 : MARCA2;
        tablero[ren][col] = marca;
        hayGanador(ren, col, marca);
    }

    /**
     * ------- *** ------- *** -------
     * Este es el método que se deja como práctica.
     * ------- *** ------- *** -------
     * Crea la lista sucesores y
     * agrega a todos los estados que sujen de tiradas válidas. Se consideran
     * tiradas válidas a aquellas en una casilla libre. Además, se optimiza el
     * proceso no agregando estados con jugadas simétricas. Los estados nuevos
     * tendrán una tirada más y el jugador en turno será el jugador
     * contrario.
     */
    LinkedList<Gato> generaSucesores() {
        if (hayGanador || tiradas == 9) {
            return null; // Es un estado meta.
        }
        sucesores = new LinkedList<>();
        //Recorremos el tablero casilla por casilla.
        for(int i=0;i<3;i++){
          for(int j=0;j<3;j++){
            //Verificamos si la casilla actual esta libre.
            if(tablero[i][j]==0){
              //Creamos un nuevo gato con el mismo tablero y numero de tiradas que el objeto Gato
              //que mando a llamar este metodo.
              Gato g = new Gato(this);
              //Indicamos el jugador que tira en este nuevo tablero.
              g.jugador1 = jugador1;
              //Establecemos como padre de este tablero al objeto Gato que mando a llamar este metodo.
              g.padre = this;
              //Marcamos la casilla que estaba libre.
              g.tiraEn(i,j);
              //El jugador en turno ahora es el contrafrio.
              g.jugador1 = !(g.jugador1);
              boolean agregar = true;
              //Comparamos este tablero con los que ya tenemos para evitar agregar jugadas simetricas.
              for(int k=0;k<sucesores.size();k++){
                if(g.equals(sucesores.get(k))){
                  agregar = false;
                  //Ahora sabemos que el tablero es simetrico a uno que ya esta en la lista, entonces salimos del for.
                  break;
                }
              }
              //Decidimos si debemos agregar este tablero a la lista o no.
              if(agregar==true){
                sucesores.add(g);
              }
            }
          }
        }
       return sucesores;
    }

    // ------- *** ------- *** -------
    // Serie de funciones que revisan la equivalencia de estados considerando las simetrías de un cuadrado.
    // ------- *** ------- *** -------
    // http://en.wikipedia.org/wiki/Examples_of_groups#The_symmetry_group_of_a_square_-_dihedral_group_of_order_8
    // ba es reflexion sobre / y ba3 reflexion sobre \.

    /**
     * Revisa si ambos gatos son exactamente el mismo.
     */
    boolean esIgual(Gato otro) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tablero[i][j] != otro.tablero[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Al reflejar el gato sobre la diagonal \ son iguales (ie traspuesta)
     */
    boolean esSimetricoDiagonalInvertida(Gato otro) {
	for(int i=0;i<3;i++){
            if(tablero[i][i]!=otro.tablero[i][i]){
	    	        return false;
	    }
	}
	if(tablero[0][1]!=otro.tablero[1][0]||tablero[1][0]!=otro.tablero[0][1]){
          return false;
        }
        if(tablero[0][2]!=otro.tablero[2][0]||tablero[2][0]!=otro.tablero[0][2]){
          return false;
        }
        if(tablero[1][2]!=otro.tablero[2][1]||tablero[2][1]!=otro.tablero[1][2]){
          return false;
        }
        return true;
    }

    /**
     * Al reflejar el gato sobre la diagonal / son iguales (ie traspuesta)
     */
    boolean esSimetricoDiagonal(Gato otro) {
        if(tablero[0][0]!=otro.tablero[2][2]){
          return false;
        }
        if(tablero[0][1]!=otro.tablero[1][2]){
          return false;
        }
        if(tablero[2][1]!=otro.tablero[1][0]){
          return false;
        }
        if(tablero[2][2]!=otro.tablero[0][0]){
          return false;
        }
        if(tablero[0][2]!=otro.tablero[0][2]){
          return false;
        }
        if(tablero[1][1]!=otro.tablero[1][1]){
          return false;
        }
	if(tablero[2][0]!=otro.tablero[2][0]){
          return false;
        }
	if(tablero[1][0]!=otro.tablero[2][1]){
          return false;
        }
	if(tablero[1][2]!=otro.tablero[0][1]){
          return false;
        }
        return true;
    }

    /**
     * Al reflejar el otro gato sobre la vertical son iguales
     */
    boolean esSimetricoVerticalmente(Gato otro) {
        if(tablero[0][1]!=otro.tablero[0][1]){
          return false;
        }
        if(tablero[1][1]!=otro.tablero[1][1]){
          return false;
        }
        if(tablero[2][1]!=otro.tablero[2][1]){
          return false;
        }
        if(tablero[0][0]!=otro.tablero[0][2]){
          return false;
        }
        if(tablero[0][2]!=otro.tablero[0][0]){
          return false;
        }
        if(tablero[1][0]!=otro.tablero[1][2]){
          return false;
        }
	if(tablero[1][2]!=otro.tablero[1][0]){
          return false;
        }
	if(tablero[2][0]!=otro.tablero[2][2]){
          return false;
        }
	if(tablero[2][2]!=otro.tablero[2][0]){
          return false;
        }
        return true;
    }

    /**
     * Al reflejar el otro gato sobre la horizontal son iguales
     */
    boolean esSimetricoHorizontalmente(Gato otro) {
        if(tablero[1][0]!=otro.tablero[1][0]){
          return false;
        }
        if(tablero[1][1]!=otro.tablero[1][1]){
          return false;
        }
        if(tablero[1][2]!=otro.tablero[1][2]){
          return false;
        }
        if(tablero[0][0]!=otro.tablero[2][0]){
          return false;
        }
        if(tablero[0][1]!=otro.tablero[2][1]){
          return false;
        }
        if(tablero[0][2]!=otro.tablero[2][2]){
          return false;
        }
	if(tablero[2][0]!=otro.tablero[0][0]){
          return false;
        }
	if(tablero[2][1]!=otro.tablero[0][1]){
          return false;
        }
	if(tablero[2][2]!=otro.tablero[0][2]){
          return false;
        }
        return true;
    }
    
    /**
     * Rota el otro tablero 90Â° en la dirección de las manecillas del reloj.
     */
    boolean esSimetrico90(Gato otro) {
      if(tablero[0][0]!=otro.tablero[2][0]){
        return false;
      }
      if(tablero[0][1]!=otro.tablero[1][0]){
        return false;
      }
      if(tablero[0][2]!=otro.tablero[0][0]){
        return false;
      }
      if(tablero[1][0]!=otro.tablero[2][1]){
        return false;
      }
      if(tablero[1][1]!=otro.tablero[1][1]){
        return false;
      }
      if(tablero[1][2]!=otro.tablero[0][1]){
        return false;
      }
      if(tablero[2][0]!=otro.tablero[2][2]){
        return false;
      }
      if(tablero[2][1]!=otro.tablero[1][2]){
        return false;
      }
      if(tablero[2][2]!=otro.tablero[0][2]){
        return false;
      }
      return true;
    }

    /**
     * Rota el otro tablero 180Â° en la dirección de las manecillas del reloj.
     */
    boolean esSimetrico180(Gato otro) {
      if(tablero[0][0]!=otro.tablero[2][2]){
        return false;
      }
      if(tablero[0][1]!=otro.tablero[2][1]){
        return false;
      }
      if(tablero[0][2]!=otro.tablero[2][0]){
        return false;
      }
      if(tablero[1][0]!=otro.tablero[1][2]){
        return false;
      }
      if(tablero[1][1]!=otro.tablero[1][1]){
        return false;
      }
      if(tablero[1][2]!=otro.tablero[1][0]){
        return false;
      }
      if(tablero[2][0]!=otro.tablero[0][2]){
        return false;
      }
      if(tablero[2][1]!=otro.tablero[0][1]){
        return false;
      }
      if(tablero[2][2]!=otro.tablero[0][0]){
        return false;
      }
      return true;
    }

    /**
     * Rota el otro tablero 270Â° en la dirección de las manecillas del reloj.
     */
    boolean esSimetrico270(Gato otro) {
      if(tablero[0][0]!=otro.tablero[0][2]){
        return false;
      }
      if(tablero[0][1]!=otro.tablero[1][2]){
        return false;
      }
      if(tablero[0][2]!=otro.tablero[2][2]){
        return false;
      }
      if(tablero[1][0]!=otro.tablero[0][1]){
        return false;
      }
      if(tablero[1][1]!=otro.tablero[1][1]){
        return false;
      }
      if(tablero[1][2]!=otro.tablero[2][1]){
        return false;
      }
      if(tablero[2][0]!=otro.tablero[0][0]){
        return false;
      }
      if(tablero[2][1]!=otro.tablero[1][0]){
        return false;
      }
      if(tablero[2][2]!=otro.tablero[2][0]){
        return false;
      }
      return true;
    }

    /**
     * Indica si dos estados del juego del gato son iguales, considerando
     * simetrías, de este modo el problema se vuelve manejable.
     */
    @Override
    public boolean equals(Object o) {
        Gato otro = (Gato) o;
        if (esIgual(otro)) {
            return true;
        }

        if (esSimetricoDiagonalInvertida(otro)) {
            return true;
        }
        if (esSimetricoDiagonal(otro)) {
            return true;
        }
        if (esSimetricoVerticalmente(otro)) {
            return true;
        }
        if (esSimetricoHorizontalmente(otro)) {
            return true;
        }
        if (esSimetrico90(otro)) {
            return true;
        }
        if (esSimetrico180(otro)) {
            return true;
        }
        if (esSimetrico270(otro)) {
            return true; // No redujo el diámetro máximo al agregarlo
        }
        return false;
    }

    /**
     * Devuelve una representación con caracteres de este estado. Se puede usar
     * como auxiliar al probar segmentos del código.
     */
    @Override
    public String toString() {
        char simbolo = jugador1 ? 'o' : 'x';
        String gs = "";
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                gs += tablero[i][j] + " ";
            }
            gs += '\n';
        }
        return gs;
    }
}

