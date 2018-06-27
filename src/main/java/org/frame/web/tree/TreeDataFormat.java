/**
 * TreeDataFormat contains methods format data for zTree
 */
package org.frame.web.tree;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.frame.common.json.JSON;
import org.frame.common.lang.reflect.Reflect;
import org.frame.web.annotation.tree.Tree;
import org.frame.web.annotation.tree.TreeElement;
import org.frame.web.annotation.tree.TreeProperties;

public class TreeDataFormat {
	
	/**
	 * format data for zTree
	 * 
	 * @param data list to be formatted
	 * 
	 * @return json string for zTree
	 */
	public String format(List<?> data) {
		String result = "[]";
		
		List<Map<String, Object>> lstData = new ArrayList<Map<String, Object>>();
		for (Object object : data) {
			lstData.add(this.data(object));
		}
		
		result = new JSON().toJsonString(lstData);
		
		return result;
	}
	
	/**
	 * format data for zTree
	 * 
	 * @param root root element content
	 * @param data List to be formatted
	 * 
	 * @return json string for zTree
	 */
	public String format(String root, List<?> data) {
		return this.format(this.root(root), data);
	}
	
	/**
	 * format data for zTree
	 * 
	 * @param root map of root element
	 * @param data List to be formatted
	 * 
	 * @return json string for zTree
	 */
	public String format(Map<String, Object> root, List<?> data) {
		String result = "[]";
		
		List<Map<String, Object>> lstData = new ArrayList<Map<String, Object>>();
		lstData.add(root);
		for (Object object : data) {
			lstData.add(this.data(object));
		}
		
		result = new JSON().toJsonString(lstData);
		
		return result;
	}
	
	/**
	 * format root element
	 * 
	 * @param object root element
	 * 
	 * @return map of root element
	 */
	public Map<String, Object> root(Object object) {
		Map<String, Object> result = new HashMap<String, Object>();
		
		if (object instanceof String) {
			result.put("id", "-1");
			result.put("pId", null);
			result.put("name", (String) object);
			result.put("t", (String) object);
			
			/*result.put("checked", "");
			result.put("chkDisabled", "");
			result.put("click", "");
			result.put("collapse", "");
			result.put("down", "");
			result.put("expand", "");
			result.put("font", "");
			result.put("halfCheck", "");
			result.put("open", "");
			result.put("icon", "");
			result.put("iconClose", "");
			result.put("iconOpen", "");
			result.put("iconSkin", "");
			result.put("isHidden", "");
			result.put("isParent", "");
			result.put("nocheck", "");
			result.put("right", "");
			result.put("target", "");
			result.put("up", "");
			result.put("url", "");*/
		} else {
			String title = null;
			Class<?> clazz = object.getClass();
			if (clazz.isAnnotationPresent(Tree.class)) {
				title = clazz.getAnnotation(Tree.class).root();
			}
			
			if (title == null || "".equals(title)) {
				//result = this.data(object);
				
				result.put("id", "-1");
				result.put("pId", null);
				result.put("name", "please select nodes");
				result.put("t", "please select nodes");
			} else {
				result.put("id", "-1");
				result.put("pId", null);
				result.put("name", title);
				result.put("t", title);
			}

			result = this.union(result, this.properties(object));
		}
		
		return result;
	}

	/**
	 * format data
	 * 
	 * @param object record to be formatted
	 * 
	 * @return map of data properties
	 */
	private Map<String, Object> data(Object object) {
		Map<String, Object> result = null;
		
		if (object != null) {
			result = new HashMap<String, Object>();
			
			String id = null, hint = null, parentid = null, title = null;
			List<String> others = new ArrayList<String>();			
			
			boolean simple = false;
			boolean parent_wrapped = false;
			
			Class<?> clazz = object.getClass();
			if (clazz.isAnnotationPresent(Tree.class)) {
				simple = clazz.getAnnotation(Tree.class).simple();
				if (simple) {
					id = "id";
					hint = "title";
					parentid = "parentid";
					title = "title";
				} else {
					Field[] fields = clazz.getDeclaredFields();
					TreeElement treeElement;
					for (Field field : fields) {
						if (field.isAnnotationPresent(TreeElement.class)) {
							treeElement = field.getAnnotation(TreeElement.class);

							if (treeElement.type().equals(TreeElement.ID)) {
								id = field.getName().toLowerCase();
							}
							
							if (treeElement.type().equals(TreeElement.HINT)) {
								hint = field.getName().toLowerCase();
							}

							if (treeElement.type().equals(TreeElement.PARENT)) {
								parent_wrapped = treeElement.wrapped();
								parentid = field.getName().toLowerCase();
							}

							if (treeElement.type().equals(TreeElement.TITLE)) {
								title = field.getName().toLowerCase();
							}
							
							if (treeElement.type().equals(TreeElement.OTHER)) {
								others.add(field.getName());
							}
						}
					}

					Reflect reflect = new Reflect();
					result.put("id", reflect.get(object, id));
					result.put("name", reflect.get(object, title));
					
					if (hint == null || "null".equals(hint) || "".equals(hint)) {
						result.put("t", result.get("name"));
					} else {
						result.put("t", reflect.get(object, hint));
					}
					
					if (parent_wrapped) {
						Map<String, Object> map = this.data(reflect.get(object, parentid));
						result.put("pId", map.get("id"));
					} else {
						result.put("pId", reflect.get(object, parentid));
					}
					
					for (String other : others) {
						result.put(other, reflect.get(object, other.toLowerCase()));
					}
				}
			}
			
			result = this.union(result, this.properties(object));
		}
		
		return result;
	}
	
	/**
	 * format properties
	 * 
	 * @param object record to be formatted
	 * 
	 * @return map of properties
	 */
	private Map<String, Object> properties(Object object) {
		Map<String, Object> result = new HashMap<String, Object>();
		
		if (object != null) {
			String  checked = "", 
					chkDisabled = "", 
					click = "", 
					collapse = "", 
					down = "", 
					expand = "", 
					font = "", 
					halfCheck = "", 
					icon = "", 
					iconClose = "", 
					iconOpen = "", 
					iconSkin = "", 
					isHidden = "", 
					isParent = "", 
					nocheck = "", 
					open = "", 
					right = "", 
					target = "", 
					up = "", 
					url = "";
			
			boolean simple = false;
			
			boolean checked_propertied = false, 
					chkDisabled_propertied = false, 
					click_propertied = false, 
					collapse_propertied = false, 
					down_propertied = false, 
					expand_propertied = false, 
					font_propertied = false, 
					halfCheck_propertied = false, 
					icon_propertied = false, 
					iconClose_propertied = false, 
					iconOpen_propertied = false, 
					iconSkin_propertied = false, 
					isHidden_propertied = false, 
					isParent_propertied = false, 
					nocheck_propertied = false, 
					open_propertied = false, 
					right_propertied = false, 
					target_propertied = false, 
					up_propertied = false, 
					url_propertied = false;

			Class<?> clazz = object.getClass();
			if (clazz.isAnnotationPresent(Tree.class)) {
				simple = clazz.getAnnotation(Tree.class).simple();
				if (simple) {
					checked = "checked";
					chkDisabled = "chkDisabled";
					click = "click";
					collapse = "collapse";
					down = "down";
					expand = "expand";
					font = "font";
					halfCheck = "halfCheck";
					icon = "icon";
					iconClose = "iconClose";
					iconOpen = "iconOpen";
					iconSkin = "iconSkin";
					isHidden = "isHidden";
					isParent = "isParent";
					nocheck = "nocheck";
					open = "open";
					right = "right";
					target = "target";
					up = "up";
					url = "url";
				} else {
					Field[] fields = clazz.getDeclaredFields();
					TreeProperties treeProperties;
					for (Field field : fields) {
						if (field.isAnnotationPresent(TreeProperties.class)) {
							treeProperties = field.getAnnotation(TreeProperties.class);
							
							checked = treeProperties.checked();
							chkDisabled = treeProperties.chkDisabled();
							click = treeProperties.click();
							collapse = String.valueOf(treeProperties.collapse());
							down = String.valueOf(treeProperties.down());
							expand = String.valueOf(treeProperties.expand());
							font = treeProperties.font();
							halfCheck = treeProperties.halfCheck();
							icon = treeProperties.icon();
							iconClose = treeProperties.iconClose();
							iconOpen = treeProperties.iconOpen();
							iconSkin = treeProperties.iconSkin();
							isHidden = treeProperties.isHidden();
							isParent = treeProperties.isParent();
							nocheck = treeProperties.nocheck();
							open = String.valueOf(treeProperties.open());
							right = String.valueOf(treeProperties.right());
							target = treeProperties.target();
							up = String.valueOf(treeProperties.up());
							url = treeProperties.url();
							
							if (treeProperties.type().equals(TreeProperties.CHECKED)) {
								checked_propertied = true;
								checked = field.getName().toLowerCase();
							}
							
							if (treeProperties.type().equals(TreeProperties.CHKDISABLED)) {
								chkDisabled_propertied = true;
								chkDisabled = field.getName().toLowerCase();
							}
							
							if (treeProperties.type().equals(TreeProperties.CLICK)) {
								click_propertied = true;
								click = field.getName().toLowerCase();
							}
							
							if (treeProperties.type().equals(TreeProperties.COLLAPSE)) {
								collapse_propertied = true;
								collapse = field.getName().toLowerCase();
							}
							
							if (treeProperties.type().equals(TreeProperties.DOWN)) {
								down_propertied = true;
								down = field.getName().toLowerCase();
							}
							
							if (treeProperties.type().equals(TreeProperties.EXPAND)) {
								expand_propertied = true;
								expand = field.getName().toLowerCase();
							}
							
							if (treeProperties.type().equals(TreeProperties.FONT)) {
								font_propertied = true;
								font = field.getName().toLowerCase();
							}
							
							if (treeProperties.type().equals(TreeProperties.HALFCHECK)) {
								halfCheck_propertied = true;
								halfCheck = field.getName().toLowerCase();
							}
							
							if (treeProperties.type().equals(TreeProperties.ICON)) {
								icon_propertied = true;
								icon = field.getName().toLowerCase();
							}
							
							if (treeProperties.type().equals(TreeProperties.ICONCLOSE)) {
								iconClose_propertied = true;
								iconClose = field.getName().toLowerCase();
							}
							
							if (treeProperties.type().equals(TreeProperties.ICONOPEN)) {
								iconOpen_propertied = true;
								iconOpen = field.getName().toLowerCase();
							}
							
							if (treeProperties.type().equals(TreeProperties.ICONSKIN)) {
								iconSkin_propertied = true;
								iconSkin = field.getName().toLowerCase();
							}
							
							if (treeProperties.type().equals(TreeProperties.ISHIDDEN)) {
								isHidden_propertied = true;
								isHidden = field.getName().toLowerCase();
							}
							
							if (treeProperties.type().equals(TreeProperties.ISPARENT)) {
								isParent_propertied = true;
								isParent = field.getName().toLowerCase();
							}
							
							if (treeProperties.type().equals(TreeProperties.NOCHECK)) {
								nocheck_propertied = true;
								nocheck = field.getName().toLowerCase();
							}
							
							if (treeProperties.type().equals(TreeProperties.OPEN)) {
								open_propertied = true;
								open = field.getName().toLowerCase();
							}
							
							if (treeProperties.type().equals(TreeProperties.RIGHT)) {
								right_propertied = true;
								right = field.getName().toLowerCase();
							}
							
							if (treeProperties.type().equals(TreeProperties.TARGET)) {
								target_propertied = true;
								target = field.getName().toLowerCase();
							}
							
							if (treeProperties.type().equals(TreeProperties.UP)) {
								up_propertied = true;
								up = field.getName().toLowerCase();
							}
							
							if (treeProperties.type().equals(TreeProperties.URL)) {
								url_propertied = true;
								url = field.getName().toLowerCase();
							}
						}
					}
				}
				
				Reflect reflect = new Reflect();
				
				if (checked_propertied) {
					result.put("checked", reflect.get(object, checked));
				} else {
					result.put("checked", checked);
				}
				
				if (chkDisabled_propertied) {
					result.put("chkDisabled", reflect.get(object, chkDisabled));
				} else {
					result.put("chkDisabled", chkDisabled);
				}
				
				if (click_propertied) {
					result.put("click", reflect.get(object, click));
				} else {
					result.put("click", click);
				}
				
				if (collapse_propertied) {
					result.put("collapse", reflect.get(object, collapse));
				} else {
					result.put("collapse", collapse);
				}
				
				if (down_propertied) {
					result.put("down", reflect.get(object, down));
				} else {
					result.put("down", down);
				}
				
				if (expand_propertied) {
					result.put("expand", reflect.get(object, expand));
				} else {
					result.put("expand", expand);
				}
				
				if (font_propertied) {
					result.put("font", reflect.get(object, font));
				} else {
					result.put("font", font);
				}
				
				if (halfCheck_propertied) {
					result.put("halfCheck", reflect.get(object, halfCheck));
				} else {
					result.put("halfCheck", halfCheck);
				}
				
				if (icon_propertied) {
					result.put("icon", reflect.get(object, icon));
				} else {
					result.put("icon", icon);
				}
				
				if (iconClose_propertied) {
					result.put("iconClose", reflect.get(object, iconClose));
				} else {
					result.put("iconClose", iconClose);
				}
				
				if (iconOpen_propertied) {
					result.put("iconOpen", reflect.get(object, iconOpen));
				} else {
					result.put("iconOpen", iconOpen);
				}
				
				if (iconSkin_propertied) {
					result.put("iconSkin", reflect.get(object, iconSkin));
				} else {
					result.put("iconSkin", iconSkin);
				}
				
				if (isHidden_propertied) {
					result.put("isHidden", reflect.get(object, isHidden));
				} else {
					result.put("isHidden", isHidden);
				}
				
				if (isParent_propertied) {
					result.put("isParent", reflect.get(object, isParent));
				} else {
					result.put("isParent", isParent);
				}
				
				if (nocheck_propertied) {
					result.put("nocheck", reflect.get(object, nocheck));
				} else {
					result.put("nocheck", nocheck);
				}
				
				if (open_propertied) {
					result.put("open", reflect.get(object, open));
				} else {
					result.put("open", open);
				}
				
				if (right_propertied) {
					result.put("right", reflect.get(object, right));
				} else {
					result.put("right", right);
				}
				
				if (target_propertied) {
					result.put("target", reflect.get(object, target));
				} else {
					result.put("target", target);
				}
				
				if (up_propertied) {
					result.put("up", reflect.get(object, up));
				} else {
					result.put("up", up);
				}
				
				if (url_propertied) {
					result.put("url", reflect.get(object, url));
				} else {
					result.put("url", url);
				}
			}
		}
		
		return result;
	}
	
	/**
	 * union data and properties
	 * 
	 * @param data map of data
	 * @param properties map of properties
	 * 
	 * @return map of all element for one record
	 */
	private Map<String, Object> union(Map<String, Object> data, Map<String, Object> properties) {
		data.put("checked", properties.get("checked"));
		data.put("chkDisabled", properties.get("chkDisabled"));
		data.put("click", properties.get("click"));
		data.put("collapse", properties.get("collapse"));
		data.put("down", properties.get("down"));
		data.put("expand", properties.get("expand"));
		data.put("font", properties.get("font"));
		data.put("halfCheck", properties.get("halfCheck"));
		data.put("open", properties.get("open"));
		data.put("icon", properties.get("icon"));
		data.put("iconClose", properties.get("iconClose"));
		data.put("iconOpen", properties.get("iconOpen"));
		data.put("iconSkin", properties.get("iconSkin"));
		data.put("isHidden", properties.get("isHidden"));
		data.put("isParent", properties.get("isParent"));
		data.put("nocheck", properties.get("nocheck"));
		data.put("right", properties.get("right"));
		data.put("target", properties.get("target"));
		data.put("up", properties.get("up"));
		data.put("url", properties.get("url"));
		
		return data;
	}
	
	/*public static void main(String[] args) {
		Data root = new Data();
		Data level11 = new Data();
		Data level12 = new Data();
		Data level21 = new Data();
		Data level22 = new Data();
		
		root.setId("-1");
		root.setParentid(null);
		root.setTitle("root");
		
		level11.setId("11");
		level11.setParentid("-1");
		level11.setTitle("level11");
		
		level12.setId("12");
		level12.setParentid("-1");
		level12.setTitle("level12");
		
		level21.setId("21");
		level21.setParentid("11");
		level21.setTitle("level21");
		
		level22.setId("22");
		level22.setParentid("12");
		level22.setTitle("level22");
		
		List<Data> lstTree = new ArrayList<Data>();
		lstTree.add(root);
		lstTree.add(level11);
		lstTree.add(level12);
		lstTree.add(level21);
		lstTree.add(level22);
		
		String info = null;
		info = new TreeDataFormat().format(lstTree);
		
		System.out.println(info);
	}*/
	
}
