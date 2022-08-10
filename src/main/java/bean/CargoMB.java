package bean;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import classes_auxiliares.Menssagens;
import dao.CargoDAO;
import dto.CargoTO;
import model.Cargo;
import java.io.Serializable;

@Named
@ViewScoped
public class CargoMB implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private CargoTO cargoTo = new CargoTO();
	private CargoDAO cargoDao = new CargoDAO();
	private Cargo cargoSelecionado;
	
	private List<Cargo> cargos;
	private List<Cargo> cargos_filtrados;
	private boolean globalFilterOnly;
	
	Menssagens msg = new Menssagens();
	
	public CargoMB() {
		atualizarCargos();
		this.setGlobalFilterOnly(false);
	}
	
	public void salvarCargo() {
		if(!this.getCargoTo().getNome().equals("")
			&& this.getCargoTo().getSalario_base() > 0) {
			cargoDao.salvar(this.getCargoTo());
			
			this.setarCargoSelecionadoNull();
			atualizarCargos();
			msg.cadastroFinalizado();
		} else {
			msg.erroValoresNulos();
		}
	}
	
	public void alterarCargo() {
		if(!this.getCargoSelecionado().getNome().equals("")
			&& this.getCargoSelecionado().getSalario_base() > 0) {
			cargoDao.alterar(this.getCargoSelecionado());
			
			msg.cadastroAlterado();
		} else {
			msg.erroValoresNulos();
		}
	}
	
	public void removerCargo() {
		try {
			cargoDao.remover(this.getCargoSelecionado());
			
			this.setarCargoSelecionadoNull();
			atualizarCargos();
			msg.removecaoFinalizada();
		} catch (Exception e) {
			msg.criarMensagem(FacesMessage.SEVERITY_ERROR, "Erro ao remover", "Este cargo está associado a algum funcionário");
		}
	}
	
	public void atualizarCargos() {
		cargos = cargoDao.listar();
	}
	
	public boolean possuiCargoSelecionado() {
		return this.getCargoSelecionado() != null;
	}
	
	public void setarCargoSelecionadoNull() {
		this.setCargoSelecionado(null);;
	}
	
	//Getters and Setters
	public CargoDAO getCargoDao() {
		return cargoDao;
	}
	public void setCargoDao(CargoDAO cargoDao) {
		this.cargoDao = cargoDao;
	}
	public List<Cargo> getCargos() {
		return cargos;
	}
	public void setCargos(List<Cargo> cargos) {
		this.cargos = cargos;
	}
	public Cargo getCargoSelecionado() {
		return cargoSelecionado;
	}
	public void setCargoSelecionado(Cargo cargoSelecionado) {
		this.cargoSelecionado = cargoSelecionado;
	}
	public List<Cargo> getCargos_filtrados() {
		return cargos_filtrados;
	}
	public void setCargos_filtrados(List<Cargo> cargos_filtrados) {
		this.cargos_filtrados = cargos_filtrados;	
	}
	public boolean isGlobalFilterOnly() {
		return globalFilterOnly;
	}
	public void setGlobalFilterOnly(boolean globalFilterOnly) {
		this.globalFilterOnly = globalFilterOnly;	
	}

	public CargoTO getCargoTo() {
		return cargoTo;
		
	}

	public void setCargoTo(CargoTO cargoTo) {
		this.cargoTo = cargoTo;
		
	}
}
