package Shoey.BetterSaves;
import com.fs.starfarer.api.GameState;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.SettingsAPI;
import com.fs.starfarer.api.characters.PersonAPI;
import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.campaign.CampaignEngine;
import com.fs.starfarer.api.campaign.CampaignClockAPI;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.lang.System;

import lunalib.lunaSettings.LunaSettings;

public class MainPlugin extends BaseModPlugin {

    public static String launchSaveDir = "";
    public static boolean needToReset = false;
    public static boolean justSaved = false;
    public static boolean runningCode = false;
    public static boolean ListenerCulling = true;
    public static String currentListener = "";
    public static PersonAPI p;
    private final Logger log = Global.getLogger(this.getClass());
    public static Level logLevel = Level.INFO;

    public static boolean ReadyForCulling = true;
    public static boolean ReadyForSavePrompting = true;

    public void setSaveDir()
    {
        log.debug("Attempting to set save directory.");
        if (p == null)
            return;
        System.setProperty("com.fs.starfarer.settings.paths.saves", launchSaveDir + "/" + p.getNameString()+"_"+p.getId());
        log.info("Set save directory property to "+System.getProperty("com.fs.starfarer.settings.paths.saves"));
        needToReset = true;
    }

    public static void setLuna()
    {
        if (Boolean.TRUE.equals(LunaSettings.getBoolean("ShoeyBetterSaves","Debugging")))
            logLevel = Level.DEBUG;
        else
            logLevel =  Level.INFO;
        ListenerCulling = Boolean.TRUE.equals(LunaSettings.getBoolean("ShoeyBetterSaves","ListenerCulling"));
        if (Global.getCurrentState() == GameState.CAMPAIGN && ListenerCulling) {
            Global.getSector().addTransientScript(new PromptListenerCull());
            ReadyForCulling = true;
        }

    }
    
    @Override
    public void onApplicationLoad() throws Exception {
        super.onApplicationLoad();
        setLuna();
        log.setLevel(logLevel);

        SettingsAPI settings = Global.getSettings();

        if (!settings.fileExistsInCommon("rootCommon"))
            settings.writeTextFileToCommon("rootCommon", "This file is used so BetterSaves can identify if the common directory under saves has been configured properly.");

        log.debug("Setting launchSaveDir.");
        launchSaveDir = System.getProperty("com.fs.starfarer.settings.paths.saves");
        log.debug("Set launchSaveDir to "+launchSaveDir);

        LunaSettings.addSettingsListener(new LunaListener());

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
            log.setLevel(logLevel);
            log.info(e.getMessage());
        }

        SettingsAPI settings = Global.getSettings();

        if (!settings.fileExistsInCommon("rootCommon")) {
            ScriptSaveLoadCommonMissing s = new ScriptSaveLoadCommonMissing();
            log.info("rootCommon not found");
            Global.getSector().addTransientScript(s);
        }

        if (ListenerCulling)
        {
            PromptListenerCull s = new PromptListenerCull();
            Global.getSector().addTransientScript(s);
            ReadyForSavePrompting = false;
        }

        if (settings.fileExistsInCommon("BetterSaves/Save and Exited IDs.txt"))
        {
            String fileString = "";
            try {
                fileString = settings.readTextFileFromCommon("BetterSaves/Save and Exited IDs.txt");
            } catch (IOException e) {
                log.debug(e.getMessage());
            }
            if (fileString.contains(p.getId()))
            {
                PromptMissingCharacterSave s = new PromptMissingCharacterSave();
                Global.getSector().addTransientScript(s);
                log.info("Found character ID in Save and Exited IDs");
            }
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
            SettingsAPI settings = Global.getSettings();

            if (settings.fileExistsInCommon("BetterSaves/Save and Exited IDs.txt"))
            {
                String fileString = "";
                try {
                    fileString = settings.readTextFileFromCommon("BetterSaves/Save and Exited IDs.txt");

                    if (fileString.contains(p.getId()))
                    {
                        fileString = fileString.replaceAll(" "+p.getId(),"");
                        settings.writeTextFileToCommon("BetterSaves/Save and Exited IDs.txt",fileString);
                    }
                } catch (IOException e) {
                    log.debug(e.getMessage());
                }
            }
        }
        runningCode = false;
    }
}
