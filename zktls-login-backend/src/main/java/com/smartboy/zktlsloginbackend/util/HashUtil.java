package com.smartboy.zktlsloginbackend.util;

import java.math.BigInteger;
import java.util.Arrays;

import org.springframework.stereotype.Component;
import org.web3j.crypto.ECDSASignature;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Sign;
import org.web3j.utils.Numeric;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.digest.DigestUtil;

@Component
public class HashUtil {

    public  String sha256(String plain){
        byte[] bytes = DigestUtil.sha256(plain);
        return Base64.encode(bytes);
    }

    public byte[] sha256(byte[] plain){
        return DigestUtil.sha256(plain);
    }


    public  String recoverAddress(byte[] digest, String signature) {
        Sign.SignatureData signatureData = getSignatureData(signature);
        int header = 0;
        for (byte b : signatureData.getV()) {
            header = (header << 8) + (b & 0xFF);
        }
        if (header < 27 || header > 34) {
            return null;
        }
        int recId = header - 27;
        BigInteger key = Sign.recoverFromSignature(
                recId,
                new ECDSASignature(
                        new BigInteger(1, signatureData.getR()), new BigInteger(1, signatureData.getS())),
                digest);
        if (key == null) {
            return null;
        }
        return ("0x" + Keys.getAddress(key)).trim();
    }

    private  Sign.SignatureData getSignatureData(String signature) {
        byte[] signatureBytes = Numeric.hexStringToByteArray(signature);
        byte v = signatureBytes[64];
        if (v < 27) {
            v += 27;
        }
        byte[] r = (byte[]) Arrays.copyOfRange(signatureBytes, 0, 32);
        byte[] s = (byte[]) Arrays.copyOfRange(signatureBytes, 32, 64);
        return new Sign.SignatureData(v, r, s);
    }
}
