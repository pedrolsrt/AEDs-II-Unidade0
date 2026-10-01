import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Scanner;

public class App {
    static final int MAX_NOVOS_PRODUTOS = 10;
    static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter
            .ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);
    static String nomeArquivoDados;
    static Scanner teclado;
    static Produto[] produtosCadastrados;
    static int quantosProdutos = 0;
    static boolean falhaLeitura = false;

    static void pausa() {
        System.out.println("Digite enter para continuar...");
        teclado.nextLine();
    }

    static void cabecalho() {
        System.out.println("AEDs II COMÉRCIO DE COISINHAS");
        System.out.println("=============================");
    }

    static int lerInteiro(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            try {
                return Integer.parseInt(teclado.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Digite um número inteiro válido.");
            }
        }
    }

    static double lerPositivo(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            try {
                double valor = Double.parseDouble(teclado.nextLine().trim().replace(',', '.'));
                if (Double.isFinite(valor) && valor > 0) {
                    return valor;
                }
            } catch (NumberFormatException e) {
                // Solicita novamente quando a entrada não é um número.
            }
            System.out.println("Digite um número maior que zero, como 12,50 ou 12.50.");
        }
    }

    static LocalDate lerValidade() {
        while (true) {
            System.out.print("Data de validade (dd/MM/yyyy): ");
            try {
                LocalDate validade = LocalDate.parse(teclado.nextLine().trim(), FORMATO_DATA);
                if (!validade.isBefore(LocalDate.now())) {
                    return validade;
                }
                System.out.println("A validade não pode ser anterior ao dia atual.");
            } catch (DateTimeParseException e) {
                System.out.println("Digite uma data válida no formato dd/MM/yyyy.");
            }
        }
    }

    static int menu() {
        cabecalho();
        System.out.println("1 - Listar todos os produtos");
        System.out.println("2 - Procurar e imprimir os dados de um produto");
        System.out.println("3 - Cadastrar novo produto");
        System.out.println("0 - Sair");
        return lerInteiro("Digite sua opção: ");
    }

    static Produto[] lerProdutos(String nomeArquivo) {
        quantosProdutos = 0;
        falhaLeitura = false;
        try (Scanner leitor = new Scanner(Path.of(nomeArquivo), StandardCharsets.UTF_8)) {
            int quantidade = Integer.parseInt(leitor.nextLine().trim());
            if (quantidade < 0 || quantidade > Integer.MAX_VALUE - MAX_NOVOS_PRODUTOS) {
                throw new IllegalArgumentException("Quantidade de produtos inválida.");
            }
            Produto[] produtos = new Produto[quantidade + MAX_NOVOS_PRODUTOS];
            for (int i = 0; i < quantidade; i++) {
                produtos[i] = Produto.criarDoTexto(leitor.nextLine());
            }
            if (leitor.ioException() != null) {
                throw leitor.ioException();
            }
            quantosProdutos = quantidade;
            return produtos;
        } catch (IOException | RuntimeException e) {
            falhaLeitura = true;
            System.out.println("Erro ao ler o arquivo de dados: " + e.getMessage());
            return new Produto[MAX_NOVOS_PRODUTOS];
        }
    }

    static void imprimirProduto(Produto produto) {
        try {
            System.out.println(produto);
        } catch (IllegalArgumentException e) {
            System.out.println(produto.descricao + ": " + e.getMessage());
        }
    }

    static void localizarProdutos() {
        System.out.print("Digite o nome do produto: ");
        String nome = teclado.nextLine().trim();
        for (int i = 0; i < quantosProdutos; i++) {
            if (produtosCadastrados[i].descricao.equalsIgnoreCase(nome)) {
                imprimirProduto(produtosCadastrados[i]);
                return;
            }
        }
        System.out.println("Produto não encontrado.");
    }

    public static void salvarProdutos(String nomeArquivo) {
        if (falhaLeitura) {
            System.out.println("Arquivo preservado: a leitura inicial não foi concluída.");
            return;
        }
        try (BufferedWriter escritor = Files.newBufferedWriter(
                Path.of(nomeArquivo), StandardCharsets.UTF_8)) {
            escritor.write(Integer.toString(quantosProdutos));
            escritor.newLine();
            for (int i = 0; i < quantosProdutos; i++) {
                escritor.write(produtosCadastrados[i].gerarDadosTexto());
                escritor.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar os produtos: " + e.getMessage());
        }
    }

    static void listarTodosOsProdutos() {
        if (quantosProdutos == 0) {
            System.out.println("Nenhum produto cadastrado.");
            return;
        }
        for (int i = 0; i < quantosProdutos; i++) {
            System.out.print((i + 1) + " - ");
            imprimirProduto(produtosCadastrados[i]);
        }
    }

    static void cadastrarProduto() {
        if (quantosProdutos >= produtosCadastrados.length) {
            System.out.println("Não há espaço para novos produtos.");
            return;
        }
        System.out.println("1 - Produto não perecível");
        System.out.println("2 - Produto perecível");
        int tipo = lerInteiro("Digite o tipo do produto: ");
        if (tipo != 1 && tipo != 2) {
            System.out.println("Tipo de produto inválido.");
            return;
        }
        String descricao;
        while (true) {
            System.out.print("Descrição: ");
            descricao = teclado.nextLine().trim();
            if (descricao.length() >= 3 && !descricao.contains(";")) {
                break;
            }
            System.out.println("Use pelo menos 3 caracteres e não inclua ponto e vírgula.");
        }
        double precoCusto = lerPositivo("Preço de custo: ");
        double margemLucro = lerPositivo("Margem de lucro (ex.: 0,20 para 20%): ");
        Produto novoProduto;
        if (tipo == 1) {
            novoProduto = new ProdutoNaoPerecivel(descricao, precoCusto, margemLucro);
        } else {
            novoProduto = new ProdutoPerecivel(descricao, precoCusto, margemLucro, lerValidade());
        }
        produtosCadastrados[quantosProdutos++] = novoProduto;
        System.out.println("Produto cadastrado com sucesso.");
    }

    public static void main(String[] args) {
        teclado = new Scanner(System.in, StandardCharsets.UTF_8);
        nomeArquivoDados = "dadosProdutos.csv";
        if (Files.notExists(Path.of(nomeArquivoDados))) {
            produtosCadastrados = new Produto[MAX_NOVOS_PRODUTOS];
            System.out.println("Arquivo de dados ausente. Iniciando um cadastro vazio.");
        } else {
            produtosCadastrados = lerProdutos(nomeArquivoDados);
        }
        if (falhaLeitura) {
            System.out.println("Corrija o arquivo de dados antes de continuar. O original foi preservado.");
            teclado.close();
            return;
        }
        int opcao;
        do {
            opcao = menu();
            switch (opcao) {
                case 1 -> listarTodosOsProdutos();
                case 2 -> localizarProdutos();
                case 3 -> cadastrarProduto();
                case 0 -> { }
                default -> System.out.println("Opção inválida.");
            }
            if (opcao != 0) {
                pausa();
            }
        } while (opcao != 0);
        salvarProdutos(nomeArquivoDados);
        teclado.close();
    }
}
