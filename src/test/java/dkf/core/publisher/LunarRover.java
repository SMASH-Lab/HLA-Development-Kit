package dkf.core.publisher;

import dkf.coder.HLAunicodeStringCoder;
import dkf.model.object.annotations.Attribute;
import dkf.model.object.annotations.ObjectClass;

@ObjectClass(name = "LunarRover")
public class LunarRover {
	
	@Attribute(name = "nome", coder = HLAunicodeStringCoder.class)
	private String entity_name = null;
	
	@Attribute(name = "tipo", coder = HLAunicodeStringCoder.class)
	private String parent_name = null;
	
	public LunarRover() {}
	
	public LunarRover(String entity_name, String parent_name) {
		
		this.entity_name = entity_name;
		this.parent_name = parent_name;
		
	}

	public String getEntity_name() {
		return entity_name;
	}

	public void setEntity_name(String entity_name) {
		this.entity_name = entity_name;
	}

	public String getParent_name() {
		return parent_name;
	}

	public void setParent_name(String parent_name) {
		this.parent_name = parent_name;
	}

}
