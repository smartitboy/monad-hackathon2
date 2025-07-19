import React from "react";
import { Modal } from "antd";
import { QRCodeCanvas } from "qrcode.react";

export default function AttestationQRCodeModal({ visible, onClose, attId, baseUrl }) {
    if (!attId) return null;
    const url = `https://api.aiassit.xyz/api/getAttestation?attId=${attId}`;
    return (
        <Modal
            title="导入证明到APP"
            open={visible}
            onCancel={onClose}
            footer={null}
            centered
        >
            <div className="flex flex-col items-center justify-center py-4">
                <QRCodeCanvas value={url} size={200} />
                <div className="mt-4 break-all text-xs text-gray-500">{url}</div>
            </div>
          <div className="flex flex-col items-center justify-center mt-4">
            <a
                href="/zktls-login.apk"
                className="inline-block px-4 py-2 bg-blue-400 text-white font-semibold text-base rounded-md hover:bg-blue-500 transition duration-300"                >
              下载APP
            </a>
          </div>
        </Modal>
    );
} 