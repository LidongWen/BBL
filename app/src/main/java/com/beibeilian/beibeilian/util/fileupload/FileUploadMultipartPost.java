package com.beibeilian.beibeilian.util.fileupload;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.beibeilian.beibeilian.receiver.ReceiverConstant;
import com.beibeilian.beibeilian.util.HelperUtil;
import com.beibeilian.beibeilian.util.HttpConstantUtil;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * 抓拍上传
 *
 */
public class FileUploadMultipartPost extends AsyncTask<String, Integer, String> {

	long totalSize;//文件总大小
	Context context;//定义上下文
	private String path;//定义路径
	public FileUploadMultipartPost(Context context) {
		this.context = context;
	}

	@Override
	protected void onPreExecute() {

	}

	@Override
	protected void onProgressUpdate(Integer... progress) {

	}

	@Override
	protected void onPostExecute(String result) {
		try {
			if (result != null) {

				if (result.equals("1")) {

					context.sendBroadcast(new Intent(ReceiverConstant.FileUploadSuccess_ACTION));


				} else {
					HelperUtil.totastShow("请检查网络是否可用或稍候再试", context);
					context.sendBroadcast(new Intent(ReceiverConstant.FileUploadFaile_ACTION));
				}
			} else {
				HelperUtil.totastShow("请检查网络是否可用或稍候再试", context);
				context.sendBroadcast(new Intent(ReceiverConstant.FileUploadFaile_ACTION));
			}
		} catch (Exception e) {
			HelperUtil.totastShow("请检查网络是否可用或稍候再试", context);
			context.sendBroadcast(new Intent(ReceiverConstant.FileUploadFaile_ACTION));
		}
		super.onPostExecute(result);
	}

	@Override
	protected String doInBackground(String... params) {

		path = params[0];
		File file = new File(path);
		FileInputStream fs = null;
		// TODO
		BitmapFactory.Options bfOptions = new BitmapFactory.Options();
		bfOptions.inDither = false;
		bfOptions.inPurgeable = true;
		bfOptions.inInputShareable = true;
		bfOptions.inSampleSize = 2;
		bfOptions.inTempStorage = new byte[64 * 1024];
		try {
			fs = new FileInputStream(file);
			Bitmap bm = BitmapFactory.decodeFileDescriptor(fs.getFD(), null,
					bfOptions);
			File filenew = new File(path);
			if (filenew.exists()) {
				filenew.delete();
				filenew.createNewFile();
			}
			FileOutputStream out = new FileOutputStream(file);
			if (bm.compress(Bitmap.CompressFormat.JPEG, 50, out)) {
				out.flush();
				out.close();
			}
			HttpClient httpClient = new DefaultHttpClient();
			HttpContext httpContext = new BasicHttpContext();
			HttpPost httpPost = new HttpPost(HttpConstantUtil.FILEUpload);
			CustomMultiPartEntity multipartContent = new CustomMultiPartEntity(
					new CustomMultiPartEntity.ProgressListener() {

						@Override
						public void transferred(long num) {
							// TODO Auto-generated method stub
							publishProgress((int) ((num / (float) totalSize) * 100));
						}
					});
			multipartContent.addPart("file", new FileBody(new File(path)));
			totalSize = multipartContent.getContentLength();
			httpPost.setEntity(multipartContent);
			HttpResponse response;
			response = httpClient.execute(httpPost, httpContext);
			String serverResponse = EntityUtils.toString(response.getEntity());
			JSONObject jsonObject = new JSONObject(serverResponse);
			int res = jsonObject.getInt("result");
			return String.valueOf(res);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}


}
