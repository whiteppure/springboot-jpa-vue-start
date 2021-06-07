<template>

    <div class="venus-list-page">
        <div class="wrapper-padding">

            <el-form
                    :inline="true"
                    :model="queryCondition"
                    class="demo-form-inline"
                    size="medium"
            >
                <el-row type="flex" justify="start" :gutter="1">

                    <el-row :span="1">
                        <el-form-item label="用户号码">
                            <el-input clearable v-model="queryCondition.loginName" placeholder="请输入用户号码"></el-input>
                        </el-form-item>
                        <el-form-item label="操作时间">
                            <el-date-picker
                                    v-model="searchOprDate"
                                    type="datetimerange"
                                    placeholder="选择操作日期"
                                    format="yyyy-MM-dd HH:mm:ss"
                                    value-format="yyyyMMddHHmmss"
                                    range-separator="-"
                                    start-placeholder="开始日期"
                                    end-placeholder="结束日期"
                                    @change="oprDateChange"
                                    :picker-options="pickerOptions">
                            </el-date-picker>
                        </el-form-item>
                    </el-row>

                </el-row>
                <!-- 右对齐-->
                <el-row type="flex" justify="end" class="list-btn">
                    <el-button type="primary" @click="searchData" size="medium">查询</el-button>
                    <el-button type="primary" plain @click="reset" size="medium">重置</el-button>
                    <el-button type="success" plain @click="exportExcel" size="medium">导出报表</el-button>
                </el-row>
            </el-form>

        </div>
        <div>
            <!--  列表区域 -->
            <el-card shadow="never" style="border: none; margin: 20px auto;">
                <el-table
                        :v-loading="listLoading"
                        element-loading-text="玩命加载中"
                        :data="tableData"
                        max-height="600px"
                        default-expand-all
                >
                    <el-table-column type="expand" width="50px" inline>
                        <template slot-scope="props">
                            <span>{{ props.row.logcontent }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column type="index" label="序号"/>
                    <el-table-column
                            prop="broswer"
                            label="浏览器类型">
                    </el-table-column>
                    <el-table-column
                            prop="ip"
                            label="IP地址">
                    </el-table-column>
                    <el-table-column
                            prop="loglevel"
                            label="日志级别">
                    </el-table-column>
                    <el-table-column prop="loginname" label="用户号码">
                        <template slot-scope="props">
                            <span>
                                {{((props.row.loginname == null) || (props.row.loginname === ''))? '[未知用户]': props.row.loginname}}
                            </span>
                        </template>
                    </el-table-column>
                    <el-table-column
                            :formatter="formatDate"
                            prop="operatetime"
                            label="操作时间">
                    </el-table-column>
                </el-table>
            </el-card>
            <el-row type="flex" justify="center">
                <el-pagination
                        background
                        @size-change="handleSizeChange"
                        @current-change="handleCurrentChange"
                        :current-page="page.current"
                        :page-size="page.size"
                        :page-sizes="[10, 20, 30]"
                        layout="total, sizes, prev, pager, next, jumper"
                        :total="tableDataTotal">
                </el-pagination>
            </el-row>
        </div>
    </div>

</template>

<script>
    import {getListNoPage, getListByPage} from "@/api/ex-log";
    import {emptyObject, formatDate, formatDateForTab, isEmpty, isNotEmpty} from "@/utils/common";
    import transform from "@/utils/export-excel";


    export default {
        name: "ExceptionLog",
        data() {
            return {
                // 查询操作日期
                searchOprDate: '',
                // 日期组件配置
                pickerOptions: {
                    shortcuts: [{
                        text: '最近一周',
                        onClick(picker) {
                            const end = new Date();
                            const start = new Date();
                            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
                            picker.$emit('pick', [start, end]);
                        }
                    }, {
                        text: '最近一个月',
                        onClick(picker) {
                            const end = new Date();
                            const start = new Date();
                            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
                            picker.$emit('pick', [start, end]);
                        }
                    }, {
                        text: '最近三个月',
                        onClick(picker) {
                            const end = new Date();
                            const start = new Date();
                            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
                            picker.$emit('pick', [start, end]);
                        }
                    }]
                },
                // 列表数据加载
                listLoading: true,
                // 列表数据
                tableData: [],
                // 列表数据总数
                tableDataTotal: 0,
                // 查询分页对象
                page: {
                    current: 1,
                    size: 10,
                    sortWay: 'DESC',
                    sortFile: ['id']
                },
                // 查询 表单条件
                queryCondition: {
                    loginName: null,
                    logContent: null,
                    logLevel: null,
                    timeBegin: null,
                    timeEnd: null
                },
            }
        },
        created() {
            // 加载数据
            this.fetchData()
        },
        methods: {
            // 获取查询列表
            fetchData() {
                this.listLoading = true
                getListByPage(this.page, this.queryCondition).then(response => {
                    this.tableDataTotal = response.totalElements
                    this.tableData = response.content
                    this.page.current = response.number + 1 //oracle 分页 rowNum从0开始要加1
                    this.page.size = response.size
                    this.listLoading = false
                })
            },
            // 格式化表格中的日期
            formatDate(row, column) {
                return formatDateForTab(row, column)
            },
            // 分页事件：当前页改变
            handleCurrentChange(currentPage) {
                this.page.current = currentPage
                this.fetchData()
            },
            // 分页事件：每页数量改变
            handleSizeChange(pageSize) {
                this.page.size = pageSize
                this.page.current = 1
                this.fetchData()
            },
            // 操作日期change事件，处理操作日期
            oprDateChange() {
                // 将日期赋值给查询条件
                if (isNotEmpty(this.searchOprDate)) {
                    this.queryCondition.timeBegin = this.searchOprDate[0];
                    this.queryCondition.timeEnd = this.searchOprDate[1];
                }
            },
            // 条件查询数据；要重置分页条件
            searchData() {
                this.page.current = 1
                this.page.size = 10
                this.fetchData()
            },
            // 重置查询条件
            reset() {
                this.searchOprDate = null
                emptyObject(this.queryCondition)
                this.page.current = 1
                this.page.size = 10
                this.fetchData()
            },
            // 导出excel
            exportExcel() {
                const headData = ['序号', '浏览器类型', 'IP地址', '日志级别', '用户号码', '操作时间', '日志内容']
                const name = '全局异常日志'
                const title = name
                const tableColumn = ['id', 'broswer', 'ip', 'loglevel', 'loginname', 'operatetime', 'logcontent']

                // 获取数据
                getListNoPage(this.queryCondition).then(response => {
                    if (isEmpty(response)) {
                        this.$message.warning('没有数据可导出')
                    }
                    for (let item in response) {
                        response[item].operatetime = formatDate(response[item].operatetime)
                        response[item].loginname = response[item].loginname == null ? '[未知用户]' : response[item].loginname
                    }
                    // 导出excel
                    transform(headData, response, tableColumn, name, title, (result) => {
                        console.log(result)
                    })
                })
            }
        }
    }
</script>

<style scoped>

</style>