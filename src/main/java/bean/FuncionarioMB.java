package bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import classes_auxiliares.Menssagens;
import dao.FuncionarioDAO;
import dto.FuncionarioTO;
import model.Funcionario;
import model.Linguagem;

@Named
@ViewScoped
public class FuncionarioMB implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private FuncionarioTO funcionarioTo = new FuncionarioTO();
	private FuncionarioDAO funcionarioDao = new FuncionarioDAO();
	private Funcionario funcionarioSelecionado = new Funcionario();
	
	private ArrayList<Integer> linguagens_id = new ArrayList<Integer>();
	private ArrayList<Linguagem> linguagens_dominio = new ArrayList<Linguagem>();
	private List<Funcionario> funcionarios;
	private List<Funcionario> funcionarios_filtrados;
	
	private boolean globalFilterOnly;
	private int linguagemFiltrada;
	
	Menssagens msg = new Menssagens();
	
	public FuncionarioMB() {
		atualizarFuncionarios();
	}
	
	public void salvar() {
		if(this.getFuncionarioTo().getCargo() != null
			&& this.getFuncionarioTo().getData_nascimento() != null
			&& this.getFuncionarioTo().getLinguagens_dominio() != null
			&& !this.getFuncionarioTo().getNome().equals("")) {
			
			this.getFuncionarioDao().salvar(this.getFuncionarioTo());
			this.setFuncionarioTo(new FuncionarioTO());
			atualizarFuncionarios();
			msg.cadastroFinalizado();
		} else {
			msg.erroValoresNulos();
		}
	}

	public void alterar() {
		if (this.getFuncionarioSelecionado().getCargo() != null
				&& this.getFuncionarioSelecionado().getData_nascimento() != null
				&& this.getFuncionarioSelecionado().getLinguagens_dominio() != null
				&& !this.getFuncionarioSelecionado().getNome().equals("")) {
			
			if(this.getFuncionarioSelecionado().getCargo().isDominarLinguagemObrigatorio() && this.getLinguagens_id().isEmpty()) {
				msg.criarMensagem(FacesMessage.SEVERITY_ERROR, "Erro ao Cadastrar",
						"O cargo " + this.getFuncionarioSelecionado().getCargo().getNome() + " Requer linguagem de domínio");
			} else {
				this.getFuncionarioDao().alterar(this.getFuncionarioSelecionado(), this.getLinguagens_id());
				msg.cadastroAlterado();
			}
		}
	}
	
	public void remover() {
		try {
			this.getFuncionarioDao().remover(this.getFuncionarioSelecionado());
			
			this.setFuncionarioSelecionado(null);
			atualizarFuncionarios();
			msg.removecaoFinalizada();
		} catch (Exception e) {
			msg.criarMensagem(FacesMessage.SEVERITY_ERROR, "Erro ao remover", "Este funcionário está associado a algum projeto!");
		}
	}
	
	public void atualizarFuncionarios() {
		funcionarios = funcionarioDao.listar();
	}
	
	public boolean possuiFuncionarioSelecionado() {
		if(this.getFuncionarioSelecionado() != null
				&& this.getFuncionarioSelecionado().getCargo() != null) {
			
			linguagensId();
		}
		
		return this.getFuncionarioSelecionado() != null
				&& this.getFuncionarioSelecionado().getCargo() != null;
	}
	
	public void setarFuncionarioSelecionadoNull() {
		if(this.getFuncionarioSelecionado() != null) {
			this.setFuncionarioSelecionado(null);
		}
	}
	
	public void linguagensId() {
		linguagens_id = new ArrayList<Integer>();
		
		for(Linguagem linguagem : this.getFuncionarioSelecionado().getLinguagens_dominio()) {
			linguagens_id.add(linguagem.getId());
		}
	}
	
	public void filtrarLinguagem() {
		if(this.getLinguagemFiltrada() != 0) {
			List<Funcionario> funcionarios = this.getFuncionarioDao().filtrarLinguagem(this.getLinguagemFiltrada());
			
			if(funcionarios != null && !funcionarios.isEmpty()) {
				this.setFuncionarios(funcionarios);
			} else {
				atualizarFuncionarios();
				msg.nadaEncontrado();
			}
		} else {
			atualizarFuncionarios();
		}
	}
	
	//Getters and Setters
	public FuncionarioTO getFuncionarioTo() {
		return funcionarioTo;
	}
	public void setFuncionarioTo(FuncionarioTO funcionarioTo) {
		this.funcionarioTo = funcionarioTo;
	}
	public FuncionarioDAO getFuncionarioDao() {
		return funcionarioDao;
	}
	public void setFuncionarioDao(FuncionarioDAO funcionarioDao) {
		this.funcionarioDao = funcionarioDao;
	}
	public Funcionario getFuncionarioSelecionado() {
		return funcionarioSelecionado;
	}
	public void setFuncionarioSelecionado(Funcionario funcionarioSelecionado) {
		this.funcionarioSelecionado = funcionarioSelecionado;
	}
	public List<Funcionario> getFuncionarios() {
		return funcionarios;
	}
	public void setFuncionarios(List<Funcionario> funcionarios) {
		this.funcionarios = funcionarios;
	}
	public List<Funcionario> getFuncionarios_filtrados() {
		return funcionarios_filtrados;
	}
	public void setFuncionarios_filtrados(List<Funcionario> funcionarios_filtrados) {
		this.funcionarios_filtrados = funcionarios_filtrados;
	}
	public boolean isGlobalFilterOnly() {
		return globalFilterOnly;
	}
	public void setGlobalFilterOnly(boolean globalFilterOnly) {
		this.globalFilterOnly = globalFilterOnly;
	}
	public int getLinguagemFiltrada() {
		return linguagemFiltrada;	
	}
	public void setLinguagemFiltrada(int linguagemFiltrada) {
		this.linguagemFiltrada = linguagemFiltrada;
	}
	public ArrayList<Integer> getLinguagens_id() {
		return linguagens_id;
	}
	public void setLinguagens_id(ArrayList<Integer> linguagens_id) {
		this.linguagens_id = linguagens_id;
	}
	public ArrayList<Linguagem> getLinguagens_dominio() {
		return linguagens_dominio;		
	}
	public void setLinguagens_dominio(ArrayList<Linguagem> linguagens_dominio) {
		this.linguagens_dominio = linguagens_dominio;	
	}
}
