package cz.manix.voting.web;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cz.manix.voting.domain.Image;
import cz.manix.voting.domain.Image_;
import cz.manix.voting.domain.Option;
import cz.manix.voting.domain.Option_;
import cz.manix.voting.domain.Survey;
import cz.manix.voting.domain.Survey_;

@RestController
@RequestMapping("/")
@Transactional
public class WebController
{
	private final static Logger logger = LoggerFactory.getLogger(WebController.class);

	@PersistenceContext
	private EntityManager em;



	@RequestMapping(path = "/survey", method = RequestMethod.POST)
	public Integer createSurvey(@RequestBody Survey survey)
	{
		em.persist(survey);

		return survey.getId();
	}



	@RequestMapping(path = "/survey", method = RequestMethod.PUT)
	public void updateSurvey(@RequestBody Survey survey)
	{
		em.merge(survey);
	}



	@RequestMapping(path = "/survey", method = RequestMethod.GET)
	public List<Survey> readSurveys()
	{
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Survey> criteriaQuery = cb.createQuery(Survey.class);
		Root<Survey> root = criteriaQuery.from(Survey.class);
		criteriaQuery.select(root);

		TypedQuery<Survey> typedQuery = em.createQuery(criteriaQuery);
		List<Survey> surveys = typedQuery.getResultList();

		return surveys;
	}



	@RequestMapping(path = { "/survey" }, method = RequestMethod.DELETE)
	public void deleteSurvey(@RequestParam(name = "id") int id)
	{
		Survey survey = em.find(Survey.class, id);

		List<Option> options = readOptions(id);

		for (Option option : options)
		{
			deleteOption(option.getId());
		}

		em.remove(survey);
	}



	@RequestMapping(path = { "/has_surveys" }, method = RequestMethod.GET)
	public boolean hasSurveys()
	{
		String jpql = "select count(survey) from Survey survey";

		Query query = em.createQuery(jpql);

		Long count = (Long) query.getResultList().get(0);

		return count > 0;
	}



	@RequestMapping(path = { "/survey_by_id" }, method = RequestMethod.GET)
	public Survey getSurveyById(@RequestParam(name = "id") Integer id)
	{
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Survey> criteriaQuery = cb.createQuery(Survey.class);
		Root<Survey> root = criteriaQuery.from(Survey.class);
		criteriaQuery.select(root).where(cb.equal(root.get(Survey_.ID), id));

		TypedQuery<Survey> typedQuery = em.createQuery(criteriaQuery);
		List<Survey> surveys = typedQuery.getResultList();

		if (surveys.size() == 0)
		{
			throw new RuntimeException(String.format("No such survey '%d'.", id));
		}

		return surveys.get(0);
	}



	@RequestMapping(path = "/option", method = RequestMethod.POST)
	public Integer createOption(@RequestParam(name = "survey_id") int surveyId, @RequestBody Option option)
	{
		Survey survey = em.find(Survey.class, surveyId);
		option.setSurvey(survey);
		em.persist(option);

		return option.getId();
	}



	@RequestMapping(path = "/option", method = RequestMethod.PUT)
	public void updateOption(@RequestParam(name = "survey_id") Integer surveyId, @RequestBody Option option)
	{
		Survey survey = em.find(Survey.class, surveyId);
		option.setSurvey(survey);

		em.merge(option);
	}



	@RequestMapping(path = "/option", method = RequestMethod.GET)
	public List<Option> readOptions(@RequestParam(name = "surveyId") int surveyId)
	{
		Survey survey = em.find(Survey.class, Integer.valueOf(surveyId));

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Option> criteriaQuery = cb.createQuery(Option.class);
		Root<Option> root = criteriaQuery.from(Option.class);
		criteriaQuery.select(root).where(cb.equal(root.get(Option_.SURVEY), survey));

		TypedQuery<Option> typedQuery = em.createQuery(criteriaQuery);
		List<Option> options = typedQuery.getResultList();

		return options;
	}



	@RequestMapping(path = "/option", method = RequestMethod.DELETE)
	public void deleteOption(@RequestParam(name = "id") int id)
	{

		Query deleteImagesQry = em.createQuery("delete from Image image where image.option.id = :id");
		deleteImagesQry.setParameter("id", id);
		int r = deleteImagesQry.executeUpdate();

		logger.info("Deleted {} images.", r);


		Option option = em.find(Option.class, id);

		em.remove(option);
	}



	@RequestMapping(path = "/image", method = RequestMethod.POST)
	public Integer createImage(@RequestParam("blob") MultipartFile blob,
			@RequestParam(name = "option_id") int optionId,
			@RequestParam(name = "seq") int seq) throws IOException
	{
		Option option = em.find(Option.class, optionId);

		Image image = new Image();
		image.setXBlob(blob.getBytes());
		image.setOption(option);
		image.setSeq(seq);
		image.setContentType(blob.getContentType());

		em.persist(image);

		return image.getId();
	}



	@RequestMapping(path = "/image", method = RequestMethod.GET)
	public void readImage(@RequestParam(name = "image_id") Integer imageId, HttpServletResponse response) throws IOException
	{
		Image image = em.find(Image.class, imageId);

		response.setContentType(image.getContentType());
		response.getOutputStream().write(image.getXBlob());
	}



	@RequestMapping(path = "/image", method = RequestMethod.DELETE)
	public void deleteImage(@RequestParam(name = "id") int id) throws IOException
	{
		Query deleteImagesQry = em.createQuery("delete from Image image where image.id = :id");
		deleteImagesQry.setParameter("id", id);
		int r = deleteImagesQry.executeUpdate();

		logger.info("Deleted {} images.", r);
	}



	@RequestMapping(path = "/image", method = RequestMethod.PUT)
	public void updateImageSeq(@RequestParam(name = "id") Integer id, @RequestParam(name = "seq") Integer seq)
	{
		Image image = em.find(Image.class, id);

		image.setSeq(seq);
	}



	@RequestMapping(path = "/images_ids", method = RequestMethod.GET)
	public List<Integer> readImagesIds(@RequestParam(name = "option_id") Integer optionId)
	{
		Option option = em.find(Option.class, optionId);

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Integer> criteriaQuery = cb.createQuery(Integer.class);
		Root<Image> root = criteriaQuery.from(Image.class);
		criteriaQuery.select(root.get(Image_.ID))
				.where(cb.equal(root.get(Image_.OPTION), option))
				.orderBy(cb.asc(root.get(Image_.SEQ)));

		TypedQuery<Integer> typedQuery = em.createQuery(criteriaQuery);
		List<Integer> imagesIds = typedQuery.getResultList();

		return imagesIds;
	}
}
