<template>
  <div id="app">
    <div class="header">
      <h1 class="todo-header">Todo</h1>
    </div>

    <div class="task-input">
      <input type="text" v-model="newTask" placeholder="添加新任务..." @keyup.enter="addNewTask">
      <button @click="addNewTask">添加</button>
    </div>

    <div class="task-list">
      <ul>
        <li v-for="task in tasks" :key="task.id" :class="{ completed: task.completed }">
          <span @click="toggleCompletion(task)" class="task-name">{{ task.name }}</span>
          <button @click="deleteTask(task)" class="delete-button">删除</button>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
import { getAllTask, addTask, delTask, updateTask } from '@/api/task';
export default {
  name: 'task',
  data() {
    return {
      newTask: '',
      tasks: [],
    };
  },
  created() {
    console.log('created');
    this.getTasks();
  },
  methods: {
    getTasks() {
      console.log('getTasks');
      getAllTask().then(res => {
        this.tasks = res.data;
      });
    },
    addNewTask() {
      if (this.newTask.trim()) {
        const task = {
          name: this.newTask,
          completed: false,
        };
        addTask(task).then(res => {
          this.getTasks();
          this.newTask = '';
        });
      }
    },
    deleteTask(task) {
      delTask(task.id).then((res) => {
        this.getTasks();

      })
    },
    toggleCompletion(task) {
        task = {
         id:task.id,
         completed:!task.completed
      }
      updateTask(task).then((res) => {
        this.getTasks();
      })
    }
  }
}
</script>
<style>
body {
  font-family: 'Arial', sans-serif;
  background-color: #f4f4f4;
  margin: 0;
}

.header {
  background-color: #3498db;
  padding: 20px;
  text-align: center;
}

.todo-header {
  color: #fff;
  margin: 0;
}

.task-input {
  margin-top: 20px;
  text-align: center;
}

input {
  padding: 8px;
  margin-right: 8px;
  border: 1px solid #3498db;
  border-radius: 4px;
}

button {
  padding: 8px 16px;
  background-color: #3498db;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

button:hover {
  background-color: #2980b9;
}

.task-list {
  margin-top: 20px;
}

ul {
  list-style: none;
  padding: 0;
}

li {
  margin: 10px 0;
  padding: 10px;
  background-color: #fff;
  border: 1px solid #ddd;
  border-radius: 4px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.completed {
  text-decoration: line-through;
  color: #888;
}

.task-name {
  cursor: pointer;
}

.delete-button {
  background-color: #e74c3c;
  color: #fff;
  border: none;
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
}

.delete-button:hover {
  background-color: #c0392b;
}
</style>