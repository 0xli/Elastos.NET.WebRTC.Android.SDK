/*
 *  Copyright 2015 The WebRTC Project Authors. All rights reserved.
 *
 *  Use of this source code is governed by a BSD-style license
 *  that can be found in the LICENSE file in the root of the source
 *  tree. An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */

package org.elastos.carrier.webrtc.demo.apprtc;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Toast;

import androidx.annotation.Nullable;

import org.elastos.carrier.webrtc.WebrtcClient;
import org.elastos.carrier.webrtc.util.WebRTCAudioManager;
import org.elastos.carrier.webrtc.util.WebRTCAudioManager.*;
import org.webrtc.Logging;
import org.webrtc.RendererCommon.ScalingType;
import org.webrtc.SurfaceViewRenderer;
import java.util.Set;


/**
 * Activity for peer connection register setup, register waiting
 * and register view.
 */
public class CallActivity extends Activity implements CallFragment.OnCallEvents {
    private static final String TAG = "CallRTCClient";

    public static final String EXTRA_IS_CALLER = "org.elastos.carrier.webrtc.demo.apprtc.IS_CALLER";
    public static final String EXTRA_ROOMID = "org.elastos.carrier.webrtc.demo.apprtc.ROOMID";

    public static final String EXTRA_VIDEO_CALL = "org.elastos.carrier.webrtc.demo.apprtc.VIDEO_CALL";
    public static final String EXTRA_SCREENCAPTURE = "org.elastos.carrier.webrtc.demo.apprtc.SCREENCAPTURE";
    public static final String EXTRA_CAMERA2 = "org.elastos.carrier.webrtc.demo.apprtc.CAMERA2";
    public static final String EXTRA_VIDEO_WIDTH = "org.elastos.carrier.webrtc.demo.apprtc.VIDEO_WIDTH";
    public static final String EXTRA_VIDEO_HEIGHT = "org.elastos.carrier.webrtc.demo.apprtc.VIDEO_HEIGHT";
    public static final String EXTRA_VIDEO_FPS = "org.elastos.carrier.webrtc.demo.apprtc.VIDEO_FPS";
    public static final String EXTRA_VIDEO_CAPTUREQUALITYSLIDER_ENABLED =
            "org.appsopt.apprtc.VIDEO_CAPTUREQUALITYSLIDER";
    public static final String EXTRA_VIDEO_BITRATE = "org.elastos.carrier.webrtc.demo.apprtc.VIDEO_BITRATE";
    public static final String EXTRA_VIDEOCODEC = "org.elastos.carrier.webrtc.demo.apprtc.VIDEOCODEC";
    public static final String EXTRA_HWCODEC_ENABLED = "org.elastos.carrier.webrtc.demo.apprtc.HWCODEC";
    public static final String EXTRA_CAPTURETOTEXTURE_ENABLED = "org.elastos.carrier.webrtc.demo.apprtc.CAPTURETOTEXTURE";
    public static final String EXTRA_FLEXFEC_ENABLED = "org.elastos.carrier.webrtc.demo.apprtc.FLEXFEC";
    public static final String EXTRA_AUDIO_BITRATE = "org.elastos.carrier.webrtc.demo.apprtc.AUDIO_BITRATE";
    public static final String EXTRA_AUDIOCODEC = "org.elastos.carrier.webrtc.demo.apprtc.AUDIOCODEC";
    public static final String EXTRA_NOAUDIOPROCESSING_ENABLED =
            "org.elastos.carrier.webrtc.demo.apprtc.NOAUDIOPROCESSING";
    public static final String EXTRA_AECDUMP_ENABLED = "org.elastos.carrier.webrtc.demo.apprtc.AECDUMP";
    public static final String EXTRA_SAVE_INPUT_AUDIO_TO_FILE_ENABLED =
            "org.elastos.carrier.webrtc.demo.apprtc.SAVE_INPUT_AUDIO_TO_FILE";
    public static final String EXTRA_OPENSLES_ENABLED = "org.elastos.carrier.webrtc.demo.apprtc.OPENSLES";
    public static final String EXTRA_DISABLE_BUILT_IN_AEC = "org.elastos.carrier.webrtc.demo.apprtc.DISABLE_BUILT_IN_AEC";
    public static final String EXTRA_DISABLE_BUILT_IN_AGC = "org.elastos.carrier.webrtc.demo.apprtc.DISABLE_BUILT_IN_AGC";
    public static final String EXTRA_DISABLE_BUILT_IN_NS = "org.elastos.carrier.webrtc.demo.apprtc.DISABLE_BUILT_IN_NS";
    public static final String EXTRA_DISABLE_WEBRTC_AGC_AND_HPF =
            "org.elastos.carrier.webrtc.demo.apprtc.DISABLE_WEBRTC_GAIN_CONTROL";
    public static final String EXTRA_DISPLAY_HUD = "org.elastos.carrier.webrtc.demo.apprtc.DISPLAY_HUD";
    public static final String EXTRA_TRACING = "org.elastos.carrier.webrtc.demo.apprtc.TRACING";
    public static final String EXTRA_CMDLINE = "org.elastos.carrier.webrtc.demo.apprtc.CMDLINE";
    public static final String EXTRA_RUNTIME = "org.elastos.carrier.webrtc.demo.apprtc.RUNTIME";
    public static final String EXTRA_VIDEO_FILE_AS_CAMERA = "org.elastos.carrier.webrtc.demo.apprtc.VIDEO_FILE_AS_CAMERA";
    public static final String EXTRA_SAVE_REMOTE_VIDEO_TO_FILE =
            "org.elastos.carrier.webrtc.demo.apprtc.SAVE_REMOTE_VIDEO_TO_FILE";
    public static final String EXTRA_SAVE_REMOTE_VIDEO_TO_FILE_WIDTH =
            "org.elastos.carrier.webrtc.demo.apprtc.SAVE_REMOTE_VIDEO_TO_FILE_WIDTH";
    public static final String EXTRA_SAVE_REMOTE_VIDEO_TO_FILE_HEIGHT =
            "org.elastos.carrier.webrtc.demo.apprtc.SAVE_REMOTE_VIDEO_TO_FILE_HEIGHT";
    public static final String EXTRA_USE_VALUES_FROM_INTENT =
            "org.elastos.carrier.webrtc.demo.apprtc.USE_VALUES_FROM_INTENT";
    public static final String EXTRA_DATA_CHANNEL_ENABLED = "org.elastos.carrier.webrtc.demo.apprtc.DATA_CHANNEL_ENABLED";
    public static final String EXTRA_ORDERED = "org.elastos.carrier.webrtc.demo.apprtc.ORDERED";
    public static final String EXTRA_MAX_RETRANSMITS_MS = "org.elastos.carrier.webrtc.demo.apprtc.MAX_RETRANSMITS_MS";
    public static final String EXTRA_MAX_RETRANSMITS = "org.elastos.carrier.webrtc.demo.apprtc.MAX_RETRANSMITS";
    public static final String EXTRA_PROTOCOL = "org.elastos.carrier.webrtc.demo.apprtc.PROTOCOL";
    public static final String EXTRA_NEGOTIATED = "org.elastos.carrier.webrtc.demo.apprtc.NEGOTIATED";
    public static final String EXTRA_ID = "org.elastos.carrier.webrtc.demo.apprtc.ID";
    public static final String EXTRA_ENABLE_RTCEVENTLOG = "org.elastos.carrier.webrtc.demo.apprtc.ENABLE_RTCEVENTLOG";

    private static final int CAPTURE_PERMISSION_REQUEST_CODE = 1;

    // List of mandatory application permissions.
    private static final String[] MANDATORY_PERMISSIONS = {"android.permission.MODIFY_AUDIO_SETTINGS",
            "android.permission.RECORD_AUDIO", "android.permission.INTERNET"};

    // Peer connection statistics callback period in ms.
    private static final int STAT_CALLBACK_PERIOD = 1000;

    @Nullable
    private WebRTCAudioManager audioManager;
    @Nullable
    private SurfaceViewRenderer pipRenderer;
    @Nullable
    private SurfaceViewRenderer fullscreenRenderer;
    private Toast logToast;
    private boolean commandLineRun;
    private boolean activityRunning;

    private boolean isCaller;
    private String remoteUserId; //remote's carrier user id

    private boolean connected;
    private boolean isError;
    private boolean callControlFragmentVisible = true;
    private long callStartedTimeMs;
    private boolean micEnabled = true;
    private boolean speakOn = false;
    private boolean videoOn = true;
    private boolean screencaptureEnabled;
    private static Intent mediaProjectionPermissionResultData;
    private static int mediaProjectionPermissionResultCode;
    // True if local view is in the fullscreen renderer.
    private boolean isSwappedFeeds;

    // Controls
    private CallFragment callFragment;
    private HudFragment hudFragment;
    private CpuMonitor cpuMonitor;

    public static CallActivity INSTANCE;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        INSTANCE  = this;
        Thread.setDefaultUncaughtExceptionHandler(new UnhandledExceptionHandler(this));

        // Set window styles for fullscreen-window size. Needs to be done before
        // adding content.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(LayoutParams.FLAG_FULLSCREEN | LayoutParams.FLAG_KEEP_SCREEN_ON
                | LayoutParams.FLAG_SHOW_WHEN_LOCKED | LayoutParams.FLAG_TURN_SCREEN_ON);
        getWindow().getDecorView().setSystemUiVisibility(getSystemUiVisibility());
        setContentView(R.layout.activity_call);

        final Intent intent = getIntent();

        Uri roomUri = intent.getData();
        if (roomUri == null) {
            logAndToast(getString(R.string.missing_url));
            Log.e(TAG, "Didn't get any URL in intent!");
            setResult(RESULT_CANCELED);
            finish();
            return;
        }

        isCaller = intent.getBooleanExtra(EXTRA_IS_CALLER, false);
        remoteUserId = intent.getStringExtra(EXTRA_ROOMID);

        //update peerConnectionPararmeters from intent.
        updatePeerConnectionParametersFromIntent(intent);

        commandLineRun = intent.getBooleanExtra(EXTRA_CMDLINE, false);
        int runTimeMs = intent.getIntExtra(EXTRA_RUNTIME, 0);

        Log.d(TAG, "VIDEO_FILE: '" + intent.getStringExtra(EXTRA_VIDEO_FILE_AS_CAMERA) + "'");

        connected = false;
        initUIControls(intent);

        // Check for mandatory permissions.
        for (String permission : MANDATORY_PERMISSIONS) {
            if (checkCallingOrSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                logAndToast("Permission " + permission + " is not granted");
                setResult(RESULT_CANCELED);
                finish();
                return;
            }
        }

        Log.d(TAG, "Callee User Id: " + remoteUserId);
        if ((remoteUserId == null || remoteUserId.length() == 0)) {
            logAndToast(getString(R.string.missing_url));
            Log.e(TAG, "Incorrect Callee User Id in intent!");
            setResult(RESULT_CANCELED);
            finish();
            return;
        }

        // WebrtcClient.getInstance().renderVideo(pipRenderer, fullscreenRenderer);
        startCall();

    }

    protected void initUIControls(Intent intent) {
        // Create UI controls.
        pipRenderer = findViewById(R.id.pip_video_view);
        fullscreenRenderer = findViewById(R.id.fullscreen_video_view);
        callFragment = new CallFragment();
        hudFragment = new HudFragment();

        // Show/hide register control fragment on view click.
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleCallControlFragmentVisibility();
            }
        };

        // Swap feeds on pip view click.
        pipRenderer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSwappedFeeds(!isSwappedFeeds);
            }
        });

        fullscreenRenderer.setOnClickListener(listener);

        // Create video renderers.
        pipRenderer.setScalingType(ScalingType.SCALE_ASPECT_FIT);

        fullscreenRenderer.setScalingType(ScalingType.SCALE_ASPECT_FILL);

        pipRenderer.setZOrderMediaOverlay(true);
        pipRenderer.setEnableHardwareScaler(true /* enabled */);
        fullscreenRenderer.setEnableHardwareScaler(false /* enabled */);
        // Start with local feed in fullscreen and swap it to the pip when the register is connected.
        // setSwappedFeeds(true /* isSwappedFeeds */);

        // Create CPU monitor
        if (CpuMonitor.isSupported()) {
            cpuMonitor = new CpuMonitor(this);
            hudFragment.setCpuMonitor(cpuMonitor);
        }

        // Send intent arguments to fragments.
        callFragment.setArguments(intent.getExtras());
        hudFragment.setArguments(intent.getExtras());
        // Activate register and HUD fragments and start the register.
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.call_fragment_container, callFragment);
        ft.add(R.id.hud_fragment_container, hudFragment);
        ft.commit();
    }

    @TargetApi(17)
    private DisplayMetrics getDisplayMetrics() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager =
                (WindowManager) getApplication().getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getRealMetrics(displayMetrics);
        return displayMetrics;
    }

    @TargetApi(19)
    private static int getSystemUiVisibility() {
        int flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            flags |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        }
        return flags;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != CAPTURE_PERMISSION_REQUEST_CODE)
            return;
        mediaProjectionPermissionResultCode = resultCode;
        mediaProjectionPermissionResultData = data;
        // startCall();
    }

    // Activity interfaces
    @Override
    public void onStop() {
        super.onStop();
        activityRunning = false;
        if (cpuMonitor != null) {
            cpuMonitor.pause();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        activityRunning = true;
        if (cpuMonitor != null) {
            cpuMonitor.resume();
        }
    }

    @Override
    protected void onDestroy() {
        Thread.setDefaultUncaughtExceptionHandler(null);
        disconnect();
        if (logToast != null) {
            logToast.cancel();
        }
        activityRunning = false;
        super.onDestroy();
    }

    // CallFragment.OnCallEvents interface implementation.
    @Override
    public void onCallHangUp() {
        WebrtcClient.getInstance().disconnectFromCall();
        disconnect();
    }

    @Override
    public void onCameraSwitch() {
        WebrtcClient.getInstance().switchCamera();
    }

    @Override
    public void onVideoScalingSwitch(ScalingType scalingType) {
        fullscreenRenderer.setScalingType(scalingType);
    }

    @Override
    public void onCaptureFormatChange(int width, int height, int framerate) {
        WebrtcClient.getInstance().setResolution(width, height, framerate);
    }

    @Override
    public boolean onToggleMic() {
        micEnabled = !micEnabled;
        WebrtcClient.getInstance().setAudioEnable(micEnabled);
        return micEnabled;
    }

    @Override
    public boolean onToggleSpeaker() {
        speakOn = !speakOn;
        audioManager.selectAudioDevice(speakOn ? AudioDevice.SPEAKER_PHONE : AudioDevice.EARPIECE);
        return speakOn;
    }

    @Override
    public boolean onToggleVideo() {
        videoOn = !videoOn;
        WebrtcClient.getInstance().setVideoEnable(videoOn);
        return videoOn;
    }

    // Helper functions.
    private void toggleCallControlFragmentVisibility() {
        if (!connected || !callFragment.isAdded()) {
            return;
        }
        // Show/hide register control fragment
        callControlFragmentVisible = !callControlFragmentVisible;
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (callControlFragmentVisible) {
            ft.show(callFragment);
            ft.show(hudFragment);
        } else {
            ft.hide(callFragment);
            ft.hide(hudFragment);
        }
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    private void startCall() {
        callStartedTimeMs = System.currentTimeMillis();

        // Start call connection.
        logAndToast(getString(R.string.connecting_to, remoteUserId));
        if (isCaller) {
            // WebrtcClient.getInstance().inviteCall(remoteUserId);
        } else {
            try {
                Thread.sleep(1000);
                WebrtcClient.getInstance().acceptCallInvite();
            } catch (Exception e) {
                Log.e(TAG, "startCall: ", e);
            }
        }
        WebrtcClient.getInstance().renderVideo(pipRenderer, fullscreenRenderer);

        // webrtcClient.initialCall(isCaller);

        // Create and audio manager that will take care of audio routing,
        // audio modes, audio device enumeration etc.
        audioManager = WebRTCAudioManager.create(getApplicationContext());
        // Store existing audio settings and change audio mode to
        // MODE_IN_COMMUNICATION for best possible VoIP performance.
        Log.d(TAG, "Starting the audio manager...");
        audioManager.start(new WebRTCAudioManager.AudioManagerEvents() {
            @Override
            public void onAudioDeviceChanged(AudioDevice selectedAudioDevice, Set<AudioDevice> availableAudioDevices) {
                onAudioManagerDevicesChanged(selectedAudioDevice, availableAudioDevices);
            }
        });
    }

    // This method is called when the audio manager reports audio device change,
    // e.g. from wired headset to speakerphone.
    private void onAudioManagerDevicesChanged(
            final AudioDevice device, final Set<AudioDevice> availableDevices) {
        Log.d(TAG, "onAudioManagerDevicesChanged: " + availableDevices + ", "
                + "selected: " + device);
    }

    // Disconnect from remote resources, dispose of local resources, and exit.
    public void disconnect() {
        try {
            activityRunning = false;
            if (pipRenderer != null) {
                pipRenderer.release();
                pipRenderer = null;
            }
            if (fullscreenRenderer != null) {
                fullscreenRenderer.release();
                fullscreenRenderer = null;
            }
            if (audioManager != null) {
                audioManager.stop();
                audioManager = null;
            }
            if (connected && !isError) {
                setResult(RESULT_OK);
            } else {
                setResult(RESULT_CANCELED);
            }
        } catch (Exception e) {
            Log.e(TAG, "disconnect: ", e);
        }
        finish();
    }

    private void disconnectWithErrorMessage(final String errorMessage) {
        if (commandLineRun || !activityRunning) {
            Log.e(TAG, "Critical error: " + errorMessage);
            disconnect();
        } else {
            new AlertDialog.Builder(this)
                    .setTitle(getText(R.string.channel_error_title))
                    .setMessage(errorMessage)
                    .setCancelable(false)
                    .setNeutralButton(R.string.ok,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    disconnect();
                                }
                            })
                    .create()
                    .show();
        }
    }

    // Log |msg| and Toast about it.
    private void logAndToast(String msg) {
        Log.d(TAG, msg);
        if (logToast != null) {
            logToast.cancel();
        }
        logToast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        logToast.show();
    }

    private void reportError(final String description) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!isError) {
                    isError = true;
                    disconnectWithErrorMessage(description);
                }
            }
        });
    }

    private void setSwappedFeeds(boolean isSwappedFeeds) {
        Logging.d(TAG, "setSwappedFeeds: " + isSwappedFeeds);
        if(fullscreenRenderer == null || pipRenderer == null){
            initUIControls(getIntent());
        }

        this.isSwappedFeeds = isSwappedFeeds;
        WebrtcClient.getInstance().swapVideoRenderer(isSwappedFeeds);
    }

    private void updatePeerConnectionParametersFromIntent(Intent intent) {
        boolean tracing = intent.getBooleanExtra(EXTRA_TRACING, false);

        int videoWidth = intent.getIntExtra(EXTRA_VIDEO_WIDTH, 0);
        int videoHeight = intent.getIntExtra(EXTRA_VIDEO_HEIGHT, 0);

        screencaptureEnabled = intent.getBooleanExtra(EXTRA_SCREENCAPTURE, false);
        // If capturing format is not specified for screencapture, use screen resolution.
        if (screencaptureEnabled && videoWidth == 0 && videoHeight == 0) {
            DisplayMetrics displayMetrics = getDisplayMetrics();
            videoWidth = displayMetrics.widthPixels;
            videoHeight = displayMetrics.heightPixels;
        }
        /*
        DataChannelParameters dataChannelParameters = null;
        if (intent.getBooleanExtra(EXTRA_DATA_CHANNEL_ENABLED, false)) {
            dataChannelParameters = new DataChannelParameters(intent.getBooleanExtra(EXTRA_ORDERED, true),
                    intent.getIntExtra(EXTRA_MAX_RETRANSMITS_MS, -1),
                    intent.getIntExtra(EXTRA_MAX_RETRANSMITS, -1), intent.getStringExtra(EXTRA_PROTOCOL),
                    intent.getBooleanExtra(EXTRA_NEGOTIATED, false), intent.getIntExtra(EXTRA_ID, -1));
        }
        */
//        peerConnectionParameters =
//                new PeerConnectionParameters(intent.getBooleanExtra(EXTRA_VIDEO_CALL, true),
//                        tracing, videoWidth, videoHeight, intent.getIntExtra(EXTRA_VIDEO_FPS, 0),
//                        intent.getIntExtra(EXTRA_VIDEO_BITRATE, 0), intent.getStringExtra(EXTRA_VIDEOCODEC),
//                        intent.getBooleanExtra(EXTRA_HWCODEC_ENABLED, true),
//                        intent.getBooleanExtra(EXTRA_FLEXFEC_ENABLED, false),
//                        intent.getIntExtra(EXTRA_AUDIO_BITRATE, 0), intent.getStringExtra(EXTRA_AUDIOCODEC),
//                        intent.getBooleanExtra(EXTRA_NOAUDIOPROCESSING_ENABLED, false),
//                        intent.getBooleanExtra(EXTRA_AECDUMP_ENABLED, false),
//                        intent.getBooleanExtra(EXTRA_SAVE_INPUT_AUDIO_TO_FILE_ENABLED, false),
//                        intent.getBooleanExtra(EXTRA_OPENSLES_ENABLED, false),
//                        intent.getBooleanExtra(EXTRA_DISABLE_BUILT_IN_AEC, false),
//                        intent.getBooleanExtra(EXTRA_DISABLE_BUILT_IN_AGC, false),
//                        intent.getBooleanExtra(EXTRA_DISABLE_BUILT_IN_NS, false),
//                        intent.getBooleanExtra(EXTRA_DISABLE_WEBRTC_AGC_AND_HPF, false),
//                        intent.getBooleanExtra(EXTRA_ENABLE_RTCEVENTLOG, false), null);
    }
}
