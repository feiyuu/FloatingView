package com.feiyu.floatingview.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.List;

public class CacheUtil {

	/**
	 * 请求数据并缓存
	 */
	public static final int CACHE_POLICY_REQUEST_ADD_CACHE = 0;
	/**
	 * 仅获取缓存数据
	 */
	public static final int CACHE_POLICY_GET_CACHE = 1;
	/**
	 * 请求数据不缓存
	 */
	public static final int CACHE_POLICY_REQUEST_NOT_CACHE = 2;
	private static SharedPreferenceUtil mInstance = new SharedPreferenceUtil();
	private static final String saveDataGrobalFileName = "saveDataGrobal";
	/**
	 * 以'-'分隔
	 */
	public static final String GPS_PROVINCE_CITY_DISTRICT = "gps_province_city_district";
	/**
	 * 以'-'分隔
	 */
	public static final String GPS_CITY_DISTRICT = "gps_city_district";
	/**
	 * 以'-'分隔
	 */
	public static final String GPS_LAT_LNG = "gps_lat_lng";
	public static final String GPS_CITY = "gps_city";
	public static final String USER_CHOSEN_CITY = "user_chosen_city";
	public static final String SEARCH_HISTORY = "search_history";
	/**
	 * 上次离开首页时间
	 */
	public static final String LAST_LEAVE_HOME_PAGE_TIME = "last_leave_home_page_time";
	/**
	 * open local data file
	 */
	public static final String SOURCE_PACKAGE_NAME = "search_package_name";

	public static void open(Context ct) {
		mInstance.open(ct, saveDataGrobalFileName);
	}

	/**
	 *
	 * @param key
	 * @param value
	 */
	public static void saveCache(String key, String value) {
		if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
			putString(key, value);
		}
	}

	public static String getCache(String key) {
		if (!TextUtils.isEmpty(key)) {
			return getString(key, "");
		}
		return "";
	}

	/**
	 * 返回搜索历史列表，最多10条
	 * 
	 * @return
	 */
	public static List<String> getSearchHistory() {
		String data = null;
		if (mInstance != null) {
			data = mInstance.getString(SEARCH_HISTORY, null);
		}
		if (data != null) {
			List<String> list = new ArrayList<String>();
			String[] splits = data.split("<bmw>");
			for (String str : splits) {
				if (!TextUtils.isEmpty(str)) {
					list.add(str);
				}
			}
			return list;
		}
		return null;
	}

	/**
	 *
	 * @param keyword
	 */
	public static void saveSearchHistoty(String keyword) {
		if (TextUtils.isEmpty(keyword)) {
			return;
		}
		List<String> list = getSearchHistory();
		if (list != null) {
			if (list.size() >= 8) {
				list.remove(list.size() - 1);
			}
			StringBuilder builder = new StringBuilder();
			builder.append(keyword);
			for (String str : list) {
				if (!TextUtils.isEmpty(str) && !str.equals(keyword)) {
					builder.append("<bmw>" + str);
				}
			}
			putString(SEARCH_HISTORY, builder.toString());
		} else {
			putString(SEARCH_HISTORY, keyword);
		}
	}

	/**
	 *
	 */
	public static void clearSearchHistory() {
		putString(SEARCH_HISTORY, "");
	}

	/**
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	public static boolean getBoolean(String key, boolean value) {
		if (mInstance != null) {
			return mInstance.getBoolean(key, value);
		}
		return false;
	}

	/**
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	public static boolean putBoolean(String key, boolean value) {
		if (mInstance != null) {
			return mInstance.putBoolean(key, value);
		}
		return false;
	}

	/**
	 *
	 * @param key
	 * @param def
	 * @return
	 */
	public static int getInt(String key, int def) {
		if (mInstance != null) {
			return mInstance.getInt(key, def);
		}
		return 0;
	}

	/**
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	public static boolean putInt(String key, int value) {
		if (mInstance != null) {
			return mInstance.putInt(key, value);
		}
		return false;
	}

	/**
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	public static String getString(String key, String value) {
		if (mInstance != null) {
			return mInstance.getString(key, value);
		}
		return null;
	}

	public static boolean putString(String key, String value) {
		if (mInstance != null) {
			return mInstance.putString(key, value);
		}
		return false;
	}

	/**
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	public static long getLong(String key, Long value) {
		if (mInstance != null) {
			return mInstance.getLong(key, value);
		}
		return 0;
	}

	/**
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	public static boolean putLong(String key, Long value) {
		if (mInstance != null) {
			return mInstance.putLong(key, value);
		}
		return false;
	}

	/**
	 * desc:获取保存的Object对象
	 * 
	 * @param key
	 * @return modified:
	 */
	public static Object readObject(String key) {
		try {
			if (mInstance.contains(key)) {
				String string = mInstance.getString(key, "");
				if (TextUtils.isEmpty(string)) {
					return null;
				} else {
					// 将16进制的数据转为数组，准备反序列化
					byte[] stringToBytes = StringToBytes(string);
					ByteArrayInputStream bis = new ByteArrayInputStream(
							stringToBytes);
					ObjectInputStream is = new ObjectInputStream(bis);
					// 返回反序列化得到的对象
					Object readObject = is.readObject();
					return readObject;
				}
			}
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		// 所有异常返回null
		return null;

	}

	/**
	 * desc:将16进制的数据转为数组
	 * 
	 * @param data
	 * @return modified:
	 */
	private static byte[] StringToBytes(String data) {
		String hexString = data.toUpperCase().trim();
		if (hexString.length() % 2 != 0) {
			return null;
		}
		byte[] retData = new byte[hexString.length() / 2];
		for (int i = 0; i < hexString.length(); i++) {
			int int_ch; // 两位16进制数转化后的10进制数
			char hex_char1 = hexString.charAt(i); // //两位16进制数中的第一位(高位*16)
			int int_ch1;
			if (hex_char1 >= '0' && hex_char1 <= '9')
				int_ch1 = (hex_char1 - 48) * 16; // // 0 的Ascll - 48
			else if (hex_char1 >= 'A' && hex_char1 <= 'F')
				int_ch1 = (hex_char1 - 55) * 16; // // A 的Ascll - 65
			else
				return null;
			i++;
			char hex_char2 = hexString.charAt(i); // /两位16进制数中的第二位(低位)
			int int_ch2;
			if (hex_char2 >= '0' && hex_char2 <= '9')
				int_ch2 = (hex_char2 - 48); // // 0 的Ascll - 48
			else if (hex_char2 >= 'A' && hex_char2 <= 'F')
				int_ch2 = hex_char2 - 55; // // A 的Ascll - 65
			else
				return null;
			int_ch = int_ch1 + int_ch2;
			retData[i / 2] = (byte) int_ch;// 将转化后的数放入Byte里
		}
		return retData;
	}

	/**
	 * desc:保存对象
	 * 
	 * @param key
	 * @param obj
	 *            要保存的对象，只能保存实现了serializable的对象 modified:
	 */
	public static boolean saveObject(String key, Object obj) {
		try {
			// 先将序列化结果写到byte缓存中，其实就分配一个内存空间
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream os = new ObjectOutputStream(bos);
			// 将对象序列化写入byte缓存
			os.writeObject(obj);
			// 将序列化的数据转为16进制保存
			String bytesToHexString = bytesToHexString(bos.toByteArray());
			// 保存该16进制数组
			return mInstance.putString(key, bytesToHexString);
		} catch (IOException e) {
			e.printStackTrace();
			Log.e("", "保存obj失败");
		}
		return false;
	}

	/**
	 * desc:将数组转为16进制
	 * 
	 * @param bArray
	 * @return modified:
	 */
	public static String bytesToHexString(byte[] bArray) {
		if (bArray == null) {
			return null;
		}
		if (bArray.length == 0) {
			return "";
		}
		StringBuffer sb = new StringBuffer(bArray.length);
		String sTemp;
		for (int i = 0; i < bArray.length; i++) {
			sTemp = Integer.toHexString(0xFF & bArray[i]);
			if (sTemp.length() < 2)
				sb.append(0);
			sb.append(sTemp.toUpperCase());
		}
		return sb.toString();
	}

}
