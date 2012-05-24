/*
 * Copyright (C) 2011-2012 sakuramilk <c.sakuramilk@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.sakuramilk.TweakGNx.GpuControl;

import android.content.Context;
import net.sakuramilk.TweakGNx.Common.Misc;
import net.sakuramilk.TweakGNx.Common.RootProcess;
import net.sakuramilk.TweakGNx.Common.SettingManager;
import net.sakuramilk.TweakGNx.Common.SysFs;

public class GpuControlSetting extends SettingManager {

    public static final String KEY_GPU_FREQ = "gpu_freq_list";
    public static final String KEY_GPU_SET_ON_BOOT = "gpu_freq_set_on_boot";

    public static final String FREQ_307MHz = "0";
    public static final String FREQ_384MHz = "1";
    public static final String FREQ_512MHz = "2";

    private final SysFs mSysFsClkCtrl = new SysFs("/sys/devices/system/cpu/cpu0/cpufreq/gpu_oc");

    public GpuControlSetting(Context context, RootProcess rootProcess) {
        super(context, rootProcess);
    }

    public GpuControlSetting(Context context) {
        this(context, null);
    }

    public boolean isEnableFreqCtrl() {
        return mSysFsClkCtrl.exists();
    }

    public String getFreq() {
        String value = mSysFsClkCtrl.read(mRootProcess);
        if (Misc.isNullOfEmpty(value)) {
        	return FREQ_307MHz;
        }
        return value;
    }

    public void setFreq(String freq) {
    	mSysFsClkCtrl.write(freq, mRootProcess);
    }

    public String loadFreq() {
        return getStringValue(KEY_GPU_FREQ, null);
    }

    public void saveFreqs(String freq) {
        setValue(KEY_GPU_FREQ, freq);
    }

    public boolean loadSetOnBoot() {
        return getBooleanValue(KEY_GPU_SET_ON_BOOT);
    }

    public void saveSetOnBoot(boolean setOnBoot) {
        setValue(KEY_GPU_SET_ON_BOOT, setOnBoot);
    }

    @Override
    public void setOnBoot() {
        if (!loadSetOnBoot()) {
            return;
        }

        if (isEnableFreqCtrl()) {
            String freq = loadFreq();
            if (!Misc.isNullOfEmpty(freq)) {
                setFreq(freq);
            }
        }
    }

    @Override
    public void setRecommend() {
        // noop
    }

    @Override
    public void reset() {
        clearValue(KEY_GPU_FREQ);
        clearValue(KEY_GPU_SET_ON_BOOT);
    }
}
