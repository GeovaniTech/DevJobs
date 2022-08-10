package dto;

import java.util.Date;
import java.util.List;

import model.Cargo;
import model.Linguagem;

public class FuncionarioTO {
	private int id;
	private String nome;
	private Date data_nascimento;
	private Cargo cargo;
	private List<Linguagem> linguagens_dominio;
	
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
	public Date getData_nascimento() {
		return data_nascimento;
	}
	public void setData_nascimento(Date data_nascimento) {
		this.data_nascimento = data_nascimento;
	}
	public Cargo getCargo() {
		return cargo;
	}
	public void setCargo(Cargo cargo) {
		this.cargo = cargo;
	}
	public List<Linguagem> getLinguagens_dominio() {
		return linguagens_dominio;
	}
	public void setLinguagens_dominio(List<Linguagem> linguagens_dominio) {
		this.linguagens_dominio = linguagens_dominio;
	}	
}
