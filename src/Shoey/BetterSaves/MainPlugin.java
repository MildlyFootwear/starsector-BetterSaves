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
    private Logger log = Global.getLogger(this.getClass());

    public void setSaveDir()
    {
        log.debug("Attempting to set save directory.");
        if (p == null)
            return;
        System.setProperty("com.fs.starfarer.settings.paths.saves", launchSaveDir + "/" + p.getNameString()+"_"+p.getId());
        log.info("Set save directory property to "+System.getProperty("com.fs.starfarer.settings.paths.saves"));
        needToReset = true;
    }

    @Override
    public void onApplicationLoad() throws Exception {
        super.onApplicationLoad();
        log.setLevel(Level.INFO);

        SettingsAPI settings = Global.getSettings();

        if (!settings.fileExistsInCommon("rootCommon"))
            settings.writeTextFileToCommon("rootCommon", "This file is used so BetterSaves can identify if the common directory under saves has been configured properly.");

        log.debug("Setting launchSaveDir.");
        launchSaveDir = System.getProperty("com.fs.starfarer.settings.paths.saves");
        log.debug("Set launchSaveDir to "+launchSaveDir);
    }

    @Override
    public void onGameLoad(boolean b) {
        super.onGameLoad(b);
        runningCode = true;
        log.debug("Running onGameLoad.");
        try {
            if (!CampaignEngine.getInstance().isIronMode()) {
                p = CampaignEngine.getInstance().getPlayerPerson();
                setSaveDir();
            } else p = null;

        } catch ( Exception e )
        {
            p = null;
            log.setLevel(Level.ERROR);
            log.info(e.getMessage());
        }
        SettingsAPI settings = Global.getSettings();
        if (!settings.fileExistsInCommon("rootCommon")) {
            log.info("rootCommon not found");
            Global.getSector().addTransientScript(new CommonMessageTimer());
        }
        runningCode = false;
    }

    @Override
    public void beforeGameSave()
    {
        super.beforeGameSave();
        runningCode = true;
        log.debug("Running beforeGameSave.");
        if (p == null)
            return;
        if (!justSaved)
        {
            System.setProperty("com.fs.starfarer.settings.paths.saves", launchSaveDir);
            CampaignEngine.getInstance().setSaveDirName("latest_" + p.getNameString()+"_"+p.getId());
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
        log.info("Set the save subdirectory to "+CampaignEngine.getInstance().getSaveDirName());

    }

    @Override
    public void afterGameSave()
    {
        super.afterGameSave();
        log.debug("Running afterGameSave, saved to "+CampaignEngine.getInstance().getSaveDirName());
        if (p == null)
            return;
        if (!justSaved) {
            justSaved = true;
            Global.getSector().addTransientScript(new CampaignSaveTimer());
        } else {
            justSaved = false;
            runningCode = false;
        }
    }
}
