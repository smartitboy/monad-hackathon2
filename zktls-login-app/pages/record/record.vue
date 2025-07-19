<template>
  <view class="container">
    <!-- 登录历史记录区块 -->
    <view v-if="loginRecordList.length > 0" style="margin-bottom: 40rpx;">
      <view style="font-size: 30rpx; font-weight: bold; margin: 0 24rpx 18rpx 24rpx;">登录历史</view>
      <view v-for="(item, idx) in loginRecordList" :key="item.attestationId" class="record-item"
        style="background: #eaf3ff;">
        <view class="record-content">
          <view style="display: flex; align-items: center; margin-bottom: 6rpx;">
            <text style="font-size: 28rpx; color: #222; font-weight: 600; margin-right: 18rpx;">{{ item.userName ||
              '未命名' }}</text>
            <text
              style="font-size: 22rpx; color: #3478f6; background: #f0f6ff; border-radius: 8rpx; padding: 2rpx 12rpx;">{{
                item.dataSource || '未知数据源' }}</text>
          </view>
          <text class="time">{{ formatLoginDate(item.loginDate) }}</text>
        </view>
      </view>
    </view>
    <!-- 原有授权记录区块 -->
    <view v-if="list.length === 0 && loginRecordList.length === 0" class="empty">暂无授权记录</view>
    <view v-for="(item, idx) in list" :key="idx" class="record-item">
      <view class="record-content">
        <text class="record-text">{{ item.content }}</text>
        <text class="time">{{ formatTime(item.time) }}</text>
      </view>
    </view>
    <CustomTabBar />
  </view>
</template>

<script>
import CustomTabBar from '@/components/CustomTabBar.vue';
export default {
  components: {
    CustomTabBar
  },
  data() {
    return {
      list: [],
      loginRecordList: [] // 新增：登录历史
    }
  },
  onShow() {
    this.loadList();
    this.loadLoginRecords();
  },
  methods: {
    loadList() {
      this.list = uni.getStorageSync('recordList') || [];
    },
    async loadLoginRecords() {
      // 获取本地 attestationList
      const attestationList = uni.getStorageSync('attestationList') || [];
      console.log("attestationList", attestationList);
      if (!attestationList.length) {
        this.loginRecordList = [];
        return;
      }
      // 拼接 attestationIds
      console.log(1);
      const attestationIds = attestationList.map(a => {
        try {
          const attestation = JSON.parse(a.content).attestation;
          console.log("attestation", attestation);
          const id = attestation.recipient + "-" + attestation.timestamp;
          console.log("attestationId", id);
          return id;
        } catch (e) {
          console.warn("解析 attestation 失败", a);
          return null;
        }
      }).filter(Boolean).join(',');
      console.log('attestationIds', attestationIds);

      if (!attestationIds) {
        this.loginRecordList = [];
        return;
      }
      // 请求登录历史接口
      console.log('https://api.aiassit.xyz/api/loginRecords?attestationIds=' + attestationIds)
      uni.request({
        url: 'https://api.aiassit.xyz/api/loginRecords?attestationIds=' + attestationIds,
        method: 'GET',
        success: (res) => {
          if (res.data && res.data.rc === 0 && Array.isArray(res.data.result)) {
            console.log(res.data.result)
            this.loginRecordList = res.data.result;
          } else {
            this.loginRecordList = [];
          }
        },
        fail: () => {
          this.loginRecordList = [];
        }
      });
    },
    formatTime(ts) {
      const date = new Date(ts);
      return date.toLocaleString();
    },
    formatLoginDate(dt) {
      if (!dt) return '';
      let safeDt = dt.replace('T', ' ').replace(/\\.[0-9]+$/, '');
      safeDt = safeDt.replace(/-/g, '/');
      const date = new Date(safeDt);
      if (isNaN(date.getTime())) return dt;
      const pad = n => n < 10 ? '0' + n : n;
      return `${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(date.getDate())} ${pad(date.getHours())}:${pad(date.getMinutes())}:${pad(date.getSeconds())}`;
    }
  }
}
</script>

<style>
.container {
  min-height: 100vh;
  background: #fff;
  padding: 40rpx 0 120rpx 0;
}

.empty {
  text-align: center;
  color: #bbb;
  font-size: 30rpx;
  margin-top: 120rpx;
}

.record-item {
  background: #f7f7f7;
  border-radius: 16rpx;
  margin: 18rpx 24rpx;
  padding: 24rpx 18rpx;
  display: flex;
  align-items: center;
}

.record-content {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.record-text {
  font-size: 28rpx;
  color: #222;
  margin-bottom: 6rpx;
  word-break: break-all;
}

.time {
  color: #aaa;
  font-size: 22rpx;
}
</style>