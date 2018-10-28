package com.example.dell.v;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import android.widget.EditText;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.GroundOverlayOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.map.SupportMapFragment;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiDetailInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiBoundSearchOption;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiDetailSearchResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.poi.PoiSortType;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.example.dell.v.OverLay.PoiOverlay;
import com.example.dell.v.Tools.ToastMaker;

import java.util.ArrayList;
import java.util.List;

import static android.content.pm.PackageManager.*;

/**
 * 演示poi搜索功能
 */
public class SearchPosition extends AppCompatActivity implements OnGetPoiSearchResultListener,OnGetSuggestionResultListener{

    private ToastMaker toast = new ToastMaker();
    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

    }

    private MapView mMapView;
    private BaiduMap mBaidumap;
    //定位相关
    /**
     * 定位客户端
     */
    LocationClient mLocClient;
    /**
     * 定位模式
     */
    private MyLocationConfiguration.LocationMode mCurrentMode;
    /**
     * 定位图标
     */
    BitmapDescriptor mCurrentMarker;
    /**
     * 传感器管理器
     */
    private SensorManager mSensorManager;
    /**
     * 当前方向
     */
    private int mCurrentDirection=0;
    /**
     * 当前经度
     */
    private double mCurrentLat=0.0;
    /**
     * 当前纬度
     */
    private double mCurrentLon=0.0;
    /**
     * 当前经度
     */
    private float mCurrentAccracy;
    /**
     * 是否首次定位
     */
    boolean isFirstLoc=true;
    /**
     * 定位数据
     */
    private MyLocationData locData;
    /**
     * 定位监听
     */
    private MyLocationListener myListener=new MyLocationListener();
    /**
     * 方向监听
     */
    private DirectionSensorListener DirectionListener=new DirectionSensorListener();
    /**
     * 上一次的方向
     */
    private Double lastX=0.0;
    /**
     * 定位所在城市
     */
    private String mCityName;
    //poi搜索相关
    /**
     * Poi检索实例
     */
    private PoiSearch mPoiSearch=null;
    private SuggestionSearch mSuggestionSearch=null;
    private List<String> suggest;

    /**
     * 搜索关键字输入窗口
     */
    private AutoCompleteTextView keyWorldsView=null;
    private ArrayAdapter<String> sugAdapter=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.poi_search_layout);
        mSensorManager= (SensorManager) getSystemService(SENSOR_SERVICE);//获取传感器管理服务
        mCurrentMode= MyLocationConfiguration.LocationMode.NORMAL;
        //初始化地图
        mMapView= (MapView) findViewById(R.id.map);
        mBaidumap=mMapView.getMap();
        //开启定位图层
        mBaidumap.setMyLocationEnabled(true);
        initLocation();
        initPoiSearch();

    }

    /**
     * 初始化定位相关
     */
    private void initLocation(){
        mLocClient=new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option=new LocationClientOption();
        option.setOpenGps(true);//打开GPS
        option.setCoorType("bd09ll");//设置坐标类型
        option.setIsNeedAddress(true);//是否需要地址信息
        option.setScanSpan(1000);
        LocationPermission();
        mLocClient.setLocOption(option);
    }

    /**
     * poi搜索相关
     */
    private void initPoiSearch(){
        //初始化搜索模块，注册搜索事件监听
        mPoiSearch=PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);

        //初始化建议搜索模块，注册建议搜索事件监听
        mSuggestionSearch=SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(this);

        keyWorldsView= (AutoCompleteTextView) findViewById(R.id.searchkey);
        sugAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line);
        keyWorldsView.setAdapter(sugAdapter);
        keyWorldsView.setThreshold(1);

        keyWorldsView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()<=0){
                    return;
                }
                /**
                 * 使用建议搜索服务获取建议列表，结果在onGetSuggestionResult中更新
                 */
                mSuggestionSearch.requestSuggestion(new SuggestionSearchOption()
                        .keyword(s.toString()).city(mCityName));
                Log.i("搜索关键字",s+"");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * 响应城市内搜索按钮点击事件
     * @param v
     */
    public void searchButtonProcess(View v){
        String keystr=keyWorldsView.getText().toString();
        mPoiSearch.searchInCity(new PoiCitySearchOption().city(mCityName).keyword(keystr).pageNum(0));
    }

    /**
     * 获取GPS定位权限(Android6.0 以上需要动态获取权限)
     */
    private void LocationPermission() {
        int checkPermission = ContextCompat.checkSelfPermission(SearchPosition.this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (checkPermission != PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SearchPosition.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            Log.d("未获得定位权限", "弹出提示");
            return;
        }
    }



    /**
     * 方向监听
     */
    public class DirectionSensorListener implements SensorEventListener {

        @Override
        public void onSensorChanged(SensorEvent event) {
            double x=event.values[SensorManager.DATA_X];
            if(Math.abs(x-lastX)>1.0){
                mCurrentDirection= (int) x;
                //设置定位数据
                locData=new MyLocationData.Builder()
                        .accuracy(mCurrentAccracy)//精度
                        .direction(mCurrentDirection) //此处设置开发者获取到的方向信息，顺时针0-360
                        .latitude(mCurrentLat)//经度
                        .longitude(mCurrentLon)//纬度
                        .build();
                mBaidumap.setMyLocationData(locData);
            }
            lastX=x;
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }


    /**
     * 定位监听
     */
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            //map view销毁后不在处理新接受的位置
            if(bdLocation==null||mMapView==null)
            {
                return;
            }
            mCurrentLat=bdLocation.getLatitude();
            mCurrentLon=bdLocation.getLongitude();
            mCurrentAccracy=bdLocation.getRadius();
            mCityName=bdLocation.getCity();
            //设置定位数据
            locData=new MyLocationData.Builder()
                    .accuracy(bdLocation.getRadius())//精度
                    .direction(mCurrentDirection) //此处设置开发者获取到的方向信息，顺时针0-360
                    .latitude(bdLocation.getLatitude())//经度
                    .longitude(bdLocation.getLongitude())//纬度
                    .build();
            mBaidumap.setMyLocationData(locData);
            //第一次定位时，将地图位置移动到当前位置
            if(isFirstLoc){
                isFirstLoc=false;
                LatLng ll=new LatLng(bdLocation.getLatitude(),bdLocation.getLongitude());
                MapStatus.Builder builder=new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaidumap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                mCurrentMarker = null;
                mBaidumap
                        .setMyLocationConfiguration(new MyLocationConfiguration(mCurrentMode, true, null));
            }
        }
    }

    //生命周期

    @Override
    protected void onStart() {
        //为系统的方向传感器注册监听器
        mSensorManager.registerListener(DirectionListener,mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION)
                ,SensorManager.SENSOR_DELAY_UI);
        //启动监听
        mLocClient.start();
        super.onStart();

    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onStop() {
        //取消传感器监听
        mSensorManager.unregisterListener(DirectionListener);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        //退出时销毁定位
        mLocClient.stop();
        //关闭定位图层
        mBaidumap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView=null;
        mPoiSearch.destroy();
        mSuggestionSearch.destroy();
        super.onDestroy();
    }

    private class MyPoiOverlay extends PoiOverlay{
        public MyPoiOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public boolean onPoiClick(int i) {
            super.onPoiClick(i);
            PoiInfo poi=getPoiResult().getAllPoi().get(i);
            mPoiSearch.searchPoiDetail(new PoiDetailSearchOption().poiUid(poi.uid));
            return true;
        }
    }
    /**
     * 获取POI搜索结果
     * @param poiResult
     */
    @Override
    public void onGetPoiResult(PoiResult poiResult) {
        toast.MakeToast(SearchPosition.this,"inside onGetPoiResult");
        if(poiResult==null||poiResult.error== SearchResult.ERRORNO.RESULT_NOT_FOUND){
            Toast.makeText(SearchPosition.this,"未找到结果",Toast.LENGTH_LONG).show();
            return;
        }
        if(poiResult.error==SearchResult.ERRORNO.NO_ERROR)
        {
            mBaidumap.clear();
            PoiOverlay overlay=new MyPoiOverlay(mBaidumap);
            mBaidumap.setOnMarkerClickListener(overlay);
            overlay.setData(poiResult);
            overlay.addToMap();
            //overlay.zoomToSpan();
        }
        /*
        if(poiResult.error==SearchResult.ERRORNO.AMBIGUOUS_KEYWORD){
            //当输入关键字在本市没有找到，但是在其他城市找到时，返回包含该关键字信息的城市列表
            String strInfo="在";
            for(CityInfo cityInfo:poiResult.getSuggestCityList()){
                strInfo+=cityInfo.city;
                strInfo+=",";
            }
            strInfo+="找到结果";
            Toast.makeText(SearchPosition.this, strInfo,Toast.LENGTH_LONG).show();
        }*/
    }

    /**
     * 获取POI详情搜索结果，得到searchPoiDetail返回的搜索结果
     * @param poiDetailResult
     */
    @Override
    public void onGetPoiDetailResult(PoiDetailSearchResult poiDetailResult) {
        toast.MakeToast(SearchPosition.this,"inside onGetDetailResult");
        if(poiDetailResult.error!=SearchResult.ERRORNO.NO_ERROR){
            Toast.makeText(SearchPosition.this,"抱歉，未找到结果",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(SearchPosition.this,poiDetailResult.toString()+":"+poiDetailResult.toString(),Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

    }

    /**
     * 获取在线建议搜索结果，得到requestSuggestion返回的搜索结果,填充到控件中
     * @param suggestionResult
     */
    @Override
    public void onGetSuggestionResult(SuggestionResult suggestionResult) {
        if(suggestionResult==null||suggestionResult.getAllSuggestions()==null){
            return;
        }
        suggest=new ArrayList<String>();
        for(SuggestionResult.SuggestionInfo info: suggestionResult.getAllSuggestions()){
            if(info.key!=null){
                suggest.add(info.key);
            }
        }
        sugAdapter=new ArrayAdapter<String>(SearchPosition.this,android.R.layout.simple_dropdown_item_1line,suggest);
        keyWorldsView.setAdapter(sugAdapter);
        sugAdapter.notifyDataSetChanged();
    }
}