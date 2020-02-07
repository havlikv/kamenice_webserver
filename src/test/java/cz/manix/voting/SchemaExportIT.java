package cz.manix.voting;

import java.util.EnumSet;

import javax.persistence.spi.PersistenceUnitInfo;

import org.hibernate.boot.spi.MetadataImplementor;
import org.hibernate.jpa.boot.internal.EntityManagerFactoryBuilderImpl;
import org.hibernate.jpa.boot.internal.PersistenceUnitInfoDescriptor;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.hbm2ddl.SchemaExport.Action;
import org.hibernate.tool.schema.TargetType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@ContextConfiguration(locations = { "classpath:spring/root-context.xml" })
public class SchemaExportIT
{
	@Autowired
	@Qualifier("&entityManagerFactory")
	private LocalContainerEntityManagerFactoryBean emfBean;



	@Test
	public void test1()
	{
		PersistenceUnitInfo persistenceUnitInfo = emfBean.getPersistenceUnitInfo();

		EntityManagerFactoryBuilderImpl emfBuilder = new EntityManagerFactoryBuilderImpl(new PersistenceUnitInfoDescriptor(persistenceUnitInfo), null);

		emfBuilder.build();
		MetadataImplementor metadata = emfBuilder.getMetadata();

		SchemaExport schemaExport = new SchemaExport();
		schemaExport.setHaltOnError(true);
		schemaExport.setFormat(true);
		schemaExport.setDelimiter(";");
		schemaExport.execute(EnumSet.of(TargetType.STDOUT), Action.BOTH, metadata);
	}
}
