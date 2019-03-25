/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package correccion;
import Controlador.ControladorPalabra;
import java.io.IOException;
import java.util.Scanner;
/**
 *
 * @author ferch5003
 */
public class Correccion {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        String ruta = "src/Modelo/cod.txt";
        ControladorPalabra controlador = new ControladorPalabra();
        if(controlador.envio(ruta)){
            
        }else{
            System.out.println("No se pudo enviar");
        }
    }
    
}
