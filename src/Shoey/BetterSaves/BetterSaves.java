package Shoey.BetterSaves;
import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.Global;
import org.lwjgl.input.Keyboard;
import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.campaign.CampaignEngine;

import java.lang.System;


public class BetterSaves extends BaseModPlugin {

    static String launchSaveDir = "";
    static boolean needToReset = false;
    static Thread RS = new Thread(new ResetScript());

    public void setSaveDir()
    {
        System.setProperty("com.fs.starfarer.settings.paths.saves", launchSaveDir + "\\" + CampaignEngine.getInstance().getPlayerPerson().getId());
        System.out.println("\n\n\nBetterSaves: set save path to "+System.getProperty("com.fs.starfarer.settings.paths.saves")+"\n\n\n");
        needToReset = true;
    }

    @Override
    public void onApplicationLoad() throws Exception {
        super.onApplicationLoad();
        launchSaveDir = System.getProperty("com.fs.starfarer.settings.paths.saves");
        RS.start();
    }

    @Override
    public void onGameLoad(boolean b) {
        super.onGameLoad(b);
        setSaveDir();
    }

//    @Override
//    public void afterGameSave()
//    {
//        super.afterGameSave();
//        if (justSaved)
//        {
//            justSaved = false;
//            CampaignEngine.getInstance().setSaveDirName("default");
//            setSaveDir();
//        } else {
//            justSaved = true;
//            System.setProperty("com.fs.starfarer.settings.paths.saves", launchSaveDir);
//            CampaignEngine.getInstance().setSaveDirName("latest_" + CampaignEngine.getInstance().getPlayerPerson().getId());
//            Global.getSector().getCampaignUI().cmdSave();
//        }
//    }
}
