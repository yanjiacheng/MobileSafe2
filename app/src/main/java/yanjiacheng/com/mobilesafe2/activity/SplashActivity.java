package yanjiacheng.com.mobilesafe2.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import java.io.File;

import yanjiacheng.com.mobilesafe2.R;

public class SplashActivity extends AppCompatActivity {
    /**
     * 更新新版本的状态码
     */
    private static final int UPDATE_VERSION =100 ;
    /**
     * 进行应用程序主界面的状态码
     */
    private static final int ENTER_HOME = 101;
    /**
     * URL地址出错状态码
     */
    private static final int URL_ERROR = 102;
    private static final int IO_ERROR = 103;
    private static final int JSON_ERROR = 104;
    TextView tv_version_name;
    int mLocalVersionCode;
    private String mVersionDes="版本更新描述";
    private String downloadUrl;//新版本下载地址
   // private int versionCode;
    /**
     * 用来接收子线程传来的消息，进行UI操作
     */
//    private Handler mHandler =new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what){
//                case UPDATE_VERSION:
//                    //提示用户更新，弹出对话框
//                    showUpdateDialog();
//                    break;
//                case ENTER_HOME:
//                    //进入主界面
//                    enterHome();
//                    break;
//                case URL_ERROR:
//                    Toast.makeText(SplashActivity.this, "", Toast.LENGTH_SHORT).show();
//                    break;
//                case IO_ERROR:
//                    break;
//                case JSON_ERROR:
//                    break;
//            }
//        }
//    };

    /**
     * 弹出对话框，提示用户更新
     */
    private void showUpdateDialog() {
        //对话框依赖于activity存在
        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.ic_file_download_24dp);
        //message  从服务器里面得到 版本描述
        builder.setTitle("版本更新").setMessage(mVersionDes);

        //设置按钮
        builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //下载我们的新版本apk  下载地址在下面一句获得
                downloadApk();
            }
        });

        builder.setNegativeButton("稍后再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //取消对话框。进入主界面
                enterHome();
            }
        });

        builder.show();
    }

    /**
     * 下载新版本apk
     */
    private void downloadApk() {
        //apk下载链接地址,下载存放地址
        //1.判断sd卡是否可用  是否挂载上
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            //2.获得sd卡路径
          String path=Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+"mobilesafe.apk";
            //3.发送请求，获取apk  下载到指定的路径
            //Xutils包里面的类
            HttpUtils httpUtils=new HttpUtils();
            //4.发送请求 下载新版本   下载地址，下载应用放置路径，
            httpUtils.download(downloadUrl, path, new RequestCallBack<File>() {
                /**
                 * 下载成功
                 * @param responseInfo 下载完成对应的文件
                 */
                @Override
                public void onSuccess(ResponseInfo<File> responseInfo) {
                   //下载成功后放置在sd卡中的apk
                    File file=responseInfo.result;
                
                }

                /**
                 * 下载失败
                 * @param e
                 * @param s
                 */
                @Override
                public void onFailure(HttpException e, String s) {

                }

                /**
                 * 刚刚开始下载的方法
                 */
                @Override
                public void onStart() {
                    super.onStart();
                }

                /**
                 * 下载过程中的方法
                 * @param total 总大小
                 * @param current 当前的下载位置
                 * @param isUploading 是否正在下载
                 */
                @Override
                public void onLoading(long total, long current, boolean isUploading) {
                    super.onLoading(total, current, isUploading);
                }
            });
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Log.d("json74", "json1");
        //初始化UI
        initUI();
        //初始化数据
        initData();
    }
    /**
     * 初始化UI方法
     */
    private void initUI() {
        tv_version_name=(TextView)findViewById(R.id.tv_version_name);

    }

    /**
     * 获取数据方法
     */
    private void initData() {
        //1.获取应用版本名称
        String version_title= (String) tv_version_name.getText();
        tv_version_name.setText(version_title + "  " + getVersionName());
        //检测（本地版本和服务器版本号对比）是否有更新，如果更新，提示用户下载
        //2.获取本地版本号
        mLocalVersionCode=getVersionCode();
        //3.获取服务端版本号（客户端请求，服务端回应 json.xml）
        //http:www.oxxx.com/update74.json?key=value   返回200，成功。以流的方式读取json数据
        /**
         *  json中内容包含：
         *  更新版本的名称，
         *  新版本的描述信息，
         *  服务器版本号，
         *  新版本APK下载地址；
         */


      //测试  showUpdateDialog();
        //直接进入主界面，没有根服务器判断版本,在导航界面停留3秒钟
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    enterHome();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();



        //发请求，（请求数据）在子线程里面完成
       //  checkVersion();

    }

    /**
     * 进入应用程序主界面
     */
    private void enterHome() {

        Intent intent= new Intent(this,HomeActivity.class);
        startActivity(intent);

        //在开启一个新的主界面后，将导航界面关闭(导航界面只可以见一次)
        finish();
    }

    /**
     * 获取版本号
     * @return 非0则代表获取成功
     */
    public int getVersionCode() {
        //1/包管理者对象packageManager
        PackageManager pm=getPackageManager();
        try {
            //2.从管理者里面，获取指定包名的基本信息（版本名称，版本号） 传0代表获取基本信息
            PackageInfo packageInfo= pm.getPackageInfo(getPackageName(), 0);
            //3.获取版本名
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            //包名没有找到的异常
            e.printStackTrace();
        }
        return 0;
    }
    /**
     * 获得版本名称
     * @return  版本名称  null代表异常
     */
    private String getVersionName() {
        //1/包管理者对象packageManager
        PackageManager pm=getPackageManager();
        try {
            //2.从管理者里面，获取指定包名的基本信息（版本名称，版本号） 传0代表获取基本信息
            PackageInfo packageInfo= pm.getPackageInfo(getPackageName(), 0);
            //3.获取版本名
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            //包名没有找到的异常
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 检测版本号
     */
//    private void checkVersion() {
//
//        //新建子线程
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Message message=Message.obtain();
//
//                //记录网络请求时间
//               Long startTime= System.currentTimeMillis();
//
//                //发送请求，获取数据  参数为请求json的请求地址（这里要写电脑的ip地址（http://192.168.56.2:8080/update.json）,本地电脑的ip地址会不断变化，服务器为ip转域名，永远不会改变）
//                //10.0.2.2只能模拟器访问电脑ip地址，规定
//                try {
//                    //1.封装url地址
//                    URL url=new URL("http://192.168.191.1:8080/update.json");
//                    //2.开启一个连接
//                    HttpURLConnection connection=(HttpURLConnection)url.openConnection();
//                    //3.设置常见参数（请求头）
//                    connection.setConnectTimeout(4000);//请求超时
//                    connection.setReadTimeout(4000);    //读取超时
//                    // connection.setRequestMethod("POST");  默认为get请求方法
//                    Log.d("json74","json1");
//                    //4.获取响应码
//                    if(connection.getResponseCode()==200){
//                        //请求成功
//                        //5.以流的方式，将数据获取下来
//                        InputStream is=connection.getInputStream();
//                        //6.流转字符串(工具类封装)  利用新建类里面的方法，将数据读取下来并且转换成字符串返回
//                        String json= StreamUtil.streamToString(is);
//                      //  Log.d("json74","json2");
//                     //   Log.d("json74",json);
//                        //7.json解析
//                        JSONObject jsonObject=new JSONObject(json);
//                        String versionName=jsonObject.getString("versionName");
//                         mVersionDes=jsonObject.getString("versionDes");
//                        String versionCode=jsonObject.getString("versionCode");
//                         downloadUrl=jsonObject.getString("downloadUrl");
//
//
//                        //8。比对版本号（服务器版本号Integer.parseInt(versionCode)》本地版本号，提示用户更新），这里直接写静态1
//                        if(mLocalVersionCode<1){
//                            //提示用户更新，弹出对话框，子线程，不能操作ui，使用消息机制
//                            message.what=UPDATE_VERSION;
//                        }else{
//                            //进入应用程序主界面
//                            message.what=ENTER_HOME;
//                        }
//                    }
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                    message.what=URL_ERROR;
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    message.what=IO_ERROR;
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    message.what=JSON_ERROR;
//                }finally {
//                    //不管是不是异常都发送消息
//                    //指定睡眠时间，请求网络时间超过四秒就不处理
//                    //未超过四秒，记录代码运行时间
//
//                    long endTime=System.currentTimeMillis();
//                    if(endTime-startTime<3000){
//                        try {
//                            Thread.sleep(3000-(endTime-startTime));
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    mHandler.sendMessage(message);
//                }
//            }
//        }).start();
//    }

}
