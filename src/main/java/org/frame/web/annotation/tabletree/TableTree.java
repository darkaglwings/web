package org.frame.web.annotation.tabletree;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface TableTree {

	public static final String TRUNK = "trunk";
	
	public static final String LEAF = "leaf";
	
	public static final String ID = "id";
	
	public static final String PID = "pid";
	
	public static final String ORDER = "order";
	
	String node() default TRUNK;
	
	String element() default "";
	
	boolean isLeaf() default false;
	
	boolean isOrder() default false;
	
	String dataObject() default "";
	
	String userObject() default "";
	
}
