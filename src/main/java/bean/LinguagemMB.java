package bean;

import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import classes_auxiliares.Menssagens;
import dao.LinguagemDAO;
import dto.LinguagemTO;
import model.Linguagem;

import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class LinguagemMB implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private LinguagemTO linguagemTo = new LinguagemTO();
	private LinguagemDAO linguagemDao = new LinguagemDAO();
	private Linguagem linguagemSelecionada;
	
	private List<Linguagem> linguagens;
	private List<Linguagem> linguagens_filtradas;
	private boolean globalFilterOnly;
	
	Menssagens msg = new Menssagens();
	
	public LinguagemMB() {
		atualizarLinguagens();
	}
	
	public void salvar() {
		if(!this.getLinguagemTo().getNome().equals("")
			&& !this.getLinguagemTo().getUrl_icone().equals("")
			&& this.getLinguagemTo().getVersao() > 0) {
		
			this.getLinguagemDao().salvar(this.getLinguagemTo());
			
			this.setarLinguagemSelecionadaNull();
			atualizarLinguagens();
			msg.cadastroFinalizado();
		} else {
			msg.erroValoresNulos();
		}
	}
	
	public void alterar() {
		if(!this.getLinguagemSelecionada().getNome().equals("")
			&& !this.getLinguagemSelecionada().getUrl_icone().equals("")
			&& this.getLinguagemSelecionada().getVersao() > 0) {
			
			this.getLinguagemDao().alterar(this.getLinguagemSelecionada());
			msg.cadastroAlterado();
		} else {
			msg.erroValoresNulos();
		}
	}
	
	public void remover() {
		try {
			this.getLinguagemDao().remover(this.getLinguagemSelecionada());
			
			this.setLinguagemSelecionada(null);
			atualizarLinguagens();
			msg.removecaoFinalizada();
		} catch (Exception e) {
			msg.criarMensagem(FacesMessage.SEVERITY_ERROR, "Erro ao remover", "Esta linguagem está associada a algum funcionário!");
		}
	}
	
	public void atualizarLinguagens() {
		linguagens = linguagemDao.listar();
	}
	
	public boolean possuiLinguagemSelecionada() {
		return this.getLinguagemSelecionada() != null;
	}
	
	public void setarLinguagemSelecionadaNull() {
		this.setLinguagemSelecionada(null);
	}
	
	//Getters and Setters
	public LinguagemTO getLinguagemTo() {
		return linguagemTo;
	}
	public void setLinguagemTo(LinguagemTO linguagemTo) {
		this.linguagemTo = linguagemTo;
	}
	public LinguagemDAO getLinguagemDao() {
		return linguagemDao;
	}
	public void setLinguagemDao(LinguagemDAO linguagemDao) {
		this.linguagemDao = linguagemDao;
	}
	public Linguagem getLinguagemSelecionada() {
		return linguagemSelecionada;
	}
	public void setLinguagemSelecionada(Linguagem linguagemSelecionada) {
		this.linguagemSelecionada = linguagemSelecionada;
	}
	public List<Linguagem> getLinguagens() {
		return linguagens;
	}
	public void setLinguagens(List<Linguagem> linguagens) {
		this.linguagens = linguagens;
	}
	public List<Linguagem> getLinguagens_filtradas() {
		return linguagens_filtradas;
	}
	public void setLinguagens_filtradas(List<Linguagem> linguagens_filtradas) {
		this.linguagens_filtradas = linguagens_filtradas;
	}
	public boolean isGlobalFilterOnly() {
		return globalFilterOnly;
	}
	public void setGlobalFilterOnly(boolean globalFilterOnly) {
		this.globalFilterOnly = globalFilterOnly;
	}
}
