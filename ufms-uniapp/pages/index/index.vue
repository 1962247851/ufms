<template>
	<view class="content">
		<view class="text-area fill-height">
			<text class="title">
				UFMS - 用户反馈管理系统
			</text>
		</view>
			<u-row justify="center">
				<u-col span="6">
					<u-button ripple>注册</u-button>
				</u-col>
				<u-col span="6">
					<u-button ripple type="primary">登录</u-button>
				</u-col>
			</u-row>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				title: 'Hello'
			}
		},
		onLoad() {

		},
		methods: {

		}
	}
</script>

<style lang="scss" scoped>
	.content {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		padding: 40rpx;
	}

	.logo {
		height: 200rpx;
		width: 200rpx;
		margin-top: 100rpx;
		margin-left: auto;
		margin-right: auto;
		margin-bottom: 50rpx;
	}

	.text-area {
		display: flex;
		justify-content: center;
	}

	.title {
		font-size: 28rpx;
		color: $u-content-color;
	}

	.button-demo {
		margin-top: 80rpx;
	}
</style>
