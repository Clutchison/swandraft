package com.hutchison.swandraft.model.log;

import com.hutchison.swandraft.model.entity.LogEntity;
import com.hutchison.swandraft.repository.LogRepository;
import lombok.Value;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;

@Value
@Log4j2
public class SwanLog {
    LogRepository repository;

    public SwanLog(LogRepository repository) {
        this.repository = repository;
    }

    public void all(String message) {
        log.log(Level.ALL, message);
        save(message, LogEntity.LogType.ALL);
    }

    public void debug(String message) {
        log.log(Level.DEBUG, message);
        save(message, LogEntity.LogType.DEBUG);
    }

    public void info(String message) {
        log.log(Level.INFO, message);
        save(message, LogEntity.LogType.INFO);
    }

    public void warn(String message) {
        log.log(Level.WARN, message);
        save(message, LogEntity.LogType.WARN);
    }

    public void error(String message) {
        log.log(Level.ERROR, message);
        save(message, LogEntity.LogType.ERROR);
    }

    public void fatal(String message) {
        log.log(Level.FATAL, message);
        save(message, LogEntity.LogType.FATAL);
    }

    public void trace(String message) {
        log.log(Level.TRACE, message);
        save(message, LogEntity.LogType.TRACE);
    }

    private void save(String message, LogEntity.LogType type) {
        repository.save(new LogEntity(null, message, type));
    }
}
