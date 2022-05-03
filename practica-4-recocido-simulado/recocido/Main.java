package recocido;

import static java.lang.System.out;

/**
Clase para ejecutar un proceso de optimización usando recocido simulado.
@author Benjamin Torres Saavedra
@author Verónica E. Arriola
@version 0.1
*/
public class Main{

  public static void main(String []args){
    int interacciones=100;
    //Se puede sustitutir "dj38.tsp" por el nombre de otro archivo en tal formato.
    RecocidoSimulado recocido=new RecocidoSimulado(new SolucionTSP("dj38.tsp"),1000,0.895f);
    Solucion s= new SolucionTSP();
    for(int i=0; i<interacciones; i++){
      s=recocido.ejecutar();
    }
    System.out.println("****SOLUCION FINAL:****");
    out.println(s.toString());
  }
}
