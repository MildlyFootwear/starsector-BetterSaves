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
        System.out.println("BetterSaves: reset thread started.");
        while (true)
        {
            try{Thread.sleep(500);} catch (InterruptedException e) {
                Logger thislog = Global.getLogger(this.getClass());
                thislog.setLevel(Level.ERROR);
                thislog.info(e.getMessage());
            }
            try {
                if (GameState.TITLE != Global.getCurrentState() && needToReset && !primeToReset) {
                    primeToReset = true;
                }
                if (GameState.TITLE == Global.getCurrentState() && needToReset && primeToReset) {
                    MainPlugin.needToReset = false;
                    primeToReset = false;
                    System.setProperty("com.fs.starfarer.settings.paths.saves", launchSaveDir);
                    p = null;
                    Logger thislog = Global.getLogger(this.getClass());
                    thislog.setLevel(Level.INFO);
                    thislog.info("Save path reset to "+System.getProperty("com.fs.starfarer.settings.paths.saves"));
                }
            } catch (Exception e) {
                Logger thislog = Global.getLogger(this.getClass());
                thislog.setLevel(Level.ERROR);
                thislog.info(e.getMessage());
            }
        }
    }
}
