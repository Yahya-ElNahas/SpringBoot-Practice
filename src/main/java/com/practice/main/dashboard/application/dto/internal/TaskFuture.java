package com.practice.main.dashboard.application.dto.internal;

import com.practice.main.dashboard.application.dto.request.TaskRequest;
import com.practice.main.dashboard.application.dto.response.TaskResponse;

import java.util.concurrent.Future;

public record TaskFuture(
        Future<TaskResponse> future,
        TaskRequest taskRequest
) {}