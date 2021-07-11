package BlackJack;


import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class BlackJackGame extends game {
    private BaralhoJogador playerExtra; // caso split_option seja utilizado, ele utiliza esse baralho 
    int indice = 0;
    
    public int getIndice() {
        return indice;
    }

    public void setIndice(int indice) {
        this.indice = indice;
    }

    // https://www.fulltilt.com/pt-BR/casino/games/blackjack/rules/?no_redirect=1https://www.fulltilt.com/pt-BR/casino/games/blackjack/rules/?no_redirect=1
    // https://www.casinotop10.com.br/blackjack-gratis REFERENCIA
    // https://www.guiacasinoonline.com/regras-do-blackjack/ referencia principal
    // DICIONARIO ENDGAME: 0 FALSO / 1 DEALER GANHA / 2 PLAYER GANHA / 3 EMPATE / 10 NÃO JOGAR NOVAMENTE

    @Override // HERANÇA
    public void jogar(List<Dado> ranking) { // jogar inicial
        Scanner sc = new Scanner(System.in);

        // criar deck jogadores
        System.out.println("Digite seu nome: ");
        BaralhoJogador player = new BaralhoJogador(sc.next().toUpperCase(Locale.ROOT));
        this.playerExtra = null;
        BaralhoJogador dealer = new BaralhoJogador("DEALER");
        // fim criar deck jogadores

        // criar baralho para comprar cartas
        Baralho baralho = new Baralho(); // cria baralho geral
        baralho.embaralhar(); // coloca cartas no baralho em ordem aleatória

        // variaveis e alteração de dados iniciais
        player.setGrana(250);
        setValorApostado(First_run(baralho, dealer, player));  // inicio de jogo
        player.setGrana(player.getGrana() - getValorApostado());
        Clear_Screen();
        
        // modos pra desenvolvimento, necessário pra testar funções
        // forçar split: força que o baralho principal possua duas cartas iguais na primeira rodada pra poder liberar o metodo split() no menu de maneira facilitada
        /* forçar split
        player.setPontos(-player.getPontos());
        player.getCartas()[1] = player.getCartas()[0];
        player.setPontos(getNum(String.valueOf(player.getCartas()[0].getFace()), player)); // adiciona valor baseado na face da carta
        player.setPontos(getNum(String.valueOf(player.getCartas()[0].getFace()), player)); // adiciona valor baseado na face da carta
        fim forçar split
        */

        // forçar black jack: força que o baralho principal possua 21 pontos com duas cartas iniciais, pra testar o funcionamento de maneira facilitada
        /* forçar black jack
        player.setPontos(-player.getPontos());
        player.setPontos(21);
        fim forçar black jack
        */

        this.setEndgame(0); 
        do {
            VerificarJogada(player, dealer); // função pra verificar e marcar se o jogo acabou
            if (FimJogo(player, dealer)) {
                if (reset(player)) // metodo pra continuar jogando
                    jogar(player, ranking);
                else
                    AddRank(player, ranking);
            }
            if (getEndgame() == 10)
                break;
            Menu(baralho, player, dealer, sc); // menu inicial
            Clear_Screen();
        } while (true);
    } // jogar inicial

    // POLIMORFISMO DE SOBRECARGA
    public void jogar(BaralhoJogador player, List<Dado> ranking) { // caso selecionado jogar novamente na função "reset", é utilizado esse jogar // SOBRECARGA
        if (this.getEndgame() == 10)
            return;
        Scanner sc = new Scanner(System.in);
        // reinicialização de variaveis
        this.playerExtra = null;
        setEndgame(0);
        player.setLastCard(-player.getLastCard());
        player.setCartas(new Carta[5]);
        player.setPontos(-player.getPontos());
        BaralhoJogador dealer = new BaralhoJogador("DEALER");

        // criar baralho para comprar cartas
        Baralho baralho = new Baralho(); // cria baralho geral
        baralho.embaralhar(); // coloca cartas no baralho em ordem aleatória

        // variaveis e alteração de dados iniciais
        setValorApostado(First_run(baralho, dealer, player));  // inicio de jogo
        player.setGrana(player.getGrana() - getValorApostado());
        Clear_Screen();

        /*// forçar black jack
        player.setPontos(-player.getPontos());
        player.setPontos(21);
        // fim forçar black jack*/

        do {
            VerificarJogada(player, dealer); // função pra verificar e marcar se o jogo acabou
            if (FimJogo(player, dealer)) {
                if (reset(player)) // metodo pra continuar jogando
                    jogar(player, ranking);
                else
                    AddRank(player, ranking);
            }
            if (getEndgame() == 10)
                break;
            Menu(baralho, player, dealer, sc); // menu inicial
            Clear_Screen();
        } while (true);
    } // OK
    
    @Override
    public void Menu(Baralho baralho, BaralhoJogador player, BaralhoJogador dealer, Scanner sc) { // menu principal
        System.out.println("[1] Hit\n[2] Stand\n[3] Double\n[4] Split\n[5] Help\n[6] Quit\n"); // opções do menu
        if (this.playerExtra != null)
            this.playerExtra.imprimir_cartas();

        player.imprimir_cartas();
        dealer.imprimir_cartas();
        int opcao = sc.nextInt();
        switch (opcao) {
            case 1:
                Hit_option(baralho, player); // opção hit OK
                break;
            case 2:
                Stand_option(baralho, dealer, player); // opção stand OK
                break;
            case 3: { // opção double OK
                if (player.getLastCard() > 2) // verificação se é possível dobrar aposta
                    System.out.println("Função disponível apenas nas duas primeiras cartas!");
                else {
                    if (player.getPontos() <= 15) { // verifica quantidade de cartas
                        if (getValorApostado() <= player.getGrana()) { // verifica se pode dobrar aposta
                            player.setGrana(player.getGrana() - getValorApostado()); // realiza a alteração na grana do player
                            setValorApostado(getValorApostado() * 2); // aumenta a aposta
                            Hit_option(baralho, player);
                            Stand_option(baralho, dealer, player); // chamada de metodo
                        } else
                            System.out.println("Sem dinheiro suficiente!");
                    } else
                        System.out.println("Não é possível utilizar está opção.");
                }
                break;
            }
            case 4: { // opção split
                if (this.playerExtra == null) { // verificação se já fez split alguma vez
                    if (getValorApostado() * 2 <= player.getGrana()) { // verifica se pode dobrar aposta
                        if (player.getCartas()[0].getFace().equals(player.getCartas()[1].getFace())) { // verifica se possui duas cartas iguais
                            player.getCartas()[1] = null; // remove uma carta do deck
                            player.setLastCard(-1); // diminui a posição da ultima carta
                            player.setPontos(-getNum(String.valueOf(player.getCartas()[0].getFace()), player)); // corrige a pontuação do player
                            Split_option(player, baralho, dealer); // chamada de metodo
                        } else
                            System.out.println("Opção dísponivel apenas se as duas primeiras cartas forem iguais!");
                    } else
                        System.out.println("Sem dinheiro suficiente!");
                } else
                    System.out.println("Já dividiu o deck uma vez!");
                break;
            }
            case 5:
                Help(); // opção ajuda
                Clear_Screen();
                break;
            case 6: { // opção sair
                Clear_Screen();
                return;
            }
            default:
                System.out.println("Digite opção válida!");
                break;
        }
    } // menu principal OK

    public void Menu(Baralho baralho, BaralhoJogador player, BaralhoJogador dealer) { // menu do split // SOBRECARGA
        Scanner sc = new Scanner(System.in);
        int resp=0;
        do {
            VerificarJogada(playerExtra, dealer);
            if (this.getEndgame() != 4 && this.getEndgame() != 5 && this.getEndgame() != 6)
            System.out.println("[1] Hit\n[2] Stand\n[3] Double\n[4] Help\n"); // opções do menu
            this.playerExtra.imprimir_cartas();
            dealer.imprimir_cartas();
            int opcao = sc.nextInt();

            switch (opcao) {
                case 1:
                    Hit_option(baralho, playerExtra); // chamada de função OK
                    break;
                case 2:
                    resp = 1;
                    break;
                case 3: {
                    if (playerExtra.getLastCard() == 2) { // se possuir duas cartas
                        if (playerExtra.getPontos() <= 15) // se os pontos totais forem menor de 15
                            if (getValorApostado() <= player.getGrana()) { // se tiver grana pra apostar
                                Hit_option(baralho, playerExtra); // chamada de função
                                player.setGrana(player.getGrana() - getValorApostado()); // reduz grana do player
                                resp = 1;
                            } else
                                System.out.println("Sem dinheiro suficiente!");
                        else
                            System.out.println("Não é possível utilizar está opção.");
                    } else
                        System.out.println("Função disponível apenas nas duas primeiras cartas!");
                    break;
                }
                case 4:
                    Help();
                    Clear_Screen();
                    break;
                default:
                    System.out.println("Digite opção válida!\n");
                    break;
            }
            if (playerExtra.getPontos() > 21) {
                resp = 1;
                break;
            }
        } while (resp != 1);
    } // menu split_option // SOBRECARGA OK

    public float First_run(Baralho baralho, BaralhoJogador dealer, BaralhoJogador player) { // aposta e distribui cartas
        setValorApostado(bet(player));
        Hit_option(baralho, player);
        Hit_option(baralho, dealer);
        Hit_option(baralho, player);
        return getValorApostado();
    } //OK

    // inicio opções do menu
    public void Hit_option(Baralho baralho, BaralhoJogador deck) { // realiza uma compra de cartas
        Carta card = baralho.darCarta(); // cria uma carta
        deck.getCartas()[deck.getLastCard()] = card; // adiciona a carta ao deck
        deck.setPontos(getNum(String.valueOf(deck.getCartas()[deck.getLastCard()].getFace()), deck)); // adiciona valor baseado na face da carta
        deck.setLastCard(1); // ultima carta +1
    } //OK

    public void Stand_option(Baralho baralho, BaralhoJogador dealer, BaralhoJogador player) { // realiza uma ultima compra
        do {
            Hit_option(baralho, dealer); // compra cartas para o dealer
            VerificarJogada(player, dealer); // verifica se é possível terminar o jogo
        } while (dealer.getPontos() <= player.getPontos() && dealer.getPontos() <= 21); 
        if (playerExtra != null) { // verifica se o split foi utilizado 
            Clear_Screen();
            playerExtra.imprimir_cartas();
            dealer.imprimir_cartas();
            ConfereReserva(dealer, player);
            Time();
        }
        if (dealer.getPontos() >= player.getPontos() && dealer.getPontos() <= 21) {
            this.setEndgame(1); // vitoria do dealer
        }
    } // OK

    public void Split_option(BaralhoJogador player, Baralho baralho, BaralhoJogador dealer) { // divide o baralho inicial
        this.playerExtra = new BaralhoJogador(player.getUser() + 1); // cria novo deck split
        this.playerExtra.getCartas()[this.playerExtra.getLastCard()] = player.getCartas()[0]; // copia o deck do player
        this.playerExtra.setPontos(getNum(String.valueOf(this.playerExtra.getCartas()[this.playerExtra.getLastCard()].getFace()), this.playerExtra)); // corrige a pontuação do playerReserv
        this.playerExtra.setLastCard(1);
        Menu(baralho, player, dealer); // chama o menu com sobrecarga
    }// OK

    public void ConfereReserva(BaralhoJogador dealer, BaralhoJogador player){
        if (playerExtra != null) {
            System.out.print(playerExtra.getUser());
            if (dealer.getPontos() > 21 && playerExtra.getPontos() > 21)
                System.out.println(" EMPATOU!");

            // se alguem estourar
            else if (playerExtra.getPontos() > 21 || dealer.getPontos() > 21) {
                if (playerExtra.getPontos() > 21) { // se player estourar
                    player.setGrana(player.getGrana() - getValorApostado());
                    player.getWL()[1] = player.getWL()[1] + 1;
                    System.out.println(" PERDEU! ");

                } else if (playerExtra.getPontos() <= 21) { // se dealer estourar
                    player.setGrana(player.getGrana() + getValorApostado());
                    player.getWL()[0] = player.getWL()[0] + 1;
                    System.out.println(" GANHOU! ");
                }
            }
            // EMPATE COM BLACK JACK
            else if (dealer.getPontos() == 21 || playerExtra.getPontos() == 21) {
                if (dealer.getLastCard() == 2) {
                    if (playerExtra.getLastCard() == 2)
                        System.out.println(" EMPATOU!");
                    else {
                        player.setGrana(player.getGrana() - getValorApostado());
                        player.getWL()[1] = player.getWL()[1] + 1;
                        System.out.println(" PERDEU! ");
                    }
                } else if (playerExtra.getLastCard() == 2) {
                    player.setGrana(player.getGrana() + getValorApostado());
                    player.getWL()[0] = player.getWL()[0] + 1;
                    System.out.println(" GANHOU! ");
                } else {
                    if (playerExtra.getPontos() > dealer.getPontos()) {
                        player.setGrana(player.getGrana() + getValorApostado());
                        player.getWL()[0] = player.getWL()[0] + 1;
                        System.out.println(" GANHOU! ");
                    } else {
                        if (dealer.getPontos() > playerExtra.getPontos()) {
                            player.setGrana(player.getGrana() - getValorApostado());
                            player.getWL()[1] = player.getWL()[1] + 1;
                            System.out.println(" PERDEU! ");
                        }
                    }
                }
            } else {
                if (playerExtra.getPontos() < 21 && dealer.getPontos() < 21) {
                    if (playerExtra.getPontos() == dealer.getPontos()) {
                        System.out.println(" EMPATOU");
                    }
                    if (playerExtra.getPontos() > dealer.getPontos()) {
                        player.setGrana(player.getGrana() + getValorApostado());
                        System.out.println(" GANHOU! ");
                    } else {
                        player.setGrana(player.getGrana() - getValorApostado());
                        System.out.println(" PERDEU! ");
                    }
                }
            }
        }
    }

    public void Help() {
        System.out.println("                        Black Jack!\n" +
                "\n" +
                "Objetivo: Chegar a 21 pontos nos valores das cartas, sem passar.\n" +
                "Hit -> Pedir mais uma carta.\n" +
                "Stand -> Manter a mão.\n" +
                "Double -> Dobra sua aposta e compra apenas mais uma carta. \n" +
                "Split -> Caso suas duas cartas inicias sejam iguais, é possível dividir em dois baralhos, dobrando sua aposta \n" +
                "Aperte Enter para prosseguir!");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    } // OK
    // fim opções do menu
    
    public void Time(){
        Scanner sc = new Scanner((System.in));
        System.out.println("APERTE ENTER PARA PROSSEGUIR!");
        sc.nextLine();
        } // OK

    public void Clear_Screen() { // gambiarra jóia pra limpar tela (C -> system("cls") >>>>> all)
        for (int i = 0; i < 10; i++)
            System.out.println("\n");
    } // Gambiarra OK

    public void VerificarJogada(BaralhoJogador player, BaralhoJogador dealer) { // verifica acontecimentos
        if (dealer.getPontos() == player.getPontos())
            this.setEndgame(0);
        // se alguem tiver mais de 21 pontos
        if (dealer.getPontos() > 21 || player.getPontos() > 21) {
            if (dealer.getPontos() > 21)
                this.setEndgame(2); // player ganha
            else {
                ConfereReserva(dealer, player);
                this.setEndgame(1);// dealer ganha
            }
        }
        // se os dois possuirem Black Jack
        if (player.getPontos() == 21 && dealer.getPontos() == 21) {
            if (dealer.getLastCard() == 2) { // se o dealer tiver Black Jack
                if (player.getLastCard() == 2) // se eu também tiver Black Jack
                    this.setEndgame(3); // empate
                else
                    this.setEndgame(1); // dealer ganha
            } else if (player.getLastCard() > 2 && dealer.getLastCard() > 2)
                this.setEndgame(3); // empate
            else {
                if (player.getLastCard() == 2)
                    this.setEndgame(2);
                else
                    this.setEndgame(3);
            }
        }
    } // OK

    public boolean FimJogo(BaralhoJogador player, BaralhoJogador dealer) { // confere as marcações de fim de jogo
        if (this.getEndgame() == 1 || this.getEndgame() == 2 || this.getEndgame() == 3) { // derrota, vitoria, empate
            player.imprimir_cartas();
            dealer.imprimir_cartas();
            System.out.print(player.getUser());
            if (this.getEndgame() == 1) {  // 1 = vitoria do dealer
                System.out.println(" PERDEU!");
                player.getWL()[1] = player.getWL()[1] + 1;
            } else if (this.getEndgame() == 2) { // 2 = vitoria do player
                System.out.println(" GANHOU!");
                player.setGrana(player.getGrana() + getValorApostado() * 2);
                player.getWL()[0] = player.getWL()[0] + 1;
            } else { // 3 = empate
                System.out.println(" EMPATOU!");
                player.setGrana(getValorApostado() + player.getGrana());
            }
            return true;
        }
        return false; // caso não haja alteração no estado da variável endgame
    } // OK

    public float bet(BaralhoJogador player) {
        float aposta = 0;
        Scanner input = new Scanner(System.in);
        int resp;
        do {
            System.out.print("\nSaldo dísponivel: R$ " + (player.getGrana() - aposta) + "\nAPOSTAS:\n\t[0] DEAL\n\t[1] 1,00\n\t[2] 5,00\n\t[3] 10,00\n\t[4] 25,00\n\t[5] 50,00\n\t[6] 100,00\n");
            resp = input.nextInt();
            switch (resp) {
                case 0: {
                    if (aposta == 0) {
                        System.out.println("Não é possível apostar R$ 0,00\n");
                        resp = 1;
                        break;
                    } else {
                        System.out.printf("\rValor total apostado: R$ %.2f\r\n", aposta);
                        return aposta;
                    }
                }
                case 1: {
                    if (aposta + 1 > player.getGrana())
                        System.out.println("Não é possível apostar este valor.");
                    else
                        aposta += 1.00;
                    break;
                }

                case 2: {
                    if (aposta + 5 > player.getGrana())
                        System.out.println("Não é possível apostar este valor.");
                    else
                        aposta += 5.00;
                    break;
                }
                case 3: {
                    if (aposta + 10 > player.getGrana())
                        System.out.println("Não é possível apostar este valor.");
                    else
                        aposta += 10.00;
                    break;
                }
                case 4: {
                    if (aposta + 25 > player.getGrana())
                        System.out.println("Não é possível apostar este valor.");
                    else
                        aposta += 25.00;
                    break;
                }
                case 5: {
                    if (aposta + 50 > player.getGrana())
                        System.out.println("Não é possível apostar este valor.");
                    else
                        aposta += 50.00;
                    break;
                }
                case 6: {
                    if (aposta + 100 > player.getGrana())
                        System.out.println("Não é possível apostar este valor.");
                    else
                        aposta += 100.00;
                    break;
                }
                default:
                    System.out.println("Informe uma opção correta.\n");
                    break;
            }
            if (aposta != 0) {
                System.out.printf("Valor total apostado: R$ %.2f\n", aposta);
            }

        } while (resp != 0.0);
        return aposta;
    } // OK

    public int getNum(String carta, BaralhoJogador deck) { // enum pra dar valores as cartas
        switch (carta) {
            case "AS":
                for (int i = 0; i < 5; i++) {
                    if (deck.getCartas()[i] != null)
                        if ((deck.getPontos() + 11) <= 21) {
                            return 11;
                        }
                }
                return 1;
            case "DOIS":
                return 2;
            case "TRES":
                return 3;
            case "QUATRO":
                return 4;
            case "CINCO":
                return 5;
            case "SEIS":
                return 6;
            case "SETE":
                return 7;
            case "OITO":
                return 8;
            case "NOVE":
                return 9;
            case "DEZ":
            case "VALETE":
            case "REI":
            case "DAMA":
                return 10;
        }
        return 0;
    } // OK

    public void AddRank(BaralhoJogador player, List<Dado> ranking) {
        Dado info = new Dado(player.getUser(), player.getWL());
        ranking.add(info);
        if (ranking.size() > 1){
            for (int i = 0; i < ranking.size(); i++) {
                for (int j = i+1; j < ranking.size() ; j++) {
                    if (ranking.get(i).getWinsloses()[0] < ranking.get(j).getWinsloses()[0]){
                        Dado aux = ranking.get(i);
                        ranking.set(i, ranking.get(j));
                        ranking.set(j, aux);
                        aux = null;
                    }
                }
            }
        }
        setIndice(getIndice() + 1);
    }  //OK

    public boolean reset(BaralhoJogador player) { // metodo pra reiniciar o jogo mantendo o mesmo jogador
        System.out.println("Deseja jogar novamente?");
        Scanner sc = new Scanner(System.in);
        String opcao = sc.next().toUpperCase(Locale.ROOT);
        if (opcao.equals("SIM") || opcao.equals("S"))
            return true;
        else if (opcao.equals("NAO") || opcao.equals("N") || opcao.equals("NÃO") || opcao.equals("Ñ")){
            this.setEndgame(10);
            return false;
        }
        return false;
    } // OK
}
