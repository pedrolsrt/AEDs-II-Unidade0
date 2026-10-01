import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

public abstract class Produto {
	
	private static final double MARGEM_PADRAO = 0.2;
	protected String descricao;
	protected double precoCusto;
	protected double margemLucro;
	
    /**
     * Inicializador privado. Os valores default, em caso de erro, são:
     * "Produto sem descrição", R$ 0.00, 0.0  
     * @param desc Descrição do produto (mínimo de 3 caracteres)
     * @param precoCusto Preço do produto (mínimo 0.01)
     * @param margemLucro Margem de lucro (mínimo 0.01)
     */
	private void init(String desc, double precoCusto, double margemLucro) {
		
		if (desc != null && desc.trim().length() >= 3
                && !desc.contains(";") && !desc.contains("\n") && !desc.contains("\r")
                && Double.isFinite(precoCusto) && precoCusto > 0.0
                && Double.isFinite(margemLucro) && margemLucro > 0.0) {
		descricao = desc.trim();
			this.precoCusto = precoCusto;
			this.margemLucro = margemLucro;
		} else {
			throw new IllegalArgumentException("Valores inválidos para os dados do produto.");
		}
	}
	
    /**
     * Construtor completo.
     */
	protected Produto(String desc, double precoCusto, double margemLucro) {
		init(desc, precoCusto, margemLucro);
	}
	
    /**
     * Construtor sem margem de lucro.
     */
	protected Produto(String desc, double precoCusto) {
		init(desc, precoCusto, MARGEM_PADRAO);
	}
	
    /**
     * Retorna o valor de venda do produto.
     */
	public abstract double valorDeVenda();
	
	/**
	 * Descrição do produto.
	 */
    @Override
	public String toString() {
    	
    	NumberFormat moeda = NumberFormat.getCurrencyInstance();
    	
    	return String.format("NOME: " + descricao + ": " + moeda.format(valorDeVenda()));
	}
    
    /**
     * Igualdade de produtos: caso possuam o mesmo nome/descrição.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Produto) {
            Produto outroProduto = (Produto) obj;
            return descricao.equalsIgnoreCase(outroProduto.descricao);
        }
        return false;
    }
    
    /**
     * Cria um produto a partir de uma linha de dados em formato texto.
     */
    static Produto criarDoTexto(String linha) {
    	
        if (linha == null) {
            throw new IllegalArgumentException("Linha de produto ausente.");
        }
    	String[] dados = linha.split(";", -1);
        if (dados.length < 4) {
            throw new IllegalArgumentException("Linha de produto incompleta.");
        }
    	
    	int tipo = Integer.parseInt(dados[0]);
        if ((tipo == 1 && dados.length != 4) || (tipo == 2 && dados.length != 5)) {
            throw new IllegalArgumentException("Quantidade de campos inválida.");
        }
    	String descricao = dados[1];
    	double precoCusto = Double.parseDouble(dados[2]);
    	double margemLucro = Double.parseDouble(dados[3]);
    	
    	if (tipo == 1) {
    		return new ProdutoNaoPerecivel(descricao, precoCusto, margemLucro);
    	}
    	
    	if (tipo == 2) {
    		DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/uuuu")
                    .withResolverStyle(ResolverStyle.STRICT);
    		LocalDate validade = LocalDate.parse(dados[4], formato);
    		
    		return new ProdutoPerecivel(descricao, precoCusto, margemLucro, validade);
    	}
    	
    	throw new IllegalArgumentException("Tipo de produto inválido: " + tipo);
    }
    	
    /**
     * Gera uma linha de texto a partir dos dados do produto.
     */
    public abstract String gerarDadosTexto();
}
