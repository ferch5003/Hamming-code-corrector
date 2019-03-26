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

            if (codificacionHamming.verificacionLectura(linea)) {

                codificacionHamming.setSizes(linea.length());

                int count = 0;
                for (char palabra : linea.toCharArray()) {
                    codificacionHamming.setPalabraDeDatos(count, palabra);
                    count++;
                }
                count = 0;
                for (int palabra[] : codificacionHamming.getPalabraDeDatos()) {
                    for (int bi : palabra) {
                    }
                    count++;
                }

                count = 0;
                for (int[] palabra : codificacionHamming.getPalabraDeDatos()) {

                    int exponente = codificacionHamming.getM() - 1;
                    int modulo;
                    int j = 0;
                    for (int i = codificacionHamming.getLongitudDeCodigo() - 1; i > -1; i--) {
                        modulo = (int) Math.pow(2, exponente);
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
                for (int[] palabra : codificacionHamming.getPalabraDeCodigos()) {
                    int exponente = codificacionHamming.getM() - 1;
                    int modulo;
                    for (int i = codificacionHamming.getLongitudDeCodigo() - 1; i > -1; i--) {
                        modulo = (int) Math.pow(2, exponente);

                        if ((i + 1) % modulo == 0) {
                            int b = codificacionHamming.bitDeParidad(exponente, modulo, count);

                            codificacionHamming.setPalabraDeCodigos(count, codificacionHamming.getLongitudDeCodigo() - i - 1, b);

                            exponente--;
                        }

                    }
                    count++;
                }
                String[] palabrasDeCodigo = new String[2];
                count = 0;
                for (int[] palabra : codificacionHamming.getPalabraDeCodigos()) {
                    palabrasDeCodigo[count] = "";
                    for (int bit : palabra) {
                        palabrasDeCodigo[count] = palabrasDeCodigo[count] + String.valueOf(bit);
                    }
                    count++;
                }

                codificacionHamming.crearCodificacionHamming(palabrasDeCodigo);
                return true;
            }
        } catch (Exception e) {
        }

        return false;
    }

    public boolean recepcion(String ruta){
        Palabra decodificacioHamming = new Palabra(ruta);

        try {
            int lineasLeidas = decodificacioHamming.lineasLeidas();
            
            if (lineasLeidas < 3 && lineasLeidas > -1) {
                decodificacioHamming.setSizes(lineasLeidas);

                decodificacioHamming.setPalabrasDeCodigo();

                int count = 0;
                for (int[] palabra : decodificacioHamming.getPalabraDeCodigos()) {
                    int exponente = decodificacioHamming.getM() - 1;
                    int modulo;
                    for (int i = decodificacioHamming.getLongitudDeCodigo() - 1; i > -1; i--) {
                        modulo = (int) Math.pow(2, exponente);

                        if ((i + 1) % modulo == 0) {
                            int posicionDeParidad = decodificacioHamming.getLongitudDeCodigo() - i - 1;
                            int b = decodificacioHamming.bitDeParidad(exponente, modulo, count);
                            if (b == decodificacioHamming.getPalabraDeCodigos(count, posicionDeParidad)) {
                                b = 0;
                            } else {
                                b = 1;
                            }
                            decodificacioHamming.setBinarioDeCorrecion(count, decodificacioHamming.getM() - exponente - 1, b);

                            exponente--;
                        }

                    }

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
                count = 0;
                String[] ascii = new String[2];
                for (int palabra[] : decodificacioHamming.getPalabraDeDatos()) {
                    int exponente = palabra.length - 1;
                    int codigoAscii = 0;
                    for (int bi : palabra) {
                        codigoAscii = codigoAscii + (bi * (int) (Math.pow(2, exponente)));
                        exponente--;
                    }
                    ascii[count] = String.valueOf((char) codigoAscii);
                    count++;
                }
                decodificacioHamming.crearDecodificacionHamming(ascii);
                return true;
            }else{
                return false;
            }

        } catch (Exception e) {
        }

        return false;
    }
}
