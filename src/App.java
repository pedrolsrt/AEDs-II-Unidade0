import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Scanner;

public class App {

    /** Quantidade máxima de produtos que podem ser armazenados no vetor */
    static final int MAX_NOVOS_PRODUTOS = 10;

    /** Nome do arquivo de dados. O arquivo deve estar localizado na raiz do projeto */
    static String nomeArquivoDados;
    
    /** Scanner para leitura de dados do teclado */
    static Scanner teclado;

    /** Vetor de produtos cadastrados */
    static Produto[] produtosCadastrados;

    /** Quantidade de produtos cadastrados atualmente no vetor */
    static int quantosProdutos = 0;

    /** Gera um efeito de pausa na CLI. Espera por um enter para continuar */
    static void pausa() {
        System.out.println("Digite enter para continuar...");
        teclado.nextLine();
    }

    /** Cabeçalho principal da CLI do sistema */
    static void cabecalho() {
        System.out.println("AEDs II COMÉRCIO DE COISINHAS");
        System.out.println("=============================");
    }
    
    /** Imprime o menu principal, lê a opção do usuário e a retorna */
    static int menu() {
        cabecalho();
        System.out.println("1 - Listar todos os produtos");
        System.out.println("2 - Procurar e imprimir os dados de um produto");
        System.out.println("3 - Cadastrar novo produto");
        System.out.println("0 - Sair");
        System.out.print("Digite sua opção: ");
        return Integer.parseInt(teclado.nextLine());
    }
    
    /**
     * Lê os dados de um arquivo-texto e retorna um vetor de produtos.
     */
    static Produto[] lerProdutos(String nomeArquivoDados) {

        try {
            File arquivo = new File(nomeArquivoDados);
            Scanner leitor = new Scanner(arquivo, Charset.forName("UTF-8"));

            int quantidade = Integer.parseInt(leitor.nextLine());

            Produto[] produtos = new Produto[quantidade + MAX_NOVOS_PRODUTOS];

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
    
    /**
     * Localiza um produto pelo nome e imprime seus dados.
     */
    static void localizarProdutos() {

        System.out.print("Digite o nome do produto: ");
        String nome = teclado.nextLine();

        Produto procurado = new ProdutoNaoPerecivel(nome, 1.0);

        for (int i = 0; i < quantosProdutos; i++) {

            if (produtosCadastrados[i].equals(procurado)) {
                System.out.println(produtosCadastrados[i]);
                return;
            }
        }

        System.out.println("Produto não encontrado.");
    }
    
    /**
     * Salva os dados dos produtos cadastrados no arquivo.
     */
    public static void salvarProdutos(String nomeArquivo) {

        try {
            FileWriter escritor = new FileWriter(nomeArquivo);

            escritor.write(quantosProdutos + "\n");

            for (int i = 0; i < quantosProdutos; i++) {
                escritor.write(produtosCadastrados[i].gerarDadosTexto() + "\n");
            }

            escritor.close();

        } catch (IOException e) {
            System.out.println("Erro ao salvar os produtos.");
        }
    }
    
    /** Lista todos os produtos cadastrados, numerados, um por linha */
    static void listarTodosOsProdutos() {
    	
    }
    
    /**
     * Rotina para cadastro de um novo produto.
     */
    static void cadastrarProduto() {
    	
    }  
    
	public static void main(String[] args) {

        teclado = new Scanner(System.in, Charset.forName("UTF-8"));

        nomeArquivoDados = "dadosProdutos.csv";

        produtosCadastrados = lerProdutos(nomeArquivoDados);
        
        int opcao = -1;
      
        do {
            opcao = menu();

            switch (opcao) {
                case 1 -> listarTodosOsProdutos();
                case 2 -> localizarProdutos();
                case 3 -> cadastrarProduto();
            }

            pausa();

        } while (opcao != 0);       

        salvarProdutos(nomeArquivoDados);

        teclado.close();    
    }
}