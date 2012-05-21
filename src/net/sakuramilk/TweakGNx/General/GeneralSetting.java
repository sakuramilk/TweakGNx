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

package net.sakuramilk.TweakGNx.General;

import android.content.Context;

import net.sakuramilk.TweakGNx.Common.Misc;
import net.sakuramilk.TweakGNx.Common.RootProcess;
import net.sakuramilk.TweakGNx.Common.SettingManager;
import net.sakuramilk.TweakGNx.Common.SysFs;
import net.sakuramilk.TweakGNx.Common.SystemCommand;

public class GeneralSetting extends SettingManager {

    public static final String KEY_IO_SCHED = "iosched_type";
    public static final String KEY_GSM_NETWORK_TWEAK = "gsm_network_tweak";
    
    private static final String PATH_IO_SCHED_MMC0 = "/sys/devices/platform/omap/omap_hsmmc.0/mmc_host/mmc0/mmc0:0001/block/mmcblk0/queue/scheduler";
    private static final String PATH_IO_SCHED_SPI3 = "/sys/devices/platform/omap/omap2_mcspi.3/spi_master/spi3/spi3.0/mtd/mtd0/mtdblock0/queue/scheduler";
    private final SysFs mSysFsIoShedulerMmc0;
    private final SysFs mSysFsIoShedulerSpi3;

    public GeneralSetting(Context context, RootProcess rootProcess) {
        super(context, rootProcess);
        mSysFsIoShedulerMmc0 = new SysFs(PATH_IO_SCHED_MMC0);
        mSysFsIoShedulerSpi3 = new SysFs(PATH_IO_SCHED_SPI3);
    }

    public GeneralSetting(Context context) {
        this(context, null);
    }

    public String getCurrentIoScheduler() {
        String value = mSysFsIoShedulerMmc0.read(mRootProcess);
        if (value != null) {
            String list[] = value.split(" ");
            for (String ioSched : list) {
                if (ioSched.charAt(0) == '[') {
                    return ioSched.substring(1, ioSched.length() - 1); 
                }
            }
        }
        return "";
    }

    public String[] getIoSchedulerList() {
        String value = mSysFsIoShedulerMmc0.read(mRootProcess);
        if (value != null) {
            String list[] = value.split(" ");
            for (int i = 0; i < list.length ; i++) {
                if (list[i].charAt(0) == '[') {
                    list[i] = list[i].substring(1, list[i].length() - 1); 
                }
            }
            return list;
        }
        return null;
    }

    public void setIoScheduler(String value) {
    	mSysFsIoShedulerMmc0.write(value, mRootProcess);
    	mSysFsIoShedulerSpi3.write(value, mRootProcess);
    }

    public String loadIoScheduler() {
        return getStringValue(KEY_IO_SCHED);
    }

    public void saveIoScheduler(String value) {
        setValue(KEY_IO_SCHED, value);
    }

    public void setGsmNetworkTweak() {
        SystemCommand.gsm_network_tweak();
    }

    public boolean loadGsmNetworkTweak() {
        return getBooleanValue(KEY_GSM_NETWORK_TWEAK, false);
    }

    public void saveGsmNetworkTweak(boolean value) {
        setValue(KEY_GSM_NETWORK_TWEAK, value);
    }

    @Override
    public void setOnBoot() {
        String value = loadIoScheduler();
        if (!Misc.isNullOfEmpty(value)) {
            setIoScheduler(value);
        }
        if (loadGsmNetworkTweak()) {
            SystemCommand.gsm_network_tweak();
        }
    }

    @Override
    public void setRecommend() {
        String[] list = getIoSchedulerList();
        for (String sched : list) {
            if ("vr".equals(sched)) {
                setIoScheduler("vr");
                saveIoScheduler("vr");
            }
        }
        setGsmNetworkTweak();
        saveGsmNetworkTweak(true);
    }

    @Override
    public void reset() {
        clearValue(KEY_IO_SCHED);
        clearValue(KEY_GSM_NETWORK_TWEAK);
    }
}
