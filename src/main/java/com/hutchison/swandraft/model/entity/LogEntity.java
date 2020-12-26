package com.hutchison.swandraft.model.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "audit_log")
@Table(name = "audit_log")
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false, name = "log_id")
    Long logId;

    @Column(unique = false, nullable = false, name = "message")
    String message;

    @Column(name = "level", nullable = false)
    LogType type;

    public enum LogType {
        ALL, DEBUG, INFO, WARN, ERROR, FATAL, TRACE;
    }
}
