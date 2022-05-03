package recocido;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

/**
 *
 * @author blackzafiro
 */
public class DatosPAV {

	private int numCiudades = -1;
	private int[] codigosCiudades;
	private double[][] coordenadasCiudades;

	public int[] getCodigosCiudades(){
		return codigosCiudades;
	}

	public double[][] getCoordenadasCiudades(){
		return coordenadasCiudades;
	}
	/** Coordenadas más pequeñas y grandes de las ciudades. */
	private double minX = Float.MAX_VALUE, maxX = Float.MIN_VALUE,
			       minY = Float.MAX_VALUE, maxY = Float.MIN_VALUE;

	public DatosPAV(File archivoTSP) {
		try (BufferedReader br = new BufferedReader(new FileReader(archivoTSP))) {
			String l;
			float x, y;
			while(!(l = br.readLine()).startsWith("NODE_COORD_SECTION")) {
				if (l.startsWith("DIMENSION")) {
					String[] tokens = l.split("\\s");
					System.out.println(tokens);
					numCiudades = Integer.parseInt(tokens[tokens.length - 1]);
					System.out.println(numCiudades);
					codigosCiudades = new int[numCiudades];
					coordenadasCiudades = new double[numCiudades][2];
				}
			}
			for(int i = 0; i < numCiudades; i++) {
				l = br.readLine();
				String[] nums = l.split(" ");
				codigosCiudades[i] = Integer.parseInt(nums[0]);
				x = Float.parseFloat(nums[1]);
				y = Float.parseFloat(nums[2]);
				coordenadasCiudades[i][0] = x;
				coordenadasCiudades[i][1] = y;
				if (minX > x) minX = x;
				if (minY > y) minY = y;
				if (maxX < x) maxX = x;
				if (maxY < y) maxY = y;
			}
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Dado un número de ciudad entre cero y <code>numCiudades()</code>
	 * devuelve el código que le fue asignado en el archivo .tsp.
	 * @param numCiudad
	 * @return código de la ciudad
	 */
	public int código(int numCiudad) {
		return codigosCiudades[numCiudad];
	}

	/**
	 * Devuelve las coordenadas de la ciudad indicada.
	 * @param codigoCiudad Código asignado en el archivo .tsp
	 * @return coordenadas de la ciudad.
	 */
	public double[] coordenadas(int codigoCiudad) {
		// Uso el hecho de que el código es la posición entera - 1
		return coordenadasCiudades[codigoCiudad - 1];
	}

	/**
	 * Devuelve el número de ciudades.
	 * @return número de ciudades.
	 */
	public int numCiudades() {
		return numCiudades;
	}

	/**
	 * Devuelven las coordenadas extremas de las ciudades.
	 * @return
	 */
	public double minX() { return minX; }
	public double minY() { return minY; }
	public double maxX() { return maxX; }
	public double maxY() { return maxY; }
}
