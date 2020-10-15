package com.zhiyou;

public class Text {

	public static void main(String[] args) {
		/*Banana bean = (Banana) ApplicationContext.getBean("banana");
		bean.bloom();
		bean.result();*/
		Apple bean = ApplicationContext.getBean(Apple.class);
		bean.bloom();
		System.out.println(bean.getName());
	}
}
