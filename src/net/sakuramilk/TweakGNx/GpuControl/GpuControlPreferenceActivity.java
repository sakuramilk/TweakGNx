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

import net.sakuramilk.TweakGNx.R;
import net.sakuramilk.TweakGNx.Common.Misc;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceChangeListener;
import android.view.Menu;
import android.view.MenuItem;

public class GpuControlPreferenceActivity extends PreferenceActivity
    implements OnPreferenceChangeListener {

    private GpuControlSetting mSetting;
    private ListPreference mFreqList;
    private CheckBoxPreference mSetOnBoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.gpu_control_pref);
        
        mSetting = new GpuControlSetting(this);

        mFreqList = (ListPreference)findPreference(GpuControlSetting.KEY_GPU_FREQ);
        if (mSetting.isEnableFreqCtrl()) {
        	mFreqList.setEnabled(true);
        	String curValue = mSetting.getFreq();
        	String savedValue = mSetting.loadFreq();
        	mFreqList.setValue(curValue);
        	mFreqList.setSummary(Misc.getCurrentAndSavedValueText(this,
        			Misc.getEntryFromEntryValue(mFreqList.getEntries(), mFreqList.getEntryValues(), curValue),
                    Misc.getEntryFromEntryValue(mFreqList.getEntries(), mFreqList.getEntryValues(), savedValue)));
        	mFreqList.setOnPreferenceChangeListener(this);

        	mSetOnBoot = (CheckBoxPreference)findPreference(GpuControlSetting.KEY_GPU_SET_ON_BOOT);
        	mSetOnBoot.setEnabled(true);
        	mSetOnBoot.setChecked(mSetting.loadSetOnBoot());
        	mSetOnBoot.setOnPreferenceChangeListener(this);
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (mFreqList == preference) {
        	String value = newValue.toString();
        	mSetting.setFreq(value);
        	mSetting.saveFreqs(value);
        	mFreqList.setValue(value);
        	mFreqList.setSummary(Misc.getCurrentAndSavedValueText(this,
        			Misc.getEntryFromEntryValue(mFreqList.getEntries(), mFreqList.getEntryValues(), value),
                    Misc.getEntryFromEntryValue(mFreqList.getEntries(), mFreqList.getEntryValues(), value)));

        } else if (mSetOnBoot == preference) {
        	mSetting.saveSetOnBoot((Boolean)newValue);
        	mSetOnBoot.setChecked((Boolean)newValue);

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
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
}
