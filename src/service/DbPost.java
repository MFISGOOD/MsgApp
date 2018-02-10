package service;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import model.Bhpost;

public class DbPost {
	public static void insert(Bhpost bhpost) {
		EntityManager em = DbUtil.getEntityManager("msgApp");
		EntityTransaction trans = em.getTransaction();
		try {
			trans.begin();
			em.persist(bhpost);
			trans.commit();
		}catch(Exception e) {
			e.getStackTrace();
			trans.rollback();
		}finally {
			em.close();
		}
	}
	public static void update(Bhpost bhpost) {
		EntityManager em = DbUtil.getEntityManager("msgApp");
		EntityTransaction trans = em.getTransaction();
		try {
			trans.begin();
			em.merge(bhpost);
			trans.commit();
		}catch(Exception e) {
			trans.rollback();
		}finally {
			em.close();
		}
	}
	public static void delete(Bhpost bhpost) {
		EntityManager em = DbUtil.getEntityManager("msgApp");
		EntityTransaction trans = em.getTransaction();
		try {
			trans.begin();
			em.remove(em.merge(bhpost));
		}catch(Exception e) {
			System.out.println(e.getMessage());
			trans.rollback();
		}finally {
			em.close();
		}
	}
	public static List<Bhpost> bhpost(){
		EntityManager em = DbUtil.getEntityManager("msgApp");
		String qString = "Select b from Bhpost b ";
		List<Bhpost> posts = null;
		try {
			TypedQuery<Bhpost> query = em.createQuery(qString,Bhpost.class);
			posts=query.getResultList();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			em.close();
		}
		return posts;
	}
	public static List<Bhpost> postsofUser(long userid){
		EntityManager em = DbUtil.getEntityManager("msgApp");
		List<Bhpost> userposts = null;
		String qString = "Select b from Bhpost b "
				+ "where b.bhuser.bhuserid = :userid";
		try {
			TypedQuery<Bhpost> query = em.createQuery(qString,Bhpost.class);
			query.setParameter("userid", userid);
			userposts =query.getResultList();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			em.close();
		}
		return userposts;
	}
	public static List<Bhpost> postsofUser(String useremail){
		EntityManager em = DbUtil.getEntityManager("msgApp");
		List<Bhpost> userposts = null;
		String qString = "Select b from Bhpost b "
				+ "where b.bhuser.useremail = :useremail";
		try {
			TypedQuery<Bhpost> query = em.createQuery(qString,Bhpost.class);
			query.setParameter("useremail", useremail);
			userposts =query.getResultList();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			em.close();
		}
		return userposts;
	}
	public static List<Bhpost> searchPosts(String search){
		EntityManager em = DbUtil.getEntityManager("msgApp");
		List<Bhpost> searchposts = null;
		String qString = "Select b from Bhpost b "
				+ "where b.posttext like :search";
		try{
			TypedQuery<Bhpost> query = em.createQuery(qString,Bhpost.class);
			query.setParameter("search", "%"+search +"%");
			searchposts = query.getResultList();
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}finally {
			em.close();
		}
		return searchposts;
	}
}
