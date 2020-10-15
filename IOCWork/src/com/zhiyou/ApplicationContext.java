package com.zhiyou;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class ApplicationContext {

	// 使用map 来充当IOC容器
	private static Map<String, Object> map = new HashMap<String, Object>();
	static{
		// 获得一个读取xml文件的对象
		SAXReader reader = new SAXReader();
		try {
			// 通过reader 获得xml中所有元素
			Document document = reader.read("src/application.xml");
			method(document);
			method2(document);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// 用来创建对象
	private static void method(Document document) throws Exception{
		// 获取到xml中最外围的标签
		Element root = document.getRootElement();
		// 获得beans标签的迭代器,来迭代里面的bean标签
		Iterator iterator = root.elementIterator();
		while(iterator.hasNext()){
			// 循环获得bean 标签
			Element bean=(Element) iterator.next();
			// 通过class 属性的value 来创建一个反射对象
			Class c = Class.forName(bean.attributeValue("class"));
			map.put(bean.attributeValue("id"), c.newInstance());
		}		
	}
	
	// 用来给创建出来的对象注入属性的
	private static void method2(Document document) throws Exception{
		// 获取到xml中最外围的标签
		Element root = document.getRootElement();
		// 获得beans标签的迭代器,来迭代里面的bean标签
		Iterator iterator = root.elementIterator();
		while(iterator.hasNext()){
			// 循环获得bean 标签
			Element bean=(Element) iterator.next();
			// 通过bean 标签id的value从容器中获取到对象
			Object obj=map.get(bean.attributeValue("id"));
			// 获得反射对象
			Class c = obj.getClass();
			//遍历bean里面的property标签
			Iterator iterator2 = bean.elementIterator();
			while(iterator2.hasNext()){
				Element property = (Element)iterator2.next();
				// 通过property标签中存放的 属性名称来创建一个属性对象
				Field field = c.getDeclaredField(property.attributeValue("name"));
				field.setAccessible(true);
				if (field.getType().getName().equals("java.lang.Integer")) {
					field.set(obj, Integer.valueOf(property.getStringValue()));
				}else if (field.getType().getName().equals("java.lang.String")) {
					field.set(obj, property.getStringValue());
				}else {
					field.set(obj, map.get(property.attributeValue("ref")));
				}
			}		
		}
	}
	
	
	public static Object getBean(String name){
		return map.get(name);
	}
	
	public static <T>T getBean(Class<T> c){
		// 获得容器中的所有对象集合
		Collection<Object> collection = map.values();
		// 获得一个集合的迭代器
		Iterator<Object> iterator = collection.iterator();
		// 遍历这个集合
		while(iterator.hasNext()){
			// 依次取出集合中的对象
			Object next = iterator.next();
			// 如果集合对象的class 名字 跟传入的class 名字一致,代表当前的这个对象就是我们要返回的对象
			if (next.getClass().getName().equals(c.getName())) {
				return (T)next;
			}
		}
		return null;
	}
}
