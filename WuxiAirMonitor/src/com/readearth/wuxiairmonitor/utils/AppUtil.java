package com.readearth.wuxiairmonitor.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

public class AppUtil {

	public static final long HALF_HOUR = 1800000;
	public static final long ONE_HOUR = 3600000;
	public static final long FIVE_MINUTES = 300000;
	public static final long TEN_MINUTES = 600000;
	public static final long ONE_MINUTE = 60000;
	public static final long HALF_MINUTE = 30000;
	public static final long TWO_MINUTES = 120000;
	public static final long FOUR_MINUTES = 240000;
	public static final long TEN_SECONDS = 10000;
	public static final int POSITION_TIMEOUT = 120000;
	public static final int ONE_KM = 1000;
	public static final long FIVE_SECONDS = 5000;
	public static final long TWO_SECONDS = 2000;

	public static final String ISO_LANG_CHINESE = "CHI";
	public static final String ACTION_NAME = "com.readearth.monitor.tracking";
	public static final double threshold = 50;

	public static final String TAG = "AppUtil";

	/**
	 * 获得程序自定义的字体
	 * 
	 * @param context
	 * @param faceName
	 * @return
	 */
	public static Typeface getCustomTypefaceByName(Context context,
			String faceName) {
		Typeface typeface = Typeface.createFromAsset(context.getAssets(),
				"fonts/" + faceName);
		return typeface;
	}

	public static Typeface getHeiTi(Context context) {
		return getCustomTypefaceByName(context, "simhei.ttf");
	}

	public static Typeface get35Thin(Context context) {
		return getCustomTypefaceByName(context, "Helvetica LT 35 Thin.ttf");
	}

	public static Typeface get45Light(Context context) {
		return getCustomTypefaceByName(context, "Helvetica LT 45 Light.ttf");
	}

	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 根据ListView的数据，计算其固定高度
	 * 
	 * @param listView
	 */
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		params.height += 5;// if without this statement,the listview will be a
							// little short
		listView.setLayoutParams(params);
	}


	/**
	 * 通过降低图片的质量来压缩图片
	 * 
	 * @param bmp
	 *            要压缩的图片
	 * @param maxSize
	 *            压缩后图片大小的最大值,单位KB
	 * @return 压缩后的图片
	 */
	public static Bitmap compressByQuality(Bitmap bitmap, int maxSize) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int quality = 100;
		bitmap.compress(CompressFormat.JPEG, quality, baos);
		while (baos.toByteArray().length / 1024 > maxSize) {
			quality -= 20;
			baos.reset();
			bitmap.compress(CompressFormat.JPEG, quality, baos);
		}
		bitmap = BitmapFactory.decodeByteArray(baos.toByteArray(), 0,
				baos.toByteArray().length);
		return bitmap;
	}

	/**
	 * 根据宽度重新设计缩放地图
	 * 
	 * @param bitmap
	 * @param newWidth
	 * @return
	 */
	public static Bitmap resizeBitmap(Bitmap bitmap, int newWidth) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float temp = ((float) height) / ((float) width);
		int newHeight = (int) ((newWidth) * temp);
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		Matrix matrix = new Matrix();
		// resize the bit map
		matrix.postScale(scaleWidth, scaleHeight);
		// matrix.postRotate(45);
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,
				matrix, true);
		bitmap.recycle();
		return resizedBitmap;

	}

	/**
	 * 获得UTC的时间戳
	 * 
	 * @return
	 */
	public static long getUTCTime() {
		Calendar cal = Calendar.getInstance();
		cal.setTimeZone(TimeZone.getTimeZone("gmt"));
		return cal.getTimeInMillis();
	}

	/**
	 * 获得本地当前时间戳
	 * 
	 * @return
	 */
	public static long getLocalTime() {
		Calendar cal = Calendar.getInstance();
		return cal.getTimeInMillis();
	}

	/**
	 * 查看手机的网络状态，移动数据网络或者Wifi网络
	 * 
	 * @param context
	 * @return
	 */
	public static boolean checkNetworkStatus(Context context) {
		boolean resp = false;
		final ConnectivityManager connMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		// final android.net.NetworkInfo wifi =
		// connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		NetworkInfo activeNetInfo = connMgr.getActiveNetworkInfo();
		if (activeNetInfo != null) {
			resp = true;
		}
		return resp;
	}

	/**
	 * 获得手机的IMEI IMEI(International Mobile Equipment Identity)是国际移动设备身份码
	 * 每部手机在出厂时都会被写入一个唯一的电子串号，即通常所说的IMEI码
	 * 
	 * @param context
	 * @return
	 */
	public static String getIMEI(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);

		String imei = tm.getSimSerialNumber();
		return imei;
	}

	/**
	 * 获得手机的IMSI 国际移动用户识别码(IMSI)：国际上为唯一识别一个移动用户所分配的号码。 IMSI是区别移动用户的标志，储存在SIM卡中
	 * 
	 * @param context
	 * @return
	 */
	public static String getIMSI(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String imsi = tm.getSubscriberId();
		return imsi;
	}

	/**
	 * 获得手机的设备号
	 * 
	 * @param context
	 * @return
	 */
	public static String getDeviceID(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String deviceID = tm.getDeviceId();
		return deviceID;
	}

	/**
	 * 获得操作系统的固件版本号
	 */
	public static String getOsVersionName() {
		return Build.VERSION.RELEASE;
	}

	/**
	 * 获得手机号
	 * 
	 * @param context
	 * @return
	 */
	public static String getPhoneNumber(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String phoneNumberText = tm.getLine1Number();
		return phoneNumberText;
	}

	/**
	 * 获得程序的版本名称
	 * 
	 * @param context
	 *            上下文
	 * @return 返回app的版本名称
	 */
	public static String getAppVersionName(Context context) {
		String versionName = "";
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			versionName = packageInfo.versionName;
			if (TextUtils.isEmpty(versionName)) {
				return "";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return versionName;
	}

	/**
	 * 查看WiFI是否可用
	 * 
	 * @param inContext
	 * @return
	 */
	public static boolean isWiFiActive(Context inContext) {
		WifiManager wm = null;
		try {
			wm = (WifiManager) inContext.getSystemService(Context.WIFI_SERVICE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (wm == null || wm.isWifiEnabled() == false)
			return false;
		return true;
	}

	/**
	 * 尝试开启手机WiFi
	 */
	public static void enableWifi(Context context) {
		Log.d(TAG, "enabling wifi");
		try {
			WifiManager wifiManager = (WifiManager) context
					.getSystemService(Context.WIFI_SERVICE);
			if (!wifiManager.isWifiEnabled()) {
				wifiManager.setWifiEnabled(true);
			}
			Settings.System.putInt(context.getContentResolver(),
					Settings.System.WIFI_SLEEP_POLICY,
					Settings.System.WIFI_SLEEP_POLICY_NEVER);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 手机无线数据信号是否可用
	 */
	public static boolean isWirelessEnabled(Context context) {
		try {
			return Boolean.parseBoolean(invokeMethod("getMobileDataEnabled",
					null, context).toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * 尝试开启手机无线数据信号
	 * 
	 * @param enbale
	 * @param context
	 */
	public static void enableWireless(boolean enbale, Context context) {
		Log.d(TAG, "toggling wireless, set wireless to " + enbale);
		try {
			invokeBooleanArgMethod("setMobileDataEnabled", enbale, context);
			// invokeBooleanArgMethod("setDataRoaming",enbale,context);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Object invokeMethod(String methodName, Object[] arg,
			Context context) throws Exception {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		Class<? extends ConnectivityManager> ownerClass = cm.getClass();
		Class[] argsClass = null;
		if (arg != null) {
			argsClass = new Class[1];
			argsClass[0] = arg.getClass();
		}
		Method method = ownerClass.getMethod(methodName, argsClass);
		return method.invoke(cm, arg);
	}

	public static Object invokeBooleanArgMethod(String methodName,
			boolean value, Context context) throws Exception {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		Class<? extends ConnectivityManager> ownerClass = cm.getClass();
		Class[] argsClass = new Class[1];
		argsClass[0] = boolean.class;
		Method method = ownerClass.getMethod(methodName, argsClass);
		return method.invoke(cm, value);
	}

	/**
	 * 是否可以切换GPS显示
	 * 
	 * @param context
	 * @return
	 */
	public static boolean canToggleGPS(Context context) {
		PackageManager pacman = context.getPackageManager();
		PackageInfo pacInfo = null;
		try {
			pacInfo = pacman.getPackageInfo("com.android.settings",
					PackageManager.GET_RECEIVERS);
		} catch (NameNotFoundException e) {
			return false; // package not found
		}
		if (pacInfo != null) {
			for (ActivityInfo actInfo : pacInfo.receivers) {
				// test if recevier is exported. if so, we can toggle GPS.
				if (actInfo.name
						.equals("com.android.settings.widget.SettingsAppWidgetProvider")
						&& actInfo.exported) {
					return true;
				}
			}
		}
		return false; // default
	}

	/**
	 * 尝试打开GPS定位功能
	 * 
	 * @param context
	 */
	public static void enableGPS(Context context) {
		Log.d("AppUtil", "enabling gps");
		LocationManager locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

			Intent GPSIntent = new Intent();
			GPSIntent.setClassName("com.android.settings",
					"com.android.settings.widget.SettingsAppWidgetProvider");
			GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
			GPSIntent.setData(Uri.parse("custom:3"));
			try {
				PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send();
			} catch (CanceledException e) {
				e.printStackTrace();
			}

			/*
			 * try{
			 * Settings.Secure.setLocationProviderEnabled(context.getContentResolver
			 * (), "gps", true); } catch (Exception e){ e.printStackTrace(); }
			 */
		}
	}

	/**
	 * 查看程序是否在系统中运行
	 * 
	 * @param activityManager
	 * @param packageName
	 * @return
	 */
	public static boolean isAppOnForeground(ActivityManager activityManager,
			String packageName) {
		// Returns a list of application processes that are running on the
		// device
		List<RunningAppProcessInfo> appProcesses = activityManager
				.getRunningAppProcesses();
		if (appProcesses == null)
			return false;
		for (RunningAppProcessInfo appProcess : appProcesses) {

			if (appProcess.processName.equals(packageName)
					&& appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				return true;
			}
		}
		return false;
	}

	public static String getValueWithUnity(String value, Context context) {
		String unity = context.getResources().getString(
				com.readearth.wuxiairmonitor.R.string.ugm3_text);
		String str = value + unity;
		if(value==null||value.equalsIgnoreCase("—")||value.equalsIgnoreCase(""))
			str = "—";
		return str;
	}

	public static String getValueWithUnity(double value, Context context) {
		String unity = context.getResources().getString(
				com.readearth.wuxiairmonitor.R.string.ugm3_text);
		String str = String.valueOf(value) + unity;
		return str;
	}

	public static String getValueWithUnity(int value, Context context) {
		String unity = context.getResources().getString(
				com.readearth.wuxiairmonitor.R.string.ugm3_text);
		String str = String.valueOf(value) + unity;
		return str;
	}

	public static boolean isValidDateTime(Context context) {
		/*
		 * V1 valid from 9:00 - 18:00, mon - fri
		 */
		return true;
	}

	/**
	 * 以post的方式，以键值对的方式向服务器请求数据
	 * 
	 * @author lphu
	 * @param url
	 * @param list
	 * @return
	 */
	public static byte[] postValuePairDate(String url, List<NameValuePair> list) {
		DefaultHttpClient client = new DefaultHttpClient();
		InputStream is = null;
		HttpPost post = null;
		try {
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list,
					"UTF-8");
			post = new HttpPost(url);
			post.setEntity(entity);
			Log.i("mytag", post.toString());
			HttpResponse respone = client.execute(post);
			int status = respone.getStatusLine().getStatusCode();

			if ((status == HttpURLConnection.HTTP_NOT_IMPLEMENTED)
					|| (status == HttpURLConnection.HTTP_VERSION)
					|| (status == HttpURLConnection.HTTP_INTERNAL_ERROR)
					|| (status == HttpURLConnection.HTTP_GATEWAY_TIMEOUT)
					|| (status == HttpURLConnection.HTTP_BAD_GATEWAY)) {
				if (post != null) {
					post.abort();
				}
				if (is != null) {
					is.close();
				}

			}
			if (status != HttpURLConnection.HTTP_OK) {
				throw new Exception("Response status not OK [" + status + "]");
			}

			is = respone.getEntity().getContent();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int readLen;
			while ((readLen = is.read(buffer)) != -1) {
				baos.write(buffer, 0, readLen);
			}

			byte[] ret = baos.toByteArray();
			baos.close();
			is.close();
			Log.d("HttpConnection", "postViaHttpConnection resp length:"
					+ ret.length);
			return ret;

		} catch (UnsupportedEncodingException e) {
			Log.e("HttpConnection", e.getMessage());
			return null;
		} catch (ClientProtocolException e) {
			Log.e("HttpConnection", e.getMessage());
			return null;
		} catch (IOException e) {
			Log.e("HttpConnection", e.getMessage());
			return null;
		} catch (Exception e) {
			Log.e("HttpConnection", e.getMessage());
			return null;
		} finally {
			try {
				if (is != null) {
					is.close();
				}
				if (post != null) {
					post.abort();
				}
			} catch (Exception e) {
			}
		}
	}

	public static String replaceEndBlank(String str) {
		String dest = "";
		if (str != null) {
			Pattern p = Pattern.compile("\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
			dest = dest.trim();
		}
		return dest;
	}

	/**
	 * 通过Url请求数据
	 * 
	 * @param requestToSend
	 * @param urlStr
	 * @return
	 */
	public static byte[] postViaHttpConnection(byte[] requestToSend,
			String urlStr) {
		int status = 0;
		HttpURLConnection c = null;
		InputStream is = null;
		OutputStream os = null;
		try {
			if (requestToSend != null) {
				// Log.d("HttpConnection","postViaHttpConnection request:\n"+new
				// String(requestToSend,"UTF-8"));
			} else {
				// Log.d("HttpConnection","postViaHttpConnection urlStr:\n"+urlStr);
			}

			URL url = new URL(urlStr);

			c = (HttpURLConnection) url.openConnection();
			c.setRequestMethod("POST");
			c.setConnectTimeout(POSITION_TIMEOUT);
			c.setReadTimeout(POSITION_TIMEOUT);
			c.setRequestProperty("Content-type", "text/html");
			c.setRequestProperty("Accept-Charset", "UTF-8");
			c.setRequestProperty("contentType", "UTF-8");

			if (requestToSend != null) {
				c.setDoInput(true);
				c.setUseCaches(false);
				c.setDoOutput(true);
				c.setRequestMethod("GET");
				// Getting the output stream may flush the headers
				os = c.getOutputStream();
				os.write(requestToSend);
				os.flush(); // Optional, openInputStream will flush
			}
			// Get the status code, causing the connection to be made
			status = c.getResponseCode();
			if ((status == HttpURLConnection.HTTP_NOT_IMPLEMENTED)
					|| (status == HttpURLConnection.HTTP_VERSION)
					|| (status == HttpURLConnection.HTTP_INTERNAL_ERROR)
					|| (status == HttpURLConnection.HTTP_GATEWAY_TIMEOUT)
					|| (status == HttpURLConnection.HTTP_BAD_GATEWAY)) {
				if (is != null) {
					is.close();
				}
				if (os != null) {
					os.close();
				}
				if (c != null) {
					c.disconnect();
				}
				return null;
			}
			// Only HTTP_OK (200) means the content is returned.
			if (status != HttpURLConnection.HTTP_OK) {
				throw new Exception("Response status not OK [" + status + "]");
			}

			is = c.getInputStream();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int readLen;
			while ((readLen = is.read(buffer)) != -1) {
				baos.write(buffer, 0, readLen);
			}

			byte[] ret = baos.toByteArray();

			baos.close();
			is.close();
			Log.d("HttpConnection", "postViaHttpConnection resp length:"
					+ ret.length);
			// Log.i("WebServices","postViaHttpConnection resp:"+new
			// String(ret));
			return ret;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} catch (OutOfMemoryError e) {
			Log.e("HttpConnection", e.getMessage());
			return null;
		} finally {
			try {
				if (os != null) {
					os.close();
				}
				if (c != null) {
					c.disconnect();
				}
			} catch (Exception e) {
			}
		}

	}

}
