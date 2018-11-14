package com.zc.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class LuceneUtil {
    private static Directory directory;
    private static Version version;
    private static Analyzer analyzer;
    private static MaxFieldLength maxFieldLength;

    static {
        try {
            directory = FSDirectory.open(new File("E:\\IndexDB"));
            version = Version.LUCENE_30;
            analyzer = new StandardAnalyzer(version);
            maxFieldLength = MaxFieldLength.LIMITED;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //私有化构造器
    private LuceneUtil() {

    }

    /**
     * 将JavaBean转成Document对象
     *
     * @return
     */
    public static Document javaben2Document(Object obj) {
        Class clazz = obj.getClass();
        Document document = new Document();
        java.lang.reflect.Field[] reflectFields = clazz.getDeclaredFields();
        for (java.lang.reflect.Field field : reflectFields) {
            String fieldName = field.getName();
            String init = fieldName.substring(0, 1).toUpperCase();
            //获取该Field对应的get方法
            String methodName = "get" + init + fieldName.substring(1);
            try {
                Method method = clazz.getDeclaredMethod(methodName, null);
                //获取属性值
                String returnValue = method.invoke(obj, null).toString();
                document.add(new Field(fieldName, returnValue, Store.YES, Index.ANALYZED));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
        return document;
    }

    public static Object document2javabean(Document document, Class clazz) {
        Object obj = null;
        try {
            obj = clazz.newInstance();
            java.lang.reflect.Field[] reflectFields = clazz.getDeclaredFields();
            for (java.lang.reflect.Field field : reflectFields) {
                field.setAccessible(true);
                String fieldName = field.getName();
                String fieldValue = document.get(fieldName);
				/*System.out.println(field.getClass());//java.lang.reflect.Field
				System.out.println(field.getType());//java.lang.Integer
*/
                System.out.println(fieldName + ":" + fieldValue);
                //调用set方法
				/*String initName=fieldName.substring(0, 1).toUpperCase();
				String setMethodName="set"+initName+fieldName.substring(1);
				System.out.println(setMethodName);
				Method setMethod=clazz.getDeclaredMethod(setMethodName, field.getType());
				System.out.println(setMethod);
				setMethod.invoke(obj, fieldValue);
				*/
                System.out.println(fieldName + ":" + fieldValue);
                BeanUtils.setProperty(obj, fieldName, fieldValue);
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static Directory getDirectory() {
        return directory;
    }

    public static Analyzer getAnalyzer() {
        return analyzer;
    }

    public static Version getVersion() {
        return version;
    }

    public static MaxFieldLength getMaxFieldLength() {
        return maxFieldLength;
    }

}
