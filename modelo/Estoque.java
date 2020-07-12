package exemplo.modelo;

public class Estoque {
	
	private int id;
	private String nomeProduto;
	
	public Estoque(int id, String nomeProduto) {
		this.id = id;
		this.nomeProduto = nomeProduto;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getNomeProduto() {
		return nomeProduto;
	}
	
	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}
	
	@Override
	public String toString() {
		return "Estoque [ID= " + id + ", Nome Produto" + nomeProduto + "]";
	}
	
}
