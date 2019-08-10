package com.example.sdfilelistndk;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class MainActivity extends Activity {
	private static final String FILE_SIZE = "file_size";
	private static final String FILE_NAME = "file_name";
	private static final String FILE_BYTES = "file_bytes";
	private ListView mFilelist = null;
	private ArrayList<Map<String,String>> al = new ArrayList<Map<String,String>>();
	private Handler mHandler = new Handler();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mFilelist = (ListView) findViewById(R.id.list_file);
		mHandler.post(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				getFileSizes();
				
			}
			
		});
		
		//getFileSizes();
		ListAdapter adapter = new SimpleAdapter(this, al, android.R.layout.simple_list_item_2, new String[]{FILE_NAME,FILE_SIZE}, new int[]{android.R.id.text1,android.R.id.text2});
		mFilelist.setAdapter(adapter);
		
	}

	private void getFileSizes(){
		// TODO Auto-generated method stub
		File root = Environment.getExternalStorageDirectory();
		String[] subFiles = root.list();
		long time1 = System.currentTimeMillis();
		for(String subFile:subFiles){
			//Log.d("MainActivity", "file_item:"+subFile);
			long size = clacSize(root.getPath()+File.separator+subFile);
			Map<String,String> item = new HashMap<String,String>();
			item.put(FILE_NAME, subFile);
			item.put(FILE_SIZE, String.valueOf(((float)size)/1024/1024)+"MB");
			item.put(FILE_BYTES, String.valueOf(size));
			al.add(item);
			//Log.d("MainActivity",item.toString());
		}
		Map<String,String>[] maps =al.toArray(new Map[]{ new HashMap<String, String>() });
		Arrays.sort(maps, new Comparator<Map<String,String>>(){

			/**
			 * 实现比较器的比较方法
			 * @author asus
			 * @param lhs
			 * @param rhs
			 * @return 差值
			 */
			@Override
			public int compare(Map<String,String> lhs, Map<String,String> rhs) {
				// TODO Auto-generated method stub
				long file_size_1 = Long.parseLong(lhs.get(FILE_BYTES));
				long file_size_2 = Long.parseLong(rhs.get(FILE_BYTES));
				return (file_size_1 - file_size_2)<0?1:((file_size_1 - file_size_2)==0?0:-1);	//意味着降序排列
			}
			
		});
		al.clear();
		//把排序后的数组加入到数组列表中
		for(Map i :maps){
			al.add(i);
		}
		Toast.makeText(this, "用时："+(((float)(System.currentTimeMillis()-time1))/1000+"s"), Toast.LENGTH_LONG).show();
	}

	private native long clacSize(String file);

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	static{
		System.loadLibrary("filelist");
	}
}
