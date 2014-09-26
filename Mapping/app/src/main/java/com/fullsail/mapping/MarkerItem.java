//Brett Gear
//MDF3 1409

package com.fullsail.mapping;

import android.net.Uri;

import java.io.Serializable;

public class MarkerItem implements Serializable {

	private static final long serialVersionUID = 517116325582936891L;

	private String mLat;
	private String mLong;
	private String mDesc1;
    private String mDesc2;
    private String mUri;

	public MarkerItem(String _lat, String _long, String _desc1, String _desc2, String _uri) {

        mLat = _lat;
        mLong = _long;
        mDesc1 = _desc1;
        mDesc2 = _desc2;
        mUri = _uri;

	}
    public String getLat() {
        return mLat;
    }

    public String getLong() {
        return mLong;
    }

	public String getDesc1() {
		return mDesc1;
	}

    public String getDesc2() {return mDesc2; }

    public String getUri() { return mUri; }
	

}