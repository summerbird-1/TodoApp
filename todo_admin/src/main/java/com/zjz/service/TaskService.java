package com.zjz.service;

import com.zjz.domain.CommonResult;
import com.zjz.domain.Task;

public interface TaskService {

    CommonResult<?> getAllTasks();

    CommonResult<?> delTask(Integer id);

    CommonResult<?> addTask(Task task);

    CommonResult<?> updateTask(Task task);
}
