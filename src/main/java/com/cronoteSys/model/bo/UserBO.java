/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cronoteSys.model.bo;

import java.util.Date;
import java.util.List;

import com.cronoteSys.model.dao.UserDAO;
import com.cronoteSys.model.vo.UserVO;

/**
 *
 * @author bruno
 */
public class UserBO {

    public boolean save(UserVO user) {
    	if(user.getRegisterDate() == null) user.setRegisterDate(new Date());
        return new UserDAO().save(user);
    }

    public void update(UserVO user) {
        new UserDAO().update(user);
    }

//    public boolean activateOrInactivate(UserVO user) {
//        if (user.getStats() == 1) { //ativado
//            user.setStats(Byte.valueOf("0"));
//        } else { // desativado
//            user.setStats(Byte.valueOf("1"));
//        }
//        return new UserDAO().update(user);
//    }

    public void delete(UserVO user) {
        new UserDAO().delete(user.getIdUser());

    }

    public List<UserVO> listAll() {
        return new UserDAO().listAll();

    }
}
