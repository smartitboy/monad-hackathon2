import React, { useState } from 'react';
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faTwitter } from "@fortawesome/free-brands-svg-icons";
import { dataSources } from '../config/config';

export default function DataSourceSelector({ selectedType, onTypeChange, onClickFn }) {
    const [refreshKey, setRefreshKey] = useState(0);

    // 检查哪些数据源已有证明
    const hasAttestation = (attestationKey) => {
        return localStorage.getItem(attestationKey) !== null;
    };

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

    const clearAttestation = (attestationKey) => {
        localStorage.removeItem(attestationKey);
        // 强制重新渲染组件
        setRefreshKey(prev => prev + 1);
        // 通知 Register 页面
        window.dispatchEvent(new CustomEvent('customStorageChange', {
            detail: { key: attestationKey, value: null }
        }));
    };

    return (
        <div className="my-4 flex flex-col gap-2 items-center w-full" key={refreshKey}>
            <div className="w-3/4">
                {dataSources.map((source) => {
                    const hasProof = hasAttestation(source.attestationKey);
                    const isDisabled = hasProof;
                    const sourceUserName = hasProof ? getUserName(source.sourceUserIdKey, source.attestationKey) : null;

                    return (
                        <div key={source.id} className="flex items-start gap-2 mt-2">
                            <label
                                className={`flex items-start gap-2 flex-1 rounded transition ${hasProof ? 'cursor-pointer hover:bg-gray-100' : ''} ${hasProof ? 'opacity-50' : ''}`}
                                onClick={async (e) => {
                                    const attestationKey = source.attestationKey;
                                    const attestation = localStorage.getItem(attestationKey);
                                    onClickFn({
                                        dataSource: attestationKey,
                                        attestation: attestation,
                                    })
                                }}
                            >
                                <input
                                    type="radio"
                                    name="registerType"
                                    value={source.id}
                                    className="mt-1 flex-shrink-0"
                                    style={{ accentColor: source.color }} checked={selectedType === source.id}
                                    onChange={e => onTypeChange(e.target.value)}
                                    disabled={isDisabled}
                                />
                                {source.logo ? (
                                    <span
                                        className="inline-block w-5 h-5 bg-contain bg-center bg-no-repeat flex-shrink-0"
                                        style={{ backgroundImage: `url(${source.logo})` }}
                                    />
                                ) : (
                                    <span className="inline-block w-5 h-5 flex-shrink-0">
                                        <FontAwesomeIcon
                                            icon={faTwitter}
                                            className="w-5 h-5"
                                            style={{ color: source.color }}
                                        />
                                    </span>
                                )}
                                <div className="flex flex-col items-start min-w-0 flex-1">
                                    <span className={isDisabled ? 'line-through' : ''}>
                                        {source.name}
                                    </span>
                                    {hasProof && (
                                        <span className="text-green-600 text-sm break-words">
                                            (已有证明: {sourceUserName})
                                        </span>
                                    )}
                                </div>
                            </label>
                            {hasProof && (
                                <button
                                    onClick={() => clearAttestation(source.attestationKey)}
                                    className="px-2 py-1 text-xs bg-red-500 text-white rounded hover:bg-red-600 transition-colors flex-shrink-0"
                                    title="清除证明"
                                >
                                    清除
                                </button>
                            )}
                        </div>
                    );
                })}
            </div>
        </div>
    );
} 