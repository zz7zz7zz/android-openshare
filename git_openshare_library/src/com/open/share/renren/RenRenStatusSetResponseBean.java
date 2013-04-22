/**
 * $id$
 * Copyright 2011-2012 Renren Inc. All rights reserved.
 */
package com.open.share.renren;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * 对StatusSetResponseBean的请求成功后的响应结果进行封装
 * 
 * @author Shaofeng Wang (shaofeng.wang@renren-inc.com)
 *
 */
public class RenRenStatusSetResponseBean implements Parcelable{
	
	private static final String RESULT = "result";
	
	public static final int DEFAULT_RESULT = 0;
	/**
	 * status.get接口返回的result
	 */
	private int result;
	
	public RenRenStatusSetResponseBean(String response) {
		try {
			JSONObject json = new JSONObject(response);
			result = json.getInt(RESULT);
		} catch(JSONException je) {
			result = DEFAULT_RESULT;
		}
	}
	public RenRenStatusSetResponseBean(Parcel in) {
		result = in.readInt();
	}
	
	public RenRenStatusSetResponseBean(int result) {
		this.result = result;
	}

	public int getResult() {
		return result;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(result);
	}

	public static final Parcelable.Creator<RenRenStatusSetResponseBean> CREATOR
             = new Parcelable.Creator<RenRenStatusSetResponseBean>() {
         public RenRenStatusSetResponseBean createFromParcel(Parcel in) {
             return new RenRenStatusSetResponseBean(in);
         }

         public RenRenStatusSetResponseBean[] newArray(int size) {
             return new RenRenStatusSetResponseBean[size];
         }
    };

	@Override
	public String toString() {
		return "result: " + result;
	}
}
