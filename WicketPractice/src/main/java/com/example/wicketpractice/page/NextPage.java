package com.example.wicketpractice.page;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.example.wicketpractice.entity.Employee;
import com.example.wicketpractice.service.IEmployeeService;

public class NextPage extends WebPage {
	@SpringBean
	private IEmployeeService service;
	// 登録フォーム用
	private Model<String> employeeId;
	private TextField<String> idForm;
	private Model<String> employeeName;
	private TextField<String> nameForm;
	
	public NextPage() {
		this.add(new Label("title","従業員の登録"));
		// 登録フォーム
		Form<WebPage> form = new Form<>("employeeForm") {
			@Override
			protected void onSubmit() {
				service.regist(new Employee(Integer.parseInt(employeeId.getObject()), employeeName.getObject()));
				setResponsePage(RegisterResult.class);
			}
		};
		// ID入力欄
		form.add(new Label("idFormTitle", "ID"));
		employeeId = new Model<>();
		idForm = new TextField<>("idForm", employeeId);
		form.add(idForm);
		
		// 氏名入力欄
		form.add(new Label("nameFormTitle", "氏名"));
		employeeName = new Model<>();
		nameForm = new TextField<>("nameForm", employeeName);
		form.add(nameForm);
		
		// 登録ボタン
		Button submit = new Button("submit");
		form.add(submit);
		this.add(form);
		// トップページに戻る用のリンク
		Link<WebPage> link = new Link<WebPage>("back") {

			@Override
			public void onClick() {
				setResponsePage(Practice.class);
			}
		};
		this.add(link);
	}

}
