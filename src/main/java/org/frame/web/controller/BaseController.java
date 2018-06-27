package org.frame.web.controller;

import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.frame.common.lang.StringHelper;
import org.frame.common.lang.reflect.Reflect;

public class BaseController {

	/**
	 * convert request content to java bean
	 * 
	 * @param request http servlet request
	 * @param clazz java bean class
	 * 
	 * @return instance of java bean class
	 */
	protected Object request2bean(HttpServletRequest request, Class<?> clazz) {
		Object object = null;
		
		try {
			Reflect reflect = new Reflect();
			
			object = clazz.newInstance();
			
			Map<String, String[]> map = (Map<String, String[]>) request.getParameterMap();
			String[] keys = new String[map.keySet().size()];
			keys = map.keySet().toArray(keys);
			Class<?> type;
			String value;
			
			for (String key : keys) {
				value = request.getParameter(key);
				for (Method method : clazz.getDeclaredMethods()) {
					if (method.getName().toLowerCase().equals("set" + key) && method.getParameterTypes().length == 1) {
						type = method.getParameterTypes()[0];
						if (StringHelper.isNull(value)) {
							reflect.set(object, key, null);
						} else if (type.getSimpleName().toLowerCase().equals("double")) {
							reflect.set(object, key, Double.parseDouble(value));
						} else if (type.getSimpleName().toLowerCase().equals("integer")) {
							reflect.set(object, key, Integer.parseInt(value));
						} else if (type.getSimpleName().toLowerCase().equals("long")) {
							reflect.set(object, key, Long.parseLong(value));
						} else if (type.getSimpleName().toLowerCase().equals("string[]")) {
							reflect.set(object, key, request.getParameterValues(key));
						} else {
							reflect.set(object, key, (type).cast(value));
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return object;
	}
	
}
