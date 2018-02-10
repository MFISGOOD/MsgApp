package service;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import service.util.MD5Util;
import model.Bhuser;
/**
 * 
 * @author mohamed
 * DbUser class contains helper for working with Bhusers 
 */
public class DbUser {
	/**
	 * Gets a Bhuser from the database
	 * @param userid - primary key from database. Must be type long.
	 * @return
	 */
	public static Bhuser getUser(long userid) {
		EntityManager em = DbUtil.getEntityManager("msgApp");
	    Bhuser user = em.find(Bhuser.class, (int)userid);
	    return user;
	}
	/**
	 * Inserts a Bhuser in the database
	 * @param bhUser
	 */
	public static void insert(Bhuser bhUser) {
		EntityManager em = DbUtil.getEntityManager("msgApp");
		EntityTransaction trans = em.getTransaction();
		try {
			trans.begin();
			em.persist(bhUser);
			trans.commit();
		}catch(Exception e) {
			e.printStackTrace();
			trans.rollback();
		}finally {
			em.close();
		}
	}
	/**
	 * Gets a Gravatar URL given the email and size
	 * In accordance with Gravatar's requirements the email
	   will be hashed
	 * with the MD5 hash and returned as part of the url
	 * The url will also include the s=xx attribute to
	   request a Gravatar of a
	 * particular size.
	 * References: <a href="http://www.gravatar.com">http://www.gravatar.com</>
	 * @param email - email of the user who's gravataryou want
     * @param size - indicates pixel height of the image to be returned. Height and Width are same.
     * @return - the gravatar URL. You can test it in abrowser.
     */
	public static String getGravatarURL(String email,Integer size){
        StringBuilder url = new StringBuilder();
        url.append("http://www.gravatar.com/avatar/");
        url.append(MD5Util.md5Hex(email));
        url.append("?s=" + size.toString());
        return url.toString();
	} 
	/**
	 * Updates the data in a Bhuser
	 * Pass the method a Bhuser with all the values set to
	   your liking and
	 * this method will update the database with these
	   values.
	 * This method doesn't actually return anything but the
	   good feeling
	 * that your update has been completed. If it can't be
	   completed then
	 * it won't tell you. Sounds like something needs to be
	   added in the future!!!!.
	 * @param bhUser
	 */
	 public static void update(Bhuser bhuser) {
		 EntityManager em = DbUtil.getEntityManager("msgApp");
		 EntityTransaction trans = em.getTransaction();
		 try {
			 trans.begin();
			 em.merge(bhuser);
			 trans.commit();
		 }catch(Exception e) {
			 System.out.println(e);
			 trans.rollback();
		 }finally {
			 em.close();
		 }
		 
	 }
	 /**
	  * Removes a Bhuser from the database.
	  * Not sure why you'd want to delete a Bhuser from the database but this
	  * method will do it for you. This method does not explicitly remove the user's
	  * posts, but most likely you've set up the database with cascading deletes, which
	  * will take care of that. Gives no feedback.
	  * @param bhUser that you never want to see again
	  */
	  public static void delete(Bhuser bhuser) {
		  EntityManager em = DbUtil.getEntityManager("msgApp");
		  EntityTransaction trans = em.getTransaction();
		  try {
			  trans.begin();
			  em.remove(em.merge(bhuser));
			  trans.commit();
		  }catch(Exception e) {
			  System.out.println(e);
			  trans.rollback();
		  }finally {
			  em.close();
		  }
	  }
	  /**
	   * Gets a user given their email address.
	   * You've got the email when they log in but you really need the
	   * user and all its related information. This method will find the user
	   * matching that email. The database should ensure that you can't have two users
	   * with the same email. Otherwise there's no telling what you'd get.
	   * @param email
	   * @return Bhuser with that unique email address
	   */
	  public static Bhuser getUserByEmail(String email) {
		  EntityManager em = DbUtil.getEntityManager("msgApp");
		  String qstring = "Select u from Bhuser u "
				  		 + "where u.useremail =:useremail";
		  TypedQuery<Bhuser> q = em.createQuery(qstring,Bhuser.class);
		  q.setParameter("useremail", email);
		  Bhuser user = null;
		  try {
			  user = q.getSingleResult();
		  }catch(NoResultException e) {
			  System.out.println(e.getMessage());
		  }finally {
			  em.close();
		  }
		  return user;
	  }
	
}
