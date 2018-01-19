package com.cdsxt.util;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.deserializer.ParseProcess;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.alibaba.fastjson.serializer.SerializeFilter;

import org.bson.BsonArray;
import org.bson.BsonValue;
import org.bson.Document;

/**
 * 
 * 定义了bean和docuemnt之间的转换功能
 *
 */
public class BeanDocumentUtils {

	/**
	 * document对象转javaBean对象
	 */
	public static <T> T toBean(Document doc, Class<T> beanCls,
			SerializeFilter filter, ParseProcess processor) {
		/**
		 * doc.toJson() 里面如果有long的数据 会把转换为{ "$numberLong" : "1503474700195" }
		 * ...
		 */
		// T bean = JSON.parseObject(doc.toJson(),beanCls);
		T bean = beanConvertBean(doc, beanCls, filter, processor);
		return bean;
	}

	public static <T> T toBean(Document doc, Class<T> beanCls) {
		/**
		 * doc.toJson() 里面如果有long的数据 会把转换为{ "$numberLong" : "1503474700195" }
		 * ...
		 */
		// T bean = JSON.parseObject(doc.toJson(),beanCls);
		T bean = beanConvertBean(doc, beanCls);
		return bean;
	}
	/**
	 * Iterable<Document>对象转 list -javaBean对象
	 */
	public static <T> List<T> toListBean(Iterable<Document> itDoc,
			Class<T> beanCls, SerializeFilter filter, ParseProcess processor) {
		List<T> list = new ArrayList<T>();
		for (Document doc : itDoc) {
			T bean = toBean(doc, beanCls, filter, processor);
			list.add(bean);
		}
		return list;
	}
	public static <T> List<T> toListBean(Iterable<Document> itDoc,Class<T> beanCls) {
		List<T> list = new ArrayList<T>();
		for (Document doc : itDoc) {
			T bean = toBean(doc, beanCls );
			list.add(bean);
		}
		return list;
	}
	/**
	 * javaBean对象转document对象
	 * 
	 * 过程：先转json字符串，再转document对象
	 */
	public static Document toDocument(Object bean) {
		return toDocumentIgnore(bean, new String[0]);
	}

	/**
	 * 
	 * javaBean对象转document对象（忽略一些不需要的java成员变量）
	 * */
	public static Document toDocumentIgnore(Object bean,
			final String... ignoreFields) {
		return Document.parse(JSON.toJSONString(bean, new PropertyFilter() {
			@Override
			public boolean apply(Object o, String s, Object o1) {
				// 优化null
				if (ignoreFields == null) {
					return true;
				}
				// 屏蔽不需要修改的字段
				for (String f : ignoreFields) {
					if (f.equals(s)) {
						return false;
					}
				}
				return true;
			}
		}));
	}

	/**
	 * 
	 * javaBean对象转document对象（允许一些需要的java成员变量）
	 * */
	public static Document toDocumentNeed(Object bean,
			final String... needFields) {
		return Document.parse(JSON.toJSONString(bean, new PropertyFilter() {
			@Override
			public boolean apply(Object o, String s, Object o1) {
				// 优化null
				if (needFields == null) {
					return false;
				}
				// 屏蔽不需要修改的字段
				for (String f : needFields) {
					if (f.equals(s)) {
						return true;
					}
				}
				return false;
			}
		}));
	}

	/**
	 * 把list bean转换为list document
	 */
	public static List<Document> toListDocument(List<?> listBean) {
		List<Document> listDoc = new ArrayList<Document>();
		for (Object bean : listBean) {
			// bean转obj
			Document doc = toDocument(bean);
			listDoc.add(doc);
		}
		return listDoc;
	}

	/**
	 * 把list bean转换为list document
	 */
	public static List<Document> toListDocumentIgnore(List<?> listBean,
			final String... ignoreFields) {
		List<Document> listDoc = new ArrayList<Document>();
		for (Object bean : listBean) {
			// bean转obj
			Document doc = toDocumentIgnore(bean, ignoreFields);
			listDoc.add(doc);
		}
		return listDoc;
	}

	/**
	 * 把java对象转换为另一种java对象 如：userVo转userPo。。。
	 */
	public static <T> T beanConvertBean(Object object, Class<T> clazz) {
		return JSON.parseObject(JSON.toJSONString(object), clazz);
	}

	/**
	 * 带过滤器的转换 object：被转换的bean对象 clazz：转换后的bean的Class对象
	 * filter：如果需要手动把object某一个属性转换为josn字符串时，传入回调函数
	 * processor：如果需要手动josn字符串某一个属性，设置到被转换的对象时，传入回调函数
	 * 
	 */
	public static <T> T beanConvertBean(Object object, Class<T> clazz,
			SerializeFilter filter, ParseProcess processor) {
		// return JSON.parseObject(JSON.toJSONString(object, filter), clazz);
		return JSON.parseObject(JSON.toJSONString(object, filter), clazz,
				processor);
	}

	public static void main(String[] args) {

		// StudentEntity sv = new StudentEntity("张无忌", 123);
		// Document doc = beanConvertBean(sv, Document.class);
		// System.out.println(doc);
		// StudentEntity sv2 = toBean(doc,StudentEntity.class,null,null);
		// System.out.println(sv2);
		// String json="{name:1}";
		String json = "[{name:1},{name:2}]";
		System.out.println(jsonToListDocument(json));

	}

	/**
	 * json 字符串对象---》document
	 * ｛name:'zzz',age:11｝
	 */
	public static Document jsonToDocument(String jsonStr) {
		Document doc = Document.parse(jsonStr);
		return doc;
	}
	/**
	 * json 字符串数组---》List -document
	 * [｛name:'zzz',age:11｝,｛name:'zzz',age:22｝]
	 */
	public static List<Document> jsonToListDocument(String jsonArrStr) {
		List<Document> list = new ArrayList<Document>();
		BsonArray ba = BsonArray.parse(jsonArrStr);
		for (BsonValue bv : ba) {
			Document doc=Document.parse(bv.asDocument().toJson());
			list.add(doc);
		}
		return list;
	}
}
