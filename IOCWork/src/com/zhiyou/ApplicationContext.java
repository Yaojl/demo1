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

	// ʹ��map ���䵱IOC����
	private static Map<String, Object> map = new HashMap<String, Object>();
	static{
		// ���һ����ȡxml�ļ��Ķ���
		SAXReader reader = new SAXReader();
		try {
			// ͨ��reader ���xml������Ԫ��
			Document document = reader.read("src/application.xml");
			method(document);
			method2(document);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// ������������
	private static void method(Document document) throws Exception{
		// ��ȡ��xml������Χ�ı�ǩ
		Element root = document.getRootElement();
		// ���beans��ǩ�ĵ�����,�����������bean��ǩ
		Iterator iterator = root.elementIterator();
		while(iterator.hasNext()){
			// ѭ�����bean ��ǩ
			Element bean=(Element) iterator.next();
			// ͨ��class ���Ե�value ������һ���������
			Class c = Class.forName(bean.attributeValue("class"));
			map.put(bean.attributeValue("id"), c.newInstance());
		}		
	}
	
	// ���������������Ķ���ע�����Ե�
	private static void method2(Document document) throws Exception{
		// ��ȡ��xml������Χ�ı�ǩ
		Element root = document.getRootElement();
		// ���beans��ǩ�ĵ�����,�����������bean��ǩ
		Iterator iterator = root.elementIterator();
		while(iterator.hasNext()){
			// ѭ�����bean ��ǩ
			Element bean=(Element) iterator.next();
			// ͨ��bean ��ǩid��value�������л�ȡ������
			Object obj=map.get(bean.attributeValue("id"));
			// ��÷������
			Class c = obj.getClass();
			//����bean�����property��ǩ
			Iterator iterator2 = bean.elementIterator();
			while(iterator2.hasNext()){
				Element property = (Element)iterator2.next();
				// ͨ��property��ǩ�д�ŵ� ��������������һ�����Զ���
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
		// ��������е����ж��󼯺�
		Collection<Object> collection = map.values();
		// ���һ�����ϵĵ�����
		Iterator<Object> iterator = collection.iterator();
		// �����������
		while(iterator.hasNext()){
			// ����ȡ�������еĶ���
			Object next = iterator.next();
			// ������϶����class ���� �������class ����һ��,����ǰ����������������Ҫ���صĶ���
			if (next.getClass().getName().equals(c.getName())) {
				return (T)next;
			}
		}
		return null;
	}
}
