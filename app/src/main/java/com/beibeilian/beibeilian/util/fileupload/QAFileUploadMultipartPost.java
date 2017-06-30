package com.beibeilian.beibeilian.util.fileupload;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.beibeilian.beibeilian.receiver.ReceiverConstant;
import com.beibeilian.beibeilian.util.HelperUtil;
import com.beibeilian.beibeilian.util.HttpConstantUtil;
import com.beibeilian.beibeilian.util.PublicConstant;

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
public class QAFileUploadMultipartPost extends AsyncTask<String, Integer, String> {

	String path;
	String filename;
	Context mContext;

	public QAFileUploadMultipartPost(Context mContext, String path, String filename) {
		this.path = path;
		this.filename = filename;
		this.mContext = mContext;
	}

	@Override
	protected void onPreExecute() {

	}

	@Override
	protected void onProgressUpdate(Integer... progress) {

	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
	}

	@Override
	protected String doInBackground(String... params) {

		try {
			File file = new File(path);
			FileInputStream fs = null;
			// TODO
			BitmapFactory.Options bfOptions = new BitmapFactory.Options();
			bfOptions.inDither = false;
			bfOptions.inPurgeable = true;
			bfOptions.inInputShareable = true;
			double filelensize = HelperUtil.formatFileSize(file.length());
			if (filelensize < 1) {
				bfOptions.inSampleSize = 0;
			} else if (filelensize >= 1 && filelensize <= 2) {
				bfOptions.inSampleSize = 2;
			} else {
				bfOptions.inSampleSize = 4;
			}
			bfOptions.inTempStorage = new byte[64 * 1024];
			fs = new FileInputStream(file);
			Bitmap bm = BitmapFactory.decodeFileDescriptor(fs.getFD(), null, bfOptions);
			String newfilepath = PublicConstant.FilePath + filename;
			File filedir = new File(PublicConstant.FilePath);
			if (!filedir.exists()) {
				filedir.mkdirs();
			}
			File newfile = new File(newfilepath);
			if (!newfile.exists()) {
				newfile.createNewFile();
			}
			FileOutputStream out = new FileOutputStream(newfile);
			if (bm.compress(Bitmap.CompressFormat.JPEG, 100, out)) {
				out.flush();
				out.close();
			}
			HttpClient httpClient = new DefaultHttpClient();
			HttpContext httpContext = new BasicHttpContext();
			HttpPost httpPost = new HttpPost(HttpConstantUtil.FILEUpload);
			CustomMultiPartEntity multipartContent = new CustomMultiPartEntity(new CustomMultiPartEntity.ProgressListener() {

				@Override
				public void transferred(long num) {
					// TODO Auto-generated method stub
				}
			});
			multipartContent.addPart("file", new FileBody(new File(newfilepath)));
			httpPost.setEntity(multipartContent);
			HttpResponse response;
			response = httpClient.execute(httpPost, httpContext);
			String serverResponse = EntityUtils.toString(response.getEntity());
			JSONObject jsonObject = new JSONObject(serverResponse);
			int res = jsonObject.getInt("result");
			mContext.sendBroadcast(new Intent(ReceiverConstant.FileUploadSuccess_ACTION));
			return String.valueOf(res);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
