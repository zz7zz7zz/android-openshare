/**
 * $id$
 * Copyright 2011-2012 Renren Inc. All rights reserved.
 */
package com.open.share.renren;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;


/**
 * photos.upload API请求的返回结果包装类
 * 
 * @author sunting ting.sun@renren-inc.com
 * 
 */
public class RenrenPhotoUploadResponseBean implements Parcelable {

	/**
	 * 字段常量
	 */
	private static final String KEY_PID = "pid";
	private static final String KEY_AID = "aid";
	private static final String KEY_UID = "uid";
	private static final String KEY_SRC = "src";
	private static final String KEY_SRC_SMALL = "src_small";
	private static final String KEY_SRC_BIG = "src_big";
	private static final String KEY_CAPTION = "caption";

	/**
	 * 照片pid
	 */
	private long pid;
	/**
	 * 照片所属相册aid
	 */
	private long aid;
	/**
	 * 照片所有者uid
	 */
	private long uid;
	/**
	 * 正常尺寸照片源url
	 */
	private String src;
	/**
	 * 小尺寸照片源url
	 */
	private String src_small;
	/**
	 * 大尺寸照片源url
	 */
	private String src_big;
	/**
	 * 照片描述
	 */
	private String caption;

	/**
	 * 构造函数，将请求返回的json串格式数据解析成对象
	 * 
	 * @param response
	 */
	public RenrenPhotoUploadResponseBean(String response) {
		this(response, RenrenShareImpl.RESPONSE_FORMAT_JSON);
	}

	/**
	 * 构造函数，将请求返回的json串格式数据解析成对象
	 * 
	 * @param response
	 *            服务器返回的请求结果串
	 * @param format
	 *            服务器返回结果的格式
	 */
	private RenrenPhotoUploadResponseBean(String response, String format) {

		if (response == null) {
			return;
		}

		// 暂时只提供json格式的数据解析
		if (format.toLowerCase().endsWith("json")) {
			try {
				JSONObject object = new JSONObject(response);
				if (object != null) {
					pid = object.optLong(KEY_PID);
					aid = object.optLong(KEY_AID);
					uid = object.optLong(KEY_UID);
					src = object.optString(KEY_SRC);
					src_small = object.optString(KEY_SRC_SMALL);
					src_big = object.optString(KEY_SRC_BIG);
					caption = object.optString(KEY_CAPTION);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	public long getPid() {
		return pid;
	}

	public void setPid(long pid) {
		this.pid = pid;
	}

	public long getAid() {
		return aid;
	}

	public void setAid(long aid) {
		this.aid = aid;
	}

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public String getSrc_small() {
		return src_small;
	}

	public void setSrc_small(String src_small) {
		this.src_small = src_small;
	}

	public String getSrc_big() {
		return src_big;
	}

	public void setSrc_big(String src_big) {
		this.src_big = src_big;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append(KEY_PID).append(" = ").append(pid).append("\r\n");
		sb.append(KEY_AID).append(" = ").append(aid).append("\r\n");
		sb.append(KEY_UID).append(" = ").append(uid).append("\r\n");
		sb.append(KEY_SRC).append(" = ").append(src).append("\r\n");
		sb.append(KEY_SRC_SMALL).append(" = ").append(src_small).append("\r\n");
		sb.append(KEY_SRC_BIG).append(" = ").append(src_big).append("\r\n");
		sb.append(KEY_CAPTION).append(" = ").append(caption).append("\r\n");

		return sb.toString();
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flag) {
		Bundle bundle = new Bundle();
		if (pid != 0) {
			bundle.putLong(KEY_PID, pid);
		}
		if (aid != 0) {
			bundle.putLong(KEY_AID, aid);
		}
		if (uid != 0) {
			bundle.putLong(KEY_UID, uid);
		}
		if (caption != null) {
			bundle.putString(KEY_CAPTION, caption);
		}
		if (src != null) {
			bundle.putString(KEY_SRC, src);
		}
		if (src_small != null) {
			bundle.putString(KEY_SRC_SMALL, src_small);
		}
		if (src_big != null) {
			bundle.putString(KEY_SRC_BIG, src_big);
		}

		dest.writeBundle(bundle);
	}

	public static final Parcelable.Creator<RenrenPhotoUploadResponseBean> CREATOR = new Parcelable.Creator<RenrenPhotoUploadResponseBean>() {
		public RenrenPhotoUploadResponseBean createFromParcel(Parcel in) {
			return new RenrenPhotoUploadResponseBean(in);
		}

		public RenrenPhotoUploadResponseBean[] newArray(int size) {
			return new RenrenPhotoUploadResponseBean[size];
		}
	};

	/**
	 * 构造函数，根据序列化对象构造实例
	 * 
	 * @param in
	 */
	public RenrenPhotoUploadResponseBean(Parcel in) {

		Bundle bundle = in.readBundle();
		if (bundle.containsKey(KEY_PID)) {
			pid = bundle.getLong(KEY_PID);
		}
		if (bundle.containsKey(KEY_AID)) {
			aid = bundle.getLong(KEY_AID);
		}
		if (bundle.containsKey(KEY_UID)) {
			uid = bundle.getLong(KEY_UID);
		}
		if (bundle.containsKey(KEY_CAPTION)) {
			caption = bundle.getString(KEY_CAPTION);
		}
		if (bundle.containsKey(KEY_SRC)) {
			src = bundle.getString(KEY_SRC);
		}
		if (bundle.containsKey(KEY_SRC_SMALL)) {
			src_small = bundle.getString(KEY_SRC_SMALL);
		}
		if (bundle.containsKey(KEY_SRC_BIG)) {
			src_big = bundle.getString(KEY_SRC_BIG);
		}
	}

}
