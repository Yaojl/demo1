package com.zhiyou;

import lombok.Data;

@Data
public class Apple{
	private String name;
	

	public void bloom() {
		System.out.println("123");
	}

	public void result() {
		System.out.println("456");
	}

}
