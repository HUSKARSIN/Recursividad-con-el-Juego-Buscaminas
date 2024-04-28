import java.awt.Color;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
public class TableroBuscaminas {
    //crea la matris 
    Casilla[][] casillas;
    //variables que almacenan los numero ,filas ,columnas y minas.
    int numFilas;
    int numColumnas;
    int numMinas;
    //eventos que pueden ser llamados con siertas acciones
    private Consumer<List<Casilla>> eventoPartidaPerdida;
    private Consumer<List<Casilla>> eventoPartidaGanada;
    private Consumer<Casilla> eventoCasillaAbierta;
    private Consumer<Casilla> eventoBloqueada;
    //Inicializa el número de filas, columnas y minas con los valores proporcionados
    public TableroBuscaminas(int numFilas, int numColumnas, int numMinas) {
        this.numFilas = numFilas;
        this.numColumnas = numColumnas;
        this.numMinas=numMinas;
        this.inicializarCasillas();
    }
    // inicializa la matriz de casillas
    public void inicializarCasillas(){
        casillas=new Casilla[this.numFilas][this.numColumnas];
        for (int i = 0; i < casillas.length; i++) {
            for (int j = 0; j < casillas[i].length; j++) {
                casillas[i][j]=new Casilla(i, j);
            }
        }
        generarMinas();
    }
    //genera un número específico de minas en posiciones aleatorias en el tablero.
    private void generarMinas(){
        int minasGeneradas=0;
        while(minasGeneradas!=numMinas){
            int posTmpFila=(int)(Math.random()*casillas.length);
            int posTmpColumna=(int)(Math.random()*casillas[0].length);
            if (!casillas[posTmpFila][posTmpColumna].isMina()){
                casillas[posTmpFila][posTmpColumna].setMina(true);
                minasGeneradas++;
            }
        }
        actualizarNumeroMinasAlrededor();
    }
    /*Imprime el tablero por consola el cual recorre por todas las sillas en busca de las minas 
    e imprime ceros y * */
    public void imprimirTablero() {
        for (int i = 0; i < casillas.length; i++) {
            for (int j = 0; j < casillas[i].length; j++) {
                System.out.print(casillas[i][j].isMina()?"*":"0");
            }
            System.out.println("");
        }
    }
    //imprime el número de minas alrededor de cada casilla en el tablero.
    private void imprimirPistas() {
        for (int i = 0; i < casillas.length; i++) {
            for (int j = 0; j < casillas[i].length; j++) {
                System.out.print(casillas[i][j].getNumMinasAlrededor());
            }
            System.out.println("");
        }
    }
    //actualiza el numero minas adyacentes
    private void actualizarNumeroMinasAlrededor(){
        for (int i = 0; i < casillas.length; i++) {
            for (int j = 0; j < casillas[i].length; j++) {
                if (casillas[i][j].isMina()){
                    List<Casilla> casillasAlrededor=obtenerCasillasAlrededor(i, j);
                    casillasAlrededor.forEach((c)->c.incrementarNumeroMinasAlrededor());
                }
            }
        }
    }
    //devuelve una lista de todas las casillas adyacentes a la casilla en la posición.
    private List<Casilla> obtenerCasillasAlrededor(int posFila, int posColumna){
        List<Casilla> listaCasillas=new LinkedList<>();
        for (int i = 0; i < 8; i++) {
            int tmpPosFila=posFila;
            int tmpPosColumna=posColumna;
            switch(i){
                case 0: tmpPosFila--;break; //Arriba
                case 1: tmpPosFila--;tmpPosColumna++;break; //Arriba Derecha
                case 2: tmpPosColumna++;break; //Derecha
                case 3: tmpPosColumna++;tmpPosFila++;break; //Derecha Abajo
                case 4: tmpPosFila++;break; //Abajo
                case 5: tmpPosFila++;tmpPosColumna--;break; //Abajo Izquierda
                case 6: tmpPosColumna--;break; //Izquierda
                case 7: tmpPosFila--; tmpPosColumna--;break; //Izquierda Arriba
            }
            
            if (tmpPosFila>=0 && tmpPosFila<this.casillas.length
                    && tmpPosColumna>=0 && tmpPosColumna<this.casillas[0].length){
                listaCasillas.add(this.casillas[tmpPosFila][tmpPosColumna]);
            }
            
        }
        return listaCasillas;
    }  
    //funcion selecionar casilla se llama al hacer click en una casilla del tablero
    public void seleccionarCasilla(int posFila, int posColumna) {
        //se activa cuando se abre una casilla (se abre la casilla que eligio el jugador)
        eventoCasillaAbierta.accept(this.casillas[posFila][posColumna]);
        //el this verifica si hay una mina.
        if (this.casillas[posFila][posColumna].isMina()) {
            //debuelve una lista
            List<Casilla> casillasConMinas = new LinkedList<>();
            for (int i = 0; i < casillas.length; i++) {
                for (int j = 0; j < casillas[i].length; j++) {
                    if (casillas[i][j].isMina()) {
                        casillasConMinas.add(casillas[i][j]);
                    }
                }
            }
            //perdio
            eventoPartidaPerdida.accept(casillasConMinas);
            //verifica si hay minas y cuantas hay
        }else if (this.casillas[posFila][posColumna].getNumMinasAlrededor()==0){
            //lista de las casillas adyacentes a la casilla seleccionada
            List<Casilla> casillasAlrededor=obtenerCasillasAlrededor(posFila, posColumna);
            for(Casilla casilla: casillasAlrededor){
                if (!casilla.isAbierta()){
                    //abre las cuando es false
                    casilla.setAbierta(true);
                    seleccionarCasilla(casilla.getPosFila(), casilla.getPosColumna());
                }
            }
        }
    }
    //Crea un nuevo tablero y luego imprime el tablero y las pistas.
    public static void main(String[] args) {
        TableroBuscaminas tablero=new TableroBuscaminas(10, 10, 20);
        tablero.imprimirTablero();
        System.out.println("---");
        tablero.imprimirPistas();
        
    }
    public void setEventoPartidaPerdida(Consumer<List<Casilla>> eventoPartidaPerdida) {
        this.eventoPartidaPerdida = eventoPartidaPerdida;
    }
    public void setEventoPartidaGanada(Consumer<List<Casilla>> eventoPartidaGanada) {
        this.eventoPartidaGanada = eventoPartidaGanada;
    }
    public void setEventoCasillaAbierta(Consumer<Casilla> eventoCasillaAbierta) {
        this.eventoCasillaAbierta = eventoCasillaAbierta;
    }
}
