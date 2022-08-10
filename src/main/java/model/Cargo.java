package model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import interfaces.BaseEntity;

@Entity
public class Cargo implements Serializable, BaseEntity {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int id;
	private String nome;
	private Double salario_base;
	private boolean dominarLinguagemObrigatorio;
	
	@Override
	public Long getIdBase() {
		Long id_base = (long) this.getId();
		return id_base;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(dominarLinguagemObrigatorio, id, nome, salario_base);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Cargo other = (Cargo) obj;
		return dominarLinguagemObrigatorio == other.dominarLinguagemObrigatorio && id == other.id
				&& Objects.equals(nome, other.nome) && Objects.equals(salario_base, other.salario_base);
	}
	
	//Getters and Setters
	public boolean isDominarLinguagemObrigatorio() {
		return dominarLinguagemObrigatorio;
	}
	public void setDominarLinguagemObrigatorio(boolean dominarLinguagemObrigatorio) {
		this.dominarLinguagemObrigatorio = dominarLinguagemObrigatorio;
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
	public Double getSalario_base() {
		return salario_base;
	}
	public void setSalario_base(Double salario_base) {
		this.salario_base = salario_base;
	}
}
