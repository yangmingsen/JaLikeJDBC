package jalikejdbc.log;

import org.slf4j.Logger;

public class Log {
    private Logger logger;
    private boolean isDebugEnabled;
    public Log(Logger logger) {
        this.logger = logger;
        isDebugEnabled = logger.isDebugEnabled();
    }

    public void debug(String msg) {
        if (isDebugEnabled && logger.isDebugEnabled()) {
            logger.debug(msg);
        }
    }
}
