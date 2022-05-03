/*
 * Código utilizado para el curso de Inteligencia Artificial.
 * Se permite consultarlo para fines didácticos en forma personal,
 * pero no esta permitido transferirlo resuelto a estudiantes actuales o potenciales.
 */
package pacman.personajes.navegacion;

import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;
import javafx.scene.paint.Color;
import pacman.personajes.Movimiento;

/**
 * Clase donde se define en algoritmo de A* para que se use en el fantasma.
 * @author baruch
 * @author blackzafiro
 */
public class AEstrella extends Algoritmo {
	
	private final static Logger LOGGER = Logger.getLogger("pacman.personajes.navegacion.AEstrella");
	static { LOGGER.setLevel(Level.FINE); }
	
	private PriorityQueue<NodoBusqueda> listaAbierta;   // Cola de prioridad de donde obtendremos los nodos
	                                                    // sobre los que se realizará el algoritmo.
	private HashMap<Estado, Estado> listaCerrada;       // Tabla de dispersión donde se agregan todos los estados
                                                        // que se terminó de revisar.
    private Estado estadoFinal;                         // Casilla donde se encuentra pacman.
    private boolean terminado;                          // Define si nuestro algoritmo ha terminado.
    private NodoBusqueda nodoSolucion;                  // Nodo a partir del cual se define la solución,
	                                                    // porque ya se encontró la mejor rutal al estado meta.
	
    /**
     * Inicializador del algoritmo.
	 * Se debe mandar llamar cada vez que cambien el estado incial y el estado
	 * final.
     * @param estadoInicial Pasillo donde se encuentra el fantasma.
     * @param estadoFinal Pasillo donde se encuentra pacman.
     */
    private void inicializa(Estado estadoInicial, Estado estadoFinal){
		this.estadoFinal = estadoFinal;
        this.terminado = false;
		this.nodoSolucion = null;
        this.listaAbierta = new PriorityQueue<>();
        this.listaCerrada = new HashMap<>();
        estadoInicial.calculaHeuristica(estadoFinal);
        this.listaAbierta.offer(new NodoBusqueda(estadoInicial));
    }
    
    /**
     * Función que realiza un paso en la ejecución del algoritmo.
     */
    private void expandeNodoSiguiente(){
	//Punto A: inicial, lo sacamos de la lista abierta.
        NodoBusqueda inicial = listaAbierta.poll();
        //Lo añadimos a la lista cerrada
        listaCerrada.put(inicial.estado(), inicial.estado());
        //Verificamos si ya encontramos la solucion.
        if(inicial.estado().equals(estadoFinal)){
            terminado = true;
            nodoSolucion = inicial;
            return;
        }else{
            //Obtenemos los vecinos del nodo actual.
            LinkedList<NodoBusqueda> vecinos = inicial.getSucesores();
            //Los añadimos a la lista.
            for(int i=0;i<vecinos.size();i++){
                //Verificamos que no este en la lista cerrada.           
                if(listaCerrada.containsKey(vecinos.get(i).estado())==false){
                    if(listaAbierta.contains(vecinos.get(i))==false){
                        vecinos.get(i).estado().calculaHeuristica(estadoFinal);
                        listaAbierta.offer(vecinos.get(i));                         
                    }
                    NodoBusqueda cont = new NodoBusqueda(null);                       
                    for(NodoBusqueda aux: listaAbierta){
                        if(aux.equals(vecinos.get(i))){
                                cont = aux;
                                break;
                        }
                    }
                    if(vecinos.get(i).gn()<cont.gn()){
                        listaAbierta.remove(cont);
                        listaAbierta.offer(vecinos.get(i)); 
                    }
                }                         
            }
        }                
		
    }
	
	/**
	 * Se puede llamar cuando se haya encontrado la solución para obtener el
	 * plan desde el nodo inicial hasta la meta.
	 * @return secuencia de movimientos que llevan del estado inicial a la meta.
	 */
	private LinkedList<Movimiento> generaTrayectoria() {
	    LinkedList<Movimiento> trayectoria = new LinkedList<>();
            NodoBusqueda temp = nodoSolucion;
		while(temp.padre() != null) {
                    trayectoria.push(temp.accionPadre());
                    temp = temp.padre();                        
		}
	    return trayectoria;
	}
	
	/**
	 * Pinta las celdas desde el nodo solución hasta el nodo inicial
	 */
	private void pintaTrayectoria(Color color) {
		if (nodoSolucion == null) return;
		NodoBusqueda temp = nodoSolucion.padre();
		while(temp.padre() != null) {
			temp.estado().pintaCelda(color);
			temp = temp.padre();
		}
	}
    
    /**
     * Función que ejecuta A* para determinar la mejor ruta desde el fantasma,
	 * cuya posición se encuetra dentro de <code>estadoInicial</code>, hasta
	 * Pacman, que se encuentra en <code>estadoFinal</code>.
	 * @return Una lista con la secuencia de movimientos que Sombra debe
	 *         ejecutar para llegar hasta PacMan.
     */
	@Override
    public LinkedList<Movimiento> resuelveAlgoritmo(Estado estadoInicial, Estado estadoFinal){
	    pintaTrayectoria(Color.BLACK);
	    inicializa(estadoInicial,estadoFinal);
            while(terminado!=true){                    
                expandeNodoSiguiente();                   
            }
            pintaTrayectoria(Color.PURPLE);               
            return generaTrayectoria();	
		
    }
	
}
