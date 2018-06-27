package org.frame.web.dao.authority;

import org.frame.web.model.authority.Authorization;


public interface IAuthorizationDao {

	public String delete();

	public String insert(Authorization authorization);

	public Authorization select();
	
	public String update(Authorization authorization);
	
}
