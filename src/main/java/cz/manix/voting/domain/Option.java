package cz.manix.voting.domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Access(AccessType.PROPERTY)
public class Option
{
	private Integer id;
	private String name;
	private String description;
	private Survey survey;


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId()
	{
		return id;
	}



	public void setId(Integer id)
	{
		this.id = id;
	}



	@Column(nullable = false, unique = true)
	public String getName()
	{
		return name;
	}



	public void setName(String name)
	{
		this.name = name;
	}



	@Column(nullable = false, unique = true)
	public String getDescription()
	{
		return description;
	}



	public void setDescription(String description)
	{
		this.description = description;
	}



	@ManyToOne
	@JoinColumn(name = "survey_id")
	@JsonIgnore
	public Survey getSurvey()
	{
		return survey;
	}



	public void setSurvey(Survey survey)
	{
		this.survey = survey;
	}
}
