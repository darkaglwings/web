/**
 * Config contains method to get system configuration
 */
package org.frame.web.profile;

import org.frame.common.lang.StringHelper;
import org.frame.common.util.Properties;
import org.frame.web.constant.IWebConstant;

public class Config {
	
	private String path;
	
	public Config(String path) {
		this.path = path;
	}
	
	/**
	 * to get system configuration
	 * 
	 * @param key key for configuration
	 * 
	 * @return value of configuration
	 */
	public String get(String key) {
		if (StringHelper.isNull(path)) path = IWebConstant.DEFAULT_CONFIG_PROPERTIES;
		
		String result = "";
		try {
			if (Thread.currentThread().getContextClassLoader().getResourceAsStream(path) != null) {
				if (new Properties(path).read(key) != null) {
					result = new Properties(path).read(key).toLowerCase();

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	/*public static void main(String[] args) {
		System.out.println(new Properties("org/frame/web/config/config.properties").read("system.database"));
	}*/

}