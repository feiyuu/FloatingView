package com.feiyu.floatingview.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceUtil {
	private SharedPreferences mInstance = null;

	/**
	 *
	 * @param ct
	 * @param fileName
	 */
	public void open(Context ct, String fileName) {
		mInstance = ct.getSharedPreferences(fileName, Context.MODE_PRIVATE);
	}

	/**
	 *
	 * @param key
	 * @return
	 */
	public boolean contains(String key) {
		if (mInstance != null) {
			return mInstance.contains(key);
		}
		return false;
	}

	/**
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean getBoolean(String key, boolean value) {
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
	public boolean putBoolean(String key, boolean value) {
		if (mInstance != null) {
			SharedPreferences.Editor editor = mInstance.edit();
			editor.putBoolean(key, value);
			return editor.commit();
		}
		return false;
	}

	/**
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	public int getInt(String key, int value) {
		if (mInstance != null) {
			return mInstance.getInt(key, value);
		}
		return 0;
	}

	/**
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean putInt(String key, int value) {
		if (mInstance != null) {
			SharedPreferences.Editor editor = mInstance.edit();
			editor.putInt(key, value);
			return editor.commit();
		}
		return false;
	}

	/**
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	public String getString(String key, String value) {
		if (mInstance != null) {
			return mInstance.getString(key, value);
		}
		return null;
	}

	/**
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean putString(String key, String value) {
		if (mInstance != null) {
			SharedPreferences.Editor editor = mInstance.edit();
			editor.putString(key, value);
			return editor.commit();
		}
		return false;
	}

	/**
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	public long getLong(String key, Long value) {
		if (mInstance != null) {
			return mInstance.getLong(key, value);
		}
		return -1;
	}

	/**
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean putLong(String key, Long value) {
		if (mInstance != null) {
			SharedPreferences.Editor editor = mInstance.edit();
			editor.putLong(key, value);
			return editor.commit();
		}
		return false;
	}
}
