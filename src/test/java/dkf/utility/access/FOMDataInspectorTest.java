package dkf.utility.access;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.jdom2.JDOMException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dkf.exception.TimeRepresentationException;

public class FOMDataInspectorTest {
	
	private File fomdir = null;
	private FOMDataInspectoryFactory finspector = null;

	@Before
	public void setUp() throws Exception {
		fomdir = new File("C:\\Users\\SEI01\\Desktop\\StarterKit project\\out");
		finspector = new FOMDataInspectoryFactory(fomdir);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testFOMDataInspector() {
		assertNotNull(finspector);
	}

	@Test
	public void testGetFOMsURL() {
		
		URL[] url = finspector.getFOMsURL();
		
		assertNotNull(url);
		assertTrue(url.length == 3);
	}

	@Test
	public void testGetTimestamp() throws JDOMException, IOException, TimeRepresentationException {
		assertTrue(finspector.getTimestamp() == TimeType.HLAinteger64Time);
	}

	@Test
	public void testGetLookahead() throws JDOMException, IOException, TimeRepresentationException {
		assertTrue(finspector.getLookahead() == TimeType.HLAinteger64Time);
	}

}
