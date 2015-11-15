package com.gtracker;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Login extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		Button submitBtn = (Button) findViewById(R.id.buttonSubmit);
		submitBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent openMainIntent = new Intent(Login.this, Main.class);
				startActivity(openMainIntent);
			}
		});
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		SQLiteDatabase db = openOrCreateDatabase("GPS", MODE_PRIVATE, null);
		db.execSQL("CREATE TABLE IF NOT EXISTS Coordinates (Lat VARCHAR, Lon VARCHAR);");
		if(db!=null)
		{
			///Log the DB first (for DEBUG)
			Cursor c = db.rawQuery("SELECT * FROM Coordinates", null);
			c.moveToFirst();
			do
			{
				Log.d("ME", c.getString(0) +"\t" + c.getString(1));
			}while(c.moveToNext());
			
			db.close();
			
		}
		android.os.Process.killProcess(android.os.Process.myPid());
	    super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
