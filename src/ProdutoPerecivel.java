import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class ProdutoPerecivel extends Produto {

	/** Desconto para proximidade de validade: 25% */
	private static final double DESCONTO = 0.25;
	
	/** Prazo, em dias, para conceder o desconto por proximidade da validade */
	private static final int PRAZO_DESCONTO = 7;
	
	/** Data de validade do produto */
	private LocalDate dataDeValidade;
	
	/**
	 * Construtor completo.
	 */
	public ProdutoPerecivel(String desc, double precoCusto, double margemLucro, LocalDate validade) {
		
		super(desc, precoCusto, margemLucro);
		
		if (validade.isBefore(LocalDate.now())) {
			throw new IllegalArgumentException("Data de validade do produto é anterior ao dia de hoje!");
		}
		
		dataDeValidade = validade;
	}
	
	/**
	 * Construtor com margem de lucro padrão.
	 */
	public ProdutoPerecivel(String desc, double precoCusto, LocalDate validade) {
		
		super(desc, precoCusto);
		
		if (validade.isBefore(LocalDate.now())) {
			throw new IllegalArgumentException("Data de validade do produto é anterior ao dia de hoje!");
		}
		
		dataDeValidade = validade;
	}

	/**
	 * Retorna o valor de venda do produto.
	 */
	@Override
	public double valorDeVenda() {
		
		double precoVenda;
		
		if (dataDeValidade.isBefore(LocalDate.now())) {
			throw new IllegalArgumentException("Data de validade do produto é anterior ao dia de hoje!");
		}
		
		precoVenda = (precoCusto * (1.0 + margemLucro));
				
		if (LocalDate.now().until(dataDeValidade).getDays() <= PRAZO_DESCONTO) {
			precoVenda = precoVenda * (1.0 - DESCONTO);
		}
		
		return precoVenda;
	}
	
	/**
	 * Descrição do produto com sua data de validade.
	 */
    @Override
    public String toString() {
    	
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        String dados = super.toString();
        dados += "\nVálido até " + formato.format(dataDeValidade);
        
        return dados;
    }
    
    /**
     * Gera uma linha de texto a partir dos dados do produto.
     */
	@Override
    public String gerarDadosTexto() {
		
		DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		
		return String.format(
				Locale.US,
				"2;%s;%.2f;%.2f;%s",
				descricao,
				precoCusto,
				margemLucro,
				formato.format(dataDeValidade)
		);
	}
}