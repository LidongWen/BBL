package com.beibeilian.beibeilian.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.beibeilian.beibeilian.util.fileupload.CustomMultiPartEntity;

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
import java.util.concurrent.Callable;

public class FileUploadTask implements Callable<String> {
	String path;
	String filename;

	public FileUploadTask(String path, String filename) {
		this.path = path;
		this.filename = filename;
	}

	@Override
	public String call() throws Exception {
		// TODO Auto-generated method stub
		File file = new File(path);
		FileInputStream fs = null;
		// TODO
		BitmapFactory.Options bfOptions = new BitmapFactory.Options();
		bfOptions.inDither = false;
		bfOptions.inPurgeable = true;
		bfOptions.inInputShareable = true;
		bfOptions.inSampleSize = 2;
		bfOptions.inTempStorage = new byte[64 * 1024];
		fs = new FileInputStream(file);
		Bitmap bm = BitmapFactory.decodeFileDescriptor(fs.getFD(), null, bfOptions);
		String newfilepath = PublicConstant.FilePath + filename + ".jpg";
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
				// publishProgress((int) ((num / (float) totalSize) * 100));
			}
		});
		multipartContent.addPart("file", new FileBody(new File(newfilepath)));
		//// totalSize = multipartContent.getContentLength();
		httpPost.setEntity(multipartContent);
		HttpResponse response;
		response = httpClient.execute(httpPost, httpContext);
		String serverResponse = EntityUtils.toString(response.getEntity());
		JSONObject jsonObject = new JSONObject(serverResponse);
		int res = jsonObject.getInt("result");
		if (res == 1) {
			newfile.delete();
		}
		return String.valueOf(res);
	}

}
