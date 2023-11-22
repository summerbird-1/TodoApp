package com.zjz.service.impl;

import com.zjz.domain.CommonResult;

import com.zjz.domain.Task;
import com.zjz.mapper.TaskMapper;
import com.zjz.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {
    @Autowired
    private TaskMapper taskMapper;
    @Override
    public CommonResult<?> getAllTasks() {
        List<Task> taskList = taskMapper.getAllTasks();
        return CommonResult.success(taskList);
    }

    @Override
    public CommonResult<?> delTask(Integer id) {
        taskMapper.delTask(id);
        return CommonResult.success("删除成功");
    }

    @Override
    public CommonResult<?> addTask(Task task) {
        taskMapper.addTask(task);
        return CommonResult.success("添加成功");
    }

    @Override
    public CommonResult<?> updateTask(Task task) {
        taskMapper.updateTask(task);
        return CommonResult.success("更新成功");
    }
}
