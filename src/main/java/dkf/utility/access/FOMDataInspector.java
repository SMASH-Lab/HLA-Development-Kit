package dkf.utility.access;


import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

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
import dkf.model.Order;
import dkf.model.Sharing;
import dkf.model.Transportation;
import dkf.model.interaction.parser.Parameter;
import dkf.model.object.annotations.Ownership;
import dkf.model.object.annotations.UpdateType;
import dkf.model.object.parser.Attribute;
import dkf.utility.dataType.TimeType;

public class FOMDataInspector {

	private static final Logger logger = LogManager.getLogger(FOMDataInspector.class);

	// default Namespace
	private static final Namespace defaultNs = Namespace.getNamespace("ns", "http://standards.ieee.org/IEEE1516-2010");
	private XPathFactory expr = null;

	private File fomsDirectory = null;

	private URL[] foms = null;
	private TimeType timestamp = null;
	private TimeType lookahead = null;

	private SAXBuilder builder = null;

	public FOMDataInspector(File fomsDirectory) {
		this.fomsDirectory = fomsDirectory;
		this.expr = XPathFactory.instance();
		this.builder = new SAXBuilder();
		
		retrieveFOMsURL();
	}

	private void retrieveFOMsURL() {

		//load FOMs Modules
		FileFilter filter = new FileFilter() {
			@Override
			public boolean accept(File file) {
				if(file.getName().endsWith(".xml")){
					return true;
				}
				return false;
			}
		};

		foms = new URL[fomsDirectory.listFiles(filter).length];
		int index = 0;
		for(File file: fomsDirectory.listFiles(filter)){
			try {
				logger.debug("Loading the "+file+" file.");
				foms[index] = file.toURI().toURL();
				index ++;
			} catch (IOException e) {
				logger.error("An error occurred during the processing of the FOM modules "+e.getMessage(), e);
			}
		}
	}

	public URL[] getFOMsURL() {
		return foms;

	}

	private TimeType retrieveTimeRepresentation(URL[] url, XPathExpression<Element> expression) throws JDOMException, IOException, TimeRepresentationException {

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

	public TimeType getTimestampType() throws JDOMException, IOException, TimeRepresentationException {
		if(timestamp == null)
			timestamp = retrieveTimeRepresentation(foms, expr.compile("//ns:time/ns:timeStamp/ns:dataType", Filters.element(), null, defaultNs));
		return timestamp;
	}

	public TimeType getLookaheadType() throws JDOMException, IOException, TimeRepresentationException {
		if(lookahead == null)
			lookahead = retrieveTimeRepresentation(foms, expr.compile("//ns:time/ns:lookahead/ns:dataType", Filters.element(), null, defaultNs));
		return lookahead;
	}

	public List<Attribute> retrieveObjectStructure(String name) throws JDOMException, IOException {

		Document document = null;
		List<Element> objectClassElementList = null;
		List<Attribute> ris = new LinkedList<Attribute>();
		Attribute atmp = null;
		
		for(URL turl : foms){
			document = (Document) builder.build(turl);
			objectClassElementList = expr.compile("//ns:objectClass[ns:name='HLAobjectRoot']//ns:objectClass[ns:name='"+name+"']/ns:attribute", Filters.element(), null, defaultNs).evaluate(document);
			if(objectClassElementList != null && !objectClassElementList.isEmpty()) {

				for(Element el : objectClassElementList){
					atmp = new Attribute();
					atmp.setName(el.getChild("name", defaultNs).getValue());
					atmp.setDataType(el.getChild("dataType", defaultNs).getValue());
					atmp.setUpdateType(UpdateType.valueOf(el.getChild("updateType", defaultNs).getValue()));
					atmp.setUpdateCondition(el.getChild("updateCondition", defaultNs).getValue());
					atmp.setOwnership(Ownership.valueOf(el.getChild("ownership", defaultNs).getValue()));
					atmp.setSharing(Sharing.valueOf(el.getChild("sharing", defaultNs).getValue()));
					atmp.setTransportation(Transportation.valueOf(el.getChild("transportation", defaultNs).getValue()));
					atmp.setOrder(Order.valueOf(el.getChild("order", defaultNs).getValue()));
					atmp.setSemantics(el.getChild("semantics", defaultNs).getValue());
					
					ris.add(atmp);
				}
				break;
			}
		}
		return ris;
	}

	public List<Parameter> retrieveInteractionStructure(String name) throws JDOMException, IOException {
		
		Document document = null;
		List<Element> interactionClassElementList = null;
		List<Parameter> ris = new LinkedList<Parameter>();
		Parameter ptmp = null;
		
		for(URL turl : foms){
			document = (Document) builder.build(turl);
			interactionClassElementList = expr.compile("//ns:interactionClass[ns:name='HLAinteractionRoot']//ns:interactionClass[ns:name='"+name+"']/ns:parameter", Filters.element(), null, defaultNs).evaluate(document);
			if(interactionClassElementList != null && !interactionClassElementList.isEmpty()) {

				for(Element el : interactionClassElementList){
					ptmp = new Parameter();
					ptmp.setName(el.getChild("name", defaultNs).getValue());
					ptmp.setDataType(el.getChild("dataType", defaultNs).getValue());
					ptmp.setSemantics(el.getChild("semantics", defaultNs).getValue());
					
					ris.add(ptmp);
				}
				break;
			}
		}
		return ris;
	}

}
