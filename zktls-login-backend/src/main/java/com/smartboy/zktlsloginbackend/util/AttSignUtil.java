package com.smartboy.zktlsloginbackend.util;

import com.smartboy.zktlsloginbackend.bean.attestation.AttNetworkRequest;
import com.smartboy.zktlsloginbackend.bean.attestation.AttNetworkResponseResolve;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;
import org.web3j.abi.DefaultFunctionEncoder;
import org.web3j.abi.TypeEncoder;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint64;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Sign;
import org.web3j.utils.Numeric;

import com.smartboy.zktlsloginbackend.bean.attestation.Attestation;

@Component
public class AttSignUtil {

    public String signatureToHex(Sign.SignatureData signature) {
        byte[] r = signature.getR();
        byte[] s = signature.getS();
        byte[] v = signature.getV();

        byte[] signatureBytes = new byte[r.length + s.length + 1];
        System.arraycopy(r, 0, signatureBytes, 0, r.length);
        System.arraycopy(s, 0, signatureBytes, r.length, s.length);
        signatureBytes[signatureBytes.length - 1] = v[0];

        return Numeric.toHexString(signatureBytes);
    }

    public String encodeAttestation(Attestation attestation) {
        List<Type> types = new ArrayList<>();
        types.add(new Address(attestation.getRecipient()));
        types.add(new Bytes32(Numeric.hexStringToByteArray(encodeReq(attestation.getRequest()))));
        types.add(new Bytes32(Numeric.hexStringToByteArray(encodeRsp(attestation.getReponseResolve()))));
        types.add(new Utf8String(attestation.getData()));
        types.add(new Utf8String(attestation.getAttConditions()));
        types.add(new Uint64(attestation.getTimestamp()));
        types.add(new Utf8String(attestation.getAdditionParams()));
        String attestationHash = "0x" + DefaultFunctionEncoder.encodeConstructorPacked(types);
        String encodeHash = Hash.sha3(attestationHash);
        return encodeHash;
    }

    private String encodeRsp(AttNetworkResponseResolve[] resolves) {

        String encodeData = "";

        for (int i = 0; i < resolves.length; i++) {
            AttNetworkResponseResolve resolve = resolves[i];
            List<Type> types = new ArrayList<>();
            types.add(new DynamicBytes(Numeric.hexStringToByteArray(encodeData)));
            types.add(new Utf8String(resolve.getKeyName()));
            types.add(new Utf8String(resolve.getParseType()));
            types.add(new Utf8String(resolve.getParsePath()));
            StringBuilder tempEncodeData = new StringBuilder();
            for (Type parameter : types) {
                String encodedType = TypeEncoder.encodePacked(parameter);
                byte[] rawBytes = Numeric.hexStringToByteArray(encodeData);
                byte[] encodeBytes = Numeric.hexStringToByteArray(encodedType);
                if (parameter instanceof DynamicBytes) {
                    byte[] trimmed = Arrays.copyOf(encodeBytes, rawBytes.length);
                    tempEncodeData.append(Numeric.toHexString(trimmed));
                } else {
                    tempEncodeData.append(encodedType);
                }
            }
            encodeData = tempEncodeData.toString();
        }

        String encodeHash = Hash.sha3(encodeData);
        System.out.println("attNetworkResponse:" + encodeHash);
        return encodeHash;
    }

    private String encodeReq(AttNetworkRequest attNetworkRequest) {
        List<Type> types =
            Arrays.asList(new Utf8String(attNetworkRequest.getUrl()), new Utf8String(attNetworkRequest.getHeader()),
                new Utf8String(attNetworkRequest.getMethod()), new Utf8String(attNetworkRequest.getBody()));
        //        TypeEncoder typeEncoder = new TypeEncoder();
        String encodedData = "0x" + DefaultFunctionEncoder.encodeConstructorPacked(types);

        String encodeHash = Hash.sha3(encodedData);
        //        System.out.println("encodedData:"+encodedData);
        System.out.println("attNetworkRequest:" + encodeHash);
        return encodeHash;
    }
}
