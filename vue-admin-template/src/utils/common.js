import {Message} from "element-ui";


/**
 * 判断是否为空
 * @param {object} object
 * @returns {boolean} isEmpty
 */
export function isEmpty(object) {
    if (object instanceof Object) {
        return '{}' === JSON.stringify(object)
    }
    if (object instanceof Array) {
        return object.length === 0
    }
    return isExist(object)
}

/**
 * 判断是否不为空
 * @param {object} object
 * @returns {boolean} isNotEmpty
 */
export function isNotEmpty(object) {
    return !isEmpty(object)
}

/**
 * 判断该对象是否存在
 * @param {object} object
 * @returns {boolean} true 不存在 false 存在
 */
export function isExist(object) {
    return object === undefined || object === '' || object === null || object === 'null'
}

/**
 * 判断该对象是否不存在
 * @param {object} object
 * @returns {boolean} true 存在 false 不存在
 */
export function isNotExist(object) {
    return !isExist(object)
}

/**
 * 数组排序
 * @param arr {array}
 */
export function sortArray(arr) {
    return arr.sort((param1, param2) => {
        return param1 - param2
    })
}

/**
 * 比较两个数组是否相同
 * @param arr1 {array}
 * @param arr2 {array}
 * @returns {boolean}
 */
export function arrayIsEqual(arr1, arr2) {
    if (arr1 === arr2) {
        // 地址相同返回true
        return true
    } else {
        // 长度是否相同
        // 不相同在去遍历数组中每一个元素
        if (arr1.length !== arr2.length) {
            return false
        } else {
            arr1 = sortArray(arr1)
            arr2 = sortArray(arr2)
            for (const i in arr1) {
                if (arr1[i] !== arr2[i]) {
                    console.log(arr1[i])
                    console.log(arr2[i])
                    return false
                }
            }
            return true
        }
    }
}

/**
 * 日期时间转换
 * @param param  日期格式： Wed May 19 2021 00:00:00 GMT+0800 (中国标准时间)
 * @return {string} Wed May 19 2021 00:00:00 GMT+0800 (中国标准时间) ==> yyyyMMddHHmmss
 */
export function timeCover(param) {
    if (isExist(param)) return
    let date = new Date(param);

    return date.getFullYear() + '' +
        p((date.getMonth() + 1)) + '' +
        p(date.getDate()) + '' +
        p(date.getHours()) + '' +
        p(date.getMinutes()) + '' +
        p(date.getSeconds());
}

/**
 * 日期转换
 * @param param  日期格式： Wed May 19 2021 00:00:00 GMT+0800 (中国标准时间)
 * @return {string} Wed May 19 2021 00:00:00 GMT+0800 (中国标准时间) ==> yyyyMMddHHmmss
 */
export function dateCover(param) {
    if (isExist(param)) return
    let date = new Date(param);

    return date.getFullYear() + '' +
        p((date.getMonth() + 1)) + '' +
        p(date.getDate());
}

/**
 * 不够10添加0
 * @param s
 * @return {*}
 */
function p(s) {
    return s < 10 ? '0' + s : s
}

/**
 * 将对象中的属性全部重置为null
 * @param object 重置对象
 */
export function emptyObject(object){
    if (isEmpty(object)) return
    for(let key in object){
        object[key]  = null
    }
}

/**
 * 格式化表格中的日期
 * @param row 行
 * @param column 列
 * @returns {string} 格式化之后的日期
 */
export function formatDateForTab(row, column) {
    // 获取单元格数据
    let data = row[column.property]
    if (isEmpty(data)) {
        return ''
    }
    return formatDate(data)
}

/**
 * 格式化日期
 * @param timeStamp 格式：yyyyMMdd、yyyyMMddHHmmss
 */
export function formatDate(timeStamp) {
    if (isEmpty(timeStamp)) return

    const spilt = '-'
    const colon = ':'
    let formatDate;

    switch (timeStamp.length) {
        // yyyyMMdd -> yyyy-MM-dd
        case 8:
            formatDate = timeStamp.substring(0, 4) + spilt +
                timeStamp.substring(4, 6) + spilt +
                timeStamp.substring(6, 8)
            break;
        //yyyyMMddHHmmss yyyy-MM-dd HH:mm:ss
        case 14:
            formatDate = timeStamp.substring(0, 4) + spilt +
                timeStamp.substring(4, 6) + spilt +
                timeStamp.substring(6, 8) + ' ' +
                timeStamp.substring(8, 10) + colon +
                timeStamp.substring(10, 12) + colon +
                timeStamp.substring(12, 14)
            break;
        default :
            formatDate = timeStamp;
    }
    return formatDate;
}

/**
 * 去除数组中指定元素
 * @param sourceArray {array}
 * @param filterArray {array}
 * @return sourceArray {array}
 */
export function arrayIsFilter(sourceArray, filterArray) {
    for (const j in filterArray) {
        for (const i in sourceArray) {
            if (sourceArray[i] === filterArray[j]) {
                sourceArray.splice(i, 1)
            }
        }
    }
    return sourceArray
}

/**
 * 获取公共请求参数
 */
export function getReqParams(oprType, page, entity, primaryKey, queryCondition, recv) {

    // set param
    entity = isEmpty(entity) ? {} : entity
    primaryKey = isEmpty(primaryKey) ? null : primaryKey
    queryCondition = isEmpty(queryCondition) ? {} : queryCondition
    recv = isEmpty(recv) ? null : recv

    switch (oprType) {
        case "query":
            if (isEmpty(page)) {
                // 默认分页对象
                page = {
                    current: 1,
                    size: 10,
                    sortWay: 'DESC',
                    sortFile: ['id']
                }
            }
            break;
        case "create":
            if (isEmpty(entity)) {
                Message.error("添加操作，添加数据不能为空！")
            }
            break;
        case "delete":
            if (isEmpty(primaryKey)) {
                Message.error("删除操作，主键不能为空！")
            }
            break;
        case "update":
            if (isEmpty(primaryKey) || isEmpty(entity)) {
                Message.error("修改操作，主键和修改数据不能为空！")
            }
            break;
        default :
            Message.error("操作类型不能识别：" + oprType)
            return;
    }
    // 请求参数
    return {
        'current': page.current,
        'size': page.size,
        'sortWay': page.sortWay,
        'sortFile': page.sortFile,
        'entity': entity,
        'primaryKey': primaryKey,
        'queryCondition': queryCondition,
        'recv': recv
    }

}
