package cz.manix.voting;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import cz.manix.voting.domain.Survey;



@RunWith(SpringRunner.class)
@ContextConfiguration(locations = { "classpath:spring/root-context.xml" })
public class JpaTest2
{
	@Autowired
	@Qualifier("entityManagerFactory")
	private EntityManagerFactory emf;

	@Test
	public void testCount()
	{
		EntityManager em = emf.createEntityManager();
		EntityTransaction transaction = em.getTransaction();
		transaction.begin();

		Survey survey = new Survey();
		survey.setName("Survey1");
		survey.setDescription("Survey1Descr");
		survey.setFromTimestamp(new Date());
		survey.setUntilTimestamp(new Date());

		em.persist(survey);

		transaction.commit();

		transaction = em.getTransaction();
		transaction.begin();

		String jpql = "select count(survey) from Survey survey";
		Query query = em.createQuery(jpql);
		Long count = (Long) query.getResultList().get(0);

		assertEquals(Long.valueOf(1), count);

		transaction.commit();
		em.close();
	}

}
