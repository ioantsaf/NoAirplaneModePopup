package com.john.noairplanemodepopup;


import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;


public class NoAirplaneModePopup implements IXposedHookLoadPackage {

    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {

        if (!lpparam.packageName.equals("com.android.systemui"))
            return;

        // Galaxy S5, Galaxy Note 4 etc.
        try {
            findAndHookMethod("com.android.systemui.qs.tiles.AirplaneModeTile", lpparam.classLoader, "showConfirmPopup",
                    boolean.class,
                    new XC_MethodReplacement() {
                        protected Object replaceHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                            XposedHelpers.callMethod(param.thisObject, "setEnabled", param.args[0]);
                            // XposedBridge.log("NoAirplaneModePopup: showConfirmPopup setEnabled: " + param.args[0]);
                            return null;
                        }
                    });
        } catch (Throwable e) {
            // XposedBridge.log("NoAirplaneModePopup: " + e);
        }

        // Galaxy S6, Galaxy S7 etc.
        try {
            XposedHelpers.setStaticBooleanField(XposedHelpers.findClass("com.android.systemui.statusbar.Feature", lpparam.classLoader),
                    "mShowAirplaneModeONPopup", false);
            // XposedBridge.log("NoAirplaneModePopup: mShowAirplaneModeONPopup set false");
        } catch (Throwable e) {
            // XposedBridge.log("NoAirplaneModePopup: " + e);
        }

    }
}
