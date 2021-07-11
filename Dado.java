package BlackJack;

public class Dado {
    private String nome;
    private final int[] winsloses;

    public Dado(String nome, int[] winsloses) {
        this.nome = nome;
        this.winsloses = winsloses;
    }

    public int[] getWinsloses() {
        return winsloses;
    }

    @Override
    public String toString() {
        return "Nome: " + this.nome + " WIN: " + this.getWinsloses()[0] + " LOSSES: " +  this.getWinsloses()[1];
    }
}
