/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cronoteSys.util;

import java.util.Random;

/**
 *
 * @author bruno
 */
public class GenCode {

    public String genCode() {
        String sCode = "";
        int iAux;
        for (int i = 0; i < 5; i++) {
            iAux = new Random().nextInt(48) + 42;
            if ((iAux > 64) || (iAux > 47 && iAux < 58)) {
                sCode += Character.toString((char) iAux);
            } else {
                i--;
            }

        }
        return sCode;

    }

}
