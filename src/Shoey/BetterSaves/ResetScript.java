package Shoey.BetterSaves;

import com.fs.starfarer.api.GameState;
import com.fs.starfarer.api.Global;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.lang.System;

import static Shoey.BetterSaves.MainPlugin.*;

public class ResetScript implements Runnable {
    @Override
    public void run()
    {
        boolean primeToReset = false;
        Logger thislog = Global.getLogger(this.getClass());
        thislog.setLevel(Level.INFO);
        thislog.info("Reset thread started.");
        while (true)
        {
            try{Thread.sleep(500);} catch (InterruptedException e) {
                thislog.setLevel(Level.ERROR);
                thislog.info(e.getMessage());
            }
            try {
                GameState currentState = Global.getCurrentState();
                if (GameState.TITLE == currentState && needToReset && primeToReset) {
                    thislog.info("Resetting save path.");
                    MainPlugin.needToReset = false;
                    primeToReset = false;
                    System.setProperty("com.fs.starfarer.settings.paths.saves", launchSaveDir);
                    p = null;
                    thislog.setLevel(Level.INFO);
                    thislog.info("Save path reset to "+System.getProperty("com.fs.starfarer.settings.paths.saves"));
                }
                if (GameState.TITLE != currentState && needToReset && !primeToReset) {
                    thislog.info("Primed to reset.");
                    primeToReset = true;
                }
            } catch (Exception e) {
                thislog.setLevel(Level.ERROR);
                thislog.info(e.getMessage());
            }
        }
    }
}
