package com.BFMe.BFMBuyer.utils;

import android.content.Context;
import android.content.res.AssetManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import javax.crypto.Cipher;
import Decoder.BASE64Encoder;

/**
 * Description: 对于账户密码的加密
 * Copyright  : Copyright (c) 2016
 * Company    : 白富美(北京)网络科技有限公司
 * Author     : 王  可
 * Date       : 2016/7/14 14:04
 */
public class EncryptCode {

    private static File file;

    /**
     * 加密方法
     * source： 源数据
     */
    public static String encrypt(String source) throws Exception {
        // 用证书的公钥加密
        CertificateFactory cff = CertificateFactory.getInstance("X.509");
        FileInputStream fis1 = new FileInputStream(file); // 证书文件
        Certificate cf = cff.generateCertificate(fis1);
        PublicKey pk1 = cf.getPublicKey();           // 得到证书文件携带的公钥
        BASE64Encoder bse = new BASE64Encoder();
        Cipher c1 = Cipher.getInstance("RSA/ECB/PKCS1Padding");      // 定义算法：RSA
        c1.init(Cipher.ENCRYPT_MODE,pk1);
        byte[] msg1 = c1.doFinal(source.getBytes());            // 加密后的数据
        String encode = bse.encode(msg1);
        return encode;
    }

    /**
     * 数据库的拷贝
     */

    public static void copyDb(String certificate,Context ctx) {

        InputStream in = null;
        FileOutputStream out = null;

        try {
            //获取本地目录路径
            File filesDir = ctx.getFilesDir();
            file = new File(filesDir,certificate);
            if (file.exists()) {
                return;
            }
            AssetManager assets = ctx.getAssets();//资产管理器
            in = assets.open(certificate);
            out = new FileOutputStream(file);
            int len;
            byte[] buffer = new byte[1024];
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer,0,len);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
