package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import entidades.Funcionario;
import util.JPAUtil;

public class FuncionarioDAO {

    /**
     * Metodo para salvar um dep no banco de dados
     */
    public static void save(Funcionario dep) {
        EntityManager em = JPAUtil.createEntityManager();
        em.getTransaction().begin();
        em.persist(dep);
        em.getTransaction().commit();
        em.close();
    }

    /**
     * Metodo para atualizar um dep no banco de dados
     * 
     * @param dep
     * @return
     */
    public static Funcionario update(Funcionario dep) {
        EntityManager em = JPAUtil.createEntityManager();
        Funcionario depAtualizado = dep;
        em.getTransaction().begin();
        depAtualizado = em.merge(dep);
        em.getTransaction().commit();
        em.close();
        return depAtualizado;
    }

    /**
     * Metodo para deletar um dep no banco de dados
     *
     * @param dep
     */
    public static void delete(Funcionario dep) {
        EntityManager em = JPAUtil.createEntityManager();
        em.getTransaction().begin();
        dep = em.find(Funcionario.class, dep.getId());
        em.remove(dep);
        em.getTransaction().commit();
        em.close();

    }

    /**
     * Metodo para buscar um dep no banco de dados
     *
     * @param id
     * @return
     */
    public static Funcionario getById(Integer id) {
        EntityManager em = JPAUtil.createEntityManager();
        Funcionario dep = em.find(Funcionario.class, id);
        em.close();
        return dep;
    }

    /**
     * Metodo para buscar todos os deps no banco de dados
     *
     * @return
     */
    public static List<Funcionario> getAll() {
        EntityManager em = JPAUtil.createEntityManager();
        Query consultaBuscaTodos = em.createQuery("SELECT a FROM Funcionario a");
        @SuppressWarnings("unchecked")
        List<Funcionario> resultadoDaBuscaDeTodos = consultaBuscaTodos.getResultList();
        return resultadoDaBuscaDeTodos;
    }
    
    
    public static Integer getMax() {
    	 EntityManager em = JPAUtil.createEntityManager();
         Query consultaBuscaTodos = em.createNamedQuery("maxNumeroSorteado");
         Integer resultadoMax = (Integer) consultaBuscaTodos.getSingleResult();
         return resultadoMax;
    }

}
