package cz.manix.voting.web;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cz.manix.voting.domain.Survey;

@RestController
@RequestMapping("/")
@Transactional
public class WebController
{
	@PersistenceContext
	private EntityManager em;



	public String createSurvey(Survey survey)
	{
		em.persist(survey);

		return String.format("%d", survey.getId());
	}



	// public String readSurveys()
	// {
	// }
}
