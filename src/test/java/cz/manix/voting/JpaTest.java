package cz.manix.voting;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

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
public class JpaTest
{
	@Autowired
	@Qualifier("entityManagerFactory")
	private EntityManagerFactory emf;


	@Test
	public void test1FirstLevelCache()
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
		em.close();


		em = emf.createEntityManager();
		transaction = em.getTransaction();
		transaction.begin();

		Query query = em.createQuery("from Survey");
		List<Survey> surveys = query.getResultList();

		assertEquals(1, surveys.size());

		assertTrue(em.contains(surveys.get(0)));

		transaction.commit();
		em.close();
	}
}
