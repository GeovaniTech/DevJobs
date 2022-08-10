package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import dto.RequisitoTO;
import jpa.JPAUtil;
import model.Requisito;

public class RequisitoDAO {
	public Requisito salvar(RequisitoTO requisitoTo) {
		Requisito requisito = new Requisito();
		
		requisito.setId(requisitoTo.getId());
		requisito.setNome(requisitoTo.getNome());
		requisito.setRequisitoHabilitado(requisitoTo.isRequisitoHabilitado());
		
		EntityManager em = JPAUtil.getEntityManager();
		
		em.getTransaction().begin();
		em.persist(requisito);
		em.getTransaction().commit();
		
		return requisito;
	}
	
	public void alterar(RequisitoTO requisitoTo) {
		EntityManager em = JPAUtil.getEntityManager();
		
		Requisito requisito = em.find(Requisito.class, requisitoTo.getId());
		
		requisito.setId(requisitoTo.getId());
		requisito.setNome(requisitoTo.getNome());
		requisito.setRequisitoHabilitado(requisitoTo.isRequisitoHabilitado());
		
		em.getTransaction().begin();
		em.merge(requisito);
		em.getTransaction().commit();
	}
	
	public void remover(Requisito requisito) {
		EntityManager em = JPAUtil.getEntityManager();
		
		em.getTransaction().begin();
		em.remove(em.contains(requisito) ? requisito : em.merge(requisito));
		em.getTransaction().commit();
	}
	
	public List<Requisito> listar() {
		EntityManager em = JPAUtil.getEntityManager();
		
		TypedQuery<Requisito> query = em.createQuery("SELECT R FROM Requisito R", Requisito.class);
		return query.getResultList();
	}
	
	public void atualizarChecked(int id, boolean habilitado) {
		EntityManager em = JPAUtil.getEntityManager();
		
		em.getTransaction().begin();
		Requisito requisito = em.find(Requisito.class, id);

		if (!habilitado) {
			requisito.setRequisitoHabilitado(false);
		} else {
			requisito.setRequisitoHabilitado(true);
		}
		
		em.merge(requisito);
		em.getTransaction().commit();
	} 
}
