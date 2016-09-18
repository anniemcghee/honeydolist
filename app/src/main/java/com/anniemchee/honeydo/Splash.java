package com.anniemchee.honeydo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import java.util.logging.Handler;

/**
 * Created by anmcghee on 9/17/16.
 */
public class Splash {

        private final int SPLASH_DISPLAY_LENGTH = 1000;
        @Override
        public void onCreate(Bundle icicle) {
            onCreate(icicle);
            setContentView(R.layout.splashscreen);

            new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {
                    Intent mainIntent = new Intent(Splash.this, Menu.class);
                    Splash.this.startActivity(mainIntent);
                    honeydo.Splash.this.finish();
                }
            }, SPLASH_DISPLAY_LENGTH);
        }
    }
}
