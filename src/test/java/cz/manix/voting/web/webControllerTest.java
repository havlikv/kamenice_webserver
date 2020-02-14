package cz.manix.voting.web;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import cz.manix.voting.domain.Survey;



@RunWith(SpringRunner.class)
@ContextConfiguration(locations = { "classpath:spring/root-context.xml" })
public class webControllerTest
{
	@Autowired
	private WebController controller;

	@PersistenceContext
	private EntityManager em;



	@Test
	@Transactional
	public void test1()
	{
		Date now = new Date();

		Survey survey = new Survey();
		survey.setName("S1");
		survey.setDescription("S1 description.");
		survey.setFromTimestamp(now);
		survey.setUntilTimestamp(now);

		Integer id = controller.createSurvey(survey);

		Survey survey2 = em.find(Survey.class, Integer.valueOf(id));

		assertEquals(Integer.valueOf(id), survey2.getId());
	}

}
