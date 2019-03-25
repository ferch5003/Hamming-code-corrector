/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

/**
 *
 * @author ferch5003
 */
public class Palabra {

    private File archivo = null;
    private FileReader fr = null;
    private BufferedReader br = null;
    private String ruta;
    private HashMap<String, Integer> hashLetras = new HashMap<String, Integer>();
    private int cantidadDePalabras;
    private int[][] palabrasDeDatos;
    private int[][] palabrasDeCodigo;
    private int m;
    private int longitudDatos;
    private int longitudCodigos;

    public Palabra(String ruta) {
        try {
            this.ruta = ruta;
            this.archivo = new File(this.ruta);
            this.fr = new FileReader(this.archivo);
            this.br = new BufferedReader(this.fr);
            setHash();

        } catch (Exception e) {
        }
    }

    private void setHash() {
        this.hashLetras.put(" ", 1);
        this.hashLetras.put(".", 1);
        this.hashLetras.put(",", 1);
        this.hashLetras.put(";", 1);
        this.hashLetras.put(":", 1);

    }

    public File getArchivo() {
        return this.archivo;
    }

    public BufferedReader getBufferedReader() {
        return this.br;
    }

    public FileReader getFileReader() {
        return this.fr;
    }

    public void setSizes(int longitud, char palabra) {
        this.cantidadDePalabras = longitud;
        this.longitudDatos = 8;
        this.m = longitudCodigo(this.longitudDatos);
        this.longitudCodigos = this.longitudDatos + this.m;
        this.palabrasDeDatos = new int[this.cantidadDePalabras][8];
        this.palabrasDeCodigo = new int[this.cantidadDePalabras][12];
    }

    public void setPalabraDeDatos(int fila, char palabra) {
        this.palabrasDeDatos[fila] = decimalABinario(palabra);
    }

    public int getPalabraDeDatos(int fila, int columna) {
        return this.palabrasDeDatos[fila][columna];
    }

    public int[][] getPalabraDeDatos() {
        return this.palabrasDeDatos;
    }

    public void setPalabraDeCodigos(int fila, int columna, int valor) {
        this.palabrasDeCodigo[fila][columna] = valor;
    }

    public int getPalabraDeCodigos(int fila, int columna) {
        return this.palabrasDeCodigo[fila][columna];
    }

    public int[][] getPalabraDeCodigos() {
        return this.palabrasDeCodigo;
    }

    public int getLongitudDeDato() {
        return this.longitudDatos;
    }

    public int getLongitudDeCodigo() {
        return this.longitudCodigos;
    }

    public int getM() {
        return this.m;
    }

    public boolean verificacionLectura(String linea) throws IOException {
        
        if (linea.length() > 2) {
            return false;
        }
        
        for (char letra : linea.toCharArray()) {
            int ascii = (int) letra;
            System.out.println(ascii);
            if (((ascii < 65 && ascii > 90) || (ascii < 97 && ascii > 122)) && esAdmitido(String.valueOf(letra))) {
                System.out.println("entro");
                return false;
            }  
        }

        return true;
    }

    private boolean esAdmitido(String letra) {
        return this.hashLetras.get(letra) == null ? true : false;
    }

    public String lineaLeida() throws IOException {
        return this.br.readLine();
    }

    public void crearCodificacionHamming(String[] codigos) throws IOException {
        String OS = System.getProperty("os.name").toLowerCase();

        // Generando el archivo .ham con ruta relativa
        String[] stringDeRuta;
        // Dependiendo del OS forma el vector para generar el .ham
        if (esWindows(OS)) {
            stringDeRuta = this.ruta.split("\\");
        } else {
            stringDeRuta = this.ruta.split("/");
        }
        String nombreDelArchivo = stringDeRuta[stringDeRuta.length - 1];
        String archivoConvertido = reemplazar(nombreDelArchivo, "txt", "ham");
        File archivoCodificado = new File("src/Modelo/" + archivoConvertido);
        FileWriter fw = new FileWriter(archivoCodificado);
        // Escribiendo en el .ham correspondiente
        BufferedWriter bw = new BufferedWriter(fw);
        for (String codigo : codigos) {
            bw.write(codigo + "\n");
        }
        bw.close();
    }

    public void crearDecodificacionHamming(String[] codigos) {
        String OS = System.getProperty("os.name").toLowerCase();
        try {
            // Generando el archivo .ham con ruta relativa
            String[] stringDeRuta;
            // Dependiendo del OS forma el vector para generar el .ham
            if (esWindows(OS)) {
                stringDeRuta = this.ruta.split("\\");
            } else {
                stringDeRuta = this.ruta.split("/");
            }
            String nombreDelArchivo = stringDeRuta[stringDeRuta.length - 1];
            String archivoConvertido = reemplazar(nombreDelArchivo, "txt", "ham");
            File archivoCodificado = new File(archivoConvertido);
            FileWriter fw = new FileWriter(archivoCodificado);
            // Escribiendo en el .ham correspondiente
            BufferedWriter bw = new BufferedWriter(fw);
            for (String codigo : codigos) {
                bw.write(codigo);
            }
        } catch (Exception e) {
        }
    }

    public int[] decimalABinario(int decimal) {
        int longitud = this.longitudDatos;
        int[] binario = new int[longitud];
        for (int i = 0; i < this.getM() - 1; i++) {
            binario[i] = 0;
        }
        while (decimal / 2 != 0 || longitud > 0) {
            binario[longitud - 1] = decimal % 2;
            decimal = decimal / 2;
            longitud--;
        }
        return binario;
    }

    public int[] Binario(int decimal) {
        int longitud = longitudBinario(decimal);
        int count = 0;
        int m = getM() - 1;
        int[] binario = new int[this.getM()];
        for (int i = 0; i < this.getM() - 1; i++) {
            binario[i] = 0;
        }
        while (decimal / 2 != 0 || longitud > 0) {
            binario[m - count] = decimal % 2;
            decimal = decimal / 2;
            count++;
            longitud--;
        }
        return binario;
    }

    public int bitDeParidad(int exponente, int modulo, int fila) {
        int b = 0;
        int count = 0;
        int m = this.getM() - 1;
        int n = this.palabrasDeCodigo[fila].length - 1;
        for (int k = modulo + 1; k <= getLongitudDeCodigo(); k++) {
            int[] binario = Binario(k);
            if (binario[m - exponente] == 1) {
                if (count == 0) {
                    b = this.palabrasDeCodigo[fila][n - k + 1];

                } else {
                    if (this.palabrasDeCodigo[fila][n - k + 1] == b) {
                        b = 0;
                    } else {
                        b = 1;
                    }
                }
                count++;
            }
        }
        return b;
    }

    private int log2(int x) {
        return (int) Math.floor(Math.log(x) / Math.log(2));
    }

    private int longitudBinario(int decimal) {
        return log2(decimal) + 1;
    }

    private int longitudCodigo(int longitud) {
        int m = 0;
        while ((int) Math.pow(2, m) <= longitud + m + 1) {
            m++;
        }
        return m;
    }

    private String reemplazar(String cadena, String buscarTexto, String reemplazarTexto) {
        String[] cadenas = cadena.split(buscarTexto);
        return cadenas[0] + reemplazarTexto;
    }

    private boolean esWindows(String OS) {

        return (OS.indexOf("win") >= 0);

    }

}