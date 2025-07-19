<template>
	<view class="container">
		<view v-if="list.length === 0" class="empty">暂无证明</view>
		<view v-for="(item, idx) in list" :key="idx" class="attestation-item" @click="viewDetail(item)">
			<view class="attestation-logo-box">
				<image :src="getLogo(item.dataSource)" class="ds-logo" />
			</view>
			<view class="attestation-content">
				<view class="attestation-row">
					<text class="attestation-title">{{ item.userName || '未命名' }}</text>
					<button class="del-btn" size="mini" @click.stop="deleteItem(idx)">删除</button>
				</view>
				<view class="attestation-row">
					<text class="attestation-ds">{{ item.dataSource || '未知数据源' }}</text>
				</view>
				<!-- <view class="attestation-row">
					<text class="attestation-text">{{ item.content }}</text>
				</view> -->
				<view class="attestation-row time-row">
					<text class="time">{{ formatTime(item.time) }}</text>
				</view>
			</view>
		</view>
		<view v-if="showDetail" class="popup-mask" @click="closeDetail">
			<view class="popup">
				<view class="popup-title">证明详情</view>
				<view class="popup-content">{{ detail.content }}</view>
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
			detail: {},
			showDetail: false
		}
	},
	onShow() {
		this.loadList();
	},
	methods: {
		loadList() {
			this.list = uni.getStorageSync('attestationList') || [];
		},
		deleteItem(idx) {
			this.list.splice(idx, 1);
			uni.setStorageSync('attestationList', this.list);
			this.$forceUpdate();
		},
		viewDetail(item) {
			this.detail = item;
			this.showDetail = true;
		},
		closeDetail() {
			this.showDetail = false;
		},
		formatTime(ts) {
			const date = new Date(ts);
			const pad = n => n < 10 ? '0' + n : n;
			return `${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(date.getDate())} ${pad(date.getHours())}:${pad(date.getMinutes())}:${pad(date.getSeconds())}`;
		},
		getLogo(ds) {
			// 优先显示对应logo，否则显示默认logo
			try {
				console.log(ds);
				if (ds && ds.length > 0) {
					return `/static/${ds}.svg`;
				}
			} catch (e) { }
			return '/static/default.svg';
		},
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

.attestation-item {
	background: #fff;
	border-radius: 18rpx;
	margin: 18rpx 24rpx;
	box-shadow: 0 4rpx 16rpx rgba(52, 120, 246, 0.10);
	padding: 24rpx 18rpx;
	display: flex;
	align-items: flex-start;
	transition: box-shadow 0.2s, background 0.2s;
	position: relative;
	cursor: pointer;
}

.attestation-item:active {
	background: #f0f6ff;
	box-shadow: 0 8rpx 32rpx rgba(52, 120, 246, 0.16);
}

.attestation-logo-box {
	width: 64rpx;
	height: 64rpx;
	border-radius: 50%;
	background: #f5f7fa;
	display: flex;
	align-items: center;
	justify-content: center;
	margin-right: 24rpx;
	box-shadow: 0 2rpx 8rpx rgba(52, 120, 246, 0.06);
}

.ds-logo {
	width: 40rpx;
	height: 40rpx;
	object-fit: contain;
	border-radius: 50%;
}

.attestation-content {
	flex: 1;
	display: flex;
	flex-direction: column;
	min-width: 0;
}

.attestation-row {
	display: flex;
	align-items: center;
	margin-bottom: 8rpx;
}

.attestation-title {
	font-size: 30rpx;
	color: #222;
	font-weight: 600;
	flex: 1;
	overflow: hidden;
	text-overflow: ellipsis;
	white-space: nowrap;
}

.attestation-ds {
	font-size: 24rpx;
	color: #3478f6;
	background: #f0f6ff;
	border-radius: 8rpx;
	padding: 2rpx 12rpx;
	margin-bottom: 2rpx;
}

.attestation-text {
	font-size: 26rpx;
	color: #333;
	word-break: break-all;
	line-height: 1.6;
	flex: 1;
}

.time-row {
	justify-content: flex-end;
	margin-bottom: 0;
}

.time {
	color: #aaa;
	font-size: 22rpx;
}

.del-btn {
	background: #eee;
	color: #888;
	border: none;
	border-radius: 24rpx;
	font-size: 24rpx;
	padding: 0 20rpx;
	height: 48rpx;
	min-width: 60rpx;
	margin-left: 18rpx;
}

.popup-mask {
	position: fixed;
	left: 0;
	top: 0;
	right: 0;
	bottom: 0;
	background: rgba(0, 0, 0, 0.15);
	display: flex;
	align-items: center;
	justify-content: center;
	z-index: 99;
}

.popup {
	background: #fff;
	border-radius: 18rpx;
	min-width: 400rpx;
	max-width: 90vw;
	padding: 40rpx 30rpx;
	box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.08);
}

.popup-title {
	font-size: 28rpx;
	font-weight: bold;
	color: #222;
	margin-bottom: 18rpx;
}

.popup-content {
	font-size: 26rpx;
	color: #333;
	word-break: break-all;
}
</style>