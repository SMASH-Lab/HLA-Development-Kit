package dkf.utility.access;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.jdom2.JDOMException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import dkf.model.object.parser.Attribute;

public class FOMDataInspectorTest {
	
	private FOMDataInspector inspector = null;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		inspector = new FOMDataInspector(new File("C:\\Users\\SEI01\\Desktop\\StarterKit project\\out"));
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testRetrieveObjectStructure() {
		try {
			List<Attribute> ris = inspector.retrieveObjectStructure("PhysicalEntity");
			
			assertTrue(!ris.isEmpty());
			
			for(Attribute entry : ris)
				System.out.println(entry.getName()+" --- "+entry.getDataType());
			
		} catch (JDOMException | IOException e) {
			e.printStackTrace();
		}
	}

}
