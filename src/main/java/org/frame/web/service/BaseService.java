package org.frame.web.service;

import java.util.ArrayList;
import java.util.List;

import org.frame.common.lang.StringHelper;

public class BaseService {

	protected String join(String[] id, Object[] execute) {
		List<Object> result = new ArrayList<Object>();
		
		for (int i = 0; i < execute.length; i++) {
			try {
				if (Integer.parseInt(String.valueOf(execute[i])) > 0) {
					result.add(id[i]);
				} else {
					result.add(execute[i]);
				}
			} catch (Exception e) {
				result.add(execute[i]);
				e.printStackTrace();
			}
		}
		
		return new StringHelper().join(result.toArray());
	}
	
}
