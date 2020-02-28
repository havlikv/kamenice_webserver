package cz.manix.voting;

import java.io.IOException;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

import cz.manix.voting.domain.Survey;
import cz.manix.voting.web.NoFailObjectMapper;



@RunWith(SpringRunner.class)
@ContextConfiguration(locations = { "classpath:spring/root-context.xml", "classpath:spring/dispatcherservlet-context.xml" })
public class ObjectMapperTest
{
	@Autowired
	NoFailObjectMapper objectMapper;


	@Test
	public void test1FirstLevelCache() throws JsonGenerationException, JsonMappingException, IOException
	{
		Survey survey = new Survey();
		survey.setName("Survey1");
		survey.setDescription("Survey1Descr");
		survey.setFromTimestamp(new Date());
		survey.setUntilTimestamp(new Date());

		objectMapper.writeValue(System.out, survey);
	}
}
