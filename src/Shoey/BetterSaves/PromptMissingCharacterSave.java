package Shoey.BetterSaves;

import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.Script;
import com.fs.starfarer.api.campaign.CampaignUIAPI;
import com.fs.starfarer.api.campaign.SectorAPI;

import static Shoey.BetterSaves.MainPlugin.*;

public class PromptMissingCharacterSave implements EveryFrameScript {

    boolean done = false;
    float timer = 0;

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
        CampaignUIAPI cUI = Global.getSector().getCampaignUI();
        if (ReadyForSavePrompting && !cUI.isShowingMenu() && !cUI.isShowingDialog())
            timer += amount;
        if (timer > 1)
        {
            if (!Global.getSector().getCampaignUI().showConfirmDialog("The last save on this character is not present in their sub-folder.\n\nSave now?", "Save", "Cancel", new Script() {
                @Override
                public void run() {
                    justSaved = true;
                    Global.getSector().addTransientScript(new CampaignSaveTimer());
                }
            }, new Script() {
                @Override
                public void run() {

                }
            }))
                return;
            done = true;
        }
    }
}
