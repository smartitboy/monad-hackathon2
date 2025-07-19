package com.smartboy.zktlsloginbackend;

import java.io.File;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;

public class KeyUtils {
    public static void main(String[] args) {
        String password = "test@test";

        try {
            // save
            String keystoreDirectory = "/Users/xuda/Downloads/";
            String fileName = WalletUtils.generateNewWalletFile(password, new File(keystoreDirectory));
            System.out.println("Keystore file name: " + fileName);

            // load
//            Credentials credentials = WalletUtils.loadCredentials(password, new File(keystoreDirectory + "/" + fileName));
//            Credentials credentials = WalletUtils.loadCredentials(password, new File("/Users/xuda/tmp/tmp.json"));
//            System.out.println("Address: " + credentials.getAddress());
//            String privateKeyHex = Numeric.toHexStringWithPrefix(credentials.getEcKeyPair().getPrivateKey());
//            System.out.println("privatekey:"+privateKeyHex);
//            String privateKey = "";
//            Credentials credentials = Credentials.create(privateKey);
//            String s = WalletUtils.generateWalletFile("testpass", credentials.getEcKeyPair(),
//                new File("/Users/xuda/tmp/keystore"), true);
//            System.out.println(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
