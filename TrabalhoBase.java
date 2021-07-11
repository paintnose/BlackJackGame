/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BlackJack;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/*
REGRAS:
    1. Este trabalho vale 30% da nota final, deve ser realizado grupos de três integrantes; OK
    2. A formação dos grupos deve ocorrer no dia da especificação do trabalho OK
    3. O código fonte deve ser entregue no ambiente virtual na tarefa correspondente até as 19h00 do dia da entrega
    4. A apresentação ocorre durante a aula do mesmo dia da entrega, quando cada integrante do grupo deve
        demonstrar e explicar todas as funcionalidades implementadas. Trabalhos entregues e não apresentados, ou
        apresentados e não entregues serão desconsiderados);
    5. Os arquivos fontes entregues serão submetidos a um software verificador de plágios (com busca online e em
        comparação aos demais grupos). Trabalhos não originais serão desconsiderados.
        Jogo de Cartas
    O baralho francês de 52 cartas (apresentado na figura a seguir) apresenta uma infinidade de
    opções de jogos. Fonte: https://pt.wikipedia.org/wiki/Baralho

    Seu grupo deve escolher e implementar um jogo de cartas na linguagem de programação Java ou python que atenda aos seguintes requisitos mínimos:
        ● Iniciar novo jogo: identificando os jogadores, embaralhando, dando as cartas e as estruturas necessárias; OK
        ● Adicionar jogada: de forma alternada entre os jogadores, validando a jogada de
        acordo com as regras do jogo e as cartas disponíveis; OK
        ● Finalizar jogo: de forma automática a partir do atingimento de uma situação de final de jogo
        ou comando do jogador (batida ou desistência); OK
        ● Ao encerrar o jogo deve ser apresentado o placar da partida (vitória, derrota, empate e pontuação); OK
        ● A qualquer momento um jogador deve poder acessar a ajuda do jogo que deve apresentar o conjunto de regras e os comandos disponíveis. OK

    Avaliação
        Correto funcionamento do programa atendendo aos requisitos mínimos;
        (0,5 pts) Iniciar novo jogo OK
        (0,3 pts) Adicionar jogada OK
        (0,2 pts) Finalizar jogo OK
        (0,2 pts) Placar da partida OK
        (0,2 pts) Ajuda do jogo OK
        (0,8 pts) Correta utilização dos conceitos de POO vistos em aula: herança OK; polimorfismo OK, sobrecarga OK;
        composição/agregação OK; listas OK;
        (0,2 pts) Organização; indentação; documentação do código fonte; OK
*/

/**
 *
 * @author ghelfer
 */
public class TrabalhoBase {
    public static void main(String[] args) {

        //instanciamento de objetos
        Scanner sc = new Scanner(System.in);
        BlackJackGame game = new BlackJackGame(); // instancia o Black Jack
        List<Dado> ranking = new ArrayList<>(4);
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
