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
        Palabra codificacionHamming = new Palabra(ruta);
        try {
            String linea = codificacionHamming.lineaLeida();
            // Verifica si la linea que se leyo tiene mas de dos letras y ademas que no pertenezca las letras minusculas,
            // mayusculas o los caracteres .|| ||,||:||;||, sino retorna verdadero
            if (codificacionHamming.verificacionLectura(linea)) {
                codificacionHamming.setSizes((linea.length()));

                int catidadDePalabras = 0;
                int count = 0;
                int count2 = 0;
                int esImpar = (linea.length()) % 2;
                String dosLetras = "";
                // Se obtiene la palabra de datos del ASCII obtenido
                for (char palabra : linea.toCharArray()) {
                    dosLetras = dosLetras + String.valueOf(palabra);
                    count2++;

                    if (count2 == 2) {

                        codificacionHamming.setPalabraDeDatos(catidadDePalabras, dosLetras);
                        dosLetras = "";
                        catidadDePalabras++;

                        count2 = 0;
                    }

                    count++;
                    if (esImpar == 1 && count == linea.length()) {

                        codificacionHamming.setPalabraDeDatos(catidadDePalabras, dosLetras);

                        catidadDePalabras++;

                    }

                }

                count = 0;

                // Forma la primera parte de las palabras de codigo.
                for (int[] palabra : codificacionHamming.getPalabraDeDatos()) {

                    int exponente = codificacionHamming.getM() - 1;
                    int modulo;
                    int j = 0;
                    for (int i = codificacionHamming.getLongitudDeCodigo() - 1; i > -1; i--) {
                        modulo = (int) Math.pow(2, exponente);
                        // Solo entra sino es una  potencia de dos de las posiciones de los bits de paridad. Solo se dan 
                        //los valores de la palabra de datos
                        if ((i + 1) % modulo != 0) {
                            codificacionHamming.setPalabraDeCodigos(count, codificacionHamming.getLongitudDeCodigo() - i - 1, palabra[j]);

                            j++;
                        } else {
                            exponente--;
                        }

                    }
                    count++;
                }
                count = 0;
                //  Segunda para formar las palabras de codigo
                for (int[] palabra : codificacionHamming.getPalabraDeCodigos()) {
                    int exponente = codificacionHamming.getM() - 1;
                    int modulo;
                    for (int i = codificacionHamming.getLongitudDeCodigo() - 1; i > -1; i--) {
                        modulo = (int) Math.pow(2, exponente);

                        // Entra si es alguna posición de dos de los bits de paridad. Pone los valores de los bits de paridad en la
                        // palabra de codigo
                        if ((i + 1) % modulo == 0) {
                            int b = codificacionHamming.bitDeParidad(exponente, modulo, count);

                            codificacionHamming.setPalabraDeCodigos(count, codificacionHamming.getLongitudDeCodigo() - i - 1, b);

                            exponente--;
                        }

                    }
                    count++;
                }

                String[] palabrasDeCodigo = new String[catidadDePalabras];
                count = 0;

                // Se construye un vector de String de las palabras de codigo, esto como tal para que se puedan guardar
                for (int[] palabra : codificacionHamming.getPalabraDeCodigos()) {
                    palabrasDeCodigo[count] = "";
                    for (int bit : palabra) {
                        palabrasDeCodigo[count] = palabrasDeCodigo[count] + String.valueOf(bit);
                    }
                    count++;
                }

                // Guarda las palabras de codigo en el .ham
                codificacionHamming.crearCodificacionHamming(palabrasDeCodigo);
                return true;

            }
        } catch (Exception e) {
        }

        return false;
    }

    public boolean recepcion(String ruta) {
        Palabra decodificacioHamming = new Palabra(ruta);

        try {
            int lineasLeidas = decodificacioHamming.lineasLeidas();
            int catidadDePalabras = 0;
            // Verifica si el archivo tiene menos de dos lineas
            decodificacioHamming.setSizesDecod(lineasLeidas);

            // Arma las palabras de codigo del .ham
            decodificacioHamming.setPalabrasDeCodigo();

            int count = 0;
            // Corrige las palabras de codigo si hay error
            for (int[] palabra : decodificacioHamming.getPalabraDeCodigos()) {
                int exponente = decodificacioHamming.getM() - 1;
                int modulo;
                for (int i = decodificacioHamming.getLongitudDeCodigo() - 1; i > -1; i--) {
                    modulo = (int) Math.pow(2, exponente);

                    // Entra si es una potencia de dos de los bits de paridad
                    if ((i + 1) % modulo == 0) {
                        int posicionDeParidad = decodificacioHamming.getLongitudDeCodigo() - i - 1;

                        int b = decodificacioHamming.bitDeParidad(exponente, modulo, count);
                        // Se construye un vector para ver cual es el bit errado
                        if (b == decodificacioHamming.getPalabraDeCodigos(count, posicionDeParidad)) {
                            b = 0;
                        } else {
                            b = 1;
                        }
                        decodificacioHamming.setBinarioDeCorrecion(count, decodificacioHamming.getM() - exponente - 1, b);

                        exponente--;
                    }
                }

                // Obtiene la posicion del bit errad. Si obtiene 0 no se hace ningún cambio
                int bitErrado = decodificacioHamming.verificacionDeParidad(count);

                if (bitErrado != 0) {
                    if (decodificacioHamming.getPalabraDeCodigos(count, decodificacioHamming.getLongitudDeCodigo() - bitErrado) == 0) {
                        decodificacioHamming.setPalabraDeCodigos(count, decodificacioHamming.getLongitudDeCodigo() - bitErrado, 1);
                    } else {
                        decodificacioHamming.setPalabraDeCodigos(count, decodificacioHamming.getLongitudDeCodigo() - bitErrado, 0);
                    }
                }

                count++;
            }

            
            count = 0;
            // Se forma la palabra de datos con el bit corregido
            for (int[] palabra : decodificacioHamming.getPalabraDeCodigos()) {
                int exponente = decodificacioHamming.getM() - 1;
                int modulo;
                int count2 = 0;
                for (int i = decodificacioHamming.getLongitudDeCodigo() - 1; i > -1; i--) {
                    modulo = (int) Math.pow(2, exponente);

                    if ((i + 1) % modulo != 0) {
                        int bitDeDatos = decodificacioHamming.getLongitudDeCodigo() - i - 1;
                        decodificacioHamming.setPalabraDeDatos(count, count2, decodificacioHamming.getPalabraDeCodigos(count, bitDeDatos));
                        count2++;
                    } else {
                        exponente--;
                    }

                }
                count++;
            }
            int esImpar = 0;
            int menos = 0;
            for (int palabra[] : decodificacioHamming.getPalabraDeDatos()) {
                count = 1;
                int i = 0;
                while (i < palabra.length) {
                    if (palabra[i] == 0) {
                        esImpar++;
                    }
                    if (esImpar % 8 == 0 && esImpar != 0) {
                        menos = 1;
                        break;
                    }
                    if ((i + 1) % 8 == 0) {
                        esImpar = 0;
                    }
                    i++;
                }
                catidadDePalabras++;
            }

            String[] ascii;
            if (menos == 1) {
                ascii = new String[catidadDePalabras * 2 - 1];
            } else {
                ascii = new String[catidadDePalabras * 2];
            }
            count = 0;
            int count2 = 0;
            // Se construye un vector de String de los caracteres originales
            for (int palabra[] : decodificacioHamming.getPalabraDeDatos()) {
                int exponente = 7;
                int codigoAscii = 0;
                for (int bi : palabra) {
                    codigoAscii = codigoAscii + (bi * (int) (Math.pow(2, exponente)));
                    exponente--;
                    if (count2 == 7) {
                        if (codigoAscii != 0) {
                            ascii[count] = String.valueOf((char) codigoAscii);
                            count++;
                        }
                        codigoAscii = 0;
                        exponente = 7;
                        count2 = -1;
                    }
                    count2++;
                }
            }

            for (int i = 0; i < ascii.length - 1; i++) {
                String aux = ascii[i];
                if (ascii[i + 1] != null && (i + 1) % 2 != 0) {
                    ascii[i] = ascii[i + 1];
                    ascii[i + 1] = aux;
                }
            }

            // Guarda los caracteres en el .txt
            decodificacioHamming.crearDecodificacionHamming(ascii);

            return true;

        } catch (Exception e) {
            System.out.println(e);
        }

        return false;
    }
}
