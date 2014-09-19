//Brett Gear
//MDF3 1409

package com.fullsail.widget;

import java.io.Serializable;

public class CharacterItem implements Serializable {

	private static final long serialVersionUID = 517116325584636891L;

	private String mName;
	private String mClass;
	private String mDesc;

	public CharacterItem(String _name, String _class, String _desc) {
        mName = _name;
        mClass = _class;
        mDesc = _desc;
	}
	
	public String getName() {
		return mName;
	}
	
	public String getSpec() {
		return mClass;
	}
	
	public String getDesc() {
		return mDesc;
	}
}