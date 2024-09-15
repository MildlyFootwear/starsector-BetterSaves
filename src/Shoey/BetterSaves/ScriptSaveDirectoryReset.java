package Shoey.BetterSaves;

import com.fs.starfarer.api.SettingsAPI;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import org.apache.log4j.Logger;

import com.fs.starfarer.api.GameState;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseEveryFrameCombatPlugin;
import com.fs.starfarer.api.input.InputEventAPI;

import java.io.IOException;
import java.util.List;

import static Shoey.BetterSaves.MainPlugin.*;


public class ScriptSaveDirectoryReset extends BaseEveryFrameCombatPlugin {

    Logger log = Global.getLogger(this.getClass());
    float timer = 0;

    @Override
    public void init(CombatEngineAPI engine) {
        super.init(engine);
        log.setLevel(logLevel);
    }

    @Override
    public void advance(float amount, List<InputEventAPI> events) {
        super.advance(amount, events);
        timer += amount;
        if (Global.getCurrentState() == GameState.TITLE && timer > 0.5)
        {
            timer = 0;
            if (!runningCode)
            {
                if (justSaved)
                {
                    SettingsAPI settings = Global.getSettings();
                    log.info("save and exited");
                    String fileString = "";

                    if (p!=null) {
                        fileString += " " + p.getId();
                    }

                    if (settings.fileExistsInCommon("BetterSaves/Save and Exited IDs.txt")) {
                        try {
                            fileString += settings.readTextFileFromCommon("BetterSaves/Save and Exited IDs.txt");
                        } catch (IOException e) {
                            log.error(e.getMessage());
                        }
                    }
                    try {
                        settings.writeTextFileToCommon("BetterSaves/Save and Exited IDs.txt", fileString);
                        log.info("Wrote ID to prompt save on load.");
                    } catch (IOException e) {
                        log.error(e.getMessage());
                    }

                    justSaved = false;
                }
                if (needToReset && !launchSaveDir.isEmpty()) {
                    log.info("Resetting save path.");
                    needToReset = false;
                    System.setProperty("com.fs.starfarer.settings.paths.saves", launchSaveDir);
                    p = null;
                    log.info("Save path reset to "+System.getProperty("com.fs.starfarer.settings.paths.saves"));
                }
            }
        }
    }
}
