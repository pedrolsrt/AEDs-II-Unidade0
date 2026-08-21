import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class App {

    static final int MAX_NOVOS_PRODUTOS = 10;

    static String nomeArquivoDados;

    static Scanner teclado;

    static Produto[] produtosCadastrados;

    static int quantosProdutos = 0;

    static void pausa() {
        System.out.println("Digite enter para continuar...");
        teclado.nextLine();
    }

    static void cabecalho() {
        System.out.println("AEDs II COMÉRCIO DE COISINHAS");
        System.out.println("=============================");
    }

    static int menu() {
        cabecalho();

        System.out.println("1 - Listar todos os produtos");
        System.out.println("2 - Procurar e imprimir os dados de um produto");
        System.out.println("3 - Cadastrar novo produto");
        System.out.println("0 - Sair");

        System.out.print("Digite sua opção: ");

        return Integer.parseInt(teclado.nextLine());
    }

    static Produto[] lerProdutos(String nomeArquivoDados) {

        try {
            File arquivo = new File(nomeArquivoDados);
            Scanner leitor = new Scanner(arquivo, Charset.forName("UTF-8"));

            int quantidade = Integer.parseInt(leitor.nextLine());

            Produto[] produtos =
                    new Produto[quantidade + MAX_NOVOS_PRODUTOS];

            for (int i = 0; i < quantidade; i++) {
                String linha = leitor.nextLine();

                produtos[i] = Produto.criarDoTexto(linha);
            }

            quantosProdutos = quantidade;

            leitor.close();

            return produtos;

        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo de dados.");

            return new Produto[MAX_NOVOS_PRODUTOS];
        }
    }

    static void localizarProdutos() {

        System.out.print("Digite o nome do produto: ");
        String nome = teclado.nextLine();

        Produto procurado =
                new ProdutoNaoPerecivel(nome, 1.0);

        for (int i = 0; i < quantosProdutos; i++) {

            if (produtosCadastrados[i].equals(procurado)) {

                System.out.println(produtosCadastrados[i]);

                return;
            }
        }

        System.out.println("Produto não encontrado.");
    }

    public static void salvarProdutos(String nomeArquivo) {

        try {
            FileWriter escritor =
                    new FileWriter(nomeArquivo);

            escritor.write(quantosProdutos + "\n");

            for (int i = 0; i < quantosProdutos; i++) {

                escritor.write(
                        produtosCadastrados[i].gerarDadosTexto()
                                + "\n"
                );
            }

            escritor.close();

        } catch (IOException e) {

            System.out.println(
                    "Erro ao salvar os produtos."
            );
        }
    }

    static void listarTodosOsProdutos() {

        if (quantosProdutos == 0) {

            System.out.println(
                    "Nenhum produto cadastrado."
            );

            return;
        }

        for (int i = 0; i < quantosProdutos; i++) {

            System.out.println(
                    (i + 1) + " - " + produtosCadastrados[i]
            );
        }
    }

    static void cadastrarProduto() {

        if (quantosProdutos >= produtosCadastrados.length) {

            System.out.println(
                    "Não há espaço para novos produtos."
            );

            return;
        }

        System.out.println("1 - Produto não perecível");
        System.out.println("2 - Produto perecível");

        System.out.print("Digite o tipo do produto: ");
        int tipo = Integer.parseInt(teclado.nextLine());

        System.out.print("Descrição: ");
        String descricao = teclado.nextLine();

        System.out.print("Preço de custo: ");
        double precoCusto =
                Double.parseDouble(teclado.nextLine());

        System.out.print("Margem de lucro: ");
        double margemLucro =
                Double.parseDouble(teclado.nextLine());

        Produto novoProduto;

        if (tipo == 1) {

            novoProduto =
                    new ProdutoNaoPerecivel(
                            descricao,
                            precoCusto,
                            margemLucro
                    );

        } else if (tipo == 2) {

            System.out.print(
                    "Data de validade (dd/MM/yyyy): "
            );

            String data = teclado.nextLine();

            DateTimeFormatter formato =
                    DateTimeFormatter.ofPattern(
                            "dd/MM/yyyy"
                    );

            LocalDate validade =
                    LocalDate.parse(data, formato);

            novoProduto =
                    new ProdutoPerecivel(
                            descricao,
                            precoCusto,
                            margemLucro,
                            validade
                    );

        } else {

            System.out.println(
                    "Tipo de produto inválido."
            );

            return;
        }

        produtosCadastrados[quantosProdutos] =
                novoProduto;

        quantosProdutos++;

        System.out.println(
                "Produto cadastrado com sucesso."
        );
    }

    public static void main(String[] args) {

        teclado =
                new Scanner(
                        System.in,
                        Charset.forName("UTF-8")
                );

        nomeArquivoDados =
                "dadosProdutos.csv";

        produtosCadastrados =
                lerProdutos(nomeArquivoDados);

        int opcao = -1;

        do {

            opcao = menu();

            switch (opcao) {

                case 1 ->
                        listarTodosOsProdutos();

                case 2 ->
                        localizarProdutos();

                case 3 ->
                        cadastrarProduto();
            }

            if (opcao != 0) {
                pausa();
            }

        } while (opcao != 0);

        salvarProdutos(nomeArquivoDados);

        teclado.close();
    }
}