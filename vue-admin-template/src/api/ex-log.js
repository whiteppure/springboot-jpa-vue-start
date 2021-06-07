import {getReqParams} from "@/utils/common";
import http from "@/utils/http";

/**
 * 分页条件查询
 * @param page 分页对象
 * @param queryCondition 查询条件
 */
export function getListByPage(page, queryCondition) {
    return http.post('/weblog/page',
        getReqParams('query', page, null, null, queryCondition, null))
}


/**
 * 不分页条件查询
 * @param queryCondition 查询条件
 */
export function getListNoPage(queryCondition) {
    return http.post('/weblog/list',
        getReqParams('query', null, null, null, queryCondition, null))
}