<template>
	<view class="container">
		<view class="title">导入内容预览</view>
		<view class="content-box">{{ displayContent }}</view>
		<button class="confirm-btn" @click="confirm">确认导入</button>
		<button class="cancel-btn" @click="cancel">取消</button>
	</view>
</template>

<script>
export default {
	data() {
		return {
			content: '',
			displayContent: '加载中...'
		}
	},
	onLoad(query) {
		this.content = decodeURIComponent(query.content || '');
		if (/^https?:\/\//.test(this.content)) {
			// 是链接，尝试获取内容
			console.log('content', this.content);
			uni.request({
				url: this.content,
				method: 'GET',
				success: (res) => {
					console.log('res', res.data);
					this.displayContent = JSON.stringify(res.data.result);
				},
				fail: () => {
					this.displayContent = '链接内容获取失败';
				}
			});
		} else {
			this.displayContent = this.content;
		}
	},
	methods: {
		confirm() {
			let list = uni.getStorageSync('attestationList') || [];
			// 按 dataSource 去重，存在则覆盖，否则插入
			const data = JSON.parse(this.displayContent);
			console.log("data", data);
			const dataSource = data.dataSource;
			const attestation = data.attestation;
			console.log("dataSource", dataSource);
			console.log("attestation", attestation);
			if (dataSource) {
				const idx = list.findIndex(item => item.dataSource === dataSource);
				let userName;
				if (dataSource === "twitter-attestation") {
					userName = JSON.parse(attestation.data).screen_name
				} else if (dataSource === "google-attestation") {
					console.log("google.data", attestation.data);
					userName = JSON.parse(attestation.data)["2"]
				} else if (dataSource === "xiaohongshu-attestation") {
					userName = JSON.parse(attestation.data).red_id
				} else { }
				console.log("userName:", userName);
				const newItem = {
					content: this.displayContent,
					time: Date.now(),
					dataSource: dataSource,
					userName: userName
				};
				if (idx !== -1) {
					list[idx] = newItem; // 覆盖已有项
				} else {
					list.unshift(newItem); // 新增
				}
				uni.setStorageSync('attestationList', list);
				uni.showToast({
					title: '导入成功',
					icon: 'success'
				});
				setTimeout(() => {
					uni.reLaunch({
						url: '/pages/attestation/attestation'
					});
				}, 500);
			} else {
				uni.setStorageSync('attestationList', list);
				uni.showToast({
					title: '导入失败',
					icon: 'error'
				});
			}

		},
		cancel() {
			uni.navigateBack();
		}
	}
}
</script>

<style>
.container {
	min-height: 100vh;
	background: #fff;
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: center;
	padding: 60rpx 30rpx 120rpx 30rpx;
}

.title {
	font-size: 34rpx;
	font-weight: bold;
	color: #222;
	margin-bottom: 40rpx;
}

.content-box {
	width: 100%;
	min-height: 120rpx;
	background: #f7f7f7;
	border-radius: 16rpx;
	padding: 32rpx 24rpx;
	font-size: 28rpx;
	color: #333;
	margin-bottom: 60rpx;
	word-break: break-all;
}

.confirm-btn {
	width: 80vw;
	max-width: 400rpx;
	padding: 28rpx 0;
	font-size: 30rpx;
	border-radius: 50rpx;
	background: #3478f6;
	color: #fff;
	font-weight: 600;
	border: none;
	margin-bottom: 24rpx;
}

.cancel-btn {
	width: 80vw;
	max-width: 400rpx;
	padding: 28rpx 0;
	font-size: 30rpx;
	border-radius: 50rpx;
	background: #eee;
	color: #888;
	font-weight: 600;
	border: none;
}
</style>