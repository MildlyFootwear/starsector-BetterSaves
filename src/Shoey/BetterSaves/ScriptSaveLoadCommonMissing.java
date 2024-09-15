package Shoey.BetterSaves;

import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.SettingsAPI;

import static Shoey.BetterSaves.MainPlugin.*;

public class ScriptSaveLoadCommonMissing implements EveryFrameScript {

    float timer = 0;
    boolean done = false;
    SettingsAPI settings = Global.getSettings();
    @Override
    public boolean isDone() {
        return done;
    }

    @Override
    public boolean runWhilePaused() {
        return true;
    }

    @Override
    public void advance(float amount) {
        timer += amount;
        if (timer > 2)
        {
            Global.getSector().getCampaignUI().addMessage("BetterSaves: Common settings redirect missing, recommend checking forum thread or readme for assistance.");
            ReadyForCulling = true;
            done = true;
        }
    }
}
