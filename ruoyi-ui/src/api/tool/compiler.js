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