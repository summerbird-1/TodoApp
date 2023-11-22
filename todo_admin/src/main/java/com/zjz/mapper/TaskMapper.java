package com.zjz.mapper;

import com.zjz.domain.Task;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TaskMapper {
    List<Task> getAllTasks();

    void delTask(Integer id);

    void addTask(Task task);

    void updateTask(Task task);
}

