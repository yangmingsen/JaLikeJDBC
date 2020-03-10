package jalikejdbc.log;

import org.slf4j.LoggerFactory;

public interface LogSupport {
   Log log = new Log(LoggerFactory.getLogger(LogSupport.class));
}
