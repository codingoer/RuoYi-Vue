import request from '@/utils/request'

// 查询列表
export function listCompiler(query) {
  return request({
    url: '/tool/compiler/list',
    method: 'get',
    params: query
  })
}