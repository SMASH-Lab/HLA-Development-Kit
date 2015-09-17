package dkf.model.object.parser;

import dkf.model.Order;
import dkf.model.Sharing;
import dkf.model.Transportation;
import dkf.model.object.annotations.Ownership;
import dkf.model.object.annotations.UpdateType;

public class Attribute {
	
	private String name = null;
    private String dataType = null;
    private UpdateType updateType = null;
    private String updateCondition = null;
    private Ownership ownership = null;
    private Sharing sharing = null;
    private Transportation transportation = null;
    private Order order = null;
    private String semantics = null;
    
	public Attribute(String name, String dataType, UpdateType updateType,
			String updateCondition, Ownership ownership, Sharing sharing,
			Transportation transportation, Order order, String semantics) {
		
		this.name = name;
		this.dataType = dataType;
		this.updateType = updateType;
		this.updateCondition = updateCondition;
		this.ownership = ownership;
		this.sharing = sharing;
		this.transportation = transportation;
		this.order = order;
		this.semantics = semantics;
	}

	public Attribute() {
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

	public UpdateType getUpdateType() {
		return updateType;
	}

	public void setUpdateType(UpdateType updateType) {
		this.updateType = updateType;
	}

	public String getUpdateCondition() {
		return updateCondition;
	}

	public void setUpdateCondition(String updateCondition) {
		this.updateCondition = updateCondition;
	}

	public Ownership getOwnership() {
		return ownership;
	}

	public void setOwnership(Ownership ownership) {
		this.ownership = ownership;
	}

	public Sharing getSharing() {
		return sharing;
	}

	public void setSharing(Sharing sharing) {
		this.sharing = sharing;
	}

	public Transportation getTransportation() {
		return transportation;
	}

	public void setTransportation(Transportation transportation) {
		this.transportation = transportation;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public String getSemantics() {
		return semantics;
	}

	public void setSemantics(String semantics) {
		this.semantics = semantics;
	}
    
}
