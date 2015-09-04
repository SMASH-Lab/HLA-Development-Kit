package dkf.utility.access;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.jdom2.JDOMException;

import dkf.exception.TimeRepresentationException;

public class FOMDataInspectoryFactory {

	private static FOMDataInspector inspector = null;
	private static URL[] url = null;
	private static TimeType timestamp = null;
	private static TimeType lookahead = null;

	public FOMDataInspectoryFactory(File fomsDirectory) {
		if(inspector == null)
			inspector = new FOMDataInspector(fomsDirectory);
	}

	public URL[] getFOMsURL() {
		if(url == null)
			url = inspector.getFOMsURL();
		return url;
	}

	public TimeType getTimestamp() throws JDOMException, IOException, TimeRepresentationException {
		if(timestamp == null){
			if(url == null)
				url = inspector.getFOMsURL();

			timestamp = inspector.getTimestamp(url);
		}
		return timestamp;
	}

	public TimeType getLookahead() throws JDOMException, IOException, TimeRepresentationException {
		if(lookahead == null){
			if(url == null)
				url = inspector.getFOMsURL();

			lookahead = inspector.getLookahead(url);
		}
		return lookahead;
	}
}
