package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import dto.LinguagemTO;
import jpa.JPAUtil;
import model.Linguagem;

public class LinguagemDAO {
	public void salvar(LinguagemTO linguagemTo) {
		EntityManager em = JPAUtil.getEntityManager();
		
		Linguagem linguagem = new Linguagem();
		
		linguagem.setId(linguagemTo.getId());
		linguagem.setNome(linguagemTo.getNome());
		linguagem.setUrl_icone(linguagemTo.getUrl_icone());
		linguagem.setVersao(linguagemTo.getVersao());
		
		em.getTransaction().begin();
		em.persist(linguagem);
		em.getTransaction().commit();
	}
	
	public void alterar(Linguagem linguagem) {
		EntityManager em = JPAUtil.getEntityManager();
		
		em.getTransaction().begin();
		em.merge(linguagem);
		em.getTransaction().commit();
	}
	
	public void remover(Linguagem linguagem) {
		EntityManager em = JPAUtil.getEntityManager();
		
		em.getTransaction().begin();
		em.remove(em.contains(linguagem) ? linguagem : em.merge(linguagem));
		em.getTransaction().commit();
	}
	
	public List<Linguagem> listar() {
		EntityManager em = JPAUtil.getEntityManager();
		
		TypedQuery<Linguagem> query = em.createQuery("SELECT L FROM Linguagem L", Linguagem.class);
		return query.getResultList();
	}
}
