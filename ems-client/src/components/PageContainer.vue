<script setup>
import { defineProps } from 'vue'
defineProps({
  title: {
    required: true,
    type: String
  }
})
</script>

<script>
export default {
  name: 'PageContainer'
}
</script>
<template>
  <el-card class="page-container">
    <!-- 传入el-card插槽 -->
    <template #header>
      <div class="header">
        <span>{{ title }}</span>
        <div class="extra">
          <!-- 自定义插槽 -->
          <slot name="extra"></slot>
        </div>
      </div>
    </template>
    <slot></slot>
  </el-card>
</template>

<style lang="scss" scoped>
.page-container {
  // height: 100%;
  min-height: 100%;
  box-sizing: border-box;
  .header {
    display: flex;
    align-items: center;
    justify-content: space-between;
  }
}
</style>
