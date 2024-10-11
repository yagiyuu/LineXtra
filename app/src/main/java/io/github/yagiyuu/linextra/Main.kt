package io.github.yagiyuu.linextra

import android.view.View
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam
import io.github.yagiyuu.linextra.Util.hideView

class Main : IXposedHookLoadPackage {
    companion object {
        private const val PACKAGE_NAME = "jp.naver.line.android"
    }

    override fun handleLoadPackage(loadPackageParam: LoadPackageParam) {
        if (loadPackageParam.packageName != PACKAGE_NAME) return

        XposedBridge.hookAllMethods(
            XposedHelpers.findClass(
                "com.linecorp.line.admolin.smartch.v2.view.SmartChannelViewLayout",
                loadPackageParam.classLoader
            ),
            "dispatchDraw",
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    val view = param.thisObject as? View ?: return
                    view.hideView()
                }
            }
        )

        XposedBridge.hookAllMethods(
            XposedHelpers.findClass(
                "com.linecorp.line.ladsdk.ui.common.view.lifecycle.LadAdView",
                loadPackageParam.classLoader
            ),
            "onAttachedToWindow",
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    val view = param.thisObject as? View ?: return
                    if (view.visibility != View.GONE) {
                        view.hideView()
                    }
                }
            }
        )
    }
}
