
package recocido;

import java.util.Arrays;
import java.io.File;
import java.util.Random;

public class SolucionTSP extends Solucion {

	//Almacena los codigos de las ciudades del archivo pasado como parametro.
	private int[] codigosCiudades;
	//Almacena la propuesta de solucion al problema.
  private int[] solucion;
  //Almacena los datos del archivo cuyo nombre es el pasado como parametro en el constructor.
  private DatosPAV datos;

  public SolucionTSP(){

  }

	/**
	 * Inicializa una representacion como propuesta para solucion del problema.
	 * @param archivoDatos el nombre del archivo donde obtendremos la informacion.
	 */
  public SolucionTSP(String archivoDatos){
		//Cargamos los datos del archivo.
    File f = new File(archivoDatos);
		this.datos = new DatosPAV(f);
    this.codigosCiudades = datos.getCodigosCiudades();
		//Se estable la solucion inicial.
		//Copiamos en un arreglo las ciudades en el orden del archivo.
    this.solucion = Arrays.copyOf(codigosCiudades,codigosCiudades.length+1);
    solucion[codigosCiudades.length] = codigosCiudades[0];
		Random r = new Random();
		//Cambiamos el punto de inicio y partida aleatoriamente.
		int inicio = solucion[0];
		int indexNuevoInicio = r.nextInt(solucion.length-2) + 1;
		int nuevoInicio = solucion[indexNuevoInicio];
		solucion[0] = nuevoInicio;
		solucion[solucion.length-1] = nuevoInicio;
		solucion[indexNuevoInicio] = inicio;
		//Revolvemos lo que hay entre medio del arreglo para obtener la solucion.
		for (int i = 1; i < solucion.length-1; i++) {
			int randomIndexToSwap = r.nextInt(solucion.length-2) + 1;
			int temp = solucion[randomIndexToSwap];
			solucion[randomIndexToSwap] = solucion[i];
			solucion[i] = temp;
		}
    //Mostramos en consola la solucion inicial.
		System.out.println("****SOLUCION INICIAL*****\n"+toString());
  }

	/**
	 * Metodo que calcula la distancia entre dos puntos dados sus coordenadas.
	 * @param x1 la coordenada x1.
	 * @param y1 la coordenada y1.
	 * @param x2 la coordenada x2.
	 * @param y2 la coordenada y2.
	 * @return la distancia entre dos puntos.
	 */
  private float distancia(double x1, double y1, double x2, double y2){
    return (float) Math.sqrt(Math.pow(x2-x1,2)+Math.pow(y2-y1,2));
  }

  public SolucionTSP siguienteSolucion(){
		//Creamos una nueva solucion.
		SolucionTSP siguiente = new SolucionTSP();
		//Necesitaremos los mismos datos.
		siguiente.datos = datos;
		//Inicializamos la solucion como la previa.
		siguiente.solucion = Arrays.copyOf(solucion,solucion.length);
		Random r = new Random();
		//Cambiamos el punto de inicio y partida aleatoriamente.
		int inicio = siguiente.solucion[0];
		int indexNuevoInicio = r.nextInt(siguiente.solucion.length-2) + 1;
		int nuevoInicio = siguiente.solucion[indexNuevoInicio];
		siguiente.solucion[0] = nuevoInicio;
		siguiente.solucion[siguiente.solucion.length-1] = nuevoInicio;
		siguiente.solucion[indexNuevoInicio] = inicio;
    return siguiente;
  }

	/**
	 * Metodo que califica la solucion en base a la distancia total recorrida.
	 * @return la distancia total recorrida.
	 */
  public float evaluar(){
    float costo=0;
    double x1 = 0;
    double y1 = 0;
    double x2 = 0;
    double y2 = 0;
    for (int i=0;i<solucion.length-1 ;i++) {
			//Establecemos las ciudades de inicio y fin.
      int ciudadInicio = solucion[i];
      int ciudadFin = solucion[i+1];
			//Obtenemos las coordenadas de ambas ciudades.
			x1 = datos.coordenadas(ciudadInicio)[0];
			y1 = datos.coordenadas(ciudadInicio)[1];
			x2 = datos.coordenadas(ciudadFin)[0];
			y2 = datos.coordenadas(ciudadFin)[1];
			//Sumamos la distancia entre ambas ciudades al costo total.
      costo += distancia(x1,y1,x2,y2);
    }
    return costo;
  }

	/**
	 * Metodo que regresa una cadena con la representacion de la solucion en forma de lista.
	 */
  public String toString(){
    return "Solucion: "+Arrays.toString(solucion)+"\n"+"Costo: "+evaluar();
  }
}
