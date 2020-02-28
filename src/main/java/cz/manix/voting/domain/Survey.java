package cz.manix.voting.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Access(AccessType.PROPERTY)
public class Survey
{
	private Integer id;
	private String name;
	private String description;
	private Date fromTimestamp;
	private Date untilTimestamp;

	private List<Option> options;


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



	@Column(nullable = false)
	public String getDescription()
	{
		return description;
	}



	public void setDescription(String description)
	{
		this.description = description;
	}



	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	public Date getFromTimestamp()
	{
		return fromTimestamp;
	}



	public void setFromTimestamp(Date fromTimestamp)
	{
		this.fromTimestamp = fromTimestamp;
	}



	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	public Date getUntilTimestamp()
	{
		return untilTimestamp;
	}



	public void setUntilTimestamp(Date untilTimestamp)
	{
		this.untilTimestamp = untilTimestamp;
	}



	@Transient
	public List<Option> getOptions()
	{
		return options;
	}



	public void setOptions(List<Option> options)
	{
		this.options = options;
	}
}
