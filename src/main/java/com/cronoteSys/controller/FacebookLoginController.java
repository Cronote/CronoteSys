package com.cronoteSys.controller;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Document;

import com.cronoteSys.util.ScreenUtil;
import com.cronoteSys.util.ScreenUtil.OnChangeScreen;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class FacebookLoginController extends MasterController {

	@FXML
	private WebView web;
	HashMap<String, String> paramMap = new HashMap<>();

	@FXML
	protected void initialize() {
		ScreenUtil.addOnChangeScreenListener(new OnChangeScreen() {
			public void onScreenChanged(String newScreen, HashMap<String, Object> hmap) {
				final WebEngine engine = web.getEngine();

				engine.load("https://www.facebook.com/v3.1/dialog/oauth?client_id=254397622087268"
						+ "&redirect_uri=https://www.facebook.com/connect/login_success.html" 
						+ "&response_type=token"
						+ "&state={st=state123abc,ds=123456789}");

				engine.getLoadWorker().stateProperty().addListener(new ChangeListener<State>() {

					public void changed(ObservableValue<? extends State> observable, State oldValue, State newValue) {
						if (newValue == State.SUCCEEDED) {
							Document doc = engine.getDocument();
							if (doc == null)
								return;
							if (doc.getDocumentElement().getTextContent().contains("Success")) {
								System.out.println(engine.getLocation());
								paramMap = mapParams(engine.getLocation().split("#")[1]);
							}

						}
					}
				});
			}
		});
	}

	private HashMap<String, String> mapParams(String params) {
		HashMap<String, String> hmp = new HashMap<String, String>();
		String[] paramsAux = params.split("&");
		for (int i = 0; i < paramsAux.length; i++) {
			hmp.put(paramsAux[i].split("=")[0], paramsAux[i].split("=")[1]);
		}

		return hmp;
	}

}
