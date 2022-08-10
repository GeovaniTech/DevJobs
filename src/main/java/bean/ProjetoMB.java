package bean;

import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import classes_auxiliares.Menssagens;
import dao.FuncionarioDAO;
import dao.ProjetoDAO;
import dao.RequisitoDAO;
import dto.ProjetoTO;
import dto.RequisitoTO;
import model.Projeto;
import model.Requisito;
import model.Funcionario;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Named
@ViewScoped
public class ProjetoMB implements Serializable {
	private static final long serialVersionUID = 1L;
	
	// Atributos Projeto
	private ProjetoTO projetoTo = new ProjetoTO();
	private ProjetoDAO projetoDao = new ProjetoDAO();
	private Projeto projetoSelecionado = new Projeto();
	
	// Atributos Requisito
	private RequisitoTO requisitoTo = new RequisitoTO();
	private RequisitoTO novoRequisitoAlterarTo = new RequisitoTO();
	private RequisitoTO requisitoAlterarTo = new RequisitoTO();
	private RequisitoDAO requisitoDao = new RequisitoDAO();

	//Lógica
	private List<Projeto> projetos;
	private List<Projeto> projetos_filtrados;
	private List<Requisito> requisitos;
	private List<Requisito> requisitos_filtrados;
	private List<Integer> envolvidos_id = new ArrayList<Integer>();
	private FuncionarioDAO funcionarioDao = new FuncionarioDAO();
	private boolean todosRequisitosFinalizados;
	private boolean todosEnvolvidosSemDemanda;
	private Date data_atual = new Date();
	
	//Filtro
	private boolean globalFilterOnly;
	private int funcionarioFiltrado;
	private int requisitoFiltrado;
	
	// Dashboard
	private BigInteger lead_time_medio;
	private int desenvolvedoresLivres;
	private int projetos_em_desenvolvimento;
	private int projetos_entregues;

	Menssagens msg = new Menssagens();

	public ProjetoMB() {
		atualizarProjetos();
		atualizarRequisitos();
		atualizarDashboard();
		
		this.getProjetoTo().setRequisitos(new ArrayList<Requisito>());
	}

	public void salvar() {
		if (!this.getProjetoTo().getNome().equals("") && !this.getProjetoTo().getDescricao().equals("")
				&& this.getProjetoTo().getData_final() != null && this.getProjetoTo().getData_inicio() != null
				&& !this.getProjetoTo().getEnvolvidos().isEmpty() && !this.getProjetoTo().getRequisitos().isEmpty()) {

			this.setTodosRequisitosFinalizados(true);

			for (Requisito requisito : this.getProjetoTo().getRequisitos()) {
				if (!requisito.isRequisitoHabilitado()) {
					this.setTodosRequisitosFinalizados(false);
					break;
				}
			}

			this.setTodosEnvolvidosSemDemanda(true);

			for (Funcionario funcionario : this.getProjetoTo().getEnvolvidos()) {
				boolean envolvido = this.getProjetoDao().funcionarioSemDemanda(funcionario.getId(), 0, 
						this.getProjetoTo().getData_inicio(), this.getProjetoTo().getData_final());
				
				if (envolvido) {
					this.setTodosEnvolvidosSemDemanda(false);
					msg.criarMensagem(FacesMessage.SEVERITY_ERROR, "Erro ao Adicionar Projeto", "O funcionário "
							+ funcionario.getNome() + " está associado a outro projeto nesse mesmo período.");
				}
			}
			
			if (this.isTodosEnvolvidosSemDemanda()) {
				if (this.isTodosRequisitosFinalizados()) {
					this.getProjetoTo().setStatus("Execução");
				} else {
					this.getProjetoTo().setStatus("Planejamento");
				}
				
				this.getProjetoDao().salvar(this.getProjetoTo());
				this.setProjetoTo(new ProjetoTO());
				this.getProjetoTo().setRequisitos(new ArrayList<Requisito>());
				
				atualizarDashboard();
				atualizarProjetos();
				msg.cadastroFinalizado();
			}
		} else {
			msg.erroValoresNulos();
		}
	}

	public void alterar(boolean finalizar) {
		if (!this.getProjetoSelecionado().getNome().equals("") 
				&& !this.getProjetoSelecionado().getDescricao().equals("")
				&& this.getProjetoSelecionado().getData_final() != null
				&& this.getProjetoSelecionado().getData_inicio() != null
				&& !this.getProjetoSelecionado().getEnvolvidos().isEmpty()
				&& !this.getProjetoSelecionado().getRequisitos().isEmpty()) {

			envolvidosObjeto();

			this.setTodosRequisitosFinalizados(true);

			for (Requisito requisito : this.getProjetoSelecionado().getRequisitos()) {
				if (!requisito.isRequisitoHabilitado()) {
					this.setTodosRequisitosFinalizados(false);
					break;
				}
			}

			this.setTodosEnvolvidosSemDemanda(true);

			for (Funcionario funcionario : this.getProjetoSelecionado().getEnvolvidos()) {
				boolean envolvido = this.getProjetoDao().funcionarioSemDemanda(funcionario.getId(), this.getProjetoSelecionado().getId(),
				this.getProjetoSelecionado().getData_inicio(), this.getProjetoSelecionado().getData_final());
				
				if(envolvido) {
					this.setTodosEnvolvidosSemDemanda(false);
					msg.criarMensagem(FacesMessage.SEVERITY_ERROR, "Erro ao Alterar Projeto", "O funcionário "
							+ funcionario.getNome() + " está associado a outro projeto nesse mesmo período.");
				}
			}
			
			if(this.isTodosEnvolvidosSemDemanda()) {
				if(finalizar) {
					if(this.isTodosRequisitosFinalizados()
						&& this.getProjetoSelecionado().getData_final().getTime() > this.getData_atual().getTime() && finalizar) {
						this.getProjetoSelecionado().setStatus("Encerrado");
						msg.criarMensagem(FacesMessage.SEVERITY_INFO, "Projeto Finalizado", "Projeto finalizado com sucesso!");
					} else {
						msg.criarMensagem(FacesMessage.SEVERITY_ERROR, "Impossível Finalizar", "O projeto está inapto para ser finalizado");
					}
				} else if (this.isTodosRequisitosFinalizados()
						&& this.getProjetoSelecionado().getData_final().getTime() > this.getData_atual().getTime()
						&& this.getProjetoSelecionado().getData_inicio().getTime() < this.getData_atual().getTime()) {
					this.getProjetoSelecionado().setStatus("Execução");
				} else {
					this.getProjetoSelecionado().setStatus("Planejamento");
				}
				
				this.getProjetoDao().alterar(this.getProjetoSelecionado());
				
				if(!finalizar) {
					msg.cadastroAlterado();
				}
				
				atualizarDashboard();
				atualizarProjetos();
			}

		} else {
			msg.erroValoresNulos();
		}
	}

	public void remover() {
		this.getProjetoDao().remover(this.getProjetoSelecionado());

		setarProjetoSelecionadoNull();
		atualizarDashboard();
		atualizarProjetos();
		msg.removecaoFinalizada();
	}
	
	public void atualizarProjetos() {
		this.setProjetos(this.getProjetoDao().listar());
	}

	public void atualizarDashboard() {
		this.setDesenvolvedoresLivres(this.getProjetoDao().desenvolvedores_livres());
		this.setLead_time_medio(this.getProjetoDao().lead_time());
		this.setProjetos_em_desenvolvimento(this.getProjetoDao().projetos_em_desenvolvimento());
		this.setProjetos_entregues(this.getProjetoDao().projetos_entregues());
	}
	
	// Requisitos
	public void salvarRequisito() {
		if (!this.getRequisitoTo().getNome().equals("")) {
			Requisito requisito = this.getRequisitoDao().salvar(this.getRequisitoTo());

			this.getProjetoTo().getRequisitos().add(requisito);
			
			this.setRequisitoTo(new RequisitoTO());			
			atualizarRequisitos();
			msg.cadastroFinalizado();
		} else {
			msg.erroValoresNulos();
		}
	}

	public void salvarRequisitoAlterar() {
		if (!this.getNovoRequisitoAlterarTo().getNome().equals("")) {
			Requisito requisito = this.getRequisitoDao().salvar(this.getNovoRequisitoAlterarTo());
			
			this.setNovoRequisitoAlterarTo(new RequisitoTO());
			this.getProjetoSelecionado().getRequisitos().add(requisito);
			msg.cadastroFinalizado();
		}
	}

	public void alterarRequisito() {
		if (!this.getRequisitoAlterarTo().getNome().equals("")) {

			this.getRequisitoDao().alterar(this.getRequisitoAlterarTo());
			msg.cadastroAlterado();
		}
	}

	public void removerRequisito(Requisito requisito) {
		for (int i = 0; i < this.getProjetoTo().getRequisitos().size(); i++) {
			if (this.getProjetoTo().getRequisitos().get(i).getId() == requisito.getId()) {
				this.getProjetoTo().getRequisitos().remove(this.getProjetoTo().getRequisitos().get(i));
				break;
			}
		}

		this.getRequisitoDao().remover(requisito);
		atualizarRequisitos();
		msg.removecaoFinalizada();
	}

	public void removerRequisitoAlterar(Requisito requisito) {
		for (int i = 0; i < this.getProjetoSelecionado().getRequisitos().size(); i++) {
			if (this.getProjetoSelecionado().getRequisitos().get(i).getId() == requisito.getId()) {
				this.getProjetoSelecionado().getRequisitos().remove(i);
				break;
			}

			this.getRequisitoDao().remover(requisito);
			atualizarRequisitos();
			msg.removecaoFinalizada();
		}
	}

	public void atualizarCheckedRequisito(int id, boolean habilitado) {
		this.getRequisitoDao().atualizarChecked(id, habilitado);
	}

	public void atualizarRequisitos() {
		this.setRequisitos(this.getRequisitoDao().listar());
	}

	//Filtros
	public void filtrarFuncionario() {
		if(this.getFuncionarioFiltrado() != 0) {
			List<Projeto> projetos_filtrados = this.getProjetoDao().filtrarFuncionario(this.getFuncionarioFiltrado());
			if(projetos_filtrados != null && !projetos_filtrados.isEmpty()) {
				this.setProjetos(projetos_filtrados);
			} else {
				msg.criarMensagem(FacesMessage.SEVERITY_ERROR, "Nada Encontrado", "Nenhum Projeto Encontrado");
				atualizarProjetos();
			}
		} else {
			atualizarProjetos();
		}
		
		
	}
	
	public void filtrarRequisito() {
		if(this.getRequisitoFiltrado() != 0) {
			List<Projeto> projetos_filtrados = this.getProjetoDao().filtrarRequisito(this.getRequisitoFiltrado());
			if(projetos_filtrados != null && !projetos_filtrados.isEmpty()) {
				this.setProjetos(projetos_filtrados);
			} else {
				atualizarProjetos();
				msg.criarMensagem(FacesMessage.SEVERITY_ERROR, "Nada Encontrado", "Nenhum Projeto Encontrado");
			}
		} else {
			atualizarProjetos();
		}
	}

	// Funções Auxiliares
	public boolean possuiProjetoSelecionado() {
		if (this.getProjetoSelecionado() != null && this.getProjetoSelecionado().getNome() != null) {
			envolvidosId();
		}
		return this.getProjetoSelecionado() != null && this.getProjetoSelecionado().getNome() != null;
	}

	public void setarProjetoSelecionadoNull() {
		this.setProjetoSelecionado(null);
	}
	
	public void setarRequisitoAlterar(Requisito requisito) {
		this.setRequisitoAlterarTo(new RequisitoTO());
		
		this.getRequisitoAlterarTo().setId(requisito.getId());
		this.getRequisitoAlterarTo().setNome(requisito.getNome());
		this.getRequisitoAlterarTo().setRequisitoHabilitado(requisito.isRequisitoHabilitado());
	}
	
	public void envolvidosId() {
		this.setEnvolvidos_id(new ArrayList<Integer>());

		for (Funcionario funcionario : this.getProjetoSelecionado().getEnvolvidos()) {
			this.getEnvolvidos_id().add(funcionario.getId());
		}
	}

	public void envolvidosObjeto() {
		this.getProjetoSelecionado().setEnvolvidos(new ArrayList<Funcionario>());

		for (int i = 0; i < this.getEnvolvidos_id().size(); i++) {
			Funcionario funcionario = funcionarioDao.pesquisarFuncionario(this.getEnvolvidos_id().get(i));
			this.getProjetoSelecionado().getEnvolvidos().add(funcionario);
		}
	}
	
	// Getters and Setters
	public ProjetoTO getProjetoTo() {
		return projetoTo;
	}
	public BigInteger getLead_time_medio() {
		return lead_time_medio;
	}
	public void setLead_time_medio(BigInteger lead_time_medio) {
		this.lead_time_medio = lead_time_medio;
	}
	public int getDesenvolvedoresLivres() {
		return desenvolvedoresLivres;
	}
	public void setDesenvolvedoresLivres(int desenvolvedoresLivres) {
		this.desenvolvedoresLivres = desenvolvedoresLivres;
	}
	public int getProjetos_em_desenvolvimento() {
		return projetos_em_desenvolvimento;
	}
	public void setProjetos_em_desenvolvimento(int projetos_em_desenvolvimento) {
		this.projetos_em_desenvolvimento = projetos_em_desenvolvimento;
	}
	public int getProjetos_entregues() {
		return projetos_entregues;
	}
	public void setProjetos_entregues(int projetos_entregues) {
		this.projetos_entregues = projetos_entregues;
	}
	public void setEnvolvidos_id(List<Integer> envolvidos_id) {
		this.envolvidos_id = envolvidos_id;
	}
	public RequisitoTO getNovoRequisitoAlterarTo() {
		return novoRequisitoAlterarTo;
	}
	public void setNovoRequisitoAlterarTo(RequisitoTO novoRequisitoAlterarTo) {
		this.novoRequisitoAlterarTo = novoRequisitoAlterarTo;
	}
	public void setProjetoTo(ProjetoTO projetoTo) {
		this.projetoTo = projetoTo;
	}
	public ProjetoDAO getProjetoDao() {
		return projetoDao;
	}
	public void setProjetoDao(ProjetoDAO projetoDao) {
		this.projetoDao = projetoDao;
	}
	public Projeto getProjetoSelecionado() {
		return projetoSelecionado;
	}
	public void setProjetoSelecionado(Projeto projetoSelecionado) {
		this.projetoSelecionado = projetoSelecionado;
	}
	public List<Projeto> getProjetos() {
		return projetos;
	}
	public void setProjetos(List<Projeto> projetos) {
		this.projetos = projetos;
	}
	public List<Projeto> getProjetos_filtrados() {
		return projetos_filtrados;
	}
	public void setProjetos_filtrados(List<Projeto> projetos_filtrados) {
		this.projetos_filtrados = projetos_filtrados;
	}
	public boolean isGlobalFilterOnly() {
		return globalFilterOnly;
	}
	public void setGlobalFilterOnly(boolean globalFilterOnly) {
		this.globalFilterOnly = globalFilterOnly;
	}
	public List<Integer> getEnvolvidos_id() {
		return envolvidos_id;
	}
	public void setEnvolvidos_id(ArrayList<Integer> envolvidos_id) {
		this.envolvidos_id = envolvidos_id;
	}
	public boolean isTodosRequisitosFinalizados() {
		return todosRequisitosFinalizados;
	}
	public void setTodosRequisitosFinalizados(boolean todosRequisitosFinalizados) {
		this.todosRequisitosFinalizados = todosRequisitosFinalizados;
	}
	public boolean isTodosEnvolvidosSemDemanda() {
		return todosEnvolvidosSemDemanda;
	}
	public void setTodosEnvolvidosSemDemanda(boolean todosEnvolvidosSemDemanda) {
		this.todosEnvolvidosSemDemanda = todosEnvolvidosSemDemanda;
	}
	public RequisitoDAO getRequisitoDao() {
		return requisitoDao;
	}
	public void setRequisitoDao(RequisitoDAO requisitoDao) {
		this.requisitoDao = requisitoDao;
	}
	public RequisitoTO getRequisitoTo() {
		return requisitoTo;
	}
	public void setRequisitoTo(RequisitoTO requisitoTo) {
		this.requisitoTo = requisitoTo;
	}
	public List<Requisito> getRequisitos() {
		return requisitos;
	}
	public void setRequisitos(List<Requisito> requisitos) {
		this.requisitos = requisitos;
	}
	public List<Requisito> getRequisitos_filtrados() {
		return requisitos_filtrados;
	}
	public void setRequisitos_filtrados(List<Requisito> requisitos_filtrados) {
		this.requisitos_filtrados = requisitos_filtrados;
	}
	public RequisitoTO getRequisitoAlterarTo() {
		return requisitoAlterarTo;
	}
	public void setRequisitoAlterarTo(RequisitoTO requisitoAlterarTo) {
		this.requisitoAlterarTo = requisitoAlterarTo;
	}
	public Date getData_atual() {
		return data_atual;
	}
	public void setData_atual(Date data_atual) {
		this.data_atual = data_atual;
	}
	public FuncionarioDAO getFuncionarioDao() {
		return funcionarioDao;
	}
	public void setFuncionarioDao(FuncionarioDAO funcionarioDao) {
		this.funcionarioDao = funcionarioDao;
	}
	public int getFuncionarioFiltrado() {
		return funcionarioFiltrado;
	}
	public void setFuncionarioFiltrado(int funcionarioFiltrado) {
		this.funcionarioFiltrado = funcionarioFiltrado;	
	}
	public int getRequisitoFiltrado() {
		return requisitoFiltrado;	
	}
	public void setRequisitoFiltrado(int requisitoFiltrado) {
		this.requisitoFiltrado = requisitoFiltrado;
	}
}
