package laberintos;

import java.util.LinkedList;
import java.util.Random;
import java.util.Stack;

import laberintos.Laberinto.Celda;
import processing.core.PApplet;

/**
 * Clase que crea un laberinto con Processing.
 * @author Sara
 * @author Baruch
 */
public class Laberinto extends PApplet {
    int alto = 40;            // Altura (en celdas) de la cuadricula.
    int ancho = 40;           // Anchura (en celdas) de la cuadricula.
    int celda = 15;           // Tamanio de cada celda cuadrada (en pixeles).
    ModeloLaberinto modelo;   // El objeto que representa el modelo del laberinto.

    @Override
    public void setup() {
        frameRate(60);
        background(50);
        modelo = new ModeloLaberinto(ancho, alto, celda);
        backtrack();
    }
    @Override
    public void settings() {
        size(ancho * celda, (alto * celda));
    }
    
    /**
     * Pintar el mundo del modelo.
     */
    @Override
    public void draw() {
      for (int i = 0; i < alto; i++)
        for (int j = 0; j < ancho; j++){
                  fill(6, 254, 212);
                  stroke(25,25,25);
                  rect(j * modelo.tamanio, i * modelo.tamanio, modelo.tamanio, modelo.tamanio);
                  // En caso de que las paredes de las celdas ya no se encuentren activas, estás se
                  // pintarán del color del fondo.
                  if(!modelo.mundo[i][j].pared_1){
                      stroke(6, 254, 212);
                      line(j * modelo.tamanio, i * modelo.tamanio, ((j + 1) * modelo.tamanio), i * modelo.tamanio);                    
                  }
                  if(!modelo.mundo[i][j].pared_2){
                      stroke(6, 254, 212);
                      line((j * modelo.tamanio) + modelo.tamanio, i * modelo.tamanio, (j + 1) * modelo.tamanio, (((i + 1) * modelo.tamanio)));
                  }
                  if(!modelo.mundo[i][j].pared_3){
                      stroke(6, 254, 212);
                      line(j * modelo.tamanio, (i * modelo.tamanio) + modelo.tamanio, ((j + 1) * modelo.tamanio), ((i + 1) * modelo.tamanio));                    
                  }
                  if(!modelo.mundo[i][j].pared_4){
                      stroke(6, 254, 212);
                      line(j * modelo.tamanio, i * modelo.tamanio, j * modelo.tamanio, ((i + 1) * modelo.tamanio));               
                  }
          }
    }

    /**
     * Clase que representa cada celda de la cuadricula.
     */
    class Celda{
        int celdaX; 
        int celdaY;
        boolean pared_1;
        boolean pared_2;
        boolean pared_3;
        boolean pared_4;
        boolean estado;
        
        /** Constructor de una celda.
          *@param celdaX Coordenada en x
          *@param celdaY Coordenada en y
          *@param estado Estado de la celda. true si no ha sido visitada, false en otro caso.
        */
        Celda(int celdaX, int celdaY, boolean estado){
          this.celdaX = celdaX;
          this.celdaY = celdaY;
          this.estado = estado;
          this.pared_1 = true; // Booleano que representa la pared de arriba
          this.pared_2 = true; // Booleano que representa la pared de la derecha
          this.pared_3 = true; // Booleano que representa la pared de abajo
          this.pared_4 = true; // Booleano que representa la pared de la izquierda
        }
    }  

    /**
     * Clase que modela el laberinto, es decir, crea el mundo del laberinto.
     */
    class ModeloLaberinto{
        int ancho, alto;  // Tamaño de celdas a lo largo y ancho de la cuadrícula.
        int tamanio;  // Tamaño en pixeles de cada celda.
        Celda[][] mundo;  // Mundo de celdas
        int direccion; // 1-arriba, 2-derecha, 3-abajo, 4-izquierda
        
      /** Constructor del modelo
        @param ancho Cantidad de celdas a lo ancho en la cuadricula.
        @param ancho Cantidad de celdas a lo largo en la cuadricula.
        @param tamanio Tamaño (en pixeles) de cada celda cuadrada que compone la cuadricula.
      */
      ModeloLaberinto(int ancho, int alto, int tamanio){
        this.ancho = ancho;
        this.alto = alto;
        this.tamanio = tamanio;
        mundo = new Celda[alto][ancho];
        for(int i = 0; i < alto; i++)
          for(int j = 0; j < ancho; j++)
            mundo[i][j] = new Celda(j,i, true);
      }
    }
    /**
     * Metodo que se encarga de hacer el algoritmo de backtrack.
     */
    public void backtrack(){ 
      //Creamos el stack.
      Stack visitadas = new Stack<Celda>();     
      Random r = new Random();
      //Elegimos una celda de inicio.
      Celda inicio = modelo.mundo[r.nextInt(alto)][r.nextInt(ancho)];
      //Marcamos la celda como visitada.
      inicio.estado = false;
      //Agregamos esta celda al stack.
      visitadas.push(inicio);
      //La celda a las que nos moveremos.
      Celda siguiente;
      //Repetir mientras haya celdas sin visitar.
      while(visitadas.empty()!=true){
        //Verificamos si la celda actual esta encerrada.
        boolean encerrada = estoyEncerrada(inicio);
        //Repetir mientras la celda actual no este encerrada.
        while(encerrada!=true){
          //Elegimos una celda hacia la cual movernos.
          siguiente = eligeDireccion(inicio);
          //Borramos las paredes entre la celda actual y a la que nos moveremos.
          borraParedes(modelo.direccion, inicio, siguiente);
          //Marcamos la celda como visitada.
          siguiente.estado = false;
          //Agregamos esta celda al stack.
          visitadas.push(siguiente);
          //La celda actual sera a la que nos acabamos de mover.
          inicio = siguiente;
          //Verificamos si la celda actual esta encerrada.
          encerrada = estoyEncerrada(inicio);
        }
        //Cuando la celda este encerrada la quitamos del stack.
        visitadas.pop();
        try {
           //La celda actual es ahora la que estaba antes de la que eliminamos.
          inicio = (Celda) visitadas.peek();
        } catch (Exception e) {
          break;
        }
      }
    }

    /**
     * Metodo que elige una direccion aleatoria a la cual moverse desde una celda dada.
     * @param c la celda respecto a la cual nos moveremos.
     * @return la celda hacia la que nos moveremos.
     */
    private Celda eligeDireccion(Celda c){
      Random r  = new Random();
      //Celda hacia la cual nos moveremos.
      Celda moverA;
      //Lista para almacenar las celdas a las que nos podemos mover.
      LinkedList posibles = new LinkedList<Celda>();
      //Lista para almacenar las direcciones hacia las que podemos movernos.
      LinkedList direcciones = new LinkedList<Integer>();
      //Coordenadas que tendrian las celdas a las que podriamos movernos.
      int xArriba = c.celdaX;
      int yArriba = c.celdaY-1;
      int xAbajo = c.celdaX;
      int yAbajo = c.celdaY+1;
      int xDerecha = c.celdaX+1;
      int yDerecha = c.celdaY;
      int xIzquierda = c.celdaX-1;
      int yIzquierda = c.celdaY;
      //Verificamos si podemos movernos hacia arriba.
      if(xArriba<ancho&&yArriba<alto&&xArriba>=0&&yArriba>=0){
        if(modelo.mundo[yArriba][xArriba].estado==true){
          posibles.add(modelo.mundo[yArriba][xArriba]);
          direcciones.add(1);
        }
        
      }
      //Verificamos si podemos movernos hacia abajo.
      if(xAbajo<ancho&&yAbajo<alto&&xAbajo>=0&&yAbajo>=0){
        if(modelo.mundo[yAbajo][xAbajo].estado==true){
          posibles.add(modelo.mundo[yAbajo][xAbajo]);
          direcciones.add(3);
        }
      }
      //Verificamos si podemos movernos hacia la derecha.
      if(xDerecha<ancho&&yDerecha<alto&&xDerecha>=0&&yDerecha>=0){
        if(modelo.mundo[yDerecha][xDerecha].estado==true){
          posibles.add(modelo.mundo[yDerecha][xDerecha]);
          direcciones.add(2);
        }
        
      }
      //Verificamos si podemos movernos hacia la izquierda.
      if(xIzquierda<ancho&&yIzquierda<alto&&xIzquierda>=0&&yIzquierda>=0){
        if(modelo.mundo[yIzquierda][xIzquierda].estado==true){
          posibles.add(modelo.mundo[yIzquierda][xIzquierda]);
          direcciones.add(4);
        }
        
      }
      //Si no podemos movernos a ninguna celda, entonces regresamos la celda pasada como parametro.
      if(posibles.size()==0){
        return c;
      }
      //Tomamos aleatoriamente una de las posibles celdas a las que nos podemos mover.
      int rnd = r.nextInt(posibles.size());
      moverA=(Celda) posibles.get(rnd);
      //Indicamos en que direccion nos movemos.
      modelo.direccion = (Integer) direcciones.get(rnd);
      return moverA;
    }

    /**
     * Metodo que borra la pared entre dos celdas.
     * @param direccion direccion respecto a la celda de origen donde se encuentra la celda destino.
     * @param origen celda de origen.
     * @param destino celda de destino.
     */
    public void borraParedes(int direccion,Celda origen, Celda destino){
      //Arriba
      if(direccion==1){
        origen.pared_1=false;
        destino.pared_3=false;
      }
      //Derecha
      if(direccion==2){
        origen.pared_2=false;
        destino.pared_4=false;
      }
      //Abajo
      if(direccion==3){
        origen.pared_3=false;
        destino.pared_1=false;
      }
      //izquierda
      if(direccion==4){
        origen.pared_4=false;
        destino.pared_2=false;
      }
    }
    
    /**
     * Metodo que verifica si una casilla esta encerrada o no.
     * @param c la casilla que vamos a verificar.
     * @return true si la celda esta encerrada o false si no.
     */
    public boolean estoyEncerrada(Celda c){
      //Coordenadas que tendrian las celdas vecinas.
      int xArriba = c.celdaX;
      int yArriba = c.celdaY-1;
      int xAbajo = c.celdaX;
      int yAbajo = c.celdaY+1;
      int xDerecha = c.celdaX+1;
      int yDerecha = c.celdaY;
      int xIzquierda = c.celdaX-1;
      int yIzquierda = c.celdaY;
      //Verificamos que las coordenadas esten dentro del rango adecuado.
      if(xIzquierda<ancho&&yIzquierda<alto&&xIzquierda>=0&&yIzquierda>=0){
        //Verificamos si la celda no ha sido visitada.
        if(modelo.mundo[yIzquierda][xIzquierda].estado==true){
          return false;
        }
      }
      //Verificamos que las coordenadas esten dentro del rango adecuado.
      if(xDerecha<ancho&&yDerecha<alto&&xDerecha>=0&&yDerecha>=0){
        //Verificamos si la celda no ha sido visitada.
        if(modelo.mundo[yDerecha][xDerecha].estado==true){
          return false;
        }
      }
      //Verificamos que las coordenadas esten dentro del rango adecuado.
      if(xArriba<ancho&&yArriba<alto&&xArriba>=0&&yArriba>=0){
        //Verificamos si la celda no ha sido visitada.
        if(modelo.mundo[yArriba][xArriba].estado==true){
          return false;
        }
      }
      //Verificamos que las coordenadas esten dentro del rango adecuado.
      if(xAbajo<ancho&&yAbajo<alto&&xAbajo>=0&&yAbajo>=0){
        //Verificamos si la celda no ha sido visitada.
        if(modelo.mundo[yAbajo][xAbajo].estado==true){
          return false;
        }
      }
     return true;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
         PApplet.main(new String[] { "laberintos.Laberinto" });
    } 
}
