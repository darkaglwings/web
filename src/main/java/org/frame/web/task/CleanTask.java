package org.frame.web.task;

import org.frame.common.io.File;
import org.frame.common.path.Path;
import org.frame.web.constant.IWebConstant;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class CleanTask {
	
	@Scheduled(cron="0 0 03 * * ?")
	public void clean() {
		new File(new Path().web_root() + IWebConstant.PATH_TEMP).delete();
	}

}
