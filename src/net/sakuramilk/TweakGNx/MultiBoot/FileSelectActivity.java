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

package net.sakuramilk.TweakGNx.MultiBoot;

import net.sakuramilk.TweakGNx.Parts.FilePickerActivity;
import android.content.Intent;
import android.util.Log;

public class FileSelectActivity extends FilePickerActivity {

    private static final String TAG = "TweakGN::FileSelectActivity";

    @Override
    public void onFilePicked(String path, String mode) {
        Log.i(TAG, "selected file path = " + path);
        Intent intent = new Intent();
        intent.putExtra("path", path);
        setResult(RESULT_OK, intent); 
        this.finish();
    }
}
