package org.frame.web.service.authority;

import org.frame.web.model.authority.Authorization;


public interface IAuthorizationService {
	
	public String create(Authorization authorization);
	
	public String edit(Authorization authorization);
	
	public String modify(Authorization authorization);
	
	public Authorization query();
	
	public String remove();
	
}
