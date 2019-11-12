package com.mohsen.falehafez_new.util;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class AudioWifiLocal {
    private static final String TAG = AudioWifiLocal.class.getSimpleName();

    /***
     * Keep a single copy of this in memory unless required to create a new instance explicitly.
     ****/
    private static AudioWifiLocal mAudioWifeLocal;

    /****
     * Playback progress update time in milliseconds
     ****/
    private static final int AUDIO_PROGRESS_UPDATE_TIME = 100;

    // TODO: externalize the error messages.
    private static final String ERROR_PLAYVIEW_NULL = "Play view cannot be null";
    private static final String ERROR_PLAYTIME_CURRENT_NEGATIVE = "Current playback time cannot be negative";
    private static final String ERROR_PLAYTIME_TOTAL_NEGATIVE = "Total playback time cannot be negative";

    private Handler mProgressUpdateHandler;

    private MediaPlayer mMediaPlayer;

    private SeekBar mSeekBar;
    private boolean autoPlay = true;
    private boolean isPrapared = false;


    @Deprecated
    /***
     * Set both current playack time and total runtime
     * of the audio in the UI.
     */
    private TextView mPlaybackTime;

    private View mPlayButton;
    private View mPauseButton;

    /***
     * Indicates the current run-time of the audio being played
     */
    private TextView mRunTime;

    /***
     * Indicates the total duration of the audio being played.
     */
    private TextView mTotalTime;

    /***
     * Set if AudioWifeLocal is using the default UI provided with the library.
     * **/
    private boolean mHasDefaultUi;

    /****
     * Array to hold custom completion listeners
     ****/
    private ArrayList<MediaPlayer.OnCompletionListener> mCompletionListeners = new ArrayList<MediaPlayer.OnCompletionListener>();

    private ArrayList<View.OnClickListener> mPlayListeners = new ArrayList<View.OnClickListener>();

    private ArrayList<View.OnClickListener> mPauseListeners = new ArrayList<View.OnClickListener>();

    /***
     * Audio URI
     ****/
    private static Uri mUri;

    public static AudioWifiLocal getInstance() {

        if (mAudioWifeLocal == null) {
            mAudioWifeLocal = new AudioWifiLocal();
        }

        return mAudioWifeLocal;
    }

    private Runnable mUpdateProgress = new Runnable() {

        public void run() {

            if (mSeekBar == null) {
                return;
            }

            if (mProgressUpdateHandler != null && mMediaPlayer.isPlaying()) {
                mSeekBar.setProgress((int) mMediaPlayer.getCurrentPosition());
                int currentTime = mMediaPlayer.getCurrentPosition();
                updatePlaytime(currentTime);
                updateRuntime(currentTime);
                // repeat the process
                mProgressUpdateHandler.postDelayed(this, AUDIO_PROGRESS_UPDATE_TIME);
            } else {
                // DO NOT update UI if the player is paused
            }
        }
    };

    public void play() {
        if(!isPrapared)
            return;

        // if play button itself is null, the whole purpose of AudioWifeLocal is
        // defeated.
        if (mPlayButton == null) {
            throw new IllegalStateException(ERROR_PLAYVIEW_NULL);
        }

        if (mUri == null) {
            throw new IllegalStateException("Uri cannot be null. Call init() before calling this method");
        }

        if (mMediaPlayer == null) {
            throw new IllegalStateException("Call init() before calling this method");
        }

        if (mMediaPlayer.isPlaying()) {
            return;
        }

        mProgressUpdateHandler.postDelayed(mUpdateProgress, AUDIO_PROGRESS_UPDATE_TIME);

        // enable visibility of all UI controls.
        setViewsVisibility();

        mMediaPlayer.start();

        setPausable();
    }

    private void setViewsVisibility() {

        if (mSeekBar != null) {
            mSeekBar.setVisibility(View.VISIBLE);
        }

        if (mPlaybackTime != null) {
            mPlaybackTime.setVisibility(View.VISIBLE);
        }

        if (mRunTime != null) {
            mRunTime.setVisibility(View.VISIBLE);
        }

        if (mTotalTime != null) {
            mTotalTime.setVisibility(View.VISIBLE);
        }

        if (mPlayButton != null) {
            mPlayButton.setVisibility(View.VISIBLE);
        }

        if (mPauseButton != null) {
            mPauseButton.setVisibility(View.VISIBLE);
        }
    }

    public void pause() {

        if (mMediaPlayer == null) {
            return;
        }

        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            setPlayable();
        }
    }
    @Deprecated
    private void updatePlaytime(int currentTime) {

        if (mPlaybackTime == null) {
            return;
        }

        if (currentTime < 0) {
            throw new IllegalArgumentException(ERROR_PLAYTIME_CURRENT_NEGATIVE);
        }

        StringBuilder playbackStr = new StringBuilder();

        // set the current time
        // its ok to show 00:00 in the UI
        playbackStr.append(String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes((long) currentTime), TimeUnit.MILLISECONDS.toSeconds((long) currentTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) currentTime))));

        playbackStr.append("/");

        // show total duration.
        long totalDuration = 0;

        if (mMediaPlayer != null) {
            try {
                totalDuration = mMediaPlayer.getDuration();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // set total time as the audio is being played
        if (totalDuration != 0) {
            playbackStr.append(String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes((long) totalDuration), TimeUnit.MILLISECONDS.toSeconds((long) totalDuration) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) totalDuration))));
        } else {
            Log.w(TAG, "Something strage this audio track duration in zero");
        }

        mPlaybackTime.setText(playbackStr);

        // DebugLog.i(currentTime + " / " + totalDuration);
    }
    private void updateRuntime(int currentTime) {

        if (mRunTime == null) {
            // this view can be null if the user
            // does not want to use it. Don't throw
            // an exception.
            return;
        }

        if (currentTime < 0) {
            throw new IllegalArgumentException(ERROR_PLAYTIME_CURRENT_NEGATIVE);
        }

        StringBuilder playbackStr = new StringBuilder();

        // set the current time
        // its ok to show 00:00 in the UI
        playbackStr.append(String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes((long) currentTime), TimeUnit.MILLISECONDS.toSeconds((long) currentTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) currentTime))));

        mRunTime.setText(playbackStr);

        // DebugLog.i(currentTime + " / " + totalDuration);
    }

    private void setTotalTime() {

        if (mTotalTime == null) {
            // this view can be null if the user
            // does not want to use it. Don't throw
            // an exception.
            return;
        }

        StringBuilder playbackStr = new StringBuilder();
        long totalDuration = 0;

        // by this point the media player is brought to ready state
        // by the call to init().
        if (mMediaPlayer != null) {
            try {
                totalDuration = mMediaPlayer.getDuration();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (totalDuration < 0) {
            throw new IllegalArgumentException(ERROR_PLAYTIME_TOTAL_NEGATIVE);
        }

        // set total time as the audio is being played
        if (totalDuration != 0) {
            playbackStr.append(String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes((long) totalDuration), TimeUnit.MILLISECONDS.toSeconds((long) totalDuration) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) totalDuration))));
        }

        mTotalTime.setText(playbackStr);
    }


    /***
     * Changes audiowife state to enable play functionality.
     */
    private void setPlayable() {
        if (mPlayButton != null) {
            mPlayButton.setVisibility(View.VISIBLE);
        }

        if (mPauseButton != null) {
            mPauseButton.setVisibility(View.GONE);
        }
    }

    /****
     * Changes audio wife to enable pause functionality.
     */
    private void setPausable() {
        if (mPlayButton != null) {
            mPlayButton.setVisibility(View.GONE);
        }

        if (mPauseButton != null) {
            mPauseButton.setVisibility(View.VISIBLE);
        }
    }

    /***
     * Initialize the audio player. This method should be the first one to be called before starting
     * to play audio using {@link nl.changer.audiowife.AudioWife}
     *
     * @param ctx
     *            {@link android.app.Activity} Context
     * @param uri
     *            Uri of the audio to be played.
     ****/
    public AudioWifiLocal init(Context ctx, Uri uri) {
        isPrapared = false;

        if (uri == null) {
            throw new IllegalArgumentException("Uri cannot be null");
        }

        if (mAudioWifeLocal == null) {
            mAudioWifeLocal = new AudioWifiLocal();
        }

        mUri = uri;

        mProgressUpdateHandler = new Handler();

        initPlayer(ctx);

        return this;
    }

    /***
     * Sets the audio play functionality on click event of this view. You can set {@link android.widget.Button} or
     * an {@link android.widget.ImageView} as audio play control
     *
     * @see nl.changer.audiowife.AudioWife#addOnPauseClickListener(android.view.View.OnClickListener)
     ****/
    public AudioWifiLocal setPlayView(View play) {

        if (play == null) {
            throw new NullPointerException("PlayView cannot be null");
        }

        if (mHasDefaultUi) {
            Log.w(TAG, "Already using default UI. Setting play view will have no effect");
            return this;
        }

        mPlayButton = play;

        initOnPlayClick();
        return this;
    }

    public AudioWifiLocal setAutoPlay(boolean autoPlay){
        this.autoPlay = autoPlay;
        return this;
    }

    private void initOnPlayClick() {
        if (mPlayButton == null) {
            throw new NullPointerException(ERROR_PLAYVIEW_NULL);
        }

        // add default click listener to the top
        // so that it is the one that gets fired first
        mPlayListeners.add(0, new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                play();
            }
        });

        // Fire all the attached listeners
        // when the play button is clicked
        mPlayButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                for (View.OnClickListener listener : mPlayListeners) {
                    listener.onClick(v);
                }
            }
        });
    }


    public AudioWifiLocal setPauseView(View pause) {

        if (pause == null) {
            throw new NullPointerException("PauseView cannot be null");
        }

        if (mHasDefaultUi) {
            Log.w(TAG, "Already using default UI. Setting pause view will have no effect");
            return this;
        }

        mPauseButton = pause;

        initOnPauseClick();
        return this;
    }

    private void initOnPauseClick() {
        if (mPauseButton == null) {
            throw new NullPointerException("Pause view cannot be null");
        }

        // add default click listener to the top
        // so that it is the one that gets fired first
        mPauseListeners.add(0, new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                pause();
            }
        });

        // Fire all the attached listeners
        // when the pause button is clicked
        mPauseButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                for (View.OnClickListener listener : mPauseListeners) {
                    listener.onClick(v);
                }
            }
        });
    }
    public AudioWifiLocal setPlaytime(TextView playTime) {

        if (mHasDefaultUi) {
            Log.w(TAG, "Already using default UI. Setting play time will have no effect");
            return this;
        }

        mPlaybackTime = playTime;

        // initialize the playtime to 0
        updatePlaytime(0);
        return this;
    }
    public AudioWifiLocal setRuntimeView(TextView currentTime) {

        if (mHasDefaultUi) {
            Log.w(TAG, "Already using default UI. Setting play time will have no effect");
            return this;
        }

        mRunTime = currentTime;

        // initialize the playtime to 0
        updateRuntime(0);
        return this;
    }

    public AudioWifiLocal setTotalTimeView(TextView totalTime) {

        if (mHasDefaultUi) {
            Log.w(TAG, "Already using default UI. Setting play time will have no effect");
            return this;
        }

        mTotalTime = totalTime;

//        setTotalTime();
        return this;
    }

    public AudioWifiLocal setSeekBar(SeekBar seekbar) {

        if (mHasDefaultUi) {
            Log.w(TAG, "Already using default UI. Setting seek bar will have no effect");
            return this;
        }

        mSeekBar = seekbar;
        return this;
    }

    public AudioWifiLocal addOnCompletionListener(MediaPlayer.OnCompletionListener listener) {

        // add default click listener to the top
        // so that it is the one that gets fired first
        mCompletionListeners.add(0, listener);

        return this;
    }

    public AudioWifiLocal addOnPlayClickListener(View.OnClickListener listener) {

        mPlayListeners.add(listener);

        return this;
    }
    public AudioWifiLocal addOnPauseClickListener(View.OnClickListener listener) {

        mPauseListeners.add(listener);

        return this;
    }

    private void initPlayer(Context ctx) {

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        try {
            mMediaPlayer.setDataSource(ctx, mUri);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            mMediaPlayer.prepareAsync();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMediaPlayer.setOnCompletionListener(mOnCompletion);


        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                isPrapared = true;
                setTotalTime();
                initMediaSeekBar();

                if(autoPlay)
                    play();
            }
        });
    }

    private MediaPlayer.OnCompletionListener mOnCompletion = new MediaPlayer.OnCompletionListener() {

        @Override
        public void onCompletion(MediaPlayer mp) {
            // set UI when audio finished playing
            int currentPlayTime = 0;
            mSeekBar.setProgress((int) currentPlayTime);
            updatePlaytime(currentPlayTime);
            updateRuntime(currentPlayTime);
            setPlayable();
            // ensure that our completion listener fires first.
            // This will provide the developer to over-ride our
            // completion listener functionality

            fireCustomCompletionListeners(mp);
        }
    };
    private void initMediaSeekBar() {

        if (mSeekBar == null) {
            return;
        }

        // update seekbar
        long finalTime = mMediaPlayer.getDuration();
        mSeekBar.setMax((int) finalTime);

        mSeekBar.setProgress(0);

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mMediaPlayer.seekTo(seekBar.getProgress());

                // if the audio is paused and seekbar is moved,
                // update the play time in the UI.
                updateRuntime(seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }
        });
    }

    private void fireCustomCompletionListeners(MediaPlayer mp) {
        for (MediaPlayer.OnCompletionListener listener : mCompletionListeners) {
            listener.onCompletion(mp);
        }
    }

    public void release() {

        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
            mProgressUpdateHandler = null;
        }
    }


}
