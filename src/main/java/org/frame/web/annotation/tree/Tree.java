package org.frame.web.annotation.tree;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Tree {

	String root() default "";
	
	boolean simple() default false;
	
}
