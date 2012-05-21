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

import java.util.ArrayList;

import net.sakuramilk.TweakGNx.R;
import net.sakuramilk.TweakGNx.Common.Misc;
import net.sakuramilk.TweakGNx.Parts.ApplyButtonPreferenceActivity;
import net.sakuramilk.TweakGNx.Parts.SeekBarPreference;
import net.sakuramilk.TweakGNx.Parts.SeekBarPreference.OnSeekBarPreferenceDoneListener;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.preference.Preference.OnPreferenceChangeListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

public class GpuControlPreferenceActivity extends ApplyButtonPreferenceActivity
    implements OnSeekBarPreferenceDoneListener, OnClickListener, OnPreferenceChangeListener {

    private GpuControlSetting mSetting;

    private Integer mCurFreqs[] = null;
    private Integer mCurVolts[] = null;
    private Integer mCurThresholds[] = null;
    private Integer mSavedFreqs[] = null;
    private Integer mSavedVolts[] = null;
    private Integer mSavedThresholds[] = null;
    private int mFreqStep;
    private int mThresholdStep;
    private ArrayList<SeekBarPreference> mFreqPrefList;
    private ArrayList<SeekBarPreference> mThresholdPrefList;
    private ArrayList<SeekBarPreference> mVoltPrefList;
    private CheckBoxPreference mSetOnBoot;
    private boolean mSetOnBootChecked;

    private void setMaxMinValue() {
        if (mFreqPrefList != null) {
            for (int i = 0; i < mFreqPrefList.size(); i++) {
                SeekBarPreference pref = mFreqPrefList.get(i);
                pref.setSummary(Misc.getCurrentAndSavedValueText(
                        this, String.valueOf(mCurFreqs[i]) + "MHz", String.valueOf(mSavedFreqs[i]) + "MHz"));
                if (i == 0) {
                    pref.setValue(mSavedFreqs[i+1], GpuControlSetting.FREQ_MIN, mSavedFreqs[i]);
                } else if (i == (mFreqPrefList.size() - 1)) {
                    pref.setValue(GpuControlSetting.FREQ_MAX, mSavedFreqs[i-1], mSavedFreqs[i]);
                } else {
                    pref.setValue(mSavedFreqs[i+1], mSavedFreqs[i-1], mSavedFreqs[i]);
                }
            }
        }
        if (mThresholdPrefList != null) {
            for (int i = 0; i < mThresholdPrefList.size(); i++) {
                SeekBarPreference pref = mThresholdPrefList.get(i);
                pref.setSummary(Misc.getCurrentAndSavedValueText(
                        this, String.valueOf(mCurThresholds[i]) + "%", String.valueOf(mSavedThresholds[i]) + "%"));
                pref.setValue(GpuControlSetting.THRESHOLD_MAX, GpuControlSetting.THRESHOLD_MIN, mSavedThresholds[i]);
            }
        }
        if (mVoltPrefList != null) {
            for (int i = 0; i < mVoltPrefList.size(); i++) {
                SeekBarPreference pref = mVoltPrefList.get(i);
                pref.setSummary(Misc.getCurrentAndSavedValueText(
                        this, String.valueOf(mCurVolts[i]) + "mV", String.valueOf(mSavedVolts[i]) + "mV"));
                if (i == 0) {
                    pref.setValue(mSavedVolts[i+1], GpuControlSetting.VOLT_MIN, mSavedVolts[i]);
                } else if (i == (mVoltPrefList.size() - 1)) {
                    pref.setValue(GpuControlSetting.VOLT_MAX, mCurVolts[i-1], mSavedVolts[i]);
                } else {
                    pref.setValue(mSavedVolts[i+1], mSavedVolts[i-1], mSavedVolts[i]);
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.gpu_control_pref);

        PreferenceManager prefManager = getPreferenceManager();
        PreferenceScreen rootPref = (PreferenceScreen)prefManager.findPreference(GpuControlSetting.KEY_ROOT_PREF);

        mSetting = new GpuControlSetting(this);
        if (mSetting.isEnableFreqCtrl()) {
            mCurFreqs = mSetting.getFreqs();
            mSavedFreqs = mSetting.loadFreqs();
            if (mSavedFreqs == null) {
                mSavedFreqs = mCurFreqs.clone();
            }
            if (mCurFreqs.length != mSavedFreqs.length) {
                mSetting.reset();
                mSavedFreqs = mCurFreqs.clone();
            }
            mFreqPrefList = new ArrayList<SeekBarPreference>();
            for (int i = 0; i < mCurFreqs.length; i++) {
                SeekBarPreference pref = new SeekBarPreference(this, null);
                pref.setKey(GpuControlSetting.KEY_GPU_FREQ_BASE + i);
                pref.setTitle(R.string.frequency);
                pref.setDialogTitle(R.string.frequency);
                pref.setOnPreferenceDoneListener(this);
                mFreqPrefList.add(pref);
            }
            mFreqStep = mCurFreqs.length;

            mCurThresholds = mSetting.getThresholds();
            if (mCurThresholds != null && mCurThresholds.length != 0) {
                mSavedThresholds = mSetting.loadThresholds();
                if (mSavedThresholds == null) {
                    mSavedThresholds = mCurThresholds.clone();
                }
                mThresholdPrefList = new ArrayList<SeekBarPreference>();
                for (int i = 0; i < mCurThresholds.length; i++) {
                    SeekBarPreference pref = new SeekBarPreference(this, null);
                    pref.setKey(GpuControlSetting.KEY_GPU_THRESHOLD_BASE + i);
                    pref.setOnPreferenceDoneListener(this);
                    mThresholdPrefList.add(pref);
                }
                mThresholdStep = mCurThresholds.length;
            }
        }
        if (mSetting.isEnableVoltageCtrl()) {
            mCurVolts = mSetting.getVolts();
            mSavedVolts = mSetting.loadVolts();
            if (mSavedVolts == null) {
                mSavedVolts = mCurVolts.clone();
            }
            mVoltPrefList = new ArrayList<SeekBarPreference>();
            for (int i = 0; i < mCurVolts.length; i++) {
                SeekBarPreference pref = new SeekBarPreference(this, null);
                pref.setKey(GpuControlSetting.KEY_GPU_VOLT_BASE + i);
                pref.setTitle(R.string.voltage);
                pref.setDialogTitle(R.string.voltage);
                pref.setOnPreferenceDoneListener(this);
                mVoltPrefList.add(pref);
            }
        }

        int j = 0;
        for (int i = 0; i < mFreqStep; i++) {
            PreferenceCategory categoryPref = new PreferenceCategory(this);
            categoryPref.setTitle("Step" + i);
            rootPref.addPreference(categoryPref);
            if (mFreqPrefList != null) {
                rootPref.addPreference(mFreqPrefList.get(i));
            }
            if (mVoltPrefList != null) {
                rootPref.addPreference(mVoltPrefList.get(i));
            }
            if (mThresholdPrefList != null) {
                if (j == 0) {
                    SeekBarPreference pref = mThresholdPrefList.get(j++);
                    pref.setTitle(R.string.up_threshold);
                    pref.setDialogTitle(R.string.up_threshold);
                    rootPref.addPreference(pref);
                } else if (j == mThresholdStep - 1) {
                    SeekBarPreference pref = mThresholdPrefList.get(j++);
                    pref.setTitle(R.string.down_threshold);
                    pref.setDialogTitle(R.string.down_threshold);
                    rootPref.addPreference(pref);
                } else {
                    SeekBarPreference pref = mThresholdPrefList.get(j++);
                    pref.setTitle(R.string.up_threshold);
                    pref.setDialogTitle(R.string.up_threshold);
                    rootPref.addPreference(pref);
                    pref = mThresholdPrefList.get(j++);
                    pref.setTitle(R.string.down_threshold);
                    pref.setDialogTitle(R.string.down_threshold);
                    rootPref.addPreference(pref);
                }
            }
        }
        
        
        
        if (mFreqStep > 0) {
            PreferenceCategory categoryPref = new PreferenceCategory(this);
            categoryPref.setTitle(R.string.option);
            mSetOnBoot = new CheckBoxPreference(this);
            mSetOnBoot.setTitle(R.string.set_on_boot);
            rootPref.addPreference(mSetOnBoot);
            mSetOnBootChecked = mSetting.loadSetOnBoot();
            mSetOnBoot.setOnPreferenceChangeListener(this);
            mSetOnBoot.setChecked(mSetOnBootChecked);
        }
        mApplyButton.setOnClickListener(this);

        setMaxMinValue();
    }

    @Override
    public boolean onPreferenceDone(Preference preference, String newValue) {

        int value = Integer.parseInt((String)newValue);
        if (mFreqPrefList != null) {
            int index = mFreqPrefList.indexOf(preference);
            if (index != -1) {
                if (mSavedFreqs[index] != value) {
                    mSavedFreqs[index] = value;
                    setMaxMinValue();
                    mApplyButton.setEnabled(true);
                }
                return false; // don't return true
            }
        }
        if (mThresholdPrefList != null) {
            int index = mThresholdPrefList.indexOf(preference);
            if (index != -1) {
                if (mSavedThresholds[index] != value) {
                    mSavedThresholds[index] = value;
                    setMaxMinValue();
                    mApplyButton.setEnabled(true);
                }
                return false; // don't return true
            }
        }
        if (mVoltPrefList != null) {
            int index = mVoltPrefList.indexOf(preference);
            if (index != -1) {
                if (mSavedVolts[index] != value) {
                    mSavedVolts[index] = value;
                    setMaxMinValue();
                    mApplyButton.setEnabled(true);
                }
                return false; // don't return true
            }
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        mApplyButton.setEnabled(false);
        mSetOnBootChecked = mSetOnBoot.isChecked();
        mSetting.saveSetOnBoot(mSetOnBootChecked);

        // NOTICE: set first volt, next freq
        if (mSavedVolts != null) {
            mSetting.saveVolts(mSavedVolts);
            mSetting.setVolts(mSavedVolts);
            mCurVolts = mSavedVolts.clone();
        }
        if (mSavedFreqs != null) {
            mSetting.saveFreqs(mSavedFreqs);
            mSetting.setFreqs(mSavedFreqs);
            mCurFreqs = mSavedFreqs.clone();
        }
        if (mSavedThresholds != null) {
            mSetting.saveThresholds(mSavedThresholds);
            mSetting.setThresholds(mSavedThresholds);
            mCurThresholds = mSavedThresholds.clone();
        }
        setMaxMinValue();
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        mApplyButton.setEnabled(true);
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mApplyButton.isEnabled()) {
            mSetting.saveSetOnBoot(mSetOnBootChecked);
        }
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
            Misc.confirmReboot(this, R.string.reboot_reflect_comfirm);
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
}
