package dkf.utility.access;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import org.jdom2.Element;

import dkf.exception.TimeRepresentationException;

public class FOMDataInspector {

	Logger logger = LogManager.getLogger(FOMDataInspector.class);

	// default Namespace
	private static final Namespace defaultNs = Namespace.getNamespace("ns", "http://standards.ieee.org/IEEE1516-2010");
	private XPathFactory expr = null;

	private File fomsDirectory = null;

	protected FOMDataInspector(File fomsDirectory) {
		this.fomsDirectory = fomsDirectory;
		this.expr = XPathFactory.instance();
	}

	protected URL[] getFOMsURL() {

		//load FOMs Modules
		FileFilter filter = new FileFilter() {
			@Override
			public boolean accept(File file) {
				if(file.getName().endsWith(".xml")){
					logger.debug("Loading the "+file+" file.");
					return true;
				}
				return false;
			}
		};

		URL[] foms = new URL[fomsDirectory.listFiles(filter).length];
		int index = 0;
		for(File file: fomsDirectory.listFiles(filter)){
			try {
				foms[index] = file.toURI().toURL();
				index ++;
			} catch (IOException e) {
				e.printStackTrace();
				logger.error("An error occurred during the processing of the FOM modules "+e.getMessage());
			}
		}

		return foms;

	}

	private TimeType retrieveTimeRepresentation(URL[] url, XPathExpression<Element> expression) throws JDOMException, IOException, TimeRepresentationException {

		SAXBuilder builder = new SAXBuilder();
		Document document = null;
		Element timetag = null;

		for(URL turl : url){
			document = (Document) builder.build(turl);
			timetag = expression.evaluateFirst(document);
			if(timetag!= null)
				return TimeType.valueOf(timetag.getText());
		}
		return null;
	}

	protected TimeType getTimestamp(URL[] url) throws JDOMException, IOException, TimeRepresentationException {
		return retrieveTimeRepresentation(url, expr.compile("//ns:time/ns:timeStamp/ns:dataType", Filters.element(), null, defaultNs));
	}

	protected TimeType getLookahead(URL[] url) throws JDOMException, IOException, TimeRepresentationException {
		return retrieveTimeRepresentation(url, expr.compile("//ns:time/ns:lookahead/ns:dataType", Filters.element(), null, defaultNs));
	}

}
