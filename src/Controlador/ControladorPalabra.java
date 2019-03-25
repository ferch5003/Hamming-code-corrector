/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.Palabra;
import java.io.BufferedReader;
import java.io.IOException;

/**
 *
 * @author ferch5003
 */
public class ControladorPalabra {

    public boolean envio(String ruta) {
        Palabra archivoDeCorreccion = new Palabra(ruta);

        try {
            String linea = archivoDeCorreccion.lineaLeida();
            if (archivoDeCorreccion.verificacionLectura(linea)) {

                archivoDeCorreccion.setSizes(linea.length(), linea.toCharArray()[0]);
                
                int count = 0;
                for (char palabra : linea.toCharArray()) {
                    archivoDeCorreccion.setPalabraDeDatos(count, palabra);
                    count++;
                }
                count = 0;
                for (int palabra[] : archivoDeCorreccion.getPalabraDeDatos()) {
                    for(int bi : palabra){
                        System.out.print(bi);
                    }
                    System.out.println("");
                    count++;
                }
                
                count = 0;
                for (int[] palabra : archivoDeCorreccion.getPalabraDeDatos()) {
                    
                    int exponente = archivoDeCorreccion.getM() - 1;
                    int modulo;
                    int j = 0;
                    for (int i = archivoDeCorreccion.getLongitudDeCodigo() - 1; i > -1; i--) {
                        modulo = (int) Math.pow(2, exponente);
                        if ((i + 1) % modulo != 0) {
                            archivoDeCorreccion.setPalabraDeCodigos(count, archivoDeCorreccion.getLongitudDeCodigo() - i - 1, palabra[j]);
                            
                            j++;
                        } else {
                            exponente--;
                        }

                    }
                    count++;
                }

                count = 0;
                for (int[] palabra : archivoDeCorreccion.getPalabraDeCodigos()) {
                    int exponente = archivoDeCorreccion.getM() - 1;
                    int modulo;
                    for (int i = archivoDeCorreccion.getLongitudDeCodigo() - 1; i > -1; i--) {
                        modulo = (int) Math.pow(2, exponente);

                        if ((i + 1) % modulo == 0) {
                            int b = archivoDeCorreccion.bitDeParidad(exponente, modulo, count);

                            archivoDeCorreccion.setPalabraDeCodigos(count, archivoDeCorreccion.getLongitudDeCodigo() - i - 1, b);

                            exponente--;
                        }

                    }
                    count++;
                }
                String[] palabrasDeCodigo = new String[2];
                count = 0;
                for (int[] palabra : archivoDeCorreccion.getPalabraDeCodigos()) {
                    palabrasDeCodigo[count] = "";
                    for (int bit : palabra) {
                        palabrasDeCodigo[count] = palabrasDeCodigo[count] + String.valueOf(bit);
                    }
                    count++;
                }

                archivoDeCorreccion.crearCodificacionHamming(palabrasDeCodigo);
                return true;
            }
        } catch (Exception e) {
        }

        return false;
    }
}
