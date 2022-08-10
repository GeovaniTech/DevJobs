package dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;


import dto.FuncionarioTO;
import jpa.JPAUtil;
import model.Cargo;
import model.Funcionario;
import model.Linguagem;

public class FuncionarioDAO {
	public void salvar(FuncionarioTO funcionarioTo) {
		Funcionario funcionario = new Funcionario();
		
		funcionario.setId(funcionarioTo.getId());
		funcionario.setNome(funcionarioTo.getNome());
		funcionario.setData_nascimento(funcionarioTo.getData_nascimento());
		
		EntityManager em = JPAUtil.getEntityManager();
		
		em.getTransaction().begin();
		Cargo cargo = em.find(Cargo.class, funcionarioTo.getCargo().getId());
		em.getTransaction().commit();
		
		funcionario.setCargo(cargo);
		funcionario.setLinguagens_dominio(funcionarioTo.getLinguagens_dominio());
		
		em.getTransaction().begin();
		em.persist(funcionario);
		em.getTransaction().commit();
	}
	
	
	public void alterar(Funcionario funcionario, ArrayList<Integer> linguagens_id) {
		EntityManager em = JPAUtil.getEntityManager();
		
		funcionario.getLinguagens_dominio().clear();
		
		for(int i = 0; i < linguagens_id.size(); i++) {
			Linguagem linguagem = em.find(Linguagem.class, linguagens_id.get(i));
			
			funcionario.getLinguagens_dominio().add(linguagem);
		}
		
		em.getTransaction().begin();
		em.merge(funcionario);
		em.getTransaction().commit();
	}
	
	public void remover(Funcionario funcionario) {
		EntityManager em = JPAUtil.getEntityManager();
		
		em.getTransaction().begin();
		em.remove(em.contains(funcionario) ? funcionario : em.merge(funcionario));
		em.getTransaction().commit();
	}
	
	public Funcionario pesquisarFuncionario(int id) {
		EntityManager em = JPAUtil.getEntityManager();
		
		em.getTransaction().begin();
		Funcionario funcionario = em.find(Funcionario.class, id);
		em.getTransaction().commit();
		
		return funcionario;
	}
	
	public List<Funcionario> listar() {
		EntityManager em = JPAUtil.getEntityManager();
		
		TypedQuery<Funcionario> query = em.createQuery("SELECT F FROM Funcionario F", Funcionario.class);
		return query.getResultList();
	}
	
	public List<Funcionario> filtrarLinguagem(int id_linguagem) {
		EntityManager em = JPAUtil.getEntityManager();
		
		String sql = "SELECT F FROM Funcionario F JOIN F.linguagens_dominio AS L "
				+ "WHERE L.id = :linguagem";
	
		return em.createQuery(sql, Funcionario.class)
				.setParameter("linguagem", id_linguagem).getResultList();
	}
}