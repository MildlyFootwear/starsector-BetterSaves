package Shoey.BetterSaves;

import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListenerPurge implements EveryFrameScript {

    boolean done;
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
        timer += amount;
        if (timer > 2)
        {
            Map<String, Integer> counts = new HashMap<>();
            List<CampaignEventListener> listenerstocancel = new ArrayList<>();
            List<String> cancelling = new ArrayList<>();
            for (CampaignEventListener l : Global.getSector().getAllListeners())
            {
                int i = 0;
                if (counts.containsKey(l.getClass().toString()))
                {
                    i = counts.get(l.getClass().toString());
                }
                i++;
                if (i == 50)
                {
                    cancelling.add(l.getClass().toString());
                }
                if (i >= 50 && !listenerstocancel.contains(l)) {
                    listenerstocancel.add(l);
                }
                counts.put(l.getClass().toString(), i);
            }
            for (CampaignEventListener l : Global.getSector().getAllListeners())
            {
                int i = counts.get(l.getClass().toString());
                if (i >= 50 && !listenerstocancel.contains(l)) {
                    listenerstocancel.add(l);
                }
            }
            for (CampaignEventListener l : listenerstocancel)
            {
                Global.getSector().removeListener(l);
            }
            for (String s : cancelling)
                Global.getSector().getCampaignUI().addMessage("Removed "+counts.get(s)+" "+s);
            done = true;
        }
    }
}
