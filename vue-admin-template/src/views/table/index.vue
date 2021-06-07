<template>

  <div class="venus-list-page">
    <div class="wrapper-padding">

      <el-form
              :inline="true"
              :model="formInline"
              class="demo-form-inline"
              size="medium"
      >
        <!--
        start 居前（默认）
        center 居中
        end 居后
        space-between 分布自适应（两边–中间，两边没有空隙）
        around （中间–两边，两边会有空隙）
        -->
        <el-row type="flex" justify="start" :gutter="1">
          <el-row :span="1">
            <el-form-item label="字段2">
              <el-input v-model="formInline.user" placeholder="字段2"></el-input>
            </el-form-item>
            <el-form-item label="字段2">
              <el-input v-model="formInline.user" placeholder="字段2"></el-input>
            </el-form-item>
          </el-row>

        </el-row>
        <!-- 右对齐-->
        <el-row type="flex" justify="end" class="list-btn">
          <el-button type="primary"  @click="" size="medium">查询</el-button>
          <el-button type="primary" plain @click="" size="medium">重置</el-button>
          <el-button type="warning" plain @click="" size="medium">添加</el-button>
        </el-row>
      </el-form>

    </div>
    <div >
      <!--  列表区域 -->
      <el-card  shadow="never" style="border: none; margin: 20px auto;">
        <el-table
                :data="tableData"
        >
          <el-table-column
                  prop="date"
                  label="字段1"
                  width="180">
          </el-table-column>
          <el-table-column
                  prop="name"
                  label="字段2"
                  width="180">
          </el-table-column>
          <el-table-column
                  prop="address"
                  label="字段3">
          </el-table-column>
          <el-table-column
                  prop="date"
                  label="字段4">
          </el-table-column>
          <el-table-column
                  prop="date"
                  label="字段5">
          </el-table-column>
          <el-table-column
                  prop="name"
                  label="字段6">
          </el-table-column>
          <el-table-column prop="solt" label="操作">
            <el-button type="primary" icon="el-icon-edit" circle plain size="medium"></el-button>
            <el-button type="danger" icon="el-icon-delete"  circle plain size="medium"></el-button>
          </el-table-column>
        </el-table>
      </el-card>
      <el-row type="flex" justify="center">
        <el-pagination
                background
                layout="prev, pager, next"
                :total="1000">
        </el-pagination>
      </el-row>
    </div>
  </div>

</template>

<script>
  export default {
    name: "Form",
    data() {
      return {
        title: '表格示例',
        formInline: {
          user: '',
          region: ''
        },
        tableData: [{
          date: '2016-05-02',
          name: '王小虎',
          address: '上海市普陀区金沙江路 1518 弄'
        }, {
          date: '2016-05-04',
          name: '王小虎',
          address: '上海市普陀区金沙江路 1517 弄'
        }, {
          date: '2016-05-01',
          name: '王小虎',
          address: '上海市普陀区金沙江路 1519 弄'
        }, {
          date: '2016-05-03',
          name: '王小虎',
          address: '上海市普陀区金沙江路 1516 弄'
        }]
      }
    }
  }
</script>

<style scoped>

</style>