import { API_CONFIG, dataSources } from '../config/config';

export async function uploadAttestation({ attId, dataSource, attestation }) {
    const body = {
        attId,
        dataSource,
        attestation
    };
    const response = await fetch(API_CONFIG.UPLOAD_URL, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(body),
    });
    if (!response.ok) {
        throw new Error('上传证明失败');
    }
    return response;
}
