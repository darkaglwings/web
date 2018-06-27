/**
 * TableTreeDataFormat contains methods format data for TableTree
 */
package org.frame.web.tree.tabletree;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.frame.common.json.JSON;
import org.frame.common.lang.reflect.Reflect;
import org.frame.web.annotation.tabletree.TableTree;
import org.frame.web.model.tabletree.Leaf;
import org.frame.web.model.tabletree.Trunk;

public class TableTreeDataFormat {
	
	/**
	 * format data for TableTree
	 * 
	 * @param data list of data to be formatted
	 * 
	 * @return json string for TableTree data
	 */
	public String format(List<?> data) {
		String result;
		
		List<Object> lstData = new ArrayList<Object>();
		
		Class<?> clazz;
		for (Object object : data) {
			clazz = object.getClass();
			if (clazz.isAnnotationPresent(TableTree.class)) {
				if (TableTree.TRUNK.equals(clazz.getAnnotation(TableTree.class).node())) {
					Trunk trunk = this.trunk(object);
					trunk.getUserObject().put("isGroup", true);
					
					lstData.add(trunk);
				}
				
				if (TableTree.LEAF.equals(clazz.getAnnotation(TableTree.class).node())) {
					Leaf leaf = this.leaf(object);
					
					lstData.add(leaf);
				}
			}
		}
		
		result = new JSON().toJsonString(lstData);
		
		return result;
	}
	
	/**
	 * format leaf elements
	 * 
	 * @param object data to be formatted
	 * 
	 * @return json string for TableTree leaf data
	 */
	private Leaf leaf(Object object) {
		Leaf leaf = null;
		
		if (object != null) {
			leaf = new Leaf();
			
			Class<?> clazz = object.getClass();
			Field[] fields = clazz.getDeclaredFields();
			TableTree tableTree;
			Reflect reflect = new Reflect();
			String order = "0";
			for (Field field : fields) {
				if (field.isAnnotationPresent(TableTree.class)) {
					tableTree = field.getAnnotation(TableTree.class);
					
					if (TableTree.ID.equals(tableTree.element())) {
						leaf.setId(String.valueOf(reflect.get(object, field.getName().toLowerCase())));
					}
					
					if (TableTree.PID.equals(tableTree.element())) {
						leaf.setPid(String.valueOf(reflect.get(object, field.getName().toLowerCase())));
					}
					
					if (tableTree.isOrder()) {
						order = String.valueOf(reflect.get(object, field.getName().toLowerCase()));
						if (order == null || "".equals(order) || "null".equals(order)) {
							order = "0";
						}
						leaf.setOrder(Integer.parseInt(order));
					}
					
					if (!"".equals(tableTree.dataObject())) {
						leaf.getDataObject().put(tableTree.dataObject(), String.valueOf(reflect.get(object, field.getName().toLowerCase())));
					}
				}
			}
		}
		
		return leaf;
	}

	/**
	 * format trunk elements
	 * 
	 * @param object data to be formatted
	 * 
	 * @return json string for TableTree trunk data
	 */
	private Trunk trunk(Object object) {
		Trunk trunk = null;
		
		if (object != null) {
			trunk = new Trunk();
			
			Class<?> clazz = object.getClass();
			Field[] fields = clazz.getDeclaredFields();
			TableTree tableTree;
			Reflect reflect = new Reflect();
			String order = "0";
			for (Field field : fields) {
				if (field.isAnnotationPresent(TableTree.class)) {
					tableTree = field.getAnnotation(TableTree.class);
					
					if (TableTree.ID.equals(tableTree.element())) {
						trunk.setId(String.valueOf(reflect.get(object, field.getName().toLowerCase())));
					}
					
					if (tableTree.isOrder()) {
						order = String.valueOf(reflect.get(object, field.getName().toLowerCase()));
						if (order == null || "".equals(order) || "null".equals(order)) {
							order = "0";
						}
						trunk.setOrder(Integer.parseInt(order));
					}
					
					if (!"".equals(tableTree.dataObject())) {
						trunk.getDataObject().put(tableTree.dataObject(), String.valueOf(reflect.get(object, field.getName().toLowerCase())));
					}
					
					if (!"".equals(tableTree.userObject())) {
						trunk.getUserObject().put(tableTree.userObject(), reflect.get(object, field.getName().toLowerCase()));
					}
				}
			}
		}
		
		return trunk;
	}
	
}
