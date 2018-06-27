package org.frame.web.annotation.tree;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface TreeProperties {

	public static boolean TRUE = true;
	
	public static boolean FALSE = false;
	
	public static String CHECKED = "checked";
	
	public static String CHKDISABLED = "chkDisabled";
	
	public static String CLICK = "click";

	public static String COLLAPSE = "collapse";
	
	public static String DOWN = "down";
	
	public static String EXPAND = "expand";
	
	public static String FONT = "font";
	
	public static String HALFCHECK = "halfCheck";
	
	public static String ICON = "icon";
	
	public static String ICONCLOSE = "iconClose";
	
	public static String ICONOPEN = "iconOpen";
	
	public static String ICONSKIN = "iconSkin";
	
	public static String ISHIDDEN = "isHidden";

	public static String ISPARENT = "isParent";

	public static String NOCHECK = "nocheck";
	
	public static String OPEN = "open";
	
	public static String RIGHT = "right";
	
	public static String TARGET = "target";
	
	public static String UP = "up";
	
	public static String URL = "url";
	
	String checked() default "";
	
	String chkDisabled() default "";
	
	String click() default "";
	
	String collapse() default "";
	
	String down() default "";
	
	String expand() default "";
	
	String font() default "";
	
	String halfCheck() default "";
	
	String icon() default "";
	
	String iconClose() default "";
	
	String iconOpen() default "";
	
	String iconSkin() default "";
	
	String isHidden() default "";
	
	String isParent() default "";
	
	String nocheck() default "";
	
	String open() default "";
	
	String right() default "";
	
	String target() default "";
	
	String up() default "";
	
	String url() default "";
	
	String type() default "";
	
}
