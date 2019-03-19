package com.cronoteSys.test;

import java.util.List;

import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.Permission;
import facebook4j.auth.AccessToken;

public class FacebookLogin {
	public static String login(String tokens) throws IllegalStateException, FacebookException {
		Facebook facebook = new FacebookFactory().getInstance();
		String addId = "2230012307237044";
		String appSecret = "";
		String permissions = "email, user_birthday";
		String token = tokens;
		
		facebook.setOAuthAppId(addId, appSecret);
		facebook.setOAuthPermissions(permissions);
		facebook.setOAuthAccessToken(new AccessToken(token));
		
		String name = facebook.getName();
		String id = facebook.getId();
		
//		System.out.println("Nome: "+name+"\nId: "+id);
		
		List<Permission> perm = facebook.getPermissions();
//		for(Permission p : perm) {
//			System.out.println("Permissão: "+p.getName()+" - "+p.toString());
//		}
		
		System.out.println(facebook.getUser(id).getBirthday());
		
		return id;
	}
	
	public static String getToken() {
		return "EAAfsLzNq6LQBABzodAHVSXE6DnorHmQZB8F4qGIxRxznh2tF9R9oUJGfFf0toZBUF3NrrYyjMclAxoLKDKh60N7CnrBiWL89j46eHIvZBvL0gkRHrW75DORRwdvtFuHsTgjgadnFZBrPHRZAL0nSy8H4mUghvQ9JmHp0f732BzNTZBuOmxa3U8W5EJLyzSDhpzWGRT6M6EZBV8b5ksR8jJX0x1kBA9En42Yv93e498tIgZDZD";
	}
}
