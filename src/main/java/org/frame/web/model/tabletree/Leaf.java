package org.frame.web.model.tabletree;

import java.util.HashMap;
import java.util.Map;

public class Leaf {
	
	private String id;
	
	private String pid;
	
	private Integer order;
	
	Map<String, String> dataObject = new HashMap<String, String>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}
	
	public Map<String, String> getDataObject() {
		return dataObject;
	}

	public void setDataObject(Map<String, String> dataObject) {
		this.dataObject = dataObject;
	}

}
