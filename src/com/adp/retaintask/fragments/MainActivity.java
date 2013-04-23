package com.adp.retaintask.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

import com.adp.retaintask.R;

/**
 * The MainActivity's only responsibility is to instantiate and display the
 * UiFragment to the screen. This activity will be destroyed and re-created on
 * configuration changes.
 */
public class MainActivity extends FragmentActivity {
  @SuppressWarnings("unused")
  private static final String TAG = MainActivity.class.getSimpleName();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (savedInstanceState == null) {
      FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
      ft.add(android.R.id.content, new UiFragment()).commit();
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.activity_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.menu_change_font_size:
        startActivity(new Intent(Settings.ACTION_DISPLAY_SETTINGS));
        return true;
    }
    return super.onOptionsItemSelected(item);
  }
}