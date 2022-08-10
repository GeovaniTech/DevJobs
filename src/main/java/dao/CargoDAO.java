package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import dto.CargoTO;
import jpa.JPAUtil;
import model.Cargo;

public class CargoDAO {
	public void salvar(CargoTO cargoTo) {
		Cargo cargo = new Cargo();
		
		cargo.setId(cargoTo.getId());
		cargo.setNome(cargoTo.getNome());
		cargo.setSalario_base(cargoTo.getSalario_base());
		cargo.setDominarLinguagemObrigatorio(cargoTo.isDominarLinguagemObrigatorio());
		
		EntityManager em = JPAUtil.getEntityManager();
		
		em.getTransaction().begin();
		em.persist(cargo);
		em.getTransaction().commit();
	}
	
	public void alterar(Cargo cargo) {
		EntityManager em = JPAUtil.getEntityManager();
		
		em.getTransaction().begin();
		em.merge(cargo);
		em.getTransaction().commit();
	}
	
	public void remover(Cargo cargo) {
		EntityManager em = JPAUtil.getEntityManager();
		
		em.getTransaction().begin();
		em.remove(em.contains(cargo) ? cargo : em.merge(cargo));
		em.getTransaction().commit();
	}

	public List<Cargo> listar() {
		EntityManager em = JPAUtil.getEntityManager();
		
		TypedQuery<Cargo> query = em.createQuery("SELECT C FROM Cargo C", Cargo.class);
		return query.getResultList();
	}
}
