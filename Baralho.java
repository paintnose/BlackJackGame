package BlackJack;

import java.util.Random;

/**
 *
 * code base
 */
public class Baralho {
    // atributos
    private final Carta[] cartas; // array de objetos Carta
    private int cartaAtual; // Índice do próximo Carta a ser distribuído
    private final int NUMERO_DE_CARTAS = 52; // número constante de Cartas
    private final Random random; // gerador de número aleatório

    // construtor preenche baralho de cartas
    public Baralho() {
        String[] faces = {"AS", "DOIS", "TRES", "QUATRO", "CINCO", "SEIS",
                "SETE", "OITO", "NOVE", "DEZ", "VALETE", "DAMA", "REI"}; //Jack, Queen, King
        String[] naipes = {"COPAS", "OUROS", "PAUS", "ESPADAS"}; //Hearts,Diamonds,Clubs,Spades

        cartas = new Carta[NUMERO_DE_CARTAS]; // cria array de objetos Carta
        cartaAtual = 0; // configura currentCarta então o primeiro Carta distribuído é deck[ 0 ]
        random = new Random(); // cria gerador de número aleatório

        // preenche baralho com objetos Carta
        for (int cont = 0; cont < cartas.length; cont++) {
            cartas[cont] = new Carta(faces[cont % 13], naipes[cont / 13]);
        }
    } // fim do construtor DeckOfCartas

    // metodos
    // embaralhar cartas
    public void embaralhar() {
        // depois de embaralhar, a distribuição deve iniciar em baralho[ 0 ] novamente
        cartaAtual = 0; // reinicializa currentCarta

        // para cada Carta, seleciona outra Carta aleatório e as compara
        for (int first = 0; first < cartas.length; first++) {
            // seleciona um número aleatório entre 0 e 51
            int second = random.nextInt(NUMERO_DE_CARTAS);

            // compara Carta atual com Carta aleatoriamente selecionada
            Carta temp = cartas[first];
            cartas[first] = cartas[second];
            cartas[second] = temp;
        } // fim de for
    } // fim do método shuffle

    // distribui um Carta
    public Carta darCarta() {
        // determina se ainda há Cartas a ser distribuídos
        if (cartaAtual < cartas.length) {
            return cartas[cartaAtual++]; // retorna Carta atual no array
        } else {
            return null; // retorna nulo p/ indicar que todos os Cartas foram distribuídos
        }
    } // fim do método dealCarta
} // fim da classe DeckOfCartas

