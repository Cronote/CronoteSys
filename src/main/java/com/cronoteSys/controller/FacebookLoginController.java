package com.cronoteSys.controller;

import java.util.HashMap;

import org.w3c.dom.Document;

import com.cronoteSys.test.FacebookLogin;
import com.cronoteSys.util.GenCode;
import com.cronoteSys.util.ScreenUtil;
import com.cronoteSys.util.ScreenUtil.OnChangeScreen;

import facebook4j.FacebookException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class FacebookLoginController extends MasterController {

	@FXML
	private WebView web;
	HashMap<String, String> paramMap = new HashMap<String, String>();
	String st, ds;

	@FXML
	protected void initialize() {
		st = GenCode.genRandomString(7);
		ds = GenCode.genRandomString(10);
		ScreenUtil.addOnChangeScreenListener(new OnChangeScreen() {
			public void onScreenChanged(String newScreen, HashMap<String, Object> hmap) {
				final WebEngine engine = web.getEngine();
				loadLoginForm(engine);

				engine.getLoadWorker().stateProperty().addListener(new ChangeListener<State>() {

					public void changed(ObservableValue<? extends State> observable, State oldValue, State newValue) {
						if (newValue == State.SUCCEEDED) {
							Document doc = engine.getDocument();
							if (doc == null)
								return;
							if (doc.getDocumentElement().getTextContent().contains("Success")) {
								try {
									mapURLParams(engine.getLocation().split("#")[1]);
								} catch (FacebookException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							if (doc.getDocumentElement().getTextContent().contains("\"data\"")) {
								mapJSONParams(doc.getDocumentElement().getTextContent());
							}

						}
					}
				});
			}
		});
	}

	private void mapURLParams(String params) throws FacebookException {
		HashMap<String, String> hmp = new HashMap<String, String>();
		String[] paramsAux = params.split("&");
		for (int i = 0; i < paramsAux.length; i++) {
			hmp.put(paramsAux[i].split("=")[0], paramsAux[i].split("=")[1]);

		}

		paramMap = hmp;
		if (paramMap.get("state").equals(st.concat(ds))) {
			checkToken(web.getEngine());
		} else {
			return;
		}

	}

	private void mapJSONParams(String JSON) {
		HashMap<String, Object> hmp = new HashMap<String, Object>();
		JSON = JSON.replace("\"", "");
		JSON = groupParam(JSON);

		System.out.println("=====================================");
		System.out.println(JSON);

	}

	private String groupParam(String JSON) {
		//metodo para agrupar, na mesma linha, grupos([]) tipo o scopes(permissoes)
		String[] aux = JSON.split("\\n");
		int j = 0, k = 0;
		for (int i = 0; i < aux.length; i++) {
			if (aux[i].contains("[")) {
				j = i;
			} else if (aux[i].contains("]")) {
				k = i;
			} else {
				if (j != 0 && k != 0) {
					i = j;

					for (int iAux = j + 1; iAux <= k; iAux++) {
						aux[i] += aux[iAux].trim().replace(",", "|");
						aux[i] = aux[i].replace("]|", "],");
						aux[iAux] = "";
						
					}

					j = k = 0;
				}
				if (!(j != 0 || k != 0))
					System.out.println(aux[i]);
			}
		}
		JSON = "";
		for (int i = 0; i < aux.length; i++) {
			System.out.println(aux[i] != "");
			JSON = JSON.concat(aux[i] != "" ? aux[i].trim() : "");
		}

		return JSON;
	}

	private void mapLine(HashMap<String, Object> hmp, String line) {

		hmp.put(line.split(":")[0], line.split(":")[1].replaceAll(",", ""));
	}

	private void loadLoginForm(WebEngine engine) {

		engine.load("https://www.facebook.com/v3.1/dialog/oauth?client_id=254397622087268"
				+ "&redirect_uri=https://www.facebook.com/connect/login_success.html" + "&response_type=token"
			 + "&state=" + st.concat(ds));
	}

	private void checkToken(WebEngine engine) throws FacebookException {
		engine.load("https://graph.facebook.com/debug_token?input_token=" + paramMap.get("access_token")
				+ "&access_token=254397622087268|lUuoEDdEW9RziQJkmC4OM2NdP0c");
		
		try {
			FacebookLogin.login(paramMap.get("access_token").toString());
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FacebookException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("https://graph.facebook.com/debug_token?input_token=" + paramMap.get("access_token")
				+ "&access_token=254397622087268|lUuoEDdEW9RziQJkmC4OM2NdP0c");
		
		engine.load("https://graph.facebook.com/"+FacebookLogin.login(FacebookLogin.getToken()).toString()+"?fields=id,name,birthday,email&access_token="+FacebookLogin.getToken().toString());
		System.out.println("===================\nhttps://graph.facebook.com/"+ FacebookLogin.login(paramMap.get("access_token").toString())+"?fields=id,email,name,birthday&access_token="+paramMap.get("access_token"));
	}
}
