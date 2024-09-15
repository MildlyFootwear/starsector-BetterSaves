package Shoey.BetterSaves;

import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.Global;
import static Shoey.BetterSaves.MainPlugin.*;

public class CampaignSaveTimer implements EveryFrameScript {

    boolean saved = false;
    float timer = 0;
    @Override
    public boolean isDone() {
        return saved;
    }

    @Override
    public boolean runWhilePaused() {
        return true;
    }

    @Override
    public void advance(float amount) {
        timer += amount;
        if (timer > 0.3 && !Global.getSector().getCampaignUI().isShowingDialog() && !Global.getSector().getCampaignUI().isShowingMenu()) {
            Global.getSector().getCampaignUI().cmdSave();
            saved = true;
        }
    }
}
