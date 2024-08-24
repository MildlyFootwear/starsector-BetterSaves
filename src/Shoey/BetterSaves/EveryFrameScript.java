package Shoey.BetterSaves;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.fs.starfarer.api.GameState;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseEveryFrameCombatPlugin;
import com.fs.starfarer.api.input.InputEventAPI;

import java.util.List;

import static Shoey.BetterSaves.MainPlugin.*;


public class EveryFrameScript extends BaseEveryFrameCombatPlugin {

    Logger thislog = Global.getLogger(this.getClass());
    float timer = 0;
    @Override
    public void advance(float amount, List<InputEventAPI> events) {
        super.advance(amount, events);
        timer += amount;
        if (timer > 0.5)
        {
            timer = 0;
            if (!runningCode)
            {
                GameState currentState = Global.getCurrentState();
                if (GameState.TITLE == currentState && needToReset && !launchSaveDir.isEmpty()) {
                    thislog.setLevel(Level.INFO);
                    thislog.info("Resetting save path.");
                    needToReset = false;
                    System.setProperty("com.fs.starfarer.settings.paths.saves", launchSaveDir);
                    p = null;
                    thislog.setLevel(Level.INFO);
                    thislog.info("Save path reset to "+System.getProperty("com.fs.starfarer.settings.paths.saves"));
                }
            }
        }
    }
}
