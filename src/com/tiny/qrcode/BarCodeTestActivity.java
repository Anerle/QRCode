package com.tiny.qrcode;

import java.io.File;
import java.util.Hashtable;


import com.tiny.qrcode.more.MoreFormatActivity;
import com.zxing.activity.CaptureActivity;
import com.zxing.android.history.HistoryActivity;
import com.zxing.android.history.HistoryItem;
import com.zxing.android.history.HistoryManager;
import com.zxing.decoding.Intents;
import com.zxing.decoding.RGBLuminanceSource;
import com.zxing.encoding.EncodingHandler;
import com.ericssonlabs.R;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class BarCodeTestActivity extends Activity {
	/** 二维码保存路径*/
	private final static String QRCODE_PATH = Environment
			.getExternalStorageDirectory() + "/save_qrcode/";
	private TextView resultTextView;// 解码结果
//	private EditText qrStrEditText;// 编辑文字
	private ClearEditText qrStrEditText;// 编辑文字
	private ImageView qrImgImageView;// 显示二维码
	private String contentString;// 编码内容
	private Bitmap qrCodeBitmap;// 生成的二维码
	private EncodingHandler encodingHandler;// 编码类
	private Bitmap mBitmap = null;// logo
	private String[] items = new String[] { "选择本地图片", "拍照" };
	/* 
	 * 请求码 
	 */
	/**嵌入logo，选择本地图片*/
	private static final int IMAGE_REQUEST_CODE = 1;
	/**嵌入logo，拍照*/
	private static final int CAMERA_REQUEST_CODE = 2;
	/**嵌入logo，图片获取结果*/
	private static final int RESULT_REQUEST_CODE = 3;
	/**编码历史记录*/
	private static final int HISTORY_REQUEST_CODE = 4;
	/**更多编码格式*/
	private static final int MORE_REQUEST_CODE = 5;
//	/**解码，打开本地图片*/
//	private static final int OPEN_IMAGE_REQUEST_CODE = 6;
//	/**解码，打开相机*/
//	private static final int OPEN_CAMERA_REQUEST_CODE = 0;
	/**解码*/
	private static final int SCAN_REQUEST_CODE = 0;
	/** logo Image */
	private static final String IMAGE_FILE_NAME = "logoImage.jpg";
	// exit
	private long exitTime = 0;
	
	//add for scan local image
//	private static final int PARSE_BARCODE_SUC = 300;
//	private static final int PARSE_BARCODE_FAIL = 303;
//	private ProgressDialog mProgress;
//	private String photo_path;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);// 加载主界面
		resultTextView = (TextView) this.findViewById(R.id.tv_scan_result);
//		qrStrEditText = (EditText) this.findViewById(R.id.et_qr_string);
		qrStrEditText = (ClearEditText) this.findViewById(R.id.et_qr_string);
		qrImgImageView = (ImageView) this.findViewById(R.id.iv_qr_image);
		Log.i("zhty contentStr", "oncreate");
		// scan qrcode
		Button scanBarCodeButton = (Button) this
				.findViewById(R.id.btn_scan_barcode);
		scanBarCodeButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				//
//				Intent openCameraIntent = new Intent(BarCodeTestActivity.this,
//						CaptureActivity.class);
//				startActivityForResult(openCameraIntent, OPEN_CAMERA_REQUEST_CODE);
				
				Intent intent = new Intent();
				intent.setClass(BarCodeTestActivity.this, CaptureActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivityForResult(intent, SCAN_REQUEST_CODE);
				
				
			}
			
		});
//		// open qrcode
//		Button openBarCodeButton = (Button) this
//				.findViewById(R.id.btn_open_barcode);
//		openBarCodeButton.setOnClickListener(new OnClickListener() {
//
//			public void onClick(View v) {
//				//
//				Intent innerIntent = new Intent(Intent.ACTION_GET_CONTENT); //"android.intent.action.GET_CONTENT"  
//	            innerIntent.setType("image/*");  
//	            Intent wrapperIntent = Intent.createChooser(innerIntent, "选择二维码图片");  
//	            startActivityForResult(wrapperIntent, OPEN_IMAGE_REQUEST_CODE); 
//				
//			}
//			
//		});
		// generate qrcode
		Button generateQRCodeButton = (Button) this
				.findViewById(R.id.btn_add_qrcode);
		generateQRCodeButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				encodingHandler = new EncodingHandler();
				contentString = qrStrEditText.getText().toString();
				if (!contentString.equals("")) {
					try {
						qrCodeBitmap = encodingHandler.cretaeBitmap(
								contentString, mBitmap);
					} catch (Exception e) {
						e.printStackTrace();
					}
					qrImgImageView.setImageBitmap(qrCodeBitmap);
					//history
					//创建对象
					HistoryManager historyManager = new HistoryManager(BarCodeTestActivity.this);
					historyManager.trimHistory();
//					HistoryItem historyItem = historyManager.buildHistoryItem(1);
					//添加编码内容的记录
					historyManager.addHistoryItem(contentString);
		
				} else {
//					qrStrEditText.setShakeAnimation();
					Toast.makeText(BarCodeTestActivity.this,
							"Text can not be empty", Toast.LENGTH_SHORT).show();
				}
			}
		});
		// save qrcode
		Button saveQRCodeButton = (Button) this
				.findViewById(R.id.btn_save_qrcode);
		saveQRCodeButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				String message;
				QRToSDcardSaveService dcardSaveService = new QRToSDcardSaveService();// 保存二维码至SD卡的业务类
				try {
					dcardSaveService.saveToSDCard(QRCODE_PATH, contentString
							+ ".png", qrCodeBitmap);
					message = "图片已保存到" + QRCODE_PATH + contentString;
				} catch (Exception e) {
					message = "保存失败";
					e.printStackTrace();
				}
				Toast.makeText(BarCodeTestActivity.this, message,
						Toast.LENGTH_SHORT).show();
			}
		});
		// embed Logo
		Button embedLogoButton = (Button) this
				.findViewById(R.id.btn_embed_logo);
		embedLogoButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				showDialog();
			}
		});
		// share QRcode
		Button shareBtn = (Button) this.findViewById(R.id.btn_share_qrcode);
		shareBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (qrCodeBitmap == null) {
					Toast.makeText(BarCodeTestActivity.this, "QRcode is null",
							Toast.LENGTH_SHORT).show();
				} else {
					//分享的Intent
					Intent intent = new Intent(Intent.ACTION_SEND, Uri
							.parse("mailto:"));
					//分享的类型为图片
					intent.setType("image/png");
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
					intent.putExtra(Intent.EXTRA_SUBJECT, "subject");
					intent.putExtra(Intent.EXTRA_TEXT, "text");
					//本地图片缓存
					QRToSDcardSaveService qr = new QRToSDcardSaveService();
					try {
						qr.saveToSDCard(QRCODE_PATH + "/cache/", "cache.png",
								qrCodeBitmap);
					} catch (Exception e) {
						e.printStackTrace();
					}
					intent.putExtra(
							Intent.EXTRA_STREAM,
							Uri.fromFile(new File(QRCODE_PATH + "/cache/"
									+ "cache.png")));
					try {
						//跳转到处理分享Intent的Activity
						startActivity(intent);
					} catch (ActivityNotFoundException anfe) {
						Log.w("zhtyshare", anfe.toString());
					}
				}

			}
		});

	}
	
//	private Handler mHandler = new Handler(){
//
//		@Override
//		public void handleMessage(Message msg) {
//			super.handleMessage(msg);
//			
//			mProgress.dismiss();
//			switch (msg.what) {
//			case PARSE_BARCODE_SUC:
//				onResultHandler((String)msg.obj, qrCodeBitmap);
//				break;
//			case PARSE_BARCODE_FAIL:
//				Toast.makeText(BarCodeTestActivity.this, (String)msg.obj, Toast.LENGTH_LONG).show();
//				break;
//
//			}
//		}
//		
//	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 显示解码结果
		// if (resultCode == RESULT_OK) {//RESULT_OK=-1,RESULT_CANCELED=0
		 Log.i("tag","requestCode="+requestCode);
		 Log.i("tag","resultCode="+resultCode);
		// Bundle bundle = data.getExtras();
		// String scanResult = bundle.getString("result");
		// resultTextView.setText(scanResult);
		// return;
		// }
		// 结果码不等于取消时候
		if (resultCode != RESULT_CANCELED) {

			switch (requestCode) {
//			case OPEN_CAMERA_REQUEST_CODE:// 0,扫描二维码，返回结果
//				Bundle bundle = data.getExtras();
//				String scanResult = bundle.getString("result");
//				resultTextView.setText(scanResult);
//				break;
			case IMAGE_REQUEST_CODE:// 1
				// Log.e("tag","IMAGE_REQUEST_CODE="+IMAGE_REQUEST_CODE);
				startPhotoZoom(data.getData());
				break;
			case CAMERA_REQUEST_CODE:// 2
				if (Tools.hasSdcard()) {
					File tempFile = new File(
							Environment.getExternalStorageDirectory()
									+ IMAGE_FILE_NAME);
					startPhotoZoom(Uri.fromFile(tempFile));
				} else {
					Toast.makeText(BarCodeTestActivity.this, "未找到存储卡，无法存储照片！",
							Toast.LENGTH_LONG).show();
				}

				break;
			case RESULT_REQUEST_CODE:// 3
				if (data != null) {
					getImageToView(data);
				}
				break;
			case HISTORY_REQUEST_CODE://4
				Log.i("tag", "history request code");
				int itemNumber = data.getIntExtra(Intents.History.ITEM_NUMBER, -1);
                if (itemNumber >= 0) {
                	HistoryManager historyManager = new HistoryManager(BarCodeTestActivity.this);
                    HistoryItem historyItem = historyManager.buildHistoryItem(itemNumber);
//                    decodeOrStoreSavedBitmap(null, historyItem.getResult());
                    historyManager.trimHistory();
//                    contentString = historyItem.getResult();
                    contentString = historyItem.getDisplayAndDetails();
//                    qrStrEditText = (EditText) this.findViewById(R.id.et_qr_string);
                    qrStrEditText.setText(contentString);
            		encodingHandler = new EncodingHandler();
            		if (contentString != null) {
            			if (!contentString.equals("")) {
            				try {
            					qrCodeBitmap = encodingHandler.cretaeBitmap(contentString,
            							mBitmap);
            				} catch (Exception e) {
            					e.printStackTrace();
            				}
            				qrImgImageView.setImageBitmap(qrCodeBitmap);
            			} else {
            				Toast.makeText(BarCodeTestActivity.this,
            						"Text can not be empty", Toast.LENGTH_SHORT).show();
            			}
            		}
                }
				break;
			case MORE_REQUEST_CODE://5
				contentString = data.getStringExtra("content");
				qrStrEditText.setText(contentString);
				encodingHandler = new EncodingHandler();
				if (contentString != null) {
					if (!contentString.equals("")) {
						try {
							qrCodeBitmap = encodingHandler.cretaeBitmap(contentString,
									mBitmap);
						} catch (Exception e) {
							e.printStackTrace();
						}
						qrImgImageView.setImageBitmap(qrCodeBitmap);
						//history
						HistoryManager historyManager = new HistoryManager(BarCodeTestActivity.this);
						historyManager.trimHistory();
						historyManager.addHistoryItem(contentString);
					} else {
						qrImgImageView.setImageBitmap(null);
						Toast.makeText(BarCodeTestActivity.this,
								"Text can not be empty", Toast.LENGTH_SHORT).show();
					}
				}
				break;
//			case OPEN_IMAGE_REQUEST_CODE:  
//                //获取选中图片的路径  
//                Cursor cursor = getContentResolver().query(data.getData(), null, null, null, null);  
//                if (cursor.moveToFirst()) {  
//                    photo_path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));  
//                }  
//                cursor.close();  
//                  
//                mProgress = new ProgressDialog(BarCodeTestActivity.this);  
//                mProgress.setMessage("正在扫描...");  
//                mProgress.setCancelable(false);  
//                mProgress.show();  
//                  
//                new Thread(new Runnable() {  
//                    @Override  
//                    public void run() {  
//                        Result result = scanningImage(photo_path);  
//                        if (result != null) {  
//                            Message m = mHandler.obtainMessage();  
//                            m.what = PARSE_BARCODE_SUC;  
//                            m.obj = result.getText();  
//                            mHandler.sendMessage(m);  
//                        } else {  
//                            Message m = mHandler.obtainMessage();  
//                            m.what = PARSE_BARCODE_FAIL;  
//                            m.obj = "Scan failed!";  
//                            mHandler.sendMessage(m);  
//                        }  
//                          
//                    }  
//                }).start();  
//                  
//                break;
			case SCAN_REQUEST_CODE:
				
					Bundle bundle = data.getExtras();
					//显示扫描到的内容
					resultTextView.setText(bundle.getString("result"));
				break;
			}
		}
	}
	

	/**
	 * 显示选择对话框
	 */
	private void showDialog() {

		new AlertDialog.Builder(this)
				.setTitle("设置Logo")
				.setItems(items, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							Intent intentFromGallery = new Intent();
							intentFromGallery.setType("image/*"); // 设置文件类型
							intentFromGallery
									.setAction(Intent.ACTION_GET_CONTENT);
							startActivityForResult(intentFromGallery,
									IMAGE_REQUEST_CODE);
							break;
						case 1:

							Intent intentFromCapture = new Intent(
									MediaStore.ACTION_IMAGE_CAPTURE);
							// 判断存储卡是否可以用，可用进行存储
							if (Tools.hasSdcard()) {

								intentFromCapture.putExtra(
										MediaStore.EXTRA_OUTPUT,
										Uri.fromFile(new File(Environment
												.getExternalStorageDirectory(),
												IMAGE_FILE_NAME)));
							}

							startActivityForResult(intentFromCapture,
									CAMERA_REQUEST_CODE);
							break;
						}
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).show();

	}

	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {

		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 设置裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 320);
		intent.putExtra("outputY", 320);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, RESULT_REQUEST_CODE);
	}

	/**
	 * 保存裁剪之后的图片数据
	 * 
	 * @param picdata
	 */
	private void getImageToView(Intent data) {
		Bundle extras = data.getExtras();
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");
			// Bitmap drawable = new BitmapDrawable(photo);
			// faceImage.setImageDrawable(drawable);
			try {
				qrCodeBitmap = encodingHandler.createImageViewQRBitmap(photo,
						contentString);
				qrImgImageView.setImageBitmap(qrCodeBitmap);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.layout.main_activity_actions, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_more:
			Intent it_m = new Intent(BarCodeTestActivity.this,
					MoreFormatActivity.class);
			startActivityForResult(it_m, MORE_REQUEST_CODE);
			return true;
		case R.id.action_history:
			Intent it_h = new Intent(BarCodeTestActivity.this,
					HistoryActivity.class);
			startActivityForResult(it_h, HISTORY_REQUEST_CODE);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onNewIntent(Intent intent) {
		// other format
		// handle intent
		super.onNewIntent(intent);
//		contentString = intent.getStringExtra("content");
//		encodingHandler = new EncodingHandler();
//		if (contentString != null) {
//			if (!contentString.equals("")) {
//				try {
//					qrCodeBitmap = encodingHandler.cretaeBitmap(contentString,
//							mBitmap);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				qrImgImageView.setImageBitmap(qrCodeBitmap);
//			} else {
//				Toast.makeText(BarCodeTestActivity.this,
//						"Text can not be empty", Toast.LENGTH_SHORT).show();
//			}
//		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), "再按一次退出程序",
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				finish();
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	/**
	 * 跳转到上一个页面
	 * @param resultString
	 * @param bitmap
	 */
	private void onResultHandler(String resultString, Bitmap bitmap){
		if(TextUtils.isEmpty(resultString)){
			Toast.makeText(BarCodeTestActivity.this, "Scan failed!", Toast.LENGTH_SHORT).show();
			return;
		}
		Intent resultIntent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putString("result", resultString);
		bundle.putParcelable("bitmap", bitmap);
		resultIntent.putExtras(bundle);
		this.setResult(RESULT_OK, resultIntent);
		BarCodeTestActivity.this.finish();
	}
	
	/**
	 * 扫描二维码图片的方法
	 * @param path
	 * @return
	 */
	public Result scanningImage(String path) {
		if(TextUtils.isEmpty(path)){
			return null;
		}
		Hashtable<DecodeHintType, String> hints = new Hashtable<DecodeHintType, String>();
		hints.put(DecodeHintType.CHARACTER_SET, "UTF8"); //设置二维码内容的编码

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true; // 先获取原大小
		qrCodeBitmap = BitmapFactory.decodeFile(path, options);
		options.inJustDecodeBounds = false; // 获取新的大小
		int sampleSize = (int) (options.outHeight / (float) 200);
		if (sampleSize <= 0)
			sampleSize = 1;
		options.inSampleSize = sampleSize;
		qrCodeBitmap = BitmapFactory.decodeFile(path, options);
		RGBLuminanceSource source = new RGBLuminanceSource(qrCodeBitmap);
		BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
		QRCodeReader reader = new QRCodeReader();
		try {
			return reader.decode(bitmap1, hints);

		} catch (NotFoundException e) {
			e.printStackTrace();
		} catch (ChecksumException e) {
			e.printStackTrace();
		} catch (FormatException e) {
			e.printStackTrace();
		}
		return null;
	}
}