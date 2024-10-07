package io.github.yagiyuu.linextra

import android.app.Activity
import android.content.res.Resources
import android.view.View
import android.view.ViewGroup
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam
import io.github.yagiyuu.linextra.Util.hideView

class Main : IXposedHookLoadPackage {
    companion object {
        private const val PACKAGE_NAME = "jp.naver.line.android"

        // bnb_timeline=VOOM, bnb_news=News, bnb_wallet=Wallet, bnb_home=Home, bnb_chat=Talk
        private val HIDDEN_TAB_IDS = arrayOf("bnb_timeline", "bnb_news", "bnb_wallet")
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

        XposedBridge.hookAllMethods(
            XposedHelpers.findClass(
                "jp.naver.line.android.activity.main.MainActivity",
                loadPackageParam.classLoader
            ),
            "onCreate",
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    val activity = param.thisObject as? Activity ?: return
                    val tabContainer = activity.findViewById<ViewGroup>(
                        activity.resources.getIdentifier(
                            "main_tab_container",
                            "id",
                            PACKAGE_NAME
                        )
                    ) ?: return

                    for (i in 0 until tabContainer.childCount) {
                        val childView = tabContainer.getChildAt(i)
                        val name = try {
                            activity.resources.getResourceEntryName(childView.id)
                        } catch (_: Resources.NotFoundException) {
                            continue
                        }
                        if (HIDDEN_TAB_IDS.any { name.startsWith(it) }) {
                            childView.hideView()
                        }
                    }
                }
            }
        )
    }
}