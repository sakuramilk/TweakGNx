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

package net.sakuramilk.TweakGNx.SoundAndVib;

import net.sakuramilk.TweakGNx.R;
import net.sakuramilk.TweakGNx.Common.Misc;
import net.sakuramilk.TweakGNx.Parts.SeekBarPreference;
import net.sakuramilk.TweakGNx.Parts.SeekBarPreference.OnSeekBarPreferenceDoneListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.view.Menu;
import android.view.MenuItem;

public class HwVolumePreferenceActivity extends PreferenceActivity
    implements OnSeekBarPreferenceDoneListener {

    private HwVolumeSetting mSetting;
    private SeekBarPreference mAvolHp;
    private SeekBarPreference mAvolHpGain;
    private SeekBarPreference mAvolRc;
    private SeekBarPreference mAvolSp;
    private SeekBarPreference mDvolDacMaster;
    private SeekBarPreference mDvolDacAtt;
    private SeekBarPreference mDvolDir0;
    private SeekBarPreference mDvolDir0Att;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.hw_volume_pref);

        mSetting = new HwVolumeSetting(this);

        mAvolHp = (SeekBarPreference)findPreference(HwVolumeSetting.KEY_AVOL_HP);
        if (mSetting.isEnableAvolHp()) {
            mAvolHp.setEnabled(true);
            String curValue = mSetting.loadAvolHp();
            mAvolHp.setSummary(Misc.getCurrentValueText(this, curValue));
            mAvolHp.setValue(HwVolumeSetting.AVOL_HP_MAX, HwVolumeSetting.AVOL_HP_MIN, Integer.parseInt(curValue));
            mAvolHp.setOnPreferenceDoneListener(this);
        }

        mAvolHpGain = (SeekBarPreference)findPreference(HwVolumeSetting.KEY_AVOL_HP_GAIN);
        if (mSetting.isEnableAvolHpGain()) {
            mAvolHpGain.setEnabled(true);
            String curValue = mSetting.loadAvolHpGain();
            mAvolHpGain.setSummary(Misc.getCurrentValueText(this, curValue));
            mAvolHpGain.setValue(HwVolumeSetting.AVOL_HP_GAIN_MAX, HwVolumeSetting.AVOL_HP_MIN, Integer.parseInt(curValue));
            mAvolHpGain.setOnPreferenceDoneListener(this);
        }

        mAvolRc = (SeekBarPreference)findPreference(HwVolumeSetting.KEY_AVOL_RC);
        if (mSetting.isEnableAvolRc()) {
            mAvolRc.setEnabled(true);
            String curValue = mSetting.getAvolRc();
            mAvolRc.setSummary(Misc.getCurrentValueText(this, curValue));
            mAvolRc.setValue(HwVolumeSetting.AVOL_RC_MAX, HwVolumeSetting.AVOL_RC_MIN, Integer.parseInt(curValue));
            mAvolRc.setOnPreferenceDoneListener(this);
        }

        mAvolSp = (SeekBarPreference)findPreference(HwVolumeSetting.KEY_AVOL_SP);
        if (mSetting.isEnableAvolSp()) {
            mAvolSp.setEnabled(true);
            String curValue = mSetting.getAvolSp();
            mAvolSp.setSummary(Misc.getCurrentValueText(this, curValue));
            mAvolSp.setValue(HwVolumeSetting.AVOL_SP_MAX, HwVolumeSetting.AVOL_SP_MIN, Integer.parseInt(curValue));
            mAvolSp.setOnPreferenceDoneListener(this);
        }

        mDvolDacMaster = (SeekBarPreference)findPreference(HwVolumeSetting.KEY_DVOL_DAC_MASTER);
        if (mSetting.isEnableDvolDacMaster()) {
            mDvolDacMaster.setEnabled(true);
            String curValue = mSetting.getDvolDacMaster();
            mDvolDacMaster.setSummary(Misc.getCurrentValueText(this, curValue));
            mDvolDacMaster.setValue(HwVolumeSetting.DVOL_DAC_MASTER_MAX, HwVolumeSetting.DVOL_DAC_MASTER_MIN, Integer.parseInt(curValue));
            mDvolDacMaster.setOnPreferenceDoneListener(this);
        }

        mDvolDacAtt = (SeekBarPreference)findPreference(HwVolumeSetting.KEY_DVOL_DAC_ATT);
        if (mSetting.isEnableDvolDacAtt()) {
            mDvolDacAtt.setEnabled(true);
            String curValue = mSetting.getDvolDacAtt();
            mDvolDacAtt.setSummary(Misc.getCurrentValueText(this, curValue));
            mDvolDacAtt.setValue(HwVolumeSetting.DVOL_DAC_ATT_MAX, HwVolumeSetting.DVOL_DAC_ATT_MIN, Integer.parseInt(curValue));
            mDvolDacAtt.setOnPreferenceDoneListener(this);
        }

        mDvolDir0 = (SeekBarPreference)findPreference(HwVolumeSetting.KEY_DVOL_DIR0);
        if (mSetting.isEnableDvolDir0()) {
            mDvolDir0.setEnabled(true);
            String curValue = mSetting.getDvolDir0();
            mDvolDir0.setSummary(Misc.getCurrentValueText(this, curValue));
            mDvolDir0.setValue(HwVolumeSetting.DVOL_DIR0_MAX, HwVolumeSetting.DVOL_DIR0_MIN, Integer.parseInt(curValue));
            mDvolDir0.setOnPreferenceDoneListener(this);
        }

        mDvolDir0Att = (SeekBarPreference)findPreference(HwVolumeSetting.KEY_DVOL_DIR0_ATT);
        if (mSetting.isEnableDvolDir0Att()) {
            mDvolDir0Att.setEnabled(true);
            String curValue = mSetting.getDvolDir0Att();
            mDvolDir0Att.setSummary(Misc.getCurrentValueText(this, curValue));
            mDvolDir0Att.setValue(HwVolumeSetting.DVOL_DIR0_ATT_MAX, HwVolumeSetting.DVOL_DIR0_ATT_MIN, Integer.parseInt(curValue));
            mDvolDir0Att.setOnPreferenceDoneListener(this);
        }
    }

    @Override
    public boolean onPreferenceDone(Preference preference, String newValue) {
        if (mAvolHp == preference) {
            mSetting.setAvolHp(newValue);
            mAvolHp.setSummary(Misc.getCurrentValueText(this, newValue));
            mSetting.updateVolume();
            return true;

        } else if (mAvolHpGain == preference) {
            mSetting.setAvolHpGain(newValue);
            mAvolHpGain.setSummary(Misc.getCurrentValueText(this, newValue));
            mSetting.updateVolume();
            return true;

        } else if (mAvolRc == preference) {
            mSetting.setAvolRc(newValue);
            mAvolRc.setSummary(Misc.getCurrentValueText(this, newValue));
            mSetting.updateVolume();
            return true;

        } else if (mAvolSp == preference) {
            mSetting.setAvolSp(newValue);
            mAvolSp.setSummary(Misc.getCurrentValueText(this, newValue));
            mSetting.updateVolume();
            return true;

        } else if (mDvolDacMaster == preference) {
            mSetting.setDvolDacMaster(newValue);
            mDvolDacMaster.setSummary(Misc.getCurrentValueText(this, newValue));
            mSetting.updateVolume();
            return true;

        } else if (mDvolDacAtt == preference) {
            mSetting.setDvolDacAtt(newValue);
            mDvolDacAtt.setSummary(Misc.getCurrentValueText(this, newValue));
            mSetting.updateVolume();
            return true;

        } else if (mDvolDir0 == preference) {
            mSetting.setDvolDir0(newValue);
            mDvolDir0.setSummary(Misc.getCurrentValueText(this, newValue));
            mSetting.updateVolume();
            return true;

        } else if (mDvolDir0Att == preference) {
            mSetting.setDvolDir0Att(newValue);
            mDvolDir0Att.setSummary(Misc.getCurrentValueText(this, newValue));
            mSetting.updateVolume();
            return true;

        }
        return false;
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean ret = super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.reset_menu, menu);
        return ret;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.menu_reset:
            mSetting.reset();
            mAvolHp.setSummary(Misc.getCurrentValueText(this, "0"));
            mAvolHpGain.setSummary(Misc.getCurrentValueText(this, "0"));
            mAvolRc.setSummary(Misc.getCurrentValueText(this, "0"));
            mAvolSp.setSummary(Misc.getCurrentValueText(this, "0"));
            mDvolDacMaster.setSummary(Misc.getCurrentValueText(this, "0"));
            mDvolDacAtt.setSummary(Misc.getCurrentValueText(this, "0"));
            mDvolDir0.setSummary(Misc.getCurrentValueText(this, "0"));
            mDvolDir0Att.setSummary(Misc.getCurrentValueText(this, "0"));
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
}
