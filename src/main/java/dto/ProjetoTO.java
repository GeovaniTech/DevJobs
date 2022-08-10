package dto;

import java.util.Date;
import java.util.List;

import model.Funcionario;
import model.Requisito;

public class ProjetoTO {
	private int id;
	private String nome;
	private String descricao;
	private Date data_inicio;
	private Date data_final;
	private List<Requisito> requisitos;
	private List<Funcionario> envolvidos;
	private String status;
	
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
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public Date getData_inicio() {
		return data_inicio;
	}
	public void setData_inicio(Date data_inicio) {
		this.data_inicio = data_inicio;
	}
	public Date getData_final() {
		return data_final;
	}
	public void setData_final(Date data_final) {
		this.data_final = data_final;
	}
	public List<Requisito> getRequisitos() {
		return requisitos;
	}
	public void setRequisitos(List<Requisito> requisitos) {
		this.requisitos = requisitos;
	}
	public List<Funcionario> getEnvolvidos() {
		return envolvidos;
	}
	public void setEnvolvidos(List<Funcionario> envolvidos) {
		this.envolvidos = envolvidos;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
