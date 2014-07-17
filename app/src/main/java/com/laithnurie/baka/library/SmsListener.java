package com.laithnurie.baka.library;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.telephony.SmsMessage;
import android.util.Log;

import com.laithnurie.baka.RssApp;

/**
 * Created by laithnurie on 17/05/2013.
 */

public class SmsListener extends BroadcastReceiver {


	private Camera cam;
	private Camera.Parameters p;
	@Override
	public void onReceive(Context context, Intent intent) {

		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(RssApp.getCurrentActivity());


		if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
			Bundle bundle = intent.getExtras();           //---get the SMS message passed in---
			SmsMessage[] msgs;
			String msg_from;
			String msgBody;
			if (bundle != null) {
				//---retrieve the SMS message received---
				try {
					Object[] pdus = (Object[]) bundle.get("pdus");
					msgs = new SmsMessage[pdus.length];
					for (int i = 0; i < msgs.length; i++) {
						msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
						msg_from = msgs[i].getOriginatingAddress();
						msgBody = msgs[i].getMessageBody();

						String phone_no = sharedPrefs.getString("location_receiver_phone_no", "NULL");
						String track_text = sharedPrefs.getString("track_text", "NULL");
						Log.v("sms", "from " + msg_from);
						Log.v("sms", "text " + msgBody);
						Log.v("sms", "phone number " + phone_no);
						Log.v("sms", "track text" + track_text);

						if (msg_from.contentEquals(phone_no) && msgBody.contentEquals(track_text)) {

							LocationProvider lp = new LocationProvider();
							lp.getLocation(RssApp.getCurrentActivity());
//							lp.getLocation(RssApp.getCurrentActivity(),"net");

							Vibrator v = (Vibrator) RssApp.getCurrentActivity().getSystemService(Context.VIBRATOR_SERVICE);
							v.vibrate(1000);

							Uri alert = RingtoneManager
									.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
							MediaPlayer mMediaPlayer = new MediaPlayer();

							mMediaPlayer.setDataSource(RssApp.getCurrentActivity(), alert);

							final AudioManager audioManager = (AudioManager) RssApp.getCurrentActivity().getSystemService(Context.AUDIO_SERVICE);
							if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
								mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
								mMediaPlayer.setLooping(false);
								mMediaPlayer.prepare();
								mMediaPlayer.start();
							}

							for(int x = 1; x < 10; x++) {

								getCamera();
								p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
								cam.setParameters(p);
								cam.startPreview();

								Thread.sleep(3000);

								p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
								cam.setParameters(p);
								cam.startPreview();

								Thread.sleep(3000);
							}
						}
					}
				} catch (Exception e) {
					Log.e("Exception caught", e.getMessage());
				}
			}
		}
	}

	private void getCamera() {
		if (cam == null) {
			try {
				cam = Camera.open();
				p = cam.getParameters();
			} catch (RuntimeException e) {
				Log.e("Camera Error. Failed to Open. Error: ", e.getMessage());
			}
		}
	}
}
