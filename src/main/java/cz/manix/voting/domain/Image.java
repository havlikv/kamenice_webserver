package cz.manix.voting.domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
@Access(AccessType.PROPERTY)
public class Image
{
	public final int SIZE = 2000000;

	private Integer id;
	private byte[] image;

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
	public byte[] getImage()
	{
		return image;
	}



	public void setImage(byte[] image)
	{
		this.image = image;
	}

}
