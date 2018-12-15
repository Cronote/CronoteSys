
package com.cronoteSys;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

import com.cronoteSys.util.ScreenUtil;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class MainApp extends Application {
	private void test() {
		System.out.println(new Date(864000*1000).getHours());
	}
	
	@Override
	public void start(Stage stage) throws Exception {
test();
		Stack<Integer> st = new Stack<Integer>();
		int n = 5;
		while (n > 0) {
			st.push(n % 2);
			n /= 2;
		}
		int auxauxiliar = -1;
		int count = 0;
		boolean consecutive = true;
		for (int i = st.size(); i > 0; i--) {
			if (st.pop() == 1 && consecutive) {
				consecutive = true;
			} else {
				consecutive = false;
				if (count > auxauxiliar)
					auxauxiliar = count;
				count = 0;
			}
			if (consecutive)
				count++;
			consecutive = true;
		}
		System.out.print(auxauxiliar > count ? auxauxiliar : count);
//		System.exit(1);
		System.setProperty("javax.xml.bind.JAXBContextFactory", "org.eclipse.persistence.jaxb.JAXBContextFactory");
		new ScreenUtil().openNewWindow(null, "SLogin", false);
//		new ScreenUtil().openNewWindow(null, "SFacebookLogin", true, null);

	}

	public static void main(String[] args) {
		launch(args);

	}

}