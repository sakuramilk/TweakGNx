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

package net.sakuramilk.TweakGNx.Common;

import java.io.File;

import android.util.Log;

public class SystemCommand {

    private static final String TAG = "TweakGNx::SystemCommand";

    public static void reboot(String action) {
        Log.d(TAG, "execute reboot action=" + action);

        RootProcess process = new RootProcess();
        if (!process.init()) {
            return;
        }

        process.write("sync\n");
        process.write("sync\n");
        process.write("sync\n");

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
        }

        if ("recovery".equals(action) || "download".equals(action)) {
            process.write("reboot " + action + "\n");
        } else {
            process.write("reboot\n");
        }

        process.term();
    }

    public static void install_zip(String targetZip) {
        Log.d(TAG, "execute install_zip target=" + targetZip);

        RootProcess process = new RootProcess();
        if (!process.init()) {
            return;
        }
        process.write("echo \"install_zip(\\\"" + targetZip + "\\\");\" > /cache/recovery/extendedcommand\n");
        process.term();
    }

    public static void backup_rom(String targetDir) {
        Log.d(TAG, "execute backup_rom target=" + targetDir);

        RootProcess process = new RootProcess();
        if (!process.init()) {
            return;
        }
        process.write("echo \"backup_rom(\\\"" + targetDir + "\\\");\" >> /cache/recovery/extendedcommand\n");
        process.term();
    }

    public static void restore_rom(String targetDir) {
        Log.d(TAG, "execute restore_rom target=" + targetDir);

        RootProcess process = new RootProcess();
        if (!process.init()) {
            return;
        }
        process.write("echo \"restore_rom(\\\"" + targetDir + "\\\");\" > /cache/recovery/extendedcommand\n");
        process.term();
    }

    public static void copy(String src, String dst) {
        Log.d(TAG, "execute copy src=" + src + " dst=" + dst);

        RootProcess process = new RootProcess();
        if (!process.init()) {
            return;
        }
        process.write("cp " + src + " " + dst + "\n");
        process.term();
    }

    public static void move(String src, String dst) {
        Log.d(TAG, "execute move src=" + src + " dst=" + dst);

        RootProcess process = new RootProcess();
        if (!process.init()) {
            return;
        }
        process.write("mv " + src + " " + dst + "\n");
        process.term();
    }
    
    public static void mkdir(String path) {
        Log.d(TAG, "execute mkdir path=" + path);

        RootProcess process = new RootProcess();
        if (!process.init()) {
            return;
        }
        process.write("mkdir -p " + path + "\n");
        process.term();
    }

    public static void touch(String path, String permission) {
        Log.d(TAG, "execute mkdir path=" + path);

        RootProcess process = new RootProcess();
        if (!process.init()) {
            return;
        }
        process.write("touch " + path + "\n");
        process.write("chmod " + permission + " " + path + "\n");
        process.term();
    }

    public static void start_dock() {
        RootProcess process = new RootProcess();
        if (!process.init()) {
            return;
        }
        process.write("export LD_LIBRARY_PATH=/vendor/lib:/system/lib\n");
        process.write("am broadcast -a android.intent.action.DOCK_EVENT --ei android.intent.extra.DOCK_STATE 1\n");
        process.term();
    }

    public static void stop_dock() {
        RootProcess process = new RootProcess();
        if (!process.init()) {
            return;
        }
        process.write("export LD_LIBRARY_PATH=/vendor/lib:/system/lib\n");
        process.write("am broadcast -a android.intent.action.DOCK_EVENT --ei android.intent.extra.DOCK_STATE 0\n");
        process.term();
    }

    public static void gsm_network_tweak() {
        Log.d(TAG, "execute gsm_network_tweak");

        RootProcess process = new RootProcess();
        if (!process.init()) {
            return;
        }
        process.write("echo 0 > /proc/sys/net/ipv4/tcp_ecn\n");
        process.write("echo 1 > /proc/sys/net/ipv4/route/flush\n");
        process.write("echo 1 > /proc/sys/net/ipv4/tcp_rfc1337\n");
        process.write("echo 0 > /proc/sys/net/ipv4/ip_no_pmtu_disc\n");
        process.write("echo 1 > /proc/sys/net/ipv4/tcp_sack\n");
        process.write("echo 1 > /proc/sys/net/ipv4/tcp_fack\n");
        process.write("echo 1 > /proc/sys/net/ipv4/tcp_window_scaling\n");
        process.write("echo 1 > /proc/sys/net/ipv4/tcp_timestamps\n");
        process.write("echo 4096 39000 187000 > /proc/sys/net/ipv4/tcp_rmem\n");
        process.write("echo 4096 39000 187000 > /proc/sys/net/ipv4/tcp_wmem\n");
        process.write("echo 187000 187000 187000 > /proc/sys/net/ipv4/tcp_mem\n");
        process.write("echo 1 > /proc/sys/net/ipv4/tcp_no_metrics_save\n");
        process.write("echo 1 > /proc/sys/net/ipv4/tcp_moderate_rcvbuf\n");
        process.term();
    }

    public static void mount(String device, String mountPoint, String format, String option) {
        RootProcess process = new RootProcess();
        if (!process.init()) {
            return;
        }
        String command = "mount ";
        if (!Misc.isNullOfEmpty(format)) {
            command += "-t " + format + " ";
        }
        if (!Misc.isNullOfEmpty(option)) {
            command += "-o " + option + " ";
        }
        command += device + " " + mountPoint + "\n";
        process.write(command);
        process.term();
    }

    public static void umount(String mountPoint) {
        RootProcess process = new RootProcess();
        if (!process.init()) {
            return;
        }
        process.write("umount " + mountPoint + "\n");
        process.term();
    }

    public static void remount_system_rw() {
        Log.d(TAG, "execute remount_system_rw");

        RootProcess process = new RootProcess();
        if (!process.init()) {
            return;
        }
        process.write("mount -o rw,remount /system\n");
        process.term();
    }

    public static void remount_system_ro() {
        Log.d(TAG, "execute remount_system_ro");

        RootProcess process = new RootProcess();
        if (!process.init()) {
            return;
        }
        process.write("mount -o ro,remount /system\n");
        process.term();
    }

    public static String uname(String option) {
        String[] ret = RuntimeExec.execute("uname " + option + "\n", true);
        return ret[0];
    }

    public static String get_prop(String key, String defaultValue) {
        Log.d(TAG, "execute set_prop key=" + key + " value=" + defaultValue);

        RootProcess process = new RootProcess();
        if (!process.init()) {
            return defaultValue;
        }
        process.write("getprop " + key + "\n");
        String[] ret = process.read();
        process.term();
        if (ret != null && ret.length > 0) {
            return ret[0];
        }
        return defaultValue;
    }

    public static String get_prop(String key) {
        return get_prop(key, null);
    }

    public static void set_prop(String key, String value) {
        Log.d(TAG, "execute set_prop key=" + key + " value=" + value);

        RootProcess process = new RootProcess();
        if (!process.init()) {
            return;
        }
        process.write("setprop " + key + " " + value + "\n");
        process.term();
    }

    public static void make_ext4_image(String path, int blockSize, int blockCount) {
        Log.d(TAG, "execute make_ext4_image path=" + path +
                " blockSize=" + blockSize+ " blockCount=" + blockCount);

        RootProcess process = new RootProcess();
        if (!process.init()) {
            return;
        }
        process.write("dd if=/dev/zero of=" + path +
                " bs=" + blockSize + " count=" + blockCount + "\n");
        process.write("mke2fs -T ext4 -F " + path + "\n");
        process.term();
    }

    public static void sbl_backup(String path) {
        Log.d(TAG, "execute sbl_backup path=" + path);

        RootProcess process = new RootProcess();
        if (!process.init()) {
            return;
        }
        process.write("dd if=/dev/block/platform/omap/omap_hsmmc.0/by-name/sbl of=" + path + " bs=4096\n");
        File file = new File(path);
        String dir = file.getParent();
        process.write("cd " + dir + "\n");
        process.write("md5sum " + file.getName() + " > " + file.getName() + ".md5\n");
        process.term();
    }

    public static void efs_backup(String path) {
        Log.d(TAG, "execute efs_backup path=" + path);

        RootProcess process = new RootProcess();
        if (!process.init()) {
            return;
        }
        process.write("dd if=/dev/block/platform/omap/omap_hsmmc.0/by-name/efs of=" + path + " bs=4096\n");
        File file = new File(path);
        String dir = file.getParent();
        process.write("cd " + dir + "\n");
        process.write("md5sum " + file.getName() + " > " + file.getName() + ".md5\n");
        process.term();
    }

    public static String df(String device) {
        String ret[] = RuntimeExec.execute("df " + device + " | grep " + device + "\n", true);
        return ret[0];
    }
}
