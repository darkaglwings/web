package org.frame.web.task;

import org.frame.common.security.Certificate;

//@Service
public class CertificateTask {
	
	//@Scheduled(fixedDelay = 1000 * 60 * 60 * 24)
	public void authorization() {
		if (!new Certificate().authorization()) System.exit(0);
	}
	
}
