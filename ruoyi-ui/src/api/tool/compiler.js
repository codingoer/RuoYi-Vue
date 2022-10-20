import request from '@/utils/request'

// 查询列表
export function listCompiler(query) {
  return request({
    url: '/tool/compiler/list',
    method: 'get',
    params: query
  })
}

// 新增配置
export function addCompiler(data) {
  return request({
    url: '/tool/compiler/add',
    method: 'post',
    data: data
  })
}

// 查询详细
export function getCompiler(id) {
  return request({
    url: '/tool/compiler/get/' + id,
    method: 'get'
  })
}

// 修改配置
export function updateCompiler(data) {
  return request({
    url: '/tool/compiler/update',
    method: 'post',
    data: data
  })
}

// 状态修改
export function changeStatus(id, status) {
  const data = {
    id,
    status
  }
  return request({
    url: '/tool/compiler/update',
    method: 'post',
    data: data
  })
}