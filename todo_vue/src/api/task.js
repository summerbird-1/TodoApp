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