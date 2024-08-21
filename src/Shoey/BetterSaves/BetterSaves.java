package Shoey.BetterSaves;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.characters.PersonAPI;
import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.campaign.CampaignEngine;
import com.fs.starfarer.api.campaign.CampaignClockAPI;

import java.lang.System;


public class BetterSaves extends BaseModPlugin {

    static String launchSaveDir = "";
    static boolean needToReset = false;
    static Thread RS = new Thread(new ResetScript());
    static boolean justSaved = false;
    static boolean latestSaving = false;
    static PersonAPI p;


    public void setSaveDir()
    {
        if (p == null)
            return;
        System.setProperty("com.fs.starfarer.settings.paths.saves", launchSaveDir + "\\" + p.getNameString()+"_"+p.getId());
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
        if (!CampaignEngine.getInstance().isIronMode())
        {
            p = CampaignEngine.getInstance().getPlayerPerson();
            setSaveDir();
        } else p = null;
    }

    @Override
    public void beforeGameSave()
    {
        super.beforeGameSave();
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
    }

    @Override
    public void afterGameSave()
    {
        super.afterGameSave();
        if (p == null)
            return;
        if (justSaved)
        {
            justSaved = false;
            setSaveDir();
        } else {
            justSaved = true;
            latestSaving = true;
            Global.getSector().getCampaignUI().cmdSave();
        }
    }
}
