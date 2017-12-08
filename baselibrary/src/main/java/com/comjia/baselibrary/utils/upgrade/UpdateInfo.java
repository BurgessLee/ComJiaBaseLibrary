package com.comjia.baselibrary.utils.upgrade;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 应用更新信息
 */
public class UpdateInfo {

    /**
     * 是否有新版本
     */
    public boolean hasUpdate = false;

    /**
     * 是否静默下载：有新版本时不提示直接下载
     */
    public boolean isSilent = false;

    /**
     * 是否强制安装：不安装无法使用app
     */
    public boolean isForce = false;

    /**
     * 是否下载完成后自动安装
     */
    public boolean isAutoInstall = true;

    /**
     * 是否可忽略该版本
     */
    public boolean isIgnorable = true;

    /**
     * 更新提醒：一天内最大提示次数，<1时不限
     */
    public int maxTimes = 0;

    public int versionCode;
    public String versionName;

    public String updateContent;

    public String url;
    public String md5;
    public long size;

    public static UpdateInfo parse(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        return parse(jsonObject.has("data") ? jsonObject.getJSONObject("data") : jsonObject);
    }

    private static UpdateInfo parse(JSONObject jsonObject) {
        UpdateInfo info = new UpdateInfo();
        if (jsonObject == null) {
            return info;
        }

        info.hasUpdate = jsonObject.optBoolean("hasUpdate", false);
        if (!info.hasUpdate) {
            return info;
        }

        info.isSilent = jsonObject.optBoolean("isSilent", false);
        info.isForce = jsonObject.optBoolean("isForce", false);
        info.isAutoInstall = jsonObject.optBoolean("isAutoInstall", !info.isSilent);
        info.isIgnorable = jsonObject.optBoolean("isIgnorable", true);

        info.versionCode = jsonObject.optInt("versionCode", 0);
        info.versionName = jsonObject.optString("versionName");
        info.updateContent = jsonObject.optString("updateContent");

        info.url = jsonObject.optString("url");
        info.md5 = jsonObject.optString("md5");
        info.size = jsonObject.optLong("size", 0);

        return info;
    }
}