/**
 * 
 */
package com.open.share.renren;

import java.util.Comparator;

import org.apache.http.message.BasicNameValuePair;

/**
 * @author Administrator
 *
 */
public class RenrenComparator implements Comparator<BasicNameValuePair> {

	@Override
	public int compare(BasicNameValuePair p1, BasicNameValuePair p2) {
		int result = p1.getName().compareTo(p2.getName());
        if (0 == result)
            result = p1.getValue().compareTo(p2.getValue());
        return result;
	}

}
