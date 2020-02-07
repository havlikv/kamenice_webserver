package cz.manix.voting.web;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class NoFailObjectMapper extends ObjectMapper
{
	private static final long serialVersionUID = 1L;



	public NoFailObjectMapper()
	{
		configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		setVisibility(PropertyAccessor.ALL, Visibility.NONE);
		// setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
		setVisibility(PropertyAccessor.GETTER, Visibility.PUBLIC_ONLY);
		setVisibility(PropertyAccessor.IS_GETTER, Visibility.PUBLIC_ONLY);

	}
}
