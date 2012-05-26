package net.sakuramilk.TweakGNx.Service;

import net.sakuramilk.TweakGNx.Common.RootProcess;
import net.sakuramilk.TweakGNx.CpuControl.CpuControlSetting;
import net.sakuramilk.TweakGNx.Dock.DockSetting;
import net.sakuramilk.TweakGNx.General.GeneralSetting;
import net.sakuramilk.TweakGNx.General.LowMemKillerSetting;
import net.sakuramilk.TweakGNx.General.VirtualMemorySetting;
import net.sakuramilk.TweakGNx.GpuControl.GpuControlSetting;
import net.sakuramilk.TweakGNx.SoundAndVib.HwVolumeSetting;
import net.sakuramilk.TweakGNx.SoundAndVib.SoundAndVibSetting;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class BootCompletedService extends Service {

    private static final String TAG = "TweakGN::BootCompletedService";
    private static Context mContext;
    private static BootCompletedThread mThread;

    class BootCompletedThread extends Thread {
        public void run() {
            RootProcess rootProcess = new RootProcess();
            Log.d(TAG, "Root init s");
            rootProcess.init();
            Log.d(TAG, "Root init e");
            
            // check safe mode
    
            // General
            GeneralSetting generalSetting = new GeneralSetting(mContext, rootProcess);
            Log.d(TAG, "start: General Setting");
            generalSetting.setOnBoot();
            LowMemKillerSetting lowMemKillerSetting = new LowMemKillerSetting(mContext, rootProcess);
            Log.d(TAG, "start: LowMemKiller Setting");
            lowMemKillerSetting.setOnBoot();
            VirtualMemorySetting vmSetting = new VirtualMemorySetting(mContext, rootProcess);
            Log.d(TAG, "start: VirtualMemory Setting");
            vmSetting.setOnBoot();
    
            // CpuControl
            CpuControlSetting cpuControlSetting = new CpuControlSetting(mContext, rootProcess);
            Log.d(TAG, "start: CpuControl Setting");
            cpuControlSetting.setOnBoot();
    
            // GpuControl
            GpuControlSetting gpuControlSetting = new GpuControlSetting(mContext, rootProcess);
            Log.d(TAG, "start: GpuControl Setting");
            gpuControlSetting.setOnBoot();
    
            // SoundAndVib
            SoundAndVibSetting soundAndVibSetting = new SoundAndVibSetting(mContext, rootProcess);
            Log.d(TAG, "start: SoundAndVib Setting");
            soundAndVibSetting.setOnBoot();
            HwVolumeSetting hwVolumeSetting = new HwVolumeSetting(mContext, rootProcess);
            Log.d(TAG, "start: HwVolume Setting");
            hwVolumeSetting.setOnBoot();
    
            // Dock
            DockSetting dockSetting = new DockSetting(mContext, rootProcess);
            Log.d(TAG, "start: Dock Setting");
            dockSetting.setOnBoot();
    
            // Display
            //DisplaySetting displaySetting = new DisplaySetting(mContext, rootProcess);
            //Log.d(TAG, "start: Display Setting");
            //displaySetting.setOnBoot();
            
            rootProcess.term();
            rootProcess = null;
        }
    }
    
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int StartId) {
        Log.d(TAG , "OnStart");
        mContext = this;
        mThread = new BootCompletedThread();
        mThread.start();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG , "OnDestroy");
        mThread.interrupt();
        mThread = null;
    }
}
