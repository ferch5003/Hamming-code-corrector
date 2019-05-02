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
    private int[][] binarioDeCorreccion;
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

    // Continuación del constructor, se encarga de obtener los valores de laslongitudes y setear los vectores necesarios
    // para el envio y recepción de los codigos
    public void setSizes(int longitud) {
        this.cantidadDePalabras = longitud;
        int esImpar= longitud % 2;
        this.longitudDatos = 16;
        this.m = longitudCodigo(this.longitudDatos);
        this.longitudCodigos = this.longitudDatos + this.m;
        this.palabrasDeDatos = new int[this.cantidadDePalabras / 2 + esImpar][16];
        this.palabrasDeCodigo = new int[this.cantidadDePalabras / 2 + esImpar][21];
        this.binarioDeCorreccion = new int[this.cantidadDePalabras / 2 + esImpar][this.m];
    }
    
    public void setSizesDecod(int longitud) {
        this.cantidadDePalabras = longitud;
        this.longitudDatos = 16;
        this.m = longitudCodigo(this.longitudDatos);
        this.longitudCodigos = this.longitudDatos + this.m;
        this.palabrasDeDatos = new int[this.cantidadDePalabras][16];
        this.palabrasDeCodigo = new int[this.cantidadDePalabras][21];
        this.binarioDeCorreccion = new int[this.cantidadDePalabras][this.m];
    }

    public void setPalabraDeDatos(int fila, String palabra) {
        this.palabrasDeDatos[fila] = decimalABinario(palabra);
    }

    public void setPalabraDeDatos(int fila, int columna, int valor) {
        this.palabrasDeDatos[fila][columna] = valor;
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

    public int[][] getBinarioDeCorrecion() {
        return this.binarioDeCorreccion;
    }

    public void setBinarioDeCorrecion(int fila, int columna, int valor) {
        this.binarioDeCorreccion[fila][columna] = valor;
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

    // Retorna falso si la linea que se leyo tiene mas de dos letras y ademas que no pertenezca las letras minusculas,
    // mayusculas o los caracteres .|| ||,||:||;||, sino retorna verdadero
    public boolean verificacionLectura(String linea) throws IOException {

        for (char letra : linea.toCharArray()) {
            int ascii = (int) letra;
            if (((ascii < 65 && ascii > 90) || (ascii < 97 && ascii > 122)) && esAdmitido(String.valueOf(letra))) {
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

    // Retorna la cantidad de palabras de codigo que hay en el archivo
    public int lineasLeidas() throws IOException {
        int count = 0;
        String linea;
        while ((linea = this.br.readLine()) != null) {
            count++;
        }
        this.br.close();
        this.archivo = new File(this.ruta);
        this.fr = new FileReader(this.archivo);
        this.br = new BufferedReader(this.fr);
        return count;
    }

    // Le da valos al vector de palabras de codigo cuando se va a recibir el .ham
    public void setPalabrasDeCodigo() throws IOException {
        int count = 0;
        String linea;
        while ((linea = this.br.readLine()) != null) {
            int binario = 0;
            String val = "";
            for (char numero : linea.toCharArray()) {
                String valor = String.valueOf(numero);
                val = val + valor;
                this.palabrasDeCodigo[count][binario] = Integer.parseInt(valor);
                binario++;
            }
            count++;
        }
        this.br.close();
    }

    // Genera el archivo .ham con las palabras de codigo
    public void crearCodificacionHamming(String[] codigos) throws IOException {

        // Generando el archivo .ham con ruta relativa
        String[] stringDeRuta;
        stringDeRuta = this.ruta.split("/");
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

    // Genera el .txt con las letras originales
    public void crearDecodificacionHamming(String[] codigos) throws IOException {

        // Generando el archivo .ham con ruta relativa
        String[] stringDeRuta;
        stringDeRuta = this.ruta.split("/");
        String nombreDelArchivo = stringDeRuta[stringDeRuta.length - 1];
        String archivoConvertido = reemplazar(nombreDelArchivo, "ham", "txt");
        File archivoCodificado = new File("src/Modelo/" + archivoConvertido);
        FileWriter fw = new FileWriter(archivoCodificado);
        // Escribiendo en el .ham correspondiente
        BufferedWriter bw = new BufferedWriter(fw);
        String letras = "";
        for (String codigo : codigos) {
            letras = letras + codigo;
        }
        bw.write(letras);
        bw.close();
    }

    public int[] decimalABinario(String palabra) {
        int decimal[] = new int[palabra.length()];
        int count = 0;
        for (char letra : palabra.toCharArray()) {
            decimal[count] = (int) letra;
            count++;
        }
        int longitud = this.longitudDatos;
        int[] binario = new int[longitud];
        for (int i = 0; i < this.getM() - 1; i++) {
            binario[i] = 0;
        }
        count = 0;
        for (int bi : decimal) {
            int longitud2 = this.longitudDatos / 2;
            while (bi / 2 != 0 || longitud2 > 0) {
                binario[longitud - 1] = bi % 2;
                bi = bi / 2;
                longitud--;
                longitud2--;
            }
            count++;
        }
        return binario;
    }

    // Esta funcion sirve es para armar el vector que se encarga de ver en que bit se encuentra el error
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

    // Retorna el bit de paridad de la potencia de dos en que se encuentre la palabra de codigo
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

    // Retorna cual bit es el que esta errado. Si es 0 es que ninguno esta errado
    public int verificacionDeParidad(int fila) {
        int exponente = this.m - 1;
        int bitErrado = 0;
        for (int bi : this.binarioDeCorreccion[fila]) {
            bitErrado = bitErrado + (bi * (int) Math.pow(2, exponente));
            exponente--;
        }
        return bitErrado;
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

    // Retorna la palabra el texto que se queria reemplazar
    private String reemplazar(String cadena, String buscarTexto, String reemplazarTexto) {
        String[] cadenas = cadena.split(buscarTexto);
        return cadenas[0] + reemplazarTexto;
    }
}
