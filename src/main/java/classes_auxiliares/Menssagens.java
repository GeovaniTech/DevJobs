package classes_auxiliares;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class Menssagens {
	public void criarMensagem(FacesMessage.Severity severity, String titulo, String descricao) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, titulo, descricao));
	}
	
	public void erroValoresNulos() {
		criarMensagem(FacesMessage.SEVERITY_ERROR, "Cadastro Incompleto", "Há valores nulos, preencha-os.");
	}
	
	public void cadastroFinalizado() {
		criarMensagem(FacesMessage.SEVERITY_INFO, "Cadastro Finalizado", "Cadastro finalizado com sucesso");
	}
	
	public void cadastroAlterado() {
		criarMensagem(FacesMessage.SEVERITY_INFO, "Cadastro Alterado", "Cadastro alterado com sucesso");
	}
	
	public void removecaoFinalizada() {
		criarMensagem(FacesMessage.SEVERITY_INFO, "Remoção concluída", "Remoção concluída com sucesso");
	}
	
	public void nadaEncontrado() {
		criarMensagem(FacesMessage.SEVERITY_ERROR, "Nada Encontrado", "Nenhum resultado encontrado");
	}
}
