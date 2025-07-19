// 环境变量配置
const isDevelopment = process.env.NODE_ENV === 'development';

// API配置
export const API_CONFIG = {
    // 基础URL - 优先使用环境变量，否则使用默认值
    BASE_URL: process.env.REACT_APP_API_BASE_URL || (isDevelopment
        ? 'https://api.aiassit.xyz'
        : 'https://api.aiassit.xyz'),

    // 登录接口 - 优先使用环境变量，否则使用默认值
    LOGIN_URL: process.env.REACT_APP_API_LOGIN_URL || (isDevelopment
        ? 'https://api.aiassit.xyz/api/login'
        : 'https://api.aiassit.xyz/api/login'),

    // 上传接口 - 优先使用环境变量，否则使用默认值
    UPLOAD_URL: process.env.REACT_APP_API_LOGIN_URL || (isDevelopment
        ? 'https://api.aiassit.xyz/api/uploadAttestation'
        : 'https://api.aiassit.xyz/api/uploadAttestation'),

    // 其他API接口可以在这里添加
    // REGISTER_URL: `${API_CONFIG.BASE_URL}/api/register`,
    // VERIFY_URL: `${API_CONFIG.BASE_URL}/api/verify`,
};

// 数据源配置
export const dataSources = [
    {
        id: 'google',
        name: 'Google',
        logo: 'https://www.svgrepo.com/show/475656/google-color.svg',
        attestationKey: 'google-attestation',
        sourceUserIdKey: "2",
        color: '#4285F4'
    },
    {
        id: 'xiaohongshu',
        name: '小红书',
        logo: 'https://www.xiaohongshu.com/favicon.ico',
        attestationKey: 'xiaohongshu-attestation',
        sourceUserIdKey: "red_id",
        color: '#ff2442'
    },
    {
        id: 'twitter',
        name: 'Twitter',
        logo: null,
        attestationKey: 'twitter-attestation',
        sourceUserIdKey: "screen_name",
        color: '#1da1f2'
    }
];

// 模板ID配置
export const templateIdConfig = {
    xiaohongshu: "93c6e6df-63ab-41af-8cba-f2927c0d2f1c",
    google: "3bad8a55-4415-4bec-9b47-a4c7bbe93518",
    twitter: "2e3160ae-8b1e-45e3-8c59-426366278b9d"
};

// 应用配置
export const APP_CONFIG = {
    APP_NAME: 'ZkTls Login',
    VERSION: '1.0.0',
    ENVIRONMENT: process.env.NODE_ENV || 'development'
};

// 调试信息
console.log('API配置:', API_CONFIG);
console.log('当前环境:', APP_CONFIG.ENVIRONMENT);