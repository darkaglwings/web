package org.frame.web.model.tree;

import org.frame.web.annotation.tree.Tree;
import org.frame.web.annotation.tree.TreeElement;
import org.frame.web.annotation.tree.TreeProperties;

@Tree(simple = false)
public class Data {

	@TreeElement(type = TreeElement.ID)
	public String id;
	
	@TreeElement(type = TreeElement.PARENT)
	public String parentid;
	
	@TreeElement(type = TreeElement.TITLE)
	public String title;
	
	@TreeElement(type = TreeElement.HINT)
	public String hint = title;
	
	@TreeProperties(type = TreeProperties.CHECKED)
	public String checked = "";
	
	@TreeProperties(type = TreeProperties.CHKDISABLED)
	public String chkDisabled = "";

	@TreeProperties(type = TreeProperties.CLICK)
	public String click = "";

	@TreeProperties(type = TreeProperties.COLLAPSE)
	public String collapse = "";

	@TreeProperties(type = TreeProperties.DOWN)
	public String down = "";

	@TreeProperties(type = TreeProperties.EXPAND)
	public String expand = "";

	@TreeProperties(type = TreeProperties.FONT)
	public String font = "";

	@TreeProperties(type = TreeProperties.HALFCHECK)
	public String halfCheck = "";

	@TreeProperties(type = TreeProperties.ICON)
	public String icon = "";

	@TreeProperties(type = TreeProperties.ICONCLOSE)
	public String iconClose = "";

	@TreeProperties(type = TreeProperties.ICONOPEN)
	public String iconOpen = "";

	@TreeProperties(type = TreeProperties.ICONSKIN)
	public String iconSkin = "";

	@TreeProperties(type = TreeProperties.ISHIDDEN)
	public String isHidden = "";

	@TreeProperties(type = TreeProperties.ISPARENT)
	public String isParent = "";

	@TreeProperties(type = TreeProperties.NOCHECK)
	public String nocheck = "";

	@TreeProperties(type = TreeProperties.OPEN)
	public String open = "";

	@TreeProperties(type = TreeProperties.RIGHT)
	public String right = "";

	@TreeProperties(type = TreeProperties.TARGET)
	public String target = "";

	@TreeProperties(type = TreeProperties.UP)
	public String up = "";

	@TreeProperties(type = TreeProperties.URL)
	public String url = "";

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getHint() {
		return hint;
	}

	public void setHint(String hint) {
		this.hint = hint;
	}

	public String getParentid() {
		return parentid;
	}

	public void setParentid(String parentid) {
		this.parentid = parentid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getChecked() {
		return checked;
	}

	public void setChecked(String checked) {
		this.checked = checked;
	}

	public String getChkDisabled() {
		return chkDisabled;
	}

	public void setChkDisabled(String chkDisabled) {
		this.chkDisabled = chkDisabled;
	}

	public String getClick() {
		return click;
	}

	public void setClick(String click) {
		this.click = click;
	}

	public String getCollapse() {
		return collapse;
	}

	public void setCollapse(String collapse) {
		this.collapse = collapse;
	}

	public String getDown() {
		return down;
	}

	public void setDown(String down) {
		this.down = down;
	}

	public String getExpand() {
		return expand;
	}

	public void setExpand(String expand) {
		this.expand = expand;
	}

	public String getFont() {
		return font;
	}

	public void setFont(String font) {
		this.font = font;
	}

	public String getHalfCheck() {
		return halfCheck;
	}

	public void setHalfCheck(String halfCheck) {
		this.halfCheck = halfCheck;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getIconClose() {
		return iconClose;
	}

	public void setIconClose(String iconClose) {
		this.iconClose = iconClose;
	}

	public String getIconOpen() {
		return iconOpen;
	}

	public void setIconOpen(String iconOpen) {
		this.iconOpen = iconOpen;
	}

	public String getIconSkin() {
		return iconSkin;
	}

	public void setIconSkin(String iconSkin) {
		this.iconSkin = iconSkin;
	}

	public String getIsHidden() {
		return isHidden;
	}

	public void setIsHidden(String isHidden) {
		this.isHidden = isHidden;
	}

	public String getIsParent() {
		return isParent;
	}

	public void setIsParent(String isParent) {
		this.isParent = isParent;
	}

	public String getNocheck() {
		return nocheck;
	}

	public void setNocheck(String nocheck) {
		this.nocheck = nocheck;
	}

	public String getOpen() {
		return open;
	}

	public void setOpen(String open) {
		this.open = open;
	}

	public String getRight() {
		return right;
	}

	public void setRight(String right) {
		this.right = right;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getUp() {
		return up;
	}

	public void setUp(String up) {
		this.up = up;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
}
