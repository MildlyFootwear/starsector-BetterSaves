package Shoey.BetterSaves;

//import com.fs.starfarer.api.GameState;
//import com.fs.starfarer.api.Global;
//import org.apache.log4j.Level;
//import org.apache.log4j.Logger;
//
//import java.lang.System;
//
//
//import static Shoey.BetterSaves.MainPlugin.*;
//
//public class ResetThread implements Runnable {
//    @Override
//    public void run()
//    {
//        boolean primeToReset = false;
//        Logger thislog = Global.getLogger(this.getClass());
//        thislog.setLevel(Level.INFO);
//        thislog.info("Reset thread started.");
//        while (true)
//        {
////            thislog.setLevel(Level.INFO);
////            thislog.info("Running check at "+new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date()));
//            if (runningCode)
//            {
//                thislog.setLevel(Level.INFO);
//                thislog.info("Code in MainPlugin is running, skipping loop.");
//
//                try{Thread.sleep(1000);} catch (InterruptedException e) {
//                    thislog.setLevel(Level.ERROR);
//                    thislog.info(e.getMessage());
//                }
//                continue;
//            }
//
//            GameState currentState = Global.getCurrentState();
//            if (GameState.TITLE == currentState && needToReset && primeToReset && !launchSaveDir.isEmpty()) {
//                thislog.info("Resetting save path.");
//                needToReset = false;
//                primeToReset = false;
//                System.setProperty("com.fs.starfarer.settings.paths.saves", launchSaveDir);
//                p = null;
//                thislog.setLevel(Level.INFO);
//                thislog.info("Save path reset to "+System.getProperty("com.fs.starfarer.settings.paths.saves"));
//            }
//            if (GameState.TITLE != currentState && needToReset && !primeToReset) {
//                thislog.setLevel(Level.INFO);
//                thislog.info("Primed to reset.");
//                primeToReset = true;
//            }
//            try{Thread.sleep(1000);} catch (InterruptedException e) {
//                thislog.setLevel(Level.ERROR);
//                thislog.info(e.getMessage());
//            }
//        }
//    }
//}
