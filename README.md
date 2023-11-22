# TodoApp
基于Springboot+Vue2+Mybatis的Todo小应用
## Springboot+Vue2.0开发简易Todo应用，尝试一键部署㊙️

### 一、后端应用

#### 1、创建Springboot工程

这个不写了😏  jdk选8 Springboot选2.x.x版本即可

#### 2、配置pom.xml依赖

```xml
 <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <!--mybatis启动器-->
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>2.2.2</version>
        </dependency>
        <!--mysql驱动-->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>8.0.16</version>
            <scope>runtime</scope>
        </dependency>
    </dependencies>
```

mybatis和mysql要用

3、配置资源文件application.yml

```yml
server:
  port: 8080
#整合Mybatis相关配置
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/mybatis_db?characterEncoding=utf-8&serverTimezone=UTC
    username: root
    password: 123456
    driver-class-name: com.mysql.cj.jdbc.Driver
mybatis:
  mapper-locations: classpath:mapper/*Mapper.xml          #mapper映射文件路径
  type-aliases-package: com.zjz.domain            #配置哪个包下面的类有默认的别名
```

##### 配置项

`url: jdbc:mysql://localhost:3306/mybatis_db?characterEncoding=utf-8&serverTimezone=UTC`

`mybatis_db`更换为自己的数据库名  

`password: 123456`密码填自己的

`type-aliases-package: com.zjz.domai`这个后面会说，填放实体类的包，数据库操作用

`mapper-locations: classpath:mapper/*Mapper.xml`这个是mapper文件夹，在resource文件夹下面创建一个mapper文件夹即可，后面Mybatis文件存放在这里

#### 3、编写接口

##### 创建数据库表

数据库sql文件

```sql
CREATE TABLE tasks (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    completed BOOLEAN
);
```

##### 主程序

com/zjz/TodoAdminApplication.java

```java
@SpringBootApplication
public class TodoAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(TodoAdminApplication.class, args);
    }

}

```

##### 添加实体类task.java

添加包domain，包下添加实体类task

com/zjz/domain/Task.java

```java
@Data
public class Task {
    private Integer id;
    private String name;
    private Boolean completed;
}
```

##### 定义统一返回类CommonResult

一般前端的请求，后端都以json形式传回去，格式大致为

```json
{
    "code": 200,
    "msg" : "成功",
    "data": []
}
```

因此需要一个类来统一下

com/zjz/domain/CommonResult.java

```java
@Data
public class CommonResult<T> implements Serializable {

    private Integer code;
    private String msg;
    private T data;


    public static <T> CommonResult<T> success(T data) {
        CommonResult<T> result = new CommonResult<>();
        result.code = 200;
        result.data = data;
        result.msg = "操作成功";
        return result;
    }

    public static <T> CommonResult<T> error(Integer code, String message) {
        CommonResult<T> result = new CommonResult<>();
        result.code = code;
        result.msg = message;
        return result;
    }
}
```

#### 4、编写getAllTask接口

##### controller层

com/zjz/controller/TaskController.java

```java
@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private TaskService taskService;
    @GetMapping
    public CommonResult<?> getAllTasks(){
        return taskService.getAllTasks();
    }
}
```

这里注入service服务，调用service方法去实现

##### service接口层

com/zjz/service/TaskService

```java
public interface TaskService {

    public CommonResult<?> getAllTasks();
    
}

```

这是一个接口，下面用impl文件去实现接口函数

##### service实现层

com/zjz/service/impl/TaskServiceImpl.java

```java
@Service
public class TaskServiceImpl implements TaskService {
    @Autowired
    private TaskMapper taskMapper;
    @Override
    public CommonResult<?> getAllTasks() {
        List<task> taskList = taskMapper.getAllTasks();
        return CommonResult.success(taskList);
    }
}
```

这里注入mapper，从数据库获取task信息，封装成CommonResult返回

注意@Service注解

##### Mapper接口层

com/zjz/mapper/TaskMapper

```java
@Mapper
public interface TaskMapper {
    List<task> getAllTasks();
}
```

注意@Mapper注解

##### Mapper实现层

这个需要在idea安装Mybatis插件，自行百度

![image-20231117205552929](todo应用编写到部署/image-20231117205552929.png)

点击接口名，Alt+Enter选择生成xml文件

![image-20231117205721047](todo应用编写到部署/image-20231117205721047.png)

mapper自己创建，选择ok

会在mapper文件夹下生成一个名为TaskMapper.xml文件，这个文件允许我们对数据库进行操作

回到mapper接口文件，点击方法名，Alt+Enter生成

![image-20231117205924139](todo应用编写到部署/image-20231117205924139.png)

编写sql语句

```xml
  <select id="getAllTasks" resultType="com.zjz.domain.task">
        select * from tasks
    </select>
```

整个接口就完成了！

#### 5、剩余接口

我们除了获取所有接口以外，还需要有

1. 添加task接口 addTask
2. 删除task接口 delTask
3. 修改task状态接口 updateTask

##### controller层

```java
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

```

##### service接口层

```java
public interface TaskService {

    CommonResult<?> getAllTasks();

    CommonResult<?> delTask(Integer id);

    CommonResult<?> addTask(Task task);

    CommonResult<?> updateTask(Task task);
}
```

##### service实现层

```java
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

```

mapper接口层

```java
@Mapper
public interface TaskMapper {
    List<Task> getAllTasks();

    void delTask(Integer id);

    void addTask(Task task);

    void updateTask(Task task);
}

```

mapper实现层

```xml
<mapper namespace="com.zjz.mapper.TaskMapper">
    <insert id="addTask">
        insert into tasks (name,completed)
        values (#{name},#{completed})
    </insert>
    <update id="updateTask">
        update tasks set completed = #{completed} where id = #{id}
    </update>
    <delete id="delTask">
        delete from tasks where id = #{id}
    </delete>
    <select id="getAllTasks" resultType="com.zjz.domain.Task">
        select * from tasks
    </select>
</mapper>
```

#### 6、跨域配置

为什么？这是chatgpt给出的回答

跨域配置是为了增加网络应用的安全性，防止恶意网站通过浏览器发起对其他域的非法请求。浏览器的同源策略（Same-Origin Policy）限制了页面上的文档或脚本如何与其他域进行交互。同源策略的目的是保护用户的信息安全，防止恶意网站通过用户的浏览器访问其他域上的敏感信息。

以下是一些常见的原因，解释了为什么需要跨域配置：

1. **安全性：** 同源策略是一种安全机制，它限制了从一个源加载的文档或脚本如何与来自另一个源的资源进行交互。跨域配置可以通过明确的设置，允许受信任的域进行访问，从而确保数据和资源只在受信任的环境中共享。
2. **隔离敏感信息：** 跨域配置防止了恶意网站通过用户的浏览器发起对其他域的请求，从而避免了潜在的信息泄漏，尤其是对于需要身份验证的接口或包含敏感信息的资源。
3. **用户隐私：** 防止未经授权的域获取用户的个人信息，保护用户的隐私。
4. **CSRF（Cross-Site Request Forgery）防护：** 跨域请求限制有助于防止CSRF攻击，其中攻击者通过伪造用户的请求来执行未经授权的操作。

在实际应用中，当前端代码部署在一个域，而后端服务部署在另一个域时，就需要进行跨域配置。跨域配置的方式包括使用CORS（Cross-Origin Resource Sharing）头部、JSONP等技术，以确保安全可控的跨域数据传输。

就是说规定可以进行哪些操作get，post ，delete，put这些

所以我们还需要为后端进行跨域配置

添加config包

com/zjz/config/WebMvcConfig.java

```java
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*") // 使用allowedOriginPatterns替代allowedOrigins
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                .allowedHeaders("Authorization", "Cache-Control", "Content-Type")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
```

到此处，后端结束

### 二、前端应用

#### 1、创建Vue2.0工程

这个不说了😏 

#### 2、页面

App.vue

```vue
<template>
  <div id="app">
    <router-view/>
  </div>
</template>

<script>
export default {
  name: 'App',
}
</script>

<style lang="scss">
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

nav {
  padding: 30px;

  a {
    font-weight: bold;
    color: #2c3e50;

    &.router-link-exact-active {
      color: #42b983;
    }
  }
}
</style>

```

views/Task.vue

```vue
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
```

`import {getAllTask,addTask,delTask,updateTask} from "@/api/task"`这有几个接口要实现

#### 3、request封装

首先安装axios 

项目目录进入终端输入`npm i axios`

src/utils/request.js

```js
import axios from 'axios';


const service = axios.create({
  baseURL: '/api',
  timeout: 5000
});

service.interceptors.request.use(
  config => {
    // 在请求发送之前对请求数据进行处理
    // ...

    return config;
  },
  error => {
    // 对请求错误做些什么
    console.log(error);
    return Promise.reject(error);
  }
);

service.interceptors.response.use(
  response => {
    // 对响应数据进行处理
    // ...

    return response.data;
  },
  error => {
    // 对响应错误做些什么
    console.log(error);
    return Promise.reject(error);
  }
);

export default service;

```

#### 4、api接口

src/api/task.js

```js
import request from '@/utils/request'


// 获取所有任务的接口
export function getAllTask() {
  return request({
    url: '/task',
    method: 'get'
  })
}
// 添加task
// 添加任务的接口
export function addTask(data) {
  return request({
    url: '/task', // 接口路径
    method: 'post', // 请求方法
    data: data // 请求参数
  });
}

  // 删除task
export function delTask(id) {

    return request({
      url: '/task/' + id,
      method: 'delete'
    })
  }

// 更新task
export function updateTask(data) {

    return request({
      url: '/task',
      method: 'put',
      data: data
    })
  }
```

#### 5、跨域配置

打开vue.config.js文件

```js
const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    proxy: {
      "/api": {
        target: "http://localhost:8080",
        changeOrigin: true,
        pathRewrite: {
          "^/api": ''
        }
      }
    }
  }
})

```

`target: "http://localhost:8080"`这个端口号根据你自己实际情况来

#### 6、有点问题🤡(已解决)

前端项目终端输入：

`npm run serve`

后端也要启动

浏览器输网址即可

![image-20231122161654585](todo应用编写到部署/image-20231122161654585.png)

#####  ！以上内容已更新😏:up:

### 三、部署

#### 1、在github上建仓库，并将项目同步到仓库

不说了，当建好了😏

2、

