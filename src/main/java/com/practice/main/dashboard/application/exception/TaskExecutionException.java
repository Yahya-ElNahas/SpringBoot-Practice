package com.practice.main.dashboard.application.exception;

import com.practice.main.common.exception.DomainException;
import lombok.Getter;

@Getter
public class TaskExecutionException extends DomainException {


    public TaskExecutionException(Exception exception) {
        super("exception.task_execution", new Object[]{exception.getMessage()}, "DASHBOARD", "002", "TASK_EXECUTION_ERROR");
    }
}
