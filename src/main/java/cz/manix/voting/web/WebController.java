package cz.manix.voting.web;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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
			throw new RuntimeException(String.format("No such survey '&d'.", id));
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
	public void deleteOption(Integer id)
	{
		Option option = em.find(Option.class, id);

		List<Image> images = readImages(option);


		for (Image image : images)
		{
			em.remove(image);
		}

		em.remove(option);
	}



	@RequestMapping(path = "/image", method = RequestMethod.POST)
	public Integer createImage(@RequestParam(name = "option_id") Integer optionId, @RequestParam("blob") MultipartFile blob) throws IOException
	{
		Option option = em.find(Option.class, optionId);

		Image image = new Image();
		image.setXBlob(blob.getBytes());
		image.setOption(option);

		em.persist(image);

		return image.getId();
	}



	@RequestMapping(path = "/image", method = RequestMethod.GET)
	@ResponseBody
	public byte[] readImage(@RequestParam(name = "image_id") Integer imageId)
	{
		Image image = em.find(Image.class, imageId);

		return image.getXBlob();
	}



	@RequestMapping(path = "/images_ids", method = RequestMethod.GET)
	public List<Integer> readImagesIds(@RequestParam(name = "option_id") Integer optionId)
	{
		Option option = em.find(Option.class, optionId);

		List<Image> images = readImages(option);

		return images.stream().map(i -> i.getId()).collect(Collectors.toList());
	}



	private List<Image> readImages(Option option)
	{
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Image> criteriaQuery = cb.createQuery(Image.class);
		Root<Image> root = criteriaQuery.from(Image.class);
		criteriaQuery.select(root.get(Image_.ID)).where(cb.equal(root.get(Image_.OPTION), option));

		TypedQuery<Image> typedQuery = em.createQuery(criteriaQuery);
		List<Image> images = typedQuery.getResultList();

		return images;
	}

}
