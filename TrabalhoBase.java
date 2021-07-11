package BlackJack;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TrabalhoBase {
    public static void main(String[] args) {

        //instanciamento de objetos
        Scanner sc = new Scanner(System.in);
        BlackJackGame game = new BlackJackGame(); // instancia o Black Jack
        List<Dado> ranking = new ArrayList<>(4); // instanciamento do ranking
        
        // menu inicial
        do{
            System.out.println("Black Jack Game\n[1] - Start\n[2] - Ranking\n[3] - Help\n[4] - Quit");
            int opcao = sc.nextInt();
            switch (opcao) {
                case 1 : {
                    game.jogar(ranking);
                    break;
                }
                case 2 :if (!ranking.isEmpty()){
                    System.out.println("=-=-=-= LENDAS =-=-=-=");
                    for (int i = 0; i < ranking.size(); i++) {
                        System.out.println("["+i+"] " + ranking.get(i).toString() + "\n");
                    }
                }
                else
                    System.out.println("Sem lendas, por enquanto!\n");
                    break;
                case 3 : System.out.println("\t\tBlack Jack!\n" +
                                            "Objetivo: Chegar o mais próximo possível de 21 no total das cartas, sem passar.\n" +
                                            "Hit -> Pedir mais uma carta.\n" +
                                            "Stand -> Manter a mão.\n" +
                                            "Double -> Dobra sua aposta e compra apenas mais uma carta.\n" +
                                            "Split -> Caso suas duas cartas inicias sejam iguais, é possível dividir em dois baralhos, dobrando sua aposta.\n");
                    break;
                case 4 : {
                    System.out.println("Obrigado por jogar nosso Black Jack!");
                    System.exit(1);
                    break;
                }
                default : System.out.println("Selecione opção válida!");
                    break;
            }

        }while (true);
    }
}
