<template>
  <div>
    <div class="panel">
      <el-table
        :data="timetable"
        :span-method="objectSpanMethod"
        border
        :header-cell-style="{
          background: '#d9e5fd',
          color: 'black',
          fontWeight: 1000
        }"
        :cell-style="tableCellStyle"
      >
        <el-table-column prop="sjd" label="时间段" width="80" align="center">
        </el-table-column>
        <el-table-column prop="jc" label="节次" width="80" align="center">
        </el-table-column>
        <el-table-column prop="mon" label="星期一" align="center">
          <template #default="scope">
            <h4>{{ scope.row.mon.title }}</h4>
            <div v-html="scope.row.mon.content"></div>
          </template>
        </el-table-column>
        <el-table-column prop="tue" label="星期二" align="center">
          <template #default="scope">
            <h4>{{ scope.row.tue.title }}</h4>
            <div v-html="scope.row.tue.content"></div>
          </template>
        </el-table-column>
        <el-table-column prop="wed" label="星期三" align="center">
          <template #default="scope">
            <h4>{{ scope.row.wed.title }}</h4>
            <div v-html="scope.row.wed.content"></div>
          </template>
        </el-table-column>
        <el-table-column prop="thu" label="星期四" align="center">
          <template #default="scope">
            <h4>{{ scope.row.thu.title }}</h4>
            <div v-html="scope.row.thu.content"></div>
          </template>
        </el-table-column>
        <el-table-column prop="fri" label="星期五" align="center">
          <template #default="scope">
            <h4>{{ scope.row.fri.title }}</h4>
            <div v-html="scope.row.fri.content"></div>
          </template>
        </el-table-column>
        <el-table-column prop="sat" label="星期六" align="center">
          <template #default="scope">
            <h4>{{ scope.row.sat.title }}</h4>
            <div v-html="scope.row.sat.content"></div>
          </template>
        </el-table-column>
        <el-table-column prop="sun" label="星期日" align="center">
          <template #default="scope">
            <h4>{{ scope.row.sun.title }}</h4>
            <div v-html="scope.row.sun.content"></div>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>
<script>
export default {
  props: {
    // 下午节次数
    afternoonLength: {
      type: [String, Number],
      default: 4
    },
    // 总节次
    length: {
      type: [String, Number],
      default: 12
    },
    // 课表数据
    events: {
      type: Array,
      default: null
    }
  },
  data() {
    return {
      // 课程表数据
      timetable: [],
      events1: [
        {
          xq: 1, //星期
          title: '形势与政治', //标题
          class: 'sport',
          content: '1-4节' + '/' + '社会学1班' + '/' + '5教-402室',
          start: 1,
          end: 4
        },
        {
          xq: 1,
          title: '形势与政治',
          class: 'leisure',
          content: '9-12节' + '<br>' + '社会学2班' + '<br>' + '5教-401室',
          start: 9,
          end: 12
        },
        {
          xq: 3,
          title: '形势与政治',
          class: 'sport',
          content: '5-6节' + '/' + '社会学2班' + '/' + '1教-401室',
          start: 5,
          end: 6
        },
        {
          xq: 4,
          title: '形势与政治',
          class: 'sport',
          content: '1-2节' + '/' + '社会学1班' + '/' + '5教-402室',
          start: 1,
          end: 2
        },
        {
          xq: 4,
          title: '形势与政治',
          class: 'sport',
          content: '7-8节' + '/' + '社会学1班' + '/' + '5教-402室',
          start: 7,
          end: 8
        },
        {
          xq: 5,
          title: '形势与政治',
          class: 'sport',
          content: '社会学1班' + '/' + '5教-402室',
          start: 3,
          end: 4
        },
        {
          xq: 5,
          title: '形势与政治',
          class: 'sport',
          content: '5-6节' + '/' + '社会学1班' + '/' + '5教-402室',
          start: 5,
          end: 6
        }
      ],
      hoverOrderArr: [],
      weeks: ['mon', 'tue', 'wed', 'thu', 'fri', 'sat', 'sun']
    }
  },
  mounted() {
    this.mergeData()
  },
  watch: {
    events: {
      // eslint-disable-next-line no-unused-vars
      handler(newVal, oldVal) {
        this.mergeData()
      },
      deep: true
    }
  },
  created() {
    this.makeTimetable()
  },
  methods: {
    // 单元格添加背景色
    // eslint-disable-next-line no-unused-vars
    tableCellStyle({ row, column, rowIndex, columnIndex }) {
      if (row[column.property].title !== undefined) {
        return {
          'background-color': 'rgb(24 144 255 / 80%)',
          color: '#fff',
          'border-radius': '10px'
        }
      }
    },
    // 构造课程表完整数据
    makeTimetable() {
      this.timetable = []
      for (let i = 0; i < this.length; i++) {
        let one = {
          sjd: '',
          jc: i + 1,
          mon: {},
          tue: {},
          wed: {},
          thu: {},
          fri: {},
          sat: {},
          sun: {}
        }
        if (i < 4) {
          one.sjd = '上午'
        } else if (i > 3 && i < this.afternoonLength + 4) {
          one.sjd = '下午'
        } else {
          one.sjd = '晚上'
        }
        this.timetable.push(one)
      }
    },
    mergeData() {
      // 合并数据
      if (this.events.length > 0) {
        for (let i = 0; i < this.events.length; i++) {
          // 获取星期几
          let week = this.weeks[this.events[i].xq - 1]
          this.timetable[this.events[i].start - 1][week] = this.events[i]
        }
      }
    },
    // eslint-disable-next-line no-unused-vars
    objectSpanMethod({ row, column, rowIndex, columnIndex }) {
      if (columnIndex === 0) {
        if (rowIndex < 4) {
          if (rowIndex === 0) {
            return {
              rowspan: 4,
              colspan: 1
            }
          } else {
            return {
              rowspan: 0,
              colspan: 0
            }
          }
        } else if (rowIndex > 3 && rowIndex < 4 + this.afternoonLength) {
          if (rowIndex === 4) {
            return {
              rowspan: this.afternoonLength,
              colspan: 1
            }
          } else {
            return {
              rowspan: 0,
              colspan: 0
            }
          }
        } else {
          if (rowIndex === 4 + this.afternoonLength) {
            return {
              rowspan: this.length - 4 - this.afternoonLength,
              colspan: 1
            }
          } else {
            return {
              rowspan: 0,
              colspan: 0
            }
          }
        }
      }
      if (columnIndex === 2) {
        if (row.mon.title !== undefined) {
          return {
            rowspan: row.mon.end - row.mon.start + 1,
            colspan: 1
          }
        } else {
          return {
            rowspan: 1,
            colspan: 1
          }
        }
      }
      if (columnIndex === 3) {
        if (row.tue.title !== undefined) {
          return {
            rowspan: row.tue.end - row.tue.start + 1,
            colspan: 1
          }
        } else {
          return {
            rowspan: 1,
            colspan: 1
          }
        }
      }
      if (columnIndex === 4) {
        if (row.wed.title !== undefined) {
          return {
            rowspan: row.wed.end - row.wed.start + 1,
            colspan: 1
          }
        } else {
          return {
            rowspan: 1,
            colspan: 1
          }
        }
      }
      if (columnIndex === 5) {
        if (row.thu.title !== undefined) {
          return {
            rowspan: row.thu.end - row.thu.start + 1,
            colspan: 1
          }
        } else {
          return {
            rowspan: 1,
            colspan: 1
          }
        }
      }
      if (columnIndex === 6) {
        if (row.fri.title !== undefined) {
          return {
            rowspan: row.fri.end - row.fri.start + 1,
            colspan: 1
          }
        } else {
          return {
            rowspan: 1,
            colspan: 1
          }
        }
      }
      if (columnIndex === 7) {
        if (row.sat.title !== undefined) {
          return {
            rowspan: row.sat.end - row.sat.start + 1,
            colspan: 1
          }
        } else {
          return {
            rowspan: 1,
            colspan: 1
          }
        }
      }
      if (columnIndex === 8) {
        if (row.sun.title !== undefined) {
          return {
            rowspan: row.sun.end - row.sun.start + 1,
            colspan: 1
          }
        } else {
          return {
            rowspan: 1,
            colspan: 1
          }
        }
      }
    }
  }
}
</script>
