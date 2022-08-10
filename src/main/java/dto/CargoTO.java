package dto;

public class CargoTO {
	private int id;
	private String nome;
	private Double salario_base;
	private boolean dominarLinguagemObrigatorio;
	
	//Getters and Setters
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public Double getSalario_base() {
		return salario_base;
	}
	public void setSalario_base(Double salario_base) {
		this.salario_base = salario_base;
	}
	public boolean isDominarLinguagemObrigatorio() {
		return dominarLinguagemObrigatorio;
	}
	public void setDominarLinguagemObrigatorio(boolean dominarLinguagemObrigatorio) {
		this.dominarLinguagemObrigatorio = dominarLinguagemObrigatorio;
	}
}
