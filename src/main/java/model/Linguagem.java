package model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import interfaces.BaseEntity;

@Entity
public class Linguagem implements Serializable, BaseEntity {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int id;
	private String nome;
	private double versao;
	private String url_icone;
	
	@Override
	public Long getIdBase() {
		Long id_base = (long) this.getId();
		return id_base;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, nome, url_icone, versao);
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
		Linguagem other = (Linguagem) obj;
		return id == other.id && Objects.equals(nome, other.nome) && Objects.equals(url_icone, other.url_icone)
				&& Double.doubleToLongBits(versao) == Double.doubleToLongBits(other.versao);
	}
	
	//Getters and Setters
	public String getUrl_icone() {
		return url_icone;
	}
	public void setUrl_icone(String url_icone) {
		this.url_icone = url_icone;
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
	public double getVersao() {
		return versao;
	}
	public void setVersao(double versao) {
		this.versao = versao;
	}
}
