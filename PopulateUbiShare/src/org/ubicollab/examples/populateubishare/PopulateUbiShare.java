package org.ubicollab.examples.populateubishare;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class PopulateUbiShare extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_populate_ubi_share);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_populate_ubi_share, menu);
		return true;
	}

}
