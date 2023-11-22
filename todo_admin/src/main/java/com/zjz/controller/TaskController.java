package com.zjz.controller;

import com.zjz.domain.CommonResult;
import com.zjz.domain.Task;
import com.zjz.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    /**
     * 获取当前所有task
     *
     * @return
     */
    @GetMapping
    public CommonResult<?> getAllTasks() {
        return taskService.getAllTasks();
    }

    /**
     * 删除指定task
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public CommonResult<?> delTask(@PathVariable("id") Integer id) {
        return taskService.delTask(id);
    }

    /**
     * 添加Task
     *
     * @param task
     * @return
     */
    @PostMapping
    public CommonResult<?> addTask(@RequestBody Task task) {
        return taskService.addTask(task);
    }

    /**
     * 更新task
     *
     * @param task
     * @return
     */
    @PutMapping
    public CommonResult<?> updateTask(@RequestBody Task task) {

        return taskService.updateTask(task);
    }

}
