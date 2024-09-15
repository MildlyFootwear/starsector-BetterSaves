package Shoey.BetterSaves;

import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.Script;
import com.fs.starfarer.api.campaign.CampaignEventListener;
import com.fs.starfarer.api.campaign.CampaignUIAPI;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static Shoey.BetterSaves.MainPlugin.*;

public class PromptListenerCull implements EveryFrameScript {

    boolean done = false;
    boolean didStuff = false;
    float timer = 0;
    Map<String, Integer> counts = new HashMap<>();
    List<CampaignEventListener> listenerstoremove = new ArrayList<>();
    List<String> remove = new ArrayList<>();
    int startingListenerCount = 0;
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
        Logger log = Global.getLogger(this.getClass());
        log.setLevel(logLevel);
        if (currentListener.isEmpty() && ReadyForCulling)
            timer += amount;
        if (timer > 1) {
            timer = 0;
            if (counts.isEmpty())
            {
                for (CampaignEventListener l : Global.getSector().getAllListeners()) {
                    int i = 0;
                    if (counts.containsKey(l.getClass().toString())) {
                        i = counts.get(l.getClass().toString());
                    }
                    i++;
                    if (i == 50) {
                        remove.add(l.getClass().toString());
                    }
                    if (i >= 50 && !listenerstoremove.contains(l)) {
                        listenerstoremove.add(l);
                    }
                    counts.put(l.getClass().toString(), i);
                }
                for (CampaignEventListener l : Global.getSector().getAllListeners()) {
                    int i = counts.get(l.getClass().toString());
                    if (i >= 50 && !listenerstoremove.contains(l)) {
                        listenerstoremove.add(l);
                    }
                }
            }
            CampaignUIAPI cUI = Global.getSector().getCampaignUI();
            if (!remove.isEmpty() && !cUI.isShowingMenu() && !cUI.isShowingDialog()) {
                if (startingListenerCount == 0)
                    startingListenerCount = Global.getSector().getAllListeners().size();
                currentListener = remove.get(0);
                String m = "BetterSaves Listener Culler\n\nRemove " + (counts.get(currentListener) - 1) + " instances of " + currentListener + "?";

                if (remove.size() > 1)
                    m += "\n"+(remove.size()-1)+" listeners to go.";

                if (!Global.getSector().getCampaignUI().showConfirmDialog(m, "Yes", "No", new Script() {
                    @Override
                    public void run() {
                        int i = 0;
                        for (CampaignEventListener l : listenerstoremove) {
                            if (!l.getClass().toString().equals(currentListener))
                                continue;
                            if (i == 0)
                                i++;
                            else
                                Global.getSector().removeListener(l);
                        }
                        currentListener = "";
                    }
                }, new Script() {
                    @Override
                    public void run() {
                        currentListener = "";
                    }
                }))
                    return;
                didStuff = true;
                remove.remove(currentListener);
            }
            if (currentListener.isEmpty())
            {
                if (didStuff) {
                    String m = "BetterSaves Listener Culler\n\n";

                    if (startingListenerCount != Global.getSector().getAllListeners().size())
                        m += "Removed " + (startingListenerCount - Global.getSector().getAllListeners().size()) + " scripts.\n";

                    m += "You can now disable the Listener Culler in Luna's Mod Settings menu if you don't want to see anymore prompts. Do note that if you do that, older saves won't be checked for this issue unless you re-enable it.\n\nSorry for the inconvenience this caused.";

                    Global.getSector().getCampaignUI().showMessageDialog(m);
                }
                ReadyForSavePrompting = true;
                done = true;
            }
        }
    }
}
