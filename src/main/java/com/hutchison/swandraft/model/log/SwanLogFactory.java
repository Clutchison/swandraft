package com.hutchison.swandraft.model.log;

import com.hutchison.swandraft.repository.LogRepository;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Value
public class SwanLogFactory {

    LogRepository repository;
    static SwanLog logger;

    @Autowired
    public SwanLogFactory(LogRepository repository) {
        this.repository = repository;
        logger = logger == null ? new SwanLog(repository) : logger;
    }

    public static SwanLog getLogger() {
        return logger;
    }

    public static void debug(String message) {
        logger.debug(message);
    }
}
