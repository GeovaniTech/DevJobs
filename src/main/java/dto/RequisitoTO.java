package dto;

public class RequisitoTO {
	private int id;
	private String nome;
	private boolean requisitoHabilitado;
	
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
	public boolean isRequisitoHabilitado() {
		return requisitoHabilitado;
	}
	public void setRequisitoHabilitado(boolean requisitoHabilitado) {
		this.requisitoHabilitado = requisitoHabilitado;
	}
}
