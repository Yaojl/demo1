package com.zhiyou;

import lombok.Data;

@Data
public class Banana{
	private String name;
	private Integer age;
	private Apple apple;
	

	public void bloom() {
		System.out.println("�㽶��������");
	}


	public void result() {
		System.out.println("�㽶�������");
	}

}
