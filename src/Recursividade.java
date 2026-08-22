import java.util.Scanner;

public class Recursividade {

    static Scanner teclado = new Scanner(System.in);

    static int somarPares(int limite) {
        if (limite <= 0) {
            return 0;
        }

        if (limite % 2 == 0) {
            return limite + somarPares(limite - 2);
        }

        return somarPares(limite - 1);
    }

    static double somarVetor(double[] vetor, int posicao) {
        if (posicao == vetor.length) {
            return 0;
        }

        return vetor[posicao] + somarVetor(vetor, posicao + 1);
    }

    static int contarRepeticoes(double[] vetor, double numero, int posicao) {
        if (posicao == vetor.length) {
            return 0;
        }

        if (vetor[posicao] == numero) {
            return 1 + contarRepeticoes(vetor, numero, posicao + 1);
        }

        return contarRepeticoes(vetor, numero, posicao + 1);
    }

    static double[] lerVetor() {
        System.out.print("Digite o tamanho do vetor: ");
        int tamanho = Integer.parseInt(teclado.nextLine());

        double[] vetor = new double[tamanho];

        for (int i = 0; i < tamanho; i++) {
            System.out.print("Digite o valor da posição " + i + ": ");
            vetor[i] = Double.parseDouble(teclado.nextLine());
        }

        return vetor;
    }

    static void menu() {
        int opcao;

        do {
            System.out.println();
            System.out.println("RECURSIVIDADE");
            System.out.println("=============");
            System.out.println("1 - Somar números pares");
            System.out.println("2 - Somar elementos de um vetor");
            System.out.println("3 - Contar repetições em um vetor");
            System.out.println("0 - Sair");
            System.out.print("Digite sua opção: ");

            opcao = Integer.parseInt(teclado.nextLine());

            switch (opcao) {

                case 1:
                    System.out.print("Digite o limite: ");
                    int limite = Integer.parseInt(teclado.nextLine());

                    System.out.println(
                        "Soma dos pares: " + somarPares(limite)
                    );
                    break;

                case 2:
                    double[] vetorSoma = lerVetor();

                    System.out.println(
                        "Soma do vetor: " + somarVetor(vetorSoma, 0)
                    );
                    break;

                case 3:
                    double[] vetorRepeticoes = lerVetor();

                    System.out.print("Digite o número que deseja procurar: ");
                    double numero = Double.parseDouble(teclado.nextLine());

                    System.out.println(
                        "Quantidade de repetições: "
                        + contarRepeticoes(vetorRepeticoes, numero, 0)
                    );
                    break;

                case 0:
                    System.out.println("Programa encerrado.");
                    break;

                default:
                    System.out.println("Opção inválida.");
            }

        } while (opcao != 0);
    }

    public static void main(String[] args) {
        menu();
        teclado.close();
    }
}