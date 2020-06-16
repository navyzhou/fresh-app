package com.yc.fresh;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

@SuppressLint("SetJavaScriptEnabled")
public class MainActivity extends Activity {
	private Activity activity;
	private WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		activity = this;

		final TextView text = (TextView) this.findViewById(R.id.textView1);

		/**
		 * CountDownTimer timer = new CountDownTimer(5000, 1000)中，
		 * 第一个参数表示总时间，第二个参数表示间隔时间。
		 * 意思就是每隔一秒会回调一次方法onTick，然后1秒之后会回调onFinish方法。
		 */
		CountDownTimer timer = new CountDownTimer(5000, 1000) {
			public void onTick(long millisUntilFinished) {
				text.setText(millisUntilFinished / 1000 + "秒");
			}

			public void onFinish() {
				gotoIndex();
			}
		};
		//调用 CountDownTimer 对象的 start() 方法开始倒计时，也不涉及到线程处理
		timer.start();
	}

	/**
	 * 获取链接
	 * @param view
	 */
	public void gotoIndex() {
		setContentView(R.layout.index_view);
		webView = (WebView) findViewById(R.id.webView1);

		if (Build.VERSION.SDK_INT >= 19) {
			webView.getSettings().setCacheMode(
					WebSettings.LOAD_CACHE_ELSE_NETWORK); // 无论是否有网络，只要本地有缓存，都使用缓存。
		}

		webView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);
				activity.setTitle("Loading...");
				activity.setProgress(newProgress * 100);
				if (newProgress == 100) {
					activity.setTitle(R.string.app_name);
				}
			}

			@Override
			public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
				return super.onJsAlert(view, url, message, result);
			}

		});

		webView.setWebViewClient(new GameWebViewClient());
		WebSettings ws = webView.getSettings();
		ws.setJavaScriptEnabled(true);
		ws.setAllowContentAccess(true);
		webView.loadUrl("http://192.168.1.5:8080/fresh/index.html");
	}

	class GameWebViewClient extends WebViewClient {
		/**
		 * 当加载的网页需要重定向的时候就会回调这个函数告知我们应用程序是否需要接管控制网页加载，如果应用程序接管，
		 *	并且return true意味着主程序接管网页加载，如果返回false让webview自己处理。
		 * 参数说明：
		 * 
		 * @param view 接收WebViewClient的那个实例，前面看到webView.setWebViewClient(new MyAndroidWebViewClient())，即是这个webview。
		 * @param url 即将要被加载的url
		 * @return true 当前应用程序要自己处理这个url， 返回false则不处理。 注："post"请求方式不会调用这个回调函数
		 */
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url_Turntable) {
			view.loadUrl(url_Turntable);
			return true;
		}

		/**
		 * onPageStarted 当内核开始加载访问的url时，会通知应用程序，对每个main frame
		 * 这个函数只会被调用一次，页面包含iframe或者framesets 不会另外调用一次onPageStarted，
		 * 当网页内内嵌的frame 发生改变时也不会调用onPageStarted。
		 * 
		 * 参数说明：
		 * @param view 接收WebViewClient的那个实例，前面看到webView.setWebViewClient(new MyAndroidWebViewClient())，即是这个webview。
		 * @param url 即将要被加载的url
		 * @param favicon 如果这个favicon已经存储在本地数据库中，则会返回这个网页的favicon，否则返回为null。
		 */
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
		}


		/**
		 * onPageFinished 当内核加载完当前页面时会通知我们的应用程序，这个函数只有在main
		 * frame情况下才会被调用，当调用这个函数之后，渲染的图片不会被更新，如果需要获得新图片的通知可以使用@link
		 * WebView.PictureListener#onNewPicture。 参数说明：
		 * 
		 * @param view 接收WebViewClient的那个实例，前面看到webView.setWebViewClient(new  MyAndroidWebViewClient())，即是这个webview。
		 * @param url 即将要被加载的url
		 */
		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
			super.onPageFinished(view, url);
		}


		/**
		 * onLoadResource 通知应用程序WebView即将加载url制定的资源
		 * 参数说明：
		 * @param view 接收WebViewClient的那个实例，前面看到webView.setWebViewClient(new MyAndroidWebViewClient())，即是这个webview。
		 * @param url 即将加载的url 资源
		 */
		@Override
		public void onLoadResource(WebView view, String url) {
			super.onLoadResource(view, url);
		}


		/**
		 * shouldInterceptRequest
		 * 通知应用程序内核即将加载url制定的资源，应用程序可以返回本地的资源提供给内核，若本地处理返回数据，内核不从网络上获取数据。
		 * 
		 * 参数说明：
		 * @param view 接收WebViewClient的那个实例，前面看到webView.setWebViewClient(new MyAndroidWebViewClient())，即是这个webview。
		 * @param url raw url 制定的资源
		 * @return 返回WebResourceResponse包含数据对象，或者返回null
		 */
		@Override
		public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
			return super.shouldInterceptRequest(view, url);
		}

		/**
		 * onReceivedError 当浏览器访问制定的网址发生错误时会通知我们应用程序 参数说明：
		 * @param view 接收WebViewClient的那个实例，前面看到webView.setWebViewClient(new MyAndroidWebViewClient())，即是这个webview。
		 * @param errorCode 错误号可以在WebViewClient.ERROR_* 里面找到对应的错误名称。
		 * @param description 描述错误的信息
		 * @param failingUrl 当前访问失败的url，注意并不一定是我们主url
		 */
		@Override
		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
			super.onReceivedError(view, errorCode, description, failingUrl);
			// view.loadUrl("file:///android_asset/error.html");
		}

		/**
		 * 如果浏览器需要重新发送POST请求，可以通过这个时机来处理。默认是不重新发送数据。 参数说明
		 * @param view  接收WebViewClient的webview
		 * @param dontResend 浏览器不需要重新发送的参数
		 * @param resend 浏览器需要重新发送的参数
		 */
		@Override
		public void onFormResubmission(WebView view, Message dontResend, Message resend) {
			// TODO Auto-generated method stub
			super.onFormResubmission(view, dontResend, resend);
		}	

		@Override
		public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
			handler.proceed();
		}



		/**
		 * 
		 */
		@Override
		public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
			// TODO Auto-generated method stub
			super.doUpdateVisitedHistory(view, url, isReload);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
			webView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
