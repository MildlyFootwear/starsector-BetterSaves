package Shoey.BetterSaves;

import com.fs.starfarer.api.GameState;
import com.fs.starfarer.api.Global;
import java.lang.System;
import java.util.Map;

import static Shoey.BetterSaves.BetterSaves.*;

public class ResetScript implements Runnable {
    @Override
    public void run()
    {
        boolean primeToReset = false;
        System.out.println("BetterSaves: reset thread started.");
        while (true)
        {
            try{Thread.sleep(500);} catch (InterruptedException e) {
                System.out.println("\n\n\nBetterSaves: "+e.getMessage()+"\n\n\n");
            }
            try {
                if (GameState.TITLE != Global.getCurrentState() && needToReset && !primeToReset) {
                    primeToReset = true;
                    System.out.println("\n\n\nBetterSaves: primed to reset save directory\n\n\n");
                }
                if (GameState.TITLE == Global.getCurrentState() && needToReset && primeToReset) {
                    BetterSaves.needToReset = false;
                    primeToReset = false;
                    System.setProperty("com.fs.starfarer.settings.paths.saves", launchSaveDir);
                    p = null;
                    System.out.println("\n\n\nBetterSaves: save path reset to " + launchSaveDir + "\n\n\n");
                }
            } catch (Exception e) {
                System.out.println("\n\n\nBetterSaves: "+e.getMessage()+"\n\n\n");
            }
        }
    }
}
