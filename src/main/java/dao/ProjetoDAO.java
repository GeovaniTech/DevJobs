package dao;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import dto.ProjetoTO;
import jpa.JPAUtil;
import model.Projeto;

public class ProjetoDAO {
	public void salvar(ProjetoTO projetoTo) {
		Projeto projeto = new Projeto();

		projeto.setId(projetoTo.getId());
		projeto.setNome(projetoTo.getNome());
		projeto.setDescricao(projetoTo.getDescricao());
		projeto.setStatus(projetoTo.getStatus());
		projeto.setData_inicio(projetoTo.getData_inicio());
		projeto.setData_final(projetoTo.getData_final());
		projeto.setEnvolvidos(projetoTo.getEnvolvidos());
		projeto.setRequisitos(projetoTo.getRequisitos());

		EntityManager em = JPAUtil.getEntityManager();

		em.getTransaction().begin();
		em.persist(projeto);
		em.getTransaction().commit();
	}

	public void alterar(Projeto projeto) {
		EntityManager em = JPAUtil.getEntityManager();

		em.getTransaction().begin();
		em.merge(projeto);
		em.getTransaction().commit();
	}

	public void remover(Projeto projeto) {
		EntityManager em = JPAUtil.getEntityManager();

		em.getTransaction().begin();
		em.remove(em.contains(projeto) ? projeto : em.merge(projeto));
		em.getTransaction().commit();
	}

	public List<Projeto> listar() {
		EntityManager em = JPAUtil.getEntityManager();

		TypedQuery<Projeto> query = em.createQuery("SELECT P FROM Projeto P", Projeto.class);
		return query.getResultList();
	}

	public boolean funcionarioSemDemanda(int id_funcionario, int id_projeto, Date data_inicio_projeto,
			Date data_final_projeto) {
		String sql = "SELECT P FROM Projeto P Join P.envolvidos AS F "
				+ "WHERE F.id = :funcionario AND P.id != :projeto AND P.status != 'Encerrado' AND"
				+ ":data_inicio <= P.data_inicio AND :data_final >= P.data_inicio OR "
				+ ":data_inicio >= P.data_inicio AND :data_inicio <= P.data_final AND :data_final <= P.data_final AND P.id != :projeto AND P.status != 'Encerrado'";

		EntityManager em = JPAUtil.getEntityManager();

		TypedQuery<Projeto> query = em.createQuery(sql, Projeto.class).setParameter("funcionario", id_funcionario)
				.setParameter("data_final", data_final_projeto).setParameter("data_inicio", data_inicio_projeto)
				.setParameter("projeto", id_projeto);

		return query.getResultList().size() > 0 && query.getResultList() != null;
	}

	// Dashboard
	public BigInteger lead_time() {
		EntityManager em = JPAUtil.getEntityManager();

		String sql = "SELECT SUM(DATE(P.data_final) - DATE(P.data_inicio)) / COUNT(P.id)  FROM Projeto AS P";

		Query query = em.createNativeQuery(sql);

		if (query.getSingleResult() != null) {
			return (BigInteger) query.getSingleResult();
		} else {
			return (BigInteger.ZERO);
		}

	}

	public int desenvolvedores_livres() {
		String sql = "SELECT Funcionario.id from Projeto_funcionario "
				+ "RIGHT JOIN funcionario ON funcionario.id = Projeto_funcionario.envolvidos_id "
				+ "INNER JOIN cargo ON cargo.id = funcionario.cargo_id "
				+ "LEFT JOIN projeto ON projeto.id = Projeto_funcionario.projeto_id "
				+ "WHERE LOWER(cargo.nome) LIKE '%desenvolvedor%' AND Projeto_funcionario.projeto_id IS NULL OR Projeto.status = 'Encerrado' "
				+ "AND NOT EXISTS ( " + "	SELECT * FROM Projeto "
				+ "	INNER JOIN projeto_funcionario ON projeto.id = Projeto_funcionario.projeto_id "
				+ "	WHERE Projeto_funcionario.envolvidos_id = Funcionario.id AND Projeto.status = 'Planejamento' OR Projeto.status = 'Execução' "
				+ ") GROUP BY Funcionario.id";

		EntityManager em = JPAUtil.getEntityManager();

		return em.createNativeQuery(sql).getResultList().size();
	}

	public int projetos_em_desenvolvimento() {
		EntityManager em = JPAUtil.getEntityManager();

		String sql = "SELECT P FROM Projeto P WHERE P.status = 'Execução'";

		return em.createQuery(sql, Projeto.class).getResultList().size();
	}

	public int projetos_entregues() {
		EntityManager em = JPAUtil.getEntityManager();

		String sql = "SELECT P FROM Projeto P WHERE P.status = 'Encerrado'";

		return em.createQuery(sql, Projeto.class).getResultList().size();
	}

	// Filtros
	public List<Projeto> filtrarFuncionario(int id_funcionario) {
		String sql = "SELECT P FROM Projeto P JOIN P.envolvidos AS F " + "WHERE F.id = :funcionario";

		EntityManager em = JPAUtil.getEntityManager();

		return em.createQuery(sql, Projeto.class).setParameter("funcionario", id_funcionario).getResultList();
	}

	public List<Projeto> filtrarRequisito(int id_requisito) {
		String sql = "SELECT P FROM Projeto P JOIN P.requisitos AS PR " + "WHERE PR.id = :requisito";

		EntityManager em = JPAUtil.getEntityManager();

		return em.createQuery(sql, Projeto.class).setParameter("requisito", id_requisito).getResultList();
	}
}
