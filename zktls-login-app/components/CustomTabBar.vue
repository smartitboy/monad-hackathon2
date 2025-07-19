<template>
  <view class="tabbar">
    <view class="tab-item" :class="{ active: isActive('attestation') }" @click="goAttestation">
      <image :src="'/static/tab_home.svg'" class="tab-icon" :class="{ selected: isActive('attestation') }"
        mode="aspectFit" />
      <text class="tab-text" :class="{ selected: isActive('attestation') }">证明列表</text>
    </view>
    <view class="tab-center" @click="importAttestation">
      <view class="plus-btn">
        <image src="/static/tab_plus.svg" class="plus-icon" mode="aspectFit" />
      </view>
    </view>
    <view class="tab-item" :class="{ active: isActive('record') }" @click="goRecord">
      <image :src="'/static/tab_record.svg'" class="tab-icon" :class="{ selected: isActive('record') }"
        mode="aspectFit" />
      <text class="tab-text" :class="{ selected: isActive('record') }">授权记录</text>
    </view>
    <view v-if="showMask" class="mask" @click="showMask = false"></view>
    <view v-if="showMask" class="popup">
      <button class="scan-btn" @click="scanCode">扫码导入证明</button>
      <button class="scan-btn" @click="scanCodeForLogin">扫码登录</button>
    </view>
    <!-- 新增：选择证明弹窗 -->
    <view v-if="showChooseProofMask" class="mask" @click="showChooseProofMask = false"></view>
    <view v-if="showChooseProofMask" class="popup choose-proof-popup">
      <view style="padding: 30rpx 40rpx; background: #fff; border-radius: 20rpx; min-width: 400rpx;">
        <view style="font-size: 32rpx; font-weight: bold; margin-bottom: 30rpx;">选择证明</view>
        <view v-for="(proof, idx) in proofList" :key="idx" class="choose-proof-item" @click="chooseProof(proof)"
          style="background: #fff; border-radius: 14rpx; box-shadow: 0 2rpx 8rpx rgba(52,120,246,0.08); display: flex; align-items: center; margin-bottom: 18rpx; padding: 18rpx 16rpx; cursor: pointer;">
          <view
            style="width: 48rpx; height: 48rpx; border-radius: 50%; background: #f5f7fa; display: flex; align-items: center; justify-content: center; margin-right: 18rpx;">
            <image :src="getLogo(proof.dataSource)"
              style="width: 32rpx; height: 32rpx; border-radius: 50%; object-fit: contain;" />
          </view>
          <view style="flex: 1; display: flex; flex-direction: column; min-width: 0;">
            <view
              style="font-size: 28rpx; color: #222; font-weight: 600; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;">
              {{ proof.userName || '未命名' }}</view>
            <view style="margin-top: 6rpx;">
              <text
                style="font-size: 22rpx; color: #3478f6; background: #f0f6ff; border-radius: 8rpx; padding: 2rpx 12rpx;">{{
                  proof.dataSource || '未知数据源' }}</text>
            </view>
          </view>
        </view>
        <view style="margin-top: 20rpx; text-align: right;">
          <button
            style="background: #eee; color: #333; border-radius: 30rpx; padding: 10rpx 40rpx; font-size: 26rpx; border: none;"
            @click="showChooseProofMask = false">取消</button>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      showMask: false,
      showChooseProofMask: false, // 控制选择证明弹窗
      scannedClientId: null,      // 扫码得到的 clientId
      proofList: []               // 证明列表，扫码后从本地读取
    }
  },
  methods: {
    isActive(tab) {
      const route = getCurrentPages().slice(-1)[0].route;
      if (tab === 'attestation') return route.includes('attestation');
      if (tab === 'record') return route.includes('record');
      return false;
    },
    goAttestation() {
      uni.reLaunch({ url: '/pages/attestation/attestation' });
    },
    goRecord() {
      uni.reLaunch({ url: '/pages/record/record' });
    },
    importAttestation() {
      this.showMask = true;
    },
    scanCode() {
      this.showMask = false;
      uni.scanCode({
        success: (res) => {
          uni.navigateTo({
            url: '/pages/preview/preview?content=' + encodeURIComponent(res.result)
          });
        },
        fail: () => {
          uni.showToast({ title: '导入失败', icon: 'none' });
        }
      });
    },
    loadProofList() {
      this.proofList = uni.getStorageSync('attestationList') || [];
    },
    scanCodeForLogin() {
      this.showMask = false;
      uni.scanCode({
        success: (res) => {
          this.scannedClientId = res.result;
          this.loadProofList();
          // send status to /api/app/updateStatus
          uni.request({
            url: 'https://api.aiassit.xyz/api/app/updateStatus',
            method: 'POST',
            data: {
              loginId: this.scannedClientId,
              status: 'scanning'
            },
            header: {
              'content-type': 'application/json'
            },
            success: (res) => {
              // 可选：处理返回结果
            },
            fail: (err) => {
              // 可选：处理错误
            }
          });
          this.showChooseProofMask = true;
        },
        fail: () => {
          uni.showToast({ title: '扫码失败', icon: 'none' });
        }
      });
    },
    chooseProof(proof) {
      this.showChooseProofMask = false;
      // 这里可以处理选择证明后的逻辑，比如发起登录请求
      const body = {
        loginId: this.scannedClientId,
        dataSource: proof.dataSource,
        attestation: JSON.parse(proof.content).attestation
      };
      console.log("body:", body);
      uni.request({
        url: 'https://api.aiassit.xyz/api/app/login',
        method: 'POST',
        data: body,
        header: {
          'content-type': 'application/json'
        },
        success: (res) => {
          // 可选：处理返回结果
          uni.showToast({ title: `登录成功`, icon: 'none' });
        },
        fail: (err) => {
          // 可选：处理错误
          uni.showToast({ title: `登录失败`, icon: 'error' });
        }
      });
    },
    getLogo(ds) {
      // 优先显示对应logo，否则显示默认logo
      try {
        if (ds && ds.length > 0) {
          return `/static/${ds}.svg`;
        }
      } catch (e) { }
      return '/static/default.svg';
    }
  }
}
</script>

<style>
.tabbar {
  position: fixed;
  left: 0;
  right: 0;
  bottom: 0;
  height: 110rpx;
  background: #fff;
  display: flex;
  align-items: center;
  justify-content: space-around;
  box-shadow: 0 -2rpx 12rpx rgba(0, 0, 0, 0.04);
  z-index: 100;
}

.tab-item {
  flex: 1;
  text-align: center;
  color: #888;
  font-size: 24rpx;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  position: relative;
}

.tab-item.active {
  color: #3478f6;
}

.tab-icon {
  width: 48rpx;
  height: 48rpx;
  margin-bottom: 4rpx;
  filter: grayscale(1) brightness(0.7);
  transition: filter 0.2s;
}

.tab-icon.selected {
  filter: none;
}

.tab-text {
  margin-top: 6rpx;
  color: #888;
  transition: color 0.2s;
}

.tab-text.selected {
  color: #3478f6;
  font-weight: 600;
}

.tab-center {
  width: 120rpx;
  height: 120rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-top: -40rpx;
  z-index: 101;
}

.plus-btn {
  width: 100rpx;
  height: 100rpx;
  background: #fff;
  border-radius: 50%;
  box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.10);
  display: flex;
  align-items: center;
  justify-content: center;
  border: 2rpx solid #eee;
}

.plus-icon {
  width: 60rpx;
  height: 60rpx;
}

.mask {
  position: fixed;
  left: 0;
  top: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.15);
  z-index: 200;
}

.popup {
  position: fixed;
  left: 0;
  right: 0;
  bottom: 140rpx;
  display: flex;
  justify-content: center;
  z-index: 201;
}

.scan-btn {
  background: #222;
  color: #fff;
  border-radius: 40rpx;
  font-size: 30rpx;
  padding: 24rpx 60rpx;
  border: none;
  font-weight: 600;
}
</style>