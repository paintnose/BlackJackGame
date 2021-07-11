package BlackJack;

public class BaralhoJogador{ // baralho utilizando por jogadores: player e dealer(npc)
    private final String user; // nome do usuario do baralho
    private float grana;
    private Carta[] cartas; // as cartas que vao possuir
    private int lastCard; // a posição da ultima carta
    private int pontos; // pontuação das cartas
    private final int[] WL; // vetor de vitórias - posição[0] e derrotas - posição[1]

    public BaralhoJogador(String user) { 
        this.user = user;
        this.lastCard = 0;
        this.cartas = new Carta[5];
        this.WL = new int[2];
    }

    public String getUser() {
        return user;
    }

    public float getGrana() {
        return grana;
    }

    public void setGrana(float grana) {
        this.grana = grana;
    }

    public Carta[] getCartas() {
        return cartas;
    }

    public void setCartas(Carta[] cartas) {
        this.cartas = cartas;
    }

    public int getLastCard() {
        return lastCard;
    }

    public void setLastCard(int lastCard) {
        this.lastCard += lastCard;
    }

    public int getPontos() {
        return pontos;
    }

    public void setPontos(int pontos) {
        this.pontos += pontos;
    }

    public int[] getWL() {
        return WL;
    }

    public void imprimir_cartas(){
        System.out.print(this.getUser() + "\n");
        for (int i = 0; i < getLastCard(); i++)
            if (this.getCartas()[i] != null)
                System.out.print("[" + this.getCartas()[i].toString() + "] \n");
        System.out.println("Valor: [" + this.getPontos() + "]\n");
    }
}
