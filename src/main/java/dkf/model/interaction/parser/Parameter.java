package dkf.model.interaction.parser;

public class Parameter {
	
	private String name = null;
    private String dataType = null;
    private String semantics = null;
    
	public Parameter(String name, String dataType, String semantics) {
		
		this.name = name;
		this.dataType = dataType;
		this.semantics = semantics;
	}

	public Parameter() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getSemantics() {
		return semantics;
	}

	public void setSemantics(String semantics) {
		this.semantics = semantics;
	}
    
}
