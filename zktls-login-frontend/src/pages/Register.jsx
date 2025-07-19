import React, { useEffect, useState } from 'react';
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faTwitter, faFacebookF } from "@fortawesome/free-brands-svg-icons";
import { primusProofTest } from "../service/attestation";
import { templateIdConfig, dataSources } from "../config/config";
import { config } from "@fortawesome/fontawesome-svg-core";
import DataSourceSelector from "../components/DataSourceSelector";
import AttestationQRCodeModal from "../components/AttestationQRCodeModal";
import { uploadAttestation } from "../service/upload";
import MD5 from 'crypto-js/md5';

export default function Register() {
    const [registerType, setRegisterType] = useState('google');
    const [xiaohongshuAttestation, setXiaohongshuAttestation] = useState(null);
    const [googleAttestation, setGoogleAttestation] = useState(null);
    const [twitterAttestation, setTwitterAttestation] = useState(null);
    const [showQrCode, setShowQrCode] = useState(false);
    const [attId, setAttId] = useState(null);
    const [refresh, setRefresh] = useState(0);
    const [loading, setLoading] = useState(false);


    useEffect(() => {
        // 监听当前页面的自定义事件
        const handleCustomStorageChange = (e) => {
            console.log('Custom storage change:', e.detail.value);
            if (e.detail.key === 'google-attestation') {
                setGoogleAttestation(e.detail.value)
            } else if (e.detail.key === 'twitter-attestation') {
                setTwitterAttestation(e.detail.value)
            } else if (e.detail.key === 'xiaohongshu-attestation') {
                setGoogleAttestation(e.detail.value)
            } else {
                console.log('Not support source:', e.detail.key)
            }
            setRefresh(v => v + 1); // 强制刷新 Register 组件
        };

        window.addEventListener('customStorageChange', handleCustomStorageChange);
        return () => {
            window.removeEventListener('customStorageChange', handleCustomStorageChange);
        };
    }, []);

    const generateAttestation = async () => {
        setLoading(true);
        console.log(config.keys)
        const templateId = templateIdConfig[registerType]
        if (!templateId) {
            alert('templateId is null')
            setLoading(false);
            return;
        }
        await primusProofTest(templateId, (res) => {
            console.log(res);
            const key = `${registerType}-attestation`
            console.log(key)
            localStorage.setItem(key, JSON.stringify(res))

            // 触发自定义事件
            window.dispatchEvent(new CustomEvent('customStorageChange', {
                detail: { key, value: res }
            }));
            setLoading(false);
        })
    };

    const onClickAttFn = async (detail) => {
        console.log(detail);
        const attObj = JSON.parse(detail.attestation)
        const requestId = attObj.recipient + "-" + attObj.timestamp;
        console.log("requestId", requestId);
        await uploadAttestation({
            attId: requestId,
            dataSource: detail.dataSource,
            attestation: attObj
        })
        setAttId(requestId);
        setShowQrCode(true)
    };

    return (
        <div
            className="flex flex-col items-center justify-center min-h-screen py-2 bg-[#e1eaf3] font-montserrat">
            {loading && (
                <div className="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-40">
                    <div className="flex flex-col items-center">
                        <svg className="animate-spin h-12 w-12 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                            <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4"></circle>
                            <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8v8z"></path>
                        </svg>
                        <span className="mt-4 text-white text-lg font-semibold">正在生成证明，请稍候...</span>
                    </div>
                </div>
            )}
            <main
                className="flex flex-col items-center justify-center w-full flex-1 px-20 text-center">
                <div
                    className="flex w-2/3 max-w-4xl bg-white rounded-[10px] shadow-lg shadow-gray-500">
                    {/* 证明生成 */}
                    <div className="w-1/2 p-2 flex flex-col justify-center">
                        <div className="py-10">
                            <h2 className="text-3xl font-extrabold">
                                ZkTls Account Account Generating
                            </h2>
                        </div>


                        {/* 使用封装的DataSourceSelector组件 */}
                        <DataSourceSelector
                            selectedType={registerType}
                            onTypeChange={setRegisterType}
                            onClickFn={onClickAttFn}
                        />

                        <button
                            type="button"
                            className="w-full py-2.5 my-2 bg-[#FE436B] hover:bg-[#ff6e54] text-white text-sm font-bold uppercase rounded-full tracking-widest"
                            onClick={generateAttestation}
                            disabled={
                                !registerType ||
                                !!localStorage.getItem(`${registerType}-attestation`)
                            }
                        >
                            生成账号证明
                        </button>

                        <p className="text-gray-500 my-3">已有证明？<a href="/login"
                            className="text-[#FE436B] hover:underline ml-1">去登录</a>
                        </p>
                    </div>

                    <div
                        className="w-1/2 bg-gradient-to-r from-[#4B79FF] to-[#436BFE] text-white rounded-r-[10px] py-48 px-14 flex flex-col justify-center">
                        <h2 className="text-3xl font-extrabold mb-4">
                            注册，开启链上新体验
                        </h2>
                        <p>
                            只需几步，加入ZKTLS生态，体验安全、隐私、便捷的新一代认证系统！
                        </p>
                    </div>
                </div>
                <AttestationQRCodeModal visible={showQrCode} attId={attId} onClose={() => setShowQrCode(false)} />
            </main>
        </div>
    );
} 