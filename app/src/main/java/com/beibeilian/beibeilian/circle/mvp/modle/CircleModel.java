package com.beibeilian.beibeilian.circle.mvp.modle;

import android.os.AsyncTask;


public class CircleModel {
	
	
	public CircleModel(){
		//
	}
	
	public void deleteCircle( final IDataRequestListener listener) {
		requestServer(listener);
	}

	public void addFavort( final IDataRequestListener listener) {
		requestServer(listener);
	}

	public void deleteFavort(final IDataRequestListener listener) {
		requestServer(listener);
	}

	public void addComment( final IDataRequestListener listener) {
		requestServer(listener);
	}

	public void deleteComment( final IDataRequestListener listener) {
		requestServer(listener);
	}
	
	/**
	 * 
	* @Title: requestServer 
	* @Description: 与后台交�? 因为demo是本地数据，不做处理
	* @param  listener    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	private void requestServer(final IDataRequestListener listener) {
		new AsyncTask<Object, Integer, Object>(){
			@Override
			protected Object doInBackground(Object... params) {
				return null;
			}
			
			protected void onPostExecute(Object result) {
				listener.loadSuccess(result);
			};
		}.execute();
	}
	
}
