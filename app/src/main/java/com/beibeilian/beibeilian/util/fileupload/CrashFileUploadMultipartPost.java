package com.beibeilian.beibeilian.util.fileupload;

import android.content.Context;
import android.os.AsyncTask;

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

/**
 * 抓拍上传
 *
 */
public class CrashFileUploadMultipartPost extends AsyncTask<String, Integer, String> {

	long totalSize;// 文件总大小
	Context context;// 定义上下文
	private String path;// 定义路径

	public CrashFileUploadMultipartPost(Context context) {
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
		super.onPostExecute(result);
	}

	@Override
	protected String doInBackground(String... params) {
		try {
			path = params[0];
			HttpClient httpClient = new DefaultHttpClient();
			HttpContext httpContext = new BasicHttpContext();
			HttpPost httpPost = new HttpPost(HttpConstantUtil.FILEUpload);
			CustomMultiPartEntity multipartContent = new CustomMultiPartEntity(new CustomMultiPartEntity.ProgressListener() {

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
			if (res > 0) {
				new File(path).delete();
			}
			return String.valueOf(res);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
