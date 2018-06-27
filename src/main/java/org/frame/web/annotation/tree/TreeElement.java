package org.frame.web.annotation.tree;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface TreeElement {

	public static String ID = "id";
	
	public static String HINT = "hint";
	
	public static String OTHER = "other";
	
	public static String PARENT = "parent";
	
	public static String TITLE = "title";
	
	String type() default "";
	
	boolean wrapped() default false;
	
}