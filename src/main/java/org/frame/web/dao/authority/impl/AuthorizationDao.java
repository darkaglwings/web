package org.frame.web.dao.authority.impl;

import org.frame.common.constant.ICommonConstant;
import org.frame.common.lang.StringHelper;
import org.frame.common.util.Properties;
import org.frame.web.dao.authority.IAuthorizationDao;
import org.frame.web.model.authority.Authorization;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

@Repository("authorizationDao")
@Scope("singleton")
public class AuthorizationDao implements IAuthorizationDao {

	@Override
	public String delete() {
		boolean result = false;
		
		Properties properties = new Properties(ICommonConstant.DEFAULT_CONFIG_PROPERTIES);
		properties.remove("c");
		properties.remove("p");
		properties.remove("a");
		
		result = true;
		
		return String.valueOf(result);
	}

	@Override
	public String insert(Authorization authorization) {
		boolean result = false;
		
		Properties properties = new Properties(ICommonConstant.DEFAULT_CONFIG_PROPERTIES);
		if (!StringHelper.isNull(authorization.getCertificate())) {
			properties.write("c", authorization.getCertificate());
		}
		
		if (!StringHelper.isNull(authorization.getPassword())) {
			properties.write("p", authorization.getPassword());
		}
		
		if (!StringHelper.isNull(authorization.getAlias())) {
			properties.write("a", authorization.getAlias());
		}
		
		result = true;
		
		return String.valueOf(result);
	}

	@Override
	public Authorization select() {
		Properties properties = new Properties(ICommonConstant.DEFAULT_CONFIG_PROPERTIES);
		
		Authorization authorization = new Authorization();
		authorization.setCertificate(properties.read("c"));
		authorization.setPassword(properties.read("p"));
		authorization.setAlias(properties.read("a"));
		
		return authorization;
	}

	@Override
	public String update(Authorization authorization) {
		boolean result = false;
		
		Properties properties = new Properties(ICommonConstant.DEFAULT_CONFIG_PROPERTIES);
		if (!StringHelper.isNull(authorization.getCertificate())) {
			properties.write("c", authorization.getCertificate());
		} else {
			properties.remove("c");
		}
		
		if (!StringHelper.isNull(authorization.getPassword())) {
			properties.write("p", authorization.getPassword());
		} else {
			properties.remove("p");
		}
		
		if (!StringHelper.isNull(authorization.getAlias())) {
			properties.write("a", authorization.getAlias());
		} else {
			properties.remove("a");
		}
		
		result = true;
		
		return String.valueOf(result);
	}
	
}
