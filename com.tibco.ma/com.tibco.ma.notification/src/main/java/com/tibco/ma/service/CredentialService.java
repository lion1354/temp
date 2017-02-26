package com.tibco.ma.service;

import com.tibco.ma.model.Credential;

public interface CredentialService extends BaseService<Credential> {
	
	Credential saveCred(Credential credential)throws Exception;
	
}
