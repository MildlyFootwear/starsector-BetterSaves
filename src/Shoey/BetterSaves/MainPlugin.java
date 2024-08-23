package Shoey.BetterSaves;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.characters.PersonAPI;
import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.campaign.CampaignEngine;
import com.fs.starfarer.api.campaign.CampaignClockAPI;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.lang.System;


public class MainPlugin extends BaseModPlugin {

    static String launchSaveDir = "";
    static boolean needToReset = false;
    static Thread RS = new Thread(new ResetScript());
    static boolean justSaved = false;
    static boolean latestSaving = false;
    static PersonAPI p;
    private Logger thislog = Global.getLogger(this.getClass());
    public void setSaveDir()
    {
        thislog.setLevel(Level.INFO);
        thislog.info("Setting save directory.");
        if (p == null)
            return;
        System.setProperty("com.fs.starfarer.settings.paths.saves", launchSaveDir + "\\" + p.getNameString()+"_"+p.getId());
        thislog.setLevel(Level.INFO);
        thislog.info("Set save directory property to "+System.getProperty("com.fs.starfarer.settings.paths.saves"));
        needToReset = true;
    }

    @Override
    public void onApplicationLoad() throws Exception {
        super.onApplicationLoad();
        thislog.setLevel(Level.INFO);
        thislog.info("Setting launchSaveDir.");
        launchSaveDir = System.getProperty("com.fs.starfarer.settings.paths.saves");
        RS.start();
        thislog.setLevel(Level.INFO);
        thislog.info("Set launchSaveDir to "+launchSaveDir);
    }

    @Override
    public void onGameLoad(boolean b) {
        super.onGameLoad(b);
        thislog.setLevel(Level.INFO);
        thislog.info("Running onGameLoad.");
        try {
            if (!CampaignEngine.getInstance().isIronMode()) {
                p = CampaignEngine.getInstance().getPlayerPerson();
                setSaveDir();
            } else p = null;

        } catch ( Exception e )
        {
            p = null;
            thislog.setLevel(Level.ERROR);
            thislog.info(e.getMessage());
        }
    }

    @Override
    public void beforeGameSave()
    {
        super.beforeGameSave();
        thislog.setLevel(Level.INFO);
        thislog.info("Running beforeGameSave.");
        if (p == null)
            return;
        if (latestSaving)
        {
            System.setProperty("com.fs.starfarer.settings.paths.saves", launchSaveDir);
            CampaignEngine.getInstance().setSaveDirName("latest_" + p.getNameString()+"_"+p.getId());
            thislog.setLevel(Level.INFO);
            thislog.info("Set the save subdirectory to "+CampaignEngine.getInstance().getSaveDirName());
            latestSaving = false;
        } else {
            setSaveDir();
            CampaignClockAPI clock = Global.getSector().getClock();
            String savNam = p.getNameString()+" c"+clock.getCycle()+" ";
            int temp = clock.getMonth();
            if (temp > 9)
                savNam += temp + " ";
            else
                savNam += "0" + temp + " ";
            temp = clock.getDay();
            if (temp > 9)
                savNam += temp + " ";
            else
                savNam += "0" + temp + " ";
            temp = clock.getHour();
            if (temp > 9)
                savNam += temp;
            else
                savNam += "0" + temp;

            CampaignEngine.getInstance().setSaveDirName(savNam);

            thislog.setLevel(Level.INFO);
            thislog.info("Set the save subdirectory to "+CampaignEngine.getInstance().getSaveDirName());

        }
    }

    @Override
    public void afterGameSave()
    {
        super.afterGameSave();
        thislog.setLevel(Level.INFO);
        thislog.info("Running afterGameSave.");
        if (p == null)
            return;
        if (justSaved)
        {
            justSaved = false;
            setSaveDir();
        } else {
            justSaved = true;
            latestSaving = true;

            thislog.setLevel(Level.INFO);
            thislog.info("Saving to character slot in root directory.");
            Global.getSector().getCampaignUI().cmdSave();

        }
    }
}
