package com.proyecto.mallnav.ui.fragments;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.proyecto.mallnav.service.NavigationService.ACTION_POSITION_ERROR;
import static com.proyecto.mallnav.service.NavigationService.ACTION_POSITION_UPDATED;
import static com.proyecto.mallnav.utils.Constants.CIRCULAR_PROGRESS_DELAY_HIDE;
import static com.proyecto.mallnav.utils.Constants.CIRCULAR_PROGRESS_DELAY_SHOW;
import static com.proyecto.mallnav.utils.Constants.DL_QUERY_LOCATION_ID;
import static com.proyecto.mallnav.utils.Constants.DL_QUERY_SUBLOCATION_ID;
import static com.proyecto.mallnav.utils.Constants.DL_QUERY_VENUE_ID;
import static com.proyecto.mallnav.utils.Constants.KEY_VENUE_CATEGORY;
import static com.proyecto.mallnav.utils.Constants.KEY_VENUE_POINT;
import static com.proyecto.mallnav.utils.Constants.KEY_VENUE_SUBLOCATION;
import static com.proyecto.mallnav.utils.Constants.LOCATION_CHANGED;
import static com.proyecto.mallnav.utils.Constants.TAG;
import static com.proyecto.mallnav.utils.Constants.VENUE_FILTER_OFF;
import static com.proyecto.mallnav.utils.Constants.VENUE_FILTER_ON;
import static com.proyecto.mallnav.utils.Constants.VENUE_SELECTED;

import android.animation.LayoutTransition;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.DataSetObserver;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.divider.MaterialDividerItemDecoration;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textview.MaterialTextView;
import com.navigine.idl.java.AnimationType;
import com.navigine.idl.java.Camera;
import com.navigine.idl.java.CameraListener;
import com.navigine.idl.java.CameraUpdateReason;
import com.navigine.idl.java.Category;
import com.navigine.idl.java.IconMapObject;
import com.navigine.idl.java.InputListener;
import com.navigine.idl.java.Location;
import com.navigine.idl.java.LocationListener;
import com.navigine.idl.java.LocationManager;
import com.navigine.idl.java.LocationPoint;
import com.navigine.idl.java.LocationPolyline;
import com.navigine.idl.java.MapObjectPickResult;
import com.navigine.idl.java.MeasurementManager;
import com.navigine.idl.java.NavigationManager;
import com.navigine.idl.java.PickListener;
import com.navigine.idl.java.Point;
import com.navigine.idl.java.Polyline;
import com.navigine.idl.java.PolylineMapObject;
import com.navigine.idl.java.Position;
import com.navigine.idl.java.PositionListener;
import com.navigine.idl.java.ResourceManager;
import com.navigine.idl.java.RouteEvent;
import com.navigine.idl.java.RouteEventType;
import com.navigine.idl.java.RouteListener;
import com.navigine.idl.java.RouteManager;
import com.navigine.idl.java.RoutePath;
import com.navigine.idl.java.Sublocation;
import com.navigine.idl.java.Venue;
import com.proyecto.mallnav.R;
//import com.proyecto.mallnav.adapters.route.RouteEventAdapter;
//import com.proyecto.mallnav.adapters.sublocations.SublocationsAdapter;
import com.proyecto.mallnav.adapters.venues.VenueListAdapter;
//import com.proyecto.mallnav.adapters.venues.VenuesIconsListAdapter;
import com.proyecto.mallnav.models.VenueIconObj;
import com.proyecto.mallnav.service.NavigationService;
//import com.proyecto.mallnav.ui.custom.lists.BottomSheetListView;
//import com.proyecto.mallnav.ui.custom.lists.ListViewLimit;
//import com.proyecto.mallnav.ui.dialogs.sheets.BottomSheetRouteFinish;
//import com.proyecto.mallnav.ui.dialogs.sheets.BottomSheetVenue;
//import com.proyecto.mallnav.utils.ColorUtils;
import com.proyecto.mallnav.utils.DimensionUtils;
//import com.proyecto.mallnav.utils.KeyboardController;
import com.proyecto.mallnav.utils.NavigineSdkManager;
//import com.proyecto.mallnav.utils.VenueIconsListProvider;
//import com.proyecto.mallnav.viewmodel.SharedViewModel;
import com.navigine.view.LocationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;



public class NavigationFragment extends BaseFragment {
    public static final int     LOCATION_ID    = 1563;
    public static final int     SUBLOCATION_ID = 2091;
    private static final String KEY_VENUE       = "name";
    public static final int LOCATION_LOAD_DELAY = 5_000;
    private static final float ROUTE_FINISH_DISTANCE_NOTICE = 1.5f;
    public static final boolean WRITE_LOGS     = false;

    private Window                        window                     = null;
    private SearchView                    mSearchField               = null;
    private ConstraintLayout              mSearchPanel               = null;
    private ConstraintLayout              mNavigationLayout          = null;
    private ConstraintLayout              mNoLocationLayout          = null;
    private ConstraintLayout              mMakeRouteSheet            = null;
    private ConstraintLayout              mCancelRouteSheet          = null;
    private LinearLayout                  mSearchLayout              = null;
    private FrameLayout                   mTransparentBackground     = null;
    private FrameLayout                   mVenueListLayout           = null;
    private FrameLayout                   mVenueIconsLayout          = null;
    private FrameLayout                   mArrowUpLayout             = null;
    private FrameLayout                   mArrowDownLayout           = null;
    private FrameLayout                   mZoomInLayout              = null;
    private FrameLayout                   mZoomOutLayout             = null;
    private FrameLayout                   mCircularProgress          = null;
    private FrameLayout                   mAdjustModeButton          = null;
    private MaterialTextView              mFromCurrentText           = null;
    private MaterialTextView              mToText                    = null;
    private MaterialTextView              mCancelRouteDistance       = null;
    private MaterialTextView              mCancelRouteTime           = null;
    private MaterialTextView              mWarningMessage            = null;
    private MaterialTextView              mDelayMessage              = null;
    private CircularProgressIndicator     mCircularProgressIndicator = null;
    private ImageView                     mFromImageView             = null;
    private ImageView                     mSearchBtnClear            = null;
    private MaterialButton                mSearchBtn                 = null;
    private MaterialButton                mSearchBtnClose            = null;
    private MaterialButton                mChoseMapButton            = null;
    private MaterialButton                mStartRouteButton          = null;
    private MaterialButton                mRouteSheetCancelButton    = null;
    private BottomSheetBehavior           mMakeRouteBehavior         = null;
    private BottomSheetBehavior           mCancelRouteBehaviour      = null;
    //private BottomSheetListView           mCancelRouteListView       = null;
    //private ListViewLimit                 mSublocationsListView      = null;
    private RecyclerView                  mVenueIconsListView        = null;
    private RecyclerView                  mVenueListView             = null;
    //private BottomSheetVenue              mVenueBottomSheet          = null;
    private IconMapObject                 mPositionIcon              = null;
    private MaterialDividerItemDecoration mItemDivider               = null;
    private HorizontalScrollView          mChipsScroll               = null;
    private ChipGroup                     mChipGroup                 = null;


    LocationView mLocationView;
    IconMapObject mPosition = null;
    Sublocation mSubLocation;
    LocationListener mLocationListener;
    LocationManager mLocationManager = NavigineSdkManager.LocationManager;
    PositionListener mPositionListener = null;
    NavigationManager mNavigationManager = NavigineSdkManager.NavigationManager;
    ResourceManager mResourceManager = null;
    RouteManager mRouteManager = null;
    MeasurementManager mMeasurementManager = null;

    private LocationPoint mPinPoint    = null;
    private LocationPoint mTargetPoint = null;
    private LocationPoint mFromPoint   = null;

    private Venue mToVenue     = null;
    private Venue mFromVenue   = null;
    private Venue mTargetVenue = null;
    private Venue mPinVenue    = null;

    private Location    mLocation    = null;
    private Sublocation mSublocation = null;

    private List<RouteEvent>        mCancelRouteList          = new ArrayList<>();
    private ArrayList<Point>        mPoints                   = new ArrayList<>();
    private List<Venue>             mVenuesList               = new ArrayList<>();
    private List<VenueIconObj>      mFilteredVenueIconsList   = new ArrayList<>();
    private Map<Chip, VenueIconObj> mChipsMap                 = new HashMap<>();

    //private RouteEventAdapter                mRouteEventAdapter      = null;
    //private VenueListAdapter                 mVenueListAdapter       = null;
    //private VenuesIconsListAdapter           mVenuesIconsListAdapter = null;
    //private SublocationsAdapter<Sublocation> mSublocationsAdapter    = null;

    private IconMapObject     mPinIconTarget       = null;
    private IconMapObject     mPinIconFrom         = null;
    private PolylineMapObject mPolylineMapObject   = null;
    private RoutePath         mRoutePath           = null;
    private RoutePath         mLastActiveRoutePath = null;

    private static Handler  mHandler = new Handler();

    private RouteListener        mRouteListener             = null;

    //private StateReceiver mStateReceiver = null;
    //private PositionReceiver mPositionReceiver = null;

    private IntentFilter mStateReceiverFilter = null;
    private IntentFilter mPositionReceiverFilter = null;


    private boolean mAdjustMode        = false;
    private boolean mSelectMapPoint    = false;
    private boolean mLocationChanged   = false;
    private boolean mLocationLoaded    = false;
    private boolean mRouting           = false;
    private boolean mSetupPosition     = true;

    private int mSublocationId = -1;
    private int mVenueId       = -1;

    private float mZoomCameraDefault = 0f;

    private final Runnable mDelayMessageCallback = () -> {
        if (mCircularProgressIndicator.isShown()) {
            mDelayMessage.setVisibility(VISIBLE);
        }
    };

    private LocationPoint mPositionLocationPoint = null;

    //private BottomSheetRouteFinish mBottomSheetRouteFinish = null;

    //private Position mPosition = null;

    LocationPoint mLocPointForever21, mLocPointOldNavy, mLocPointConverse, mLocPointTennis,
    mLocPointBoato, mLocPointAdidas;

    ArrayList<Venue> mRackList;
    ArrayList<String> mVenuesAlias = new ArrayList<>();
    LocationPoint curLocPoint;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initNavigation();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navigation, container, false);
        initViews(view);
        mLocationView = view.findViewById(R.id.location_view);
        mPosition = mLocationView.getLocationWindow().addIconMapObject();
        mPosition.setSize(30, 30);
        mPosition.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_current_point));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        onHiddenChanged(!isVisible());
        //addListeners();
    }

    private void initViews(View view){
        mWarningMessage = view.findViewById(R.id.navigation__warning);
    }

    @Override
    protected void updateWarningMessageState() {

        if (!isGpsEnabled() && !isBluetoothEnabled())
        {
            showWarning(getString(R.string.err_navigation_state_gps_bluetooth));
            return;
        }

        if (!isBluetoothEnabled())
        {
            showWarning(getString(R.string.err_navigation_state_bluetooth));
            return;
        }

        if (!isGpsEnabled())
        {
            showWarning(getString(R.string.err_navigation_state_gps));
            return;
        }

        hideWarning();
    }

    private void showWarning(String message) {
        mWarningMessage.setText(message);
        mWarningMessage.setVisibility(VISIBLE);
    }

    private void hideWarning() {
        mWarningMessage.setVisibility(GONE);
    }

    @Override
    protected void updateStatusBar() {
        window.setStatusBarColor(mNavigationLayout.getVisibility() == VISIBLE ?
                ContextCompat.getColor(requireActivity(), R.color.colorOnBackground) :
                ContextCompat.getColor(requireActivity(), R.color.colorBackground));
    }

    @Override
    protected void updateUiState() {
        //if (mLocationChanged) showLoadProgress();
        //if (mLocationLoaded)  loadMap();
        if (mLocationView != null) mLocationView.onStart();
    }

    public void initNavigation(){
        mLocationListener = new LocationListener() {
            @Override
            public void onLocationLoaded(Location location) {
                mSubLocation = location.getSublocations().get(0);
                mRackList = mSubLocation.getVenues();
                for(int i=0; i<mRackList.size(); i++){
                    String temp = mRackList.get(i).getAlias();
                    mVenuesAlias.add(temp);
                }
                mLocPointForever21 = new LocationPoint(getStorePoint("SECTOR01"),LOCATION_ID,SUBLOCATION_ID);
                mLocPointOldNavy = new LocationPoint(getStorePoint("SECTOR02"),LOCATION_ID,SUBLOCATION_ID);
                mLocPointConverse = new LocationPoint(getStorePoint("SECTOR03"),LOCATION_ID,SUBLOCATION_ID);
                mLocPointTennis = new LocationPoint(getStorePoint("SECTOR04"),LOCATION_ID,SUBLOCATION_ID);
                mLocPointBoato = new LocationPoint(getStorePoint("SECTOR05"),LOCATION_ID,SUBLOCATION_ID);
                mLocPointAdidas = new LocationPoint(getStorePoint("SECTOR06"),LOCATION_ID,SUBLOCATION_ID);

                
                mLocationView.getLocationWindow().setSublocationId(SUBLOCATION_ID);
            }

            @Override
            public void onLocationFailed(int i, Error error) {
                Toast.makeText(getContext(), "failed load location", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onLocationUploaded(int i) {

            }
        };


        mLocationManager.addLocationListener(mLocationListener);
        mLocationManager.setLocationId(LOCATION_ID);
    }

    private Point getStorePoint(String sector){
        Point resultPoint = null;
        for(int k = 0; k< mVenuesAlias.size(); k++){
            String temp = mVenuesAlias.get(k);
            if(temp.equals(sector)){
                resultPoint = mRackList.get(k).getPoint();
                break;
            }
        }
        return resultPoint;
    }

    /*private void initListeners() {

        mRouteListener = new RouteListener() {
            @Override
            public void onPathsUpdated(ArrayList<RoutePath> arrayList) {

                if (!mRouting) return;

                if (arrayList == null || arrayList.isEmpty()) {
                    cancelRouteAndHideSheet();
                    showWarningTemp(getString(R.string.err_navigation_no_route), 3000);
                    return;
                }

                mPoints.clear();

                try {
                    mRoutePath = arrayList.get(0);

                    if (mRoutePath == null) return;

                    List<LocationPoint> routePathPoints = mRoutePath.getPoints();

                    if (routePathPoints == null) return;


                    for (LocationPoint locationPoint : mRoutePath.getPoints()) {
                        if (locationPoint.getSublocationId() == mSublocation.getId()) {
                            mPoints.add(locationPoint.getPoint());
                        }
                    }

                    if (!mPoints.isEmpty()) {
                        LocationPolyline polyline = new LocationPolyline(new Polyline(mPoints), mLocation.getId(), mSublocation.getId());
                        mPolylineMapObject.setPolyLine(polyline);
                        mPolylineMapObject.setVisible(true);
                    } else {
                        mPolylineMapObject.setVisible(false);
                    }

                    if (mRoutePath.getLength() < ROUTE_FINISH_DISTANCE_NOTICE) showRouteFinishSheet();

                } catch (IndexOutOfBoundsException e) {
                    mRoutePath = null;
                    mPolylineMapObject.setVisible(false);
                }

                handleDeviceUpdate(mRoutePath);
            }
        };
    }*/

}