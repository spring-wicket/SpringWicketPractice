package com.example.wicketpractice.page;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;

public class RegisterResult extends WebPage {
	public RegisterResult() {
		this.add(new Label("resultTitle", "登録結果"));
		// 簡易的なものなので成功したことだけ表示する
		this.add(new Label("result", "登録に成功しました"));
		
		Link<WebPage> link = new Link<WebPage>("topPage") {
			@Override
			public void onClick() {
				setResponsePage(Practice.class);
			}
		};
		this.add(link);
	}
}
