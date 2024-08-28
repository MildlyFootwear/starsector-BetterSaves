package Shoey.BetterSaves;

import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.Global;

public class CommonMessageTimer implements EveryFrameScript {

    float timer = 0;
    boolean messageShown = false;

    @Override
    public boolean isDone() {
        return messageShown;
    }

    @Override
    public boolean runWhilePaused() {
        return false;
    }

    @Override
    public void advance(float amount) {
        timer += amount;
        if (timer > 2)
        {
            Global.getSector().getCampaignUI().addMessage("BetterSaves: Common settings redirect missing, recommend checking forum thread or readme for assistance.");
            messageShown = true;
        }
    }
}
