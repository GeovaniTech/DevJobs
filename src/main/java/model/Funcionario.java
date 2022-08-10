package model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

@Entity
public class Funcionario implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int id;
	private String nome;
	private Date data_nascimento;
	
	@OneToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "cargo_id")
	private Cargo cargo;
	
	@JoinColumn(name = "linguagens_dominio_id")
	@ManyToMany(targetEntity = Linguagem.class, fetch = FetchType.LAZY)
	private List<Linguagem> linguagens_dominio;
	
	public List<Linguagem> getLinguagens_dominio() {
		return linguagens_dominio;
	}
	public void setLinguagens_dominio(List<Linguagem> linguagens_dominio) {
		this.linguagens_dominio = linguagens_dominio;
	}
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
}
