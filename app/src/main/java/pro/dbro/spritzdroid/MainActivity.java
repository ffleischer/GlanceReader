package pro.dbro.spritzdroid;

import android.app.ActionBar;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

public class MainActivity extends ActionBarActivity implements WpmDialogFragment.OnWpmSelectListener {
    private static final String PREFS = "ui_prefs";
    private static final int THEME_LIGHT = 0;
    private static final int THEME_DARK = 1;

    private int mWpm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int theme = getSharedPreferences(PREFS, Context.MODE_PRIVATE)
                .getInt("THEME", 0);
        switch(theme){
            case THEME_LIGHT:
                setTheme(R.style.Light);
                break;
            case THEME_DARK:
                setTheme(R.style.Dark);
                break;
        }
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        ActionBar actionBar = getActionBar();
        actionBar.setTitle("");
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new SpritzFragment(), "spritsfrag")
                .commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_speed) {
            if (mWpm == 0) {
                if (((SpritzFragment) getSupportFragmentManager().findFragmentByTag("spritsfrag")).getSpritzer() != null) {
                    mWpm = ((SpritzFragment) getSupportFragmentManager().findFragmentByTag("spritsfrag")).getSpritzer().mWPM;
                }
            }
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            DialogFragment newFragment = WpmDialogFragment.newInstance(mWpm);
            newFragment.show(ft, "dialog");
            return true;
        } else if (id == R.id.action_theme) {
            int theme = getSharedPreferences(PREFS, Context.MODE_PRIVATE)
                    .getInt("THEME", THEME_LIGHT);
            if (theme == THEME_LIGHT) {
                applyDarkTheme();
            } else {
                applyLightTheme();
            }
        } else if (id == R.id.action_open) {
            ((SpritzFragment) getSupportFragmentManager().findFragmentByTag("spritsfrag")).chooseEpub();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onWpmSelected(int wpm) {
        if (((SpritzFragment) getSupportFragmentManager().findFragmentByTag("spritsfrag")).getSpritzer() != null) {
            ((SpritzFragment) getSupportFragmentManager().findFragmentByTag("spritsfrag")).getSpritzer()
                    .setWpm(wpm);
        }
        mWpm = wpm;
    }

    private void applyDarkTheme() {
        getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit()
                .putInt("THEME", THEME_DARK)
                .commit();
        recreate();
//        ((TextView) findViewById(R.id.spritzText)).setTextColor(Color.WHITE);
//        ((TextView) findViewById(R.id.spritzText)).setBackgroundColor(Color.BLACK);
    }

    private void applyLightTheme() {
        getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit()
                .putInt("THEME", THEME_LIGHT)
                .commit();
        recreate();
//        ((TextView) findViewById(R.id.spritzText)).setTextColor(Color.BLACK);
//        ((TextView) findViewById(R.id.spritzText)).setBackgroundColor(Color.WHITE);
    }
}