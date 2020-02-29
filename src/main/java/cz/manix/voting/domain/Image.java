package cz.manix.voting.domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Access(AccessType.PROPERTY)
public class Image
{
	public final int SIZE = 2000000;

	private Integer id;
	private byte[] xblob;
	private Option option;


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



	@Lob
	@Column(length = SIZE)
	@Basic(fetch = FetchType.LAZY)
	public byte[] getXBlob()
	{
		return xblob;
	}



	public void setXBlob(byte[] xblob)
	{
		this.xblob = xblob;
	}



	@ManyToOne
	@JoinColumn(name = "option_id")
	@JsonIgnore
	public Option getOption()
	{
		return option;
	}



	public void setOption(Option option)
	{
		this.option = option;
	}

}
