package com.superrecyclerview.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Base64;

import com.google.gson.Gson;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Properties;

/**
 * 1、SharedPreferences操作工具类,以私有的文件类型保存数据,文件名以字段SP_NAME值为准，可自行修改。
 * 2、添加对Java对象存储和获取的方法，且该对象必须实现序列化接口。
 * 3、添加对Java bean对象的存储和获取方法。
 * @author lilei
 * @version 1.8
 */
public class SPUtils {
	
	/**
	 * SharedPreferences存储的文件名.
	 */
	private final static String SP_NAME = "config";
	/**
	 * SharedPreferences.
	 */
	private static SharedPreferences sp;


	
	/**
	 * 向SharedPreferences中保存布尔类型数据.
	 * @param context  上下文
	 * @param key       键
	 * @param value     值
	 */
	public static void saveBoolean(Context context, String key, boolean value) {
		if (sp == null)
			sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
		sp.edit().putBoolean(key, value).commit();
	}

	/**
	 * 保存字符串
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void saveString(Context context, String key, String value) {
		if (sp == null)
			sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
		sp.edit().putString(key, value).commit();
		
	}
	
	/**
	 * 清空SharedPreferences中所有的数据.
	 * @param context  上下文
	 */
	public static void clear(Context context){
		if (sp == null)
			sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
		sp.edit().clear().commit();
	}

	
	/**
	 * 保存long型
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void saveLong(Context context, String key, long value) {
		if (sp == null)
			sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
		sp.edit().putLong(key, value).commit();
	}

	
	/**
	 * 保存int型
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void saveInt(Context context, String key, int value) {
		if (sp == null)
			sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
		sp.edit().putInt(key, value).commit();
	}

	
	/**
	 * 保存float型
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void saveFloat(Context context, String key, float value) {
		if (sp == null)
			sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
		sp.edit().putFloat(key, value).commit();
	}

	
	/**
	 * 获取字符串
	 * @param context  上下文
	 * @param key       键
	 * @param defValue  默认值,如果在SharedPreferences中,键没有对应的值,就返回该默认值.
	 * @return   返回SharedPreferences中,给定键对应的值,如果没有就返回传入的默认值.
	 */
	public static String getString(Context context, String key, String defValue) {
		if (sp == null)
			sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
		return sp.getString(key, defValue);
	}

	
	/**
	 * 获取int值
	 * @param context
	 * @param key
	 * @param defValue
	 * @return
	 */
	public static int getInt(Context context, String key, int defValue) {
		if (sp == null)
			sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
		return sp.getInt(key, defValue);
	}

	
	/**
	 * 获取long值
	 * @param context
	 * @param key
	 * @param defValue
	 * @return
	 */
	public static long getLong(Context context, String key, long defValue) {
		if (sp == null)
			sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
		return sp.getLong(key, defValue);
	}

	
	/**
	 * 获取float值
	 * @param context
	 * @param key
	 * @param defValue
	 * @return
	 */
	public static float getFloat(Context context, String key, float defValue) {
		if (sp == null)
			sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
		return sp.getFloat(key, defValue);
	}

	
	/**
	 * 获取布尔值
	 * @param context
	 * @param key
	 * @param defValue
	 * @return
	 */
	public static boolean getBoolean(Context context, String key,
			boolean defValue) {
		if (sp == null)
			sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
		return sp.getBoolean(key, defValue);
	}


	/**
	 * 保存Java Bean对象。
	 * @param context
	 * @param key
	 * @param obj
     */
	public static  void  saveBean(Context context,String key,Object obj){
		Gson gson=new Gson();
		String json=gson.toJson(obj);
		saveString(context,key,json);
	}


	/**
	 * 获取Java Bean 对象。
	 * @param context
	 * @param key
	 * @param t
	 * @param <T>
     * @return
     */
	public static <T> T getBean(Context context,String key,Class<T> t){
		String cache=getString(context,key,"");
		if(!TextUtils.isEmpty(cache)){
			Gson gson=new Gson();
			return gson.fromJson(cache,t);
		}else {
			return null;
		}
	}


	
	/**
	 * 将对象进行base64编码后保存到SharedPreferences中。
	 * @param context  上下文
	 * @param key       键
	 * @param object    对象
	 */
	public static void saveObj(Context context, String key, Serializable object) {
		if (sp == null)
			sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
			// 将对象的转为base64码
			String objBase64 = new String(Base64.encode(baos.toByteArray(),Base64.NO_WRAP));
			sp.edit().putString(key,objBase64).commit();
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * 将SharePref中经过base64编码的对象读取出来
	 * @param context
	 * @param key
	 * @return
	 */
	public static Object getObj(Context context, String key) {
		if (sp == null)
			sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
		String objBase64 = sp.getString(key, null);
		if (TextUtils.isEmpty(objBase64))
			return null;

		// 对Base64格式的字符串进行解码
		byte[] base64Bytes = Base64.decode(objBase64.getBytes(),Base64.NO_WRAP);
		ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);

		ObjectInputStream ois;
		Object obj = null;
		try {
			ois = new ObjectInputStream(bais);
			obj = (Object) ois.readObject();
			ois.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}

	/**
	 * 根据key获取config.properties里面的值
	 * @param context
	 * @param key
	 * @return
	 */
	public static String getProperty(Context context, String key) {
		try {
			Properties props = new Properties();
			InputStream input = context.getAssets().open("config.properties");
			if (input != null) {
				props.load(input);
				return props.getProperty(key);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

}
