package org.frame.web.model.tabletree;

import java.util.HashMap;
import java.util.Map;

public class Trunk {
	
	private String id;
	
	private Integer order;
	
	private boolean isLeaf = false;
	
	Map<String, String> dataObject = new HashMap<String, String>();
	
	Map<String, Object> userObject = new HashMap<String, Object>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public boolean getIsLeaf() {
		return isLeaf;
	}

	public void setIsLeaf(boolean isLeaf) {
		this.isLeaf = isLeaf;
	}

	public Map<String, String> getDataObject() {
		return dataObject;
	}

	public void setDataObject(Map<String, String> dataObject) {
		this.dataObject = dataObject;
	}

	public Map<String, Object> getUserObject() {
		return userObject;
	}

	public void setUserObject(Map<String, Object> userObject) {
		this.userObject = userObject;
	}

}
