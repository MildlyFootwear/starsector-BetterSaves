package Shoey.BetterSaves;

import lunalib.lunaSettings.LunaSettingsListener;

import static Shoey.BetterSaves.MainPlugin.setLuna;

public class LunaListener implements LunaSettingsListener {
    @Override
    public void settingsChanged(String s) {
        if (s.equals("ShoeyBetterSaves"))
            setLuna();
    }
}
