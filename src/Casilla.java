public class Casilla {
    private int posFila;
    private int posColumna;
    private boolean mina;
    private int numMinasAlrededor;
    private boolean abierta;
//Inicializa las posiciones de las filas y columnas
    public Casilla(int posFila, int posColumna) {
        this.posFila = posFila;
        this.posColumna = posColumna;
    }
    // Este método incrementa en uno el número de minas alrededor de esta casilla.
    
    //metodo 
    public void incrementarNumeroMinasAlrededor(){
        this.numMinasAlrededor++;
    }
    //--------------------------------------------
    public int getPosFila() {
        return posFila;
    }
    public void setPosFila(int posFila) {
        this.posFila = posFila;
    }
    public int getPosColumna() {
        return posColumna;
    }
    public void setPosColumna(int posColumna) {
        this.posColumna = posColumna;
    }
    public boolean isMina() {
        return mina;
    }
    public void setMina(boolean mina) {
        this.mina = mina;
    }
    public int getNumMinasAlrededor() {
        return numMinasAlrededor;
    }
    public void setNumMinasAlrededor(int numMinasAlrededor) {
        this.numMinasAlrededor = numMinasAlrededor;
    }
    public boolean isAbierta() {
        return abierta;
    }
    public void setAbierta(boolean abierta) {
        this.abierta = abierta;
    }
}
