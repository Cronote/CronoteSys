/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cronoteSys.util;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bruno
 */
public class GenHash {

    public String hashIt(String sPasswd) {
        String sFinalPasswd = "";
        MessageDigest msgDigest;
        try {
            msgDigest = MessageDigest.getInstance("md5");
            msgDigest.reset();
            msgDigest.update(StandardCharsets.UTF_8.encode(sPasswd));
            sFinalPasswd = String.format("%032x", new BigInteger(1, msgDigest.digest()));
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(GenHash.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sFinalPasswd;

    }
}
