package com.readearth.wuxiairmonitor.datebase;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Handler;

import com.readearth.wuxiairmonitor.R;
import com.readearth.wuxiairmonitor.ui.StartActivity;

 
public class TranscribeDatabase{
    public static String dbName="wuxi_aqi.db";//数据库的名字
    private static String DATABASE_PATH="/data/data/com.readearth.wuxiairmonitor/databases/";//数据库在手机里的路径
     int alpha = 255; 
     int b = 0; 
     Context context;
     
     
     
     public TranscribeDatabase(Context context) {
		super();
		this.context = context;
	}

	public void startTransWork(){ 
            //判断数据库是否存在
            boolean dbExist = checkDataBase();
            if(dbExist){
                 
            }else{//不存在就把raw里的数据库写入手机
                try{
                    copyDataBase();
                }catch(IOException e){
                    throw new Error("Error copying database");
                }
            
             
            new Thread(new Runnable() { 
                public void run() { 
                    initApp(); //初始化程序 
                      
                    while (b < 2) { 
                        try { 
                            if (b == 0) { 
                                Thread.sleep(20); 
                                b = 1; 
                            } else { 
                                Thread.sleep(50); 
                            } 
//                            updateApp(); 
                        } catch (InterruptedException e) { 
                            e.printStackTrace(); 
                        } 
                    } 
                } 
            }).start(); 
 
            }
        } 
 
        public void updateApp() { 
            alpha -= 5; 
            if (alpha <= 0) { 
                b = 2; 
                if(true){
                try{
                	
                   Intent in = new Intent(context,StartActivity.class); 
                   context.startActivity(in); 
                }catch(Exception e){
                         
                    }
                }
                
            }
            
        } 
        /**
         * 判断数据库是否存在
         * @return false or true
         */
        public static boolean checkDataBase(){
            SQLiteDatabase checkDB = null;
            try{
                String databaseFilename = DATABASE_PATH+dbName;
                checkDB =SQLiteDatabase.openDatabase(databaseFilename, null,
                        SQLiteDatabase.OPEN_READONLY);
            }catch(SQLiteException e){
                 
            }
            if(checkDB!=null){
                checkDB.close();
            }
            return checkDB !=null?true:false;
        }
        /**
         * 复制数据库到手机指定文件夹下
         * @throws IOException
         */
        public void copyDataBase() throws IOException{
            String databaseFilenames =DATABASE_PATH+dbName;
            File dir = new File(DATABASE_PATH);
            if(!dir.exists())//判断文件夹是否存在，不存在就新建一个
                dir.mkdir();
            FileOutputStream os = null;
            try{
                os = new FileOutputStream(databaseFilenames);//得到数据库文件的写入流
            }catch(FileNotFoundException e){
                e.printStackTrace();
            }
            InputStream is = context.getResources().openRawResource(R.raw.wuxi_aqi);//得到数据库文件的数据流
            byte[] buffer = new byte[8192];
            int count = 0;
            try{
                 
                while((count=is.read(buffer))>0){
                    os.write(buffer, 0, count);
                    os.flush();
                }
            }catch(IOException e){
                 
            }
            try{
                is.close();
                os.close();
                handler.sendEmptyMessage(888);
            }catch(IOException e){
                e.printStackTrace();
            }
        }
        /**
         * 初始化，这里是起始页的没有用
         */
        public void initApp(){          
        } 
        
        OnTransCompleted onTransCompleted;
        public void setOnTransCompleted(OnTransCompleted onTransCompleted){
        	this.onTransCompleted = onTransCompleted;
        }
        
        @SuppressLint("HandlerLeak")
    	Handler handler = new Handler() {

    		@Override
    		public void handleMessage(android.os.Message msg) {
    			if (msg.what == 888) {
    				onTransCompleted.onCompleted();
    			}
    		};
    	};
    	
    	public interface OnTransCompleted{
    		public void onCompleted();
    	}
    } 
