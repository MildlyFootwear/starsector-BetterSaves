package Shoey.BetterSaves;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.SettingsAPI;
import com.fs.starfarer.api.characters.PersonAPI;
import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.campaign.CampaignEngine;
import com.fs.starfarer.api.campaign.CampaignClockAPI;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.lang.System;


public class MainPlugin extends BaseModPlugin {

    public static String launchSaveDir = "";
    public static boolean needToReset = false;
    public static boolean justSaved = false;
    public static boolean latestSaving = false;
    public static boolean runningCode = false;
    public static PersonAPI p;
    private Logger thislog = Global.getLogger(this.getClass());

    public void setSaveDir()
    {
        thislog.debug("Attempting to set save directory.");
        if (p == null)
            return;
        System.setProperty("com.fs.starfarer.settings.paths.saves", launchSaveDir + "/" + p.getNameString()+"_"+p.getId());
        thislog.info("Set save directory property to "+System.getProperty("com.fs.starfarer.settings.paths.saves"));
        needToReset = true;
    }

    @Override
    public void onApplicationLoad() throws Exception {
        super.onApplicationLoad();
        thislog.setLevel(Level.INFO);

        SettingsAPI settings = Global.getSettings();

        if (!settings.fileExistsInCommon("rootCommon"))
            settings.writeTextFileToCommon("rootCommon", "This file is used so BetterSaves can identify if the common directory under saves has been configured properly.");

        thislog.debug("Setting launchSaveDir.");
        launchSaveDir = System.getProperty("com.fs.starfarer.settings.paths.saves");
        thislog.debug("Set launchSaveDir to "+launchSaveDir);
    }

    @Override
    public void onGameLoad(boolean b) {
        super.onGameLoad(b);
        runningCode = true;
        thislog.debug("Running onGameLoad.");
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
        SettingsAPI settings = Global.getSettings();

        if (!settings.fileExistsInCommon("rootCommon")) {
            thislog.info("rootCommon not found");
            Global.getSector().addTransientScript(new CommonMessageTimer());
        }
        runningCode = false;
    }

    @Override
    public void beforeGameSave()
    {
        super.beforeGameSave();
        runningCode = true;
        thislog.debug("Running beforeGameSave.");
        if (p == null)
            return;
        if (latestSaving)
        {
            System.setProperty("com.fs.starfarer.settings.paths.saves", launchSaveDir);
            CampaignEngine.getInstance().setSaveDirName("latest_" + p.getNameString()+"_"+p.getId());
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
        }
        thislog.info("Set the save subdirectory to "+CampaignEngine.getInstance().getSaveDirName());

    }

    @Override
    public void afterGameSave()
    {
        super.afterGameSave();
        thislog.debug("Running afterGameSave.");
        if (p == null)
            return;
        if (justSaved)
        {
            justSaved = false;
            setSaveDir();
            runningCode = false;
        } else {
            justSaved = true;
            latestSaving = true;
            thislog.info("Saving to character slot in root directory.");
            Global.getSector().getCampaignUI().cmdSave();

        }
    }
}
