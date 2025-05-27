package com.example.wicketpractice.page;

import java.util.Date;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.example.wicketpractice.entity.Employee;
import com.example.wicketpractice.service.IEmployeeService;

public class Practice extends WebPage {
	@SpringBean
	private IEmployeeService service;
	
	// 検索フォーム用
	private Employee condition = new Employee(null, null);
	private Model<String> filterId;
	private TextField<String> filterIdForm;
	private Model<String> filterName;
	private TextField<String> filterNameForm;
	
	public Practice() {
		this.add(new Label("title", "wicketで設定した文字列"));
		
		// 検索条件入力フォーム
		this.add(new Label("filterName", "検索条件"));
		Form<WebPage> filterForm = new Form<>("filterForm");
		
		filterId = new Model<>();
		filterIdForm = new TextField<>("filteredId", filterId);
		filterForm.add(filterIdForm);
		
		filterName = new Model<>();
		filterNameForm = new TextField<>("filteredName", filterName);
		filterForm.add(filterNameForm);
		// フィルタ検索用ボタン
		Button filterButton = new Button("filterButton") {
			@Override
			public void onSubmit() {
				Integer id = null;
				if (filterId.getObject() != null) {
					// 入力チェックはしていないので数値以外ではエラーになる
					id = Integer.parseInt(filterId.getObject());
				}
				condition = new Employee(id, "%" + filterName.getObject() + "%");
			}
		};
		filterForm.add(filterButton);
		
		// 条件クリアボタン
		Button clearButton = new Button("clearButton") {
			@Override
			public void onSubmit() {
				// 検索条件と条件入力欄をクリアする
				condition = new Employee(null, null);
				filterIdForm.setModelObject(null);
				filterNameForm.setModelObject(null);
			}
		};
		filterForm.add(clearButton);
		
		// Ajaxのボタン(普通のボタンとの比較用)
		Button filterButtonAjax = new AjaxButton("ajaxFilterButton") {
			@Override
			protected void onSubmit(AjaxRequestTarget target) {
				Integer id = null;
				if (filterId.getObject() != null) {
					id = Integer.parseInt(filterId.getObject());
				}
				condition = new Employee(id, "%" + filterName.getObject() + "%");
				// container(従業員テーブル)のみ更新する
				target.add(getPage().get("container"));
			}
		};
		filterForm.add(filterButtonAjax);
		
		Button clearButtonAjax = new AjaxButton("ajaxClearButton") {
			@Override
			protected void onSubmit(AjaxRequestTarget target) {
				condition = new Employee(null, null);
				filterIdForm.setModelObject(null);
				filterNameForm.setModelObject(null);
				// 検索フォームと従業員テーブルを更新する
				target.add(getPage().get("filterForm"));
				target.add(getPage().get("container"));
			}
		};
		filterForm.add(clearButtonAjax);
		this.add(filterForm);
		
		//  LoadableDetachableModelでDB更新を検知
		// 普通のリストをListViewに渡すと登録や検索で表示するデータを更新できない
		LoadableDetachableModel<List<Employee>> model = new LoadableDetachableModel<List<Employee>>() {
			@Override
			protected List<Employee> load() {
				return service.select(condition);
			}
		};
		
		// 従業員テーブルの表示
		this.add(new Label("tableName", "従業員テーブル"));
		
		// AjaxButtonで従業員テーブルを更新するためにContainerを作成(ListViewだけではできなかった)
		WebMarkupContainer container = new WebMarkupContainer("container");
		container.setOutputMarkupId(true);
		
		ListView<Employee> employeeView = new ListView<Employee>("employees", model) {

			@Override
			protected void populateItem(ListItem<Employee> item) {
				Employee employee = item.getModelObject();
				item.add(new Label("id", employee.getId()));
				item.add(new Label("name", employee.getName()));
				
				HiddenField<String> hidden = new HiddenField<>("hidden", Model.of(String.valueOf(employee.getId())));
				item.add(hidden);
				
				// フォームにボタンを入れないと削除ができない
				Form<WebPage> delete = new Form<>("delete") {
					@Override
					protected void onSubmit() {
						service.delete(Integer.parseInt(hidden.getModelObject()));
					}
				};
				delete.add(new Button("deleteButton"));
				item.add(delete);
			}
		};
		
		container.add(employeeView);
		this.add(container);
		
		// 入力フォームに行くボタン
		Form<WebPage> regist = new Form<>("regist") {
			@Override
			protected void onSubmit() {
				setResponsePage(NextPage.class);
			}
		};
		Button registButton = new Button("registButton");
		regist.add(registButton);
		this.add(regist);
		
		// リンク
		Link<WebPage> link = new Link<WebPage>("nextPage") {
			@Override
			public void onClick() {
				setResponsePage(NextPage.class);
				
			}
		};
		this.add(link);
		
		this.add(new Label("dateTitleHtml", "HTML側で作ったDatePicker"));
		DateTextField dateField1 = new DateTextField("datePickerHtml", Model.of(new Date()), "yyyy-MM-dd");
		this.add(dateField1);
		
		this.add(new Label("dateTitleWicket", "Wicketで作ったDatePicker"));
		DateTextField dateField2 = new DateTextField("datePickerWicket", Model.of(new Date()), "yyyy-MM-dd");
		// wicket-datetimeを追加すれば使える
		DatePicker datePicker = new DatePicker();
		datePicker.setShowOnFieldClick(true);
		datePicker.setAutoHide(true);
		dateField2.add(datePicker);
		this.add(dateField2);
		
	}
	
}
