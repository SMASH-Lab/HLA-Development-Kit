package dkf.core.publisher;

import dkf.coder.HLAunicodeStringCoder;
import dkf.model.interaction.annotations.InteractionClass;
import dkf.model.interaction.annotations.Parameter;

@InteractionClass(name = "RoverInteraction")
public class RoverInteraction {
	
	@Parameter(name= "name", coder = HLAunicodeStringCoder.class)
	private String name = null;
	
	@Parameter(name= "payload", coder = HLAunicodeStringCoder.class)
	private String payload = null;

	public RoverInteraction(String name, String payload) {
		this.name = name;
		this.payload = payload;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	

}
