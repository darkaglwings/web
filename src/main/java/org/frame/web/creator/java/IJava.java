package org.frame.web.creator.java;

import java.util.List;

public interface IJava {

	public void create(String schema, List<String> required, boolean controller, boolean dao, boolean model, boolean service);
	
}
