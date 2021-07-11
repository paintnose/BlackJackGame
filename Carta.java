package BlackJack;

/**
 *
 * carta base
 */
public class Carta {

    private final String face; // face da carta ("Ace", "Deuce", ...)
    private final String naipe; // naipe da carta ("Hearts", "Diamonds", ...)

    // construtor de dois argumentos inicializa face e naipe da carta
    public Carta(String face, String naipe) {
        this.face = face; // inicializa face da carta
        this.naipe = naipe; // inicializa naipe da carta
    } // fim do construtor Card de dois argumentos

    public String getFace() {
        return face;
    }

    // retorna representação String de Card
    @Override
    public String toString() {
        return face + " de " + naipe;
    }

} 
