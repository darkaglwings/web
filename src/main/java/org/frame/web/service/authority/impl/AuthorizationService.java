package org.frame.web.service.authority.impl;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.frame.common.lang.StringHelper;
import org.frame.common.security.Coder;
import org.frame.web.dao.authority.IAuthorizationDao;
import org.frame.web.model.authority.Authorization;
import org.frame.web.service.authority.IAuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

@Repository("authorizationService")
@Scope("singleton")
public class AuthorizationService implements IAuthorizationService {

	@SuppressWarnings("unused")
	private Log logger = LogFactory.getLog(AuthorizationService.class);
	
	@Resource
	@Autowired
	IAuthorizationDao authorizationDao;
	
	@Override
	public String create(Authorization authorization) {
		Coder coder = new Coder();
		
		String alias = authorization.getAlias();
		String certificate = authorization.getCertificate();
		String password = authorization.getPassword();
		
		if (!StringHelper.isNull(alias)) {
			alias = "A" + coder.base64Encoder(coder.encode(alias));
		} else {
			alias = "";
		}
		
		if (!StringHelper.isNull(alias)) {
			certificate = "C" + coder.base64Encoder(coder.encode(certificate));
		} else {
			certificate = "";
		}
		
		if (!StringHelper.isNull(alias)) {
			password = "P" + coder.base64Encoder(coder.encode(password));
		} else {
			password = "";
		}
		
		authorization.setAlias(alias);
		authorization.setCertificate(certificate);
		authorization.setPassword(password);
		
		return authorizationDao.insert(authorization);
	}

	@Override
	public String edit(Authorization authorization) {
		Coder coder = new Coder();
		
		String alias = authorization.getAlias();
		String certificate = authorization.getCertificate();
		String password = authorization.getPassword();
		
		if (!StringHelper.isNull(alias)) {
			alias = "A" + coder.base64Encoder(coder.encode(alias));
		} else {
			alias = "";
		}
		
		if (!StringHelper.isNull(alias)) {
			certificate = "C" + coder.base64Encoder(coder.encode(certificate));
		} else {
			certificate = "";
		}
		
		if (!StringHelper.isNull(alias)) {
			password = "P" + coder.base64Encoder(coder.encode(password));
		} else {
			password = "";
		}
		
		authorization.setAlias(alias);
		authorization.setCertificate(certificate);
		authorization.setPassword(password);
		
		return authorizationDao.update(authorization);
	}

	@Override
	public String modify(Authorization authorization) {
		Coder coder = new Coder();
		
		String alias = authorization.getAlias();
		String certificate = authorization.getCertificate();
		String password = authorization.getPassword();
		
		if (!StringHelper.isNull(alias)) {
			alias = "A" + coder.base64Encoder(coder.encode(alias));
		} else {
			alias = "";
		}
		
		if (!StringHelper.isNull(alias)) {
			certificate = "C" + coder.base64Encoder(coder.encode(certificate));
		} else {
			certificate = "";
		}
		
		if (!StringHelper.isNull(alias)) {
			password = "P" + coder.base64Encoder(coder.encode(password));
		} else {
			password = "";
		}
		
		authorization.setAlias(alias);
		authorization.setCertificate(certificate);
		authorization.setPassword(password);
		
		return authorizationDao.update(authorization);
	}

	@Override
	public Authorization query() {
		Authorization authorization = authorizationDao.select();
		
		Coder coder = new Coder();
		
		String alias = authorization.getAlias();
		String certificate = authorization.getCertificate();
		String password = authorization.getPassword();
		
		if (!StringHelper.isNull(alias)) {
			alias = alias.substring(1, alias.length());
			alias = coder.decode(coder.base64Decoder(alias));
		} else {
			alias = "";
		}
		
		if (!StringHelper.isNull(alias)) {
			certificate = certificate.substring(1, certificate.length());
			certificate = coder.decode(coder.base64Decoder(certificate));
		} else {
			certificate = "";
		}
		
		if (!StringHelper.isNull(alias)) {
			password = password.substring(1, password.length());
			password = coder.decode(coder.base64Decoder(password));
		} else {
			password = "";
		}
		
		authorization.setAlias(alias);
		authorization.setCertificate(certificate);
		authorization.setPassword(password);
		
		return authorization;
	}

	@Override
	public String remove() {
		return authorizationDao.delete();
	}
	
}
