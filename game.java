package BlackJack;

import java.util.List;
import java.util.Scanner;

public abstract class game {

    private int endgame;
    private float valorApostado;

    public int getEndgame() {
        return endgame;
    }

    public void setEndgame(int endgame) {
        this.endgame = endgame;
    }

    public float getValorApostado() {
        return valorApostado;
    }

    public void setValorApostado(float valorApostado) {
        this.valorApostado = valorApostado;
    }

    public abstract void jogar(List<Dado> ranking);

    public abstract void Menu(Baralho baralho, BaralhoJogador player, BaralhoJogador dealer, Scanner sc);

    public abstract boolean reset(BaralhoJogador player, List<Dado> ranking);
}
