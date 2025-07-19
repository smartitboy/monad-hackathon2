import React, { useState, useEffect } from 'react'
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faTwitter, faFacebookF } from "@fortawesome/free-brands-svg-icons";
import { API_CONFIG, dataSources } from '../config/config';
import { message } from 'antd';
import { QrcodeOutlined } from '@ant-design/icons';
import { Input } from 'antd';
import { v4 as uuidv4 } from 'uuid';
import { QRCodeCanvas } from 'qrcode.react';

export default function Login() {
    const [selectedProof, setSelectedProof] = useState('');
    const [attestationKey, setAttestationKey] = useState('');
    const [isLoading, setIsLoading] = useState(false);
    const [mode, setMode] = useState('local'); // local or qrcode
    const [scanUuid, setScanUuid] = useState('');
    const [scanStatus, setScanStatus] = useState('waiting'); // waiting, success, failed
    const [scanMsg, setScanMsg] = useState('请使用APP扫码登录');
    const [scanAttId, setScanAttId] = useState('');

    // 检查哪些数据源已有证明
    const hasAttestation = (attestationKey) => {
        return localStorage.getItem(attestationKey) !== null;
    };

    // 获取用户名
    const getUserName = (sourceUserIdKey, attestationKey) => {
        console.log("attestationKey:", attestationKey);
        const attestation = localStorage.getItem(attestationKey);
        if (!attestation) return null;

        try {
            const parsedAttestation = JSON.parse(attestation);
            const data = parsedAttestation?.data;

            if (!data || !sourceUserIdKey) return null;

            // 动态从data中取出sourceUserIdKey对应的值
            const userName = JSON.parse(data)[sourceUserIdKey];
            console.log("userName:", userName);
            return userName;
        } catch (error) {
            console.error("Error parsing attestation:", error);
            return null;
        }
    };

    // 获取可用的证明列表
    const getAvailableProofs = () => {
        return dataSources.filter(source => hasAttestation(source.attestationKey));
    };

    const handleLogin = async () => {
        if (!selectedProof) {
            message.warning('请选择一个证明进行登录');
            return;
        }

        setIsLoading(true);
        try {
            const attestation = localStorage.getItem(selectedProof);
            if (!attestation) {
                message.error('证明数据不存在');
                return;
            }

            // 使用配置文件中的API地址
            const response = await fetch(API_CONFIG.LOGIN_URL, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    dataSource: selectedProof,
                    attestation: JSON.parse(attestation)
                })
            });

            if (response.ok) {
                const result = await response.json();
                console.log('登录成功:', result);
                if (result.rc === "-1") {
                    message.error(result.msg);
                    return;
                }
                message.success('登录成功！');
                localStorage.setItem('token', result.result.token);
                // 这里可以添加登录成功后的跳转逻辑
                window.location.href = '/home';
            } else {
                const error = await response.json();
                message.error(`登录失败: ${error.message || '未知错误'}`);
            }
        } catch (error) {
            console.error('登录请求失败:', error);
            message.error('登录请求失败，请检查网络连接');
        } finally {
            setIsLoading(false);
        }
    };

    // 扫码登录逻辑
    const handleScanLogin = async () => {
        if (!scanAttId) {
            message.warning('请输入或扫码证明ID');
            return;
        }
        setIsLoading(true);
        try {
            const url = `${API_CONFIG.BASE_URL}/api/getAttestation?attId=${encodeURIComponent(scanAttId)}`;
            const res = await fetch(url);
            if (!res.ok) {
                message.error('未找到该证明');
                return;
            }
            const data = await res.json();
            if (!data.result || !data.result.attestation) {
                message.error('证明数据无效');
                return;
            }
            // 直接用证明登录
            const response = await fetch(API_CONFIG.LOGIN_URL, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    dataSource: data.result.dataSource,
                    attestation: data.result.attestation
                })
            });
            if (response.ok) {
                const result = await response.json();
                if (result.rc === "-1") {
                    message.error(result.msg);
                    return;
                }
                message.success('扫码登录成功！');
                localStorage.setItem('token', result.result.token);
                window.location.href = '/home';
            } else {
                const error = await response.json();
                message.error(`登录失败: ${error.message || '未知错误'}`);
            }
        } catch (e) {
            message.error('扫码登录失败: ' + e.message);
        } finally {
            setIsLoading(false);
        }
    };

    // 切换到扫码登录时生成uuid
    useEffect(() => {
        if (mode === 'qrcode') {
            const uuid = uuidv4();
            setScanUuid(uuid);
            setScanStatus('waiting');
            setScanMsg('请使用APP扫码登录');
        }
    }, [mode]);

    // 轮询扫码登录状态
    useEffect(() => {
        if (mode !== 'qrcode' || !scanUuid) return;
        let timer = null;
        const poll = async () => {
            try {
                const url = `https://api.aiassit.xyz/api/app/getStatus?loginId=${encodeURIComponent(scanUuid)}`;
                const res = await fetch(url);
                if (res.ok) {
                    const data = await res.json();
                    if (data.result?.status === 'success') {
                        setScanStatus('success');
                        localStorage.setItem('token', data.result.token)
                        setScanMsg('扫码登录成功，正在跳转...');
                        setTimeout(() => {
                            window.location.href = '/home';
                        }, 1000);
                        return;
                    } else if (data.result?.status === 'scanning') {
                        setScanStatus('scanning');
                        setScanMsg('请在APP上确认');
                    } else if (data.result?.status === 'failed') {
                        setScanStatus('failed');
                        setScanMsg('扫码登录失败，请刷新二维码重试');
                        return;
                    }
                }
            } catch { }
            timer = setTimeout(poll, 2000);
        };
        poll();
        return () => { if (timer) clearTimeout(timer); };
    }, [mode, scanUuid]);

    const availableProofs = getAvailableProofs();

    return (
        <div className="flex flex-col items-center justify-center min-h-screen py-2 bg-[#f3e1e3] font-montserrat">
            {/* Primus 插件提示 */}
            <div className="w-full bg-red-600 text-white text-base font-semibold text-center py-2 mb-4 shadow-lg z-50">
                请先确保已安装Primus 插件  <a href="https://chromewebstore.google.com/detail/primus/oeiomhmbaapihbilkfkhmlajkeegnjhe">&gt;&gt;点此安装&lt;&lt;</a>
            </div>
            <main className="flex flex-col items-center justify-center w-full flex-1 px-20 text-center">
                <div className="flex w-2/3 max-w-4xl bg-white rounded-[10px] shadow-lg shadow-gray-500">
                    <div className="w-1/2 p-2">
                        <div className="py-10 flex flex-col items-center">
                            <h2 className="text-3xl font-extrabold mb-2">
                                ZkTls Login
                            </h2>
                            <div className="flex gap-4 mt-2">
                                <button
                                    className={`px-4 py-1 rounded-full text-sm font-bold ${mode === 'local' ? 'bg-[#FE436B] text-white' : 'bg-gray-200 text-gray-700'}`}
                                    onClick={() => setMode('local')}
                                >
                                    本地证明登录
                                </button>
                                <button
                                    className={`px-4 py-1 rounded-full text-sm font-bold ${mode === 'qrcode' ? 'bg-[#4B79FF] text-white' : 'bg-gray-200 text-gray-700'}`}
                                    onClick={() => setMode('qrcode')}
                                >
                                    <QrcodeOutlined className="mr-1" />扫码登录
                                </button>
                            </div>
                        </div>
                        {mode === 'local' ? (
                            <>
                                {availableProofs.length === 0 ? (
                                    <div className="text-center py-8">
                                        <p className="text-gray-500 mb-4">暂无可用证明</p>
                                        <a
                                            href="/register"
                                            className="inline-block bg-[#FE436B] hover:bg-[#ff6e54] text-white px-6 py-2 rounded-full text-sm font-bold uppercase tracking-widest transition-colors"
                                        >
                                            去生成证明
                                        </a>
                                    </div>
                                ) : (
                                    <>
                                        <div className="mb-6">
                                            <h3 className="text-lg font-semibold text-gray-700 mb-4">选择登录证明</h3>
                                            <div className="space-y-3">
                                                {availableProofs.map((source) => {
                                                    const userName = getUserName(source.sourceUserIdKey, source.attestationKey);
                                                    return (
                                                        <label
                                                            key={source.id}
                                                            className="flex items-center gap-3 cursor-pointer p-3 border border-gray-200 rounded-lg hover:bg-gray-50 transition-colors"
                                                        >
                                                            <input
                                                                type="radio"
                                                                name="loginProof"
                                                                value={source.attestationKey}
                                                                checked={selectedProof === source.attestationKey}
                                                                onChange={(e) => {
                                                                    setSelectedProof(e.target.value);
                                                                    setAttestationKey(source.attestationKey);
                                                                }}
                                                                className="accent-[#FE436B]"
                                                            />
                                                            {source.logo ? (
                                                                <span
                                                                    className="inline-block w-6 h-6 bg-contain bg-center bg-no-repeat"
                                                                    style={{ backgroundImage: `url(${source.logo})` }}
                                                                />
                                                            ) : (
                                                                <span className="inline-block w-6 h-6">
                                                                    <FontAwesomeIcon
                                                                        icon={faTwitter}
                                                                        className="w-6 h-6"
                                                                        style={{ color: source.color }}
                                                                    />
                                                                </span>
                                                            )}
                                                            <div className="flex flex-col items-start flex-1">
                                                                <span className="font-medium">{source.name}</span>
                                                                <span className="text-sm text-gray-500">
                                                                    已有证明: {userName || '未知用户'}
                                                                </span>
                                                            </div>
                                                        </label>
                                                    );
                                                })}
                                            </div>
                                        </div>

                                        <div className="flex flex-col items-center">
                                            <div>
                                                <button
                                                    className='w-full py-2.5 my-4 w-36 bg-[#FF4B2B] hover:bg-[#ff6e54] text-white text-sm font-bold uppercase rounded-full tracking-widest disabled:opacity-50 disabled:cursor-not-allowed transition-colors'
                                                    onClick={handleLogin}
                                                    disabled={!selectedProof || isLoading}
                                                >
                                                    {isLoading ? '登录中...' : 'Log In'}
                                                </button>
                                            </div>
                                        </div>
                                    </>
                                )}
                            </>
                        ) : (
                            // 扫码登录模式
                            <div className="flex flex-col items-center mt-8 w-full">
                                {scanUuid && (
                                    <div className="relative flex flex-col items-center">
                                        <div className="mb-2 text-base font-semibold text-gray-700">扫码登录</div>
                                        <div className="relative">
                                            <QRCodeCanvas value={scanUuid} size={200} />
                                            {(scanStatus !== 'waiting') && (
                                                <div className="absolute inset-0 flex items-center justify-center bg-black bg-opacity-60 rounded-lg">
                                                    <span className="text-white text-lg font-bold text-center px-4 break-words">{scanMsg}</span>
                                                </div>
                                            )}
                                        </div>
                                    </div>
                                )}
                            </div>
                        )}
                        <p className="text-gray-500 my-3">
                            没有证明？<a href="/register" className="text-[#FE436B] hover:underline ml-1">去生成</a>
                        </p>
                    </div>
                    {/* RIGHT SIDE BEGIN ============== */}
                    <div className="w-1/2 bg-gradient-to-r from-[#FE454A] to-[#FE436B] text-white rounded-r-[10px] py-48 px-14">
                        <h2 className="text-3xl font-extrabold mb-4">
                            安全 隐私 便捷 高效
                        </h2>
                        <p>
                            新一代用户登录认证系统——使用ZKTLS技术，实现任一社交账号登录！
                        </p>
                    </div>
                </div>
            </main>
        </div>
    )
}