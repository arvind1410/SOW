package com.silicontechnologies.plantix;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

public class LocationHelper
    implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
    LocationListener {
  /**
   * Constant used in the location settings dialog.
   */
  public static final int REQUEST_CHECK_SETTINGS = 8001;
  /**
   * Request code to attempt to resolve Google Play services connection failures.
   */
  public final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 8000;

  public static final int CONST_API_CONNECTION = 8002;
  public static final int CONST_LOCATION_SETTINGS = 8003;
  protected static final String TAG = LocationHelper.class.getSimpleName();
  /**
   * The desired interval for location updates. Inexact. Updates may be more or less frequent.
   */
  private long updateIntervalInMilliseconds = 5000;
  /**
   * The fastest rate for active location updates. Exact. Updates will never be more frequent
   * than this value.
   */
  private long fastestUpdateIntervalInMilliseconds = updateIntervalInMilliseconds / 2;
  /**
   * Provides the entry point to Google Play services.
   */
  private GoogleApiClient mGoogleApiClient;
  /**  
   * Stores parameters for requests to the FusedLocationProviderApi.
   */
  private LocationRequest mLocationRequest;
  private Activity activity;
  private LocationListener onLocationCompleteListener;

  public LocationHelper(@NonNull Activity activity,
      @NonNull LocationListener onLocationCompleteListener) {
    this.activity = activity;
    this.onLocationCompleteListener = onLocationCompleteListener;
  }

  public void setUpdateIntervalInMilliseconds(long updateIntervalInMilliseconds) {
    this.updateIntervalInMilliseconds = updateIntervalInMilliseconds;
  }

  public void setFastestUpdateIntervalInMilliseconds(long fastestUpdateIntervalInMilliseconds) {
    this.fastestUpdateIntervalInMilliseconds = fastestUpdateIntervalInMilliseconds;
  }

  public void init() {
    if (mGoogleApiClient == null) {
      buildGoogleApiClient();
    }
    mGoogleApiClient.connect();
  }

  /**
   * Builds a GoogleApiClient. Uses the {@code #addApi} method to request the
   * LocationServices API.
   */
  private synchronized void buildGoogleApiClient() {
    mGoogleApiClient = new GoogleApiClient.Builder(activity).addConnectionCallbacks(this)
        .addOnConnectionFailedListener(this)
        .addApi(LocationServices.API)
        .build();
  }

  public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
    switch (requestCode) {
      case CONNECTION_FAILURE_RESOLUTION_REQUEST:
        if (resultCode == Activity.RESULT_OK) {
          mGoogleApiClient.connect();
        } else {
          onLocationCompleteListener.onUserDeclineRequest(CONST_API_CONNECTION);
        }
        return true;
      case REQUEST_CHECK_SETTINGS:
        if (resultCode == Activity.RESULT_OK) {
          onLocationCompleteListener.onReadyForLocationUpdates();
        } else {
          onLocationCompleteListener.onUserDeclineRequest(CONST_LOCATION_SETTINGS);
        }
        return true;
      default:
        return false;
    }
  }

  /**
   * Sets up the location request. Android has two location request settings:
   * {@code ACCESS_COARSE_LOCATION} and {@code ACCESS_FINE_LOCATION}. These settings control
   * the accuracy of the current location. This sample uses ACCESS_FINE_LOCATION, as defined in
   * the AndroidManifest.xml.
   * <p>
   * When the ACCESS_FINE_LOCATION setting is specified, combined with a fast update
   * interval (5 seconds), the Fused Location Provider API returns location updates that are
   * accurate to within a few feet.
   * <p>
   * These settings are appropriate for mapping applications that show real-time location
   * updates.
   */
  private void createLocationRequest() {
    mLocationRequest = new LocationRequest();
    // Sets the desired interval for active location updates. This interval is
    // inexact. You may not receive updates at all if no location sources are available, or
    // you may receive them slower than requested. You may also receive updates faster than
    // requested if other applications are requesting location at a faster interval.
    mLocationRequest.setInterval(updateIntervalInMilliseconds);

    // Sets the fastest rate for active location updates. This interval is exact, and your
    // application will never receive updates faster than this value.
    mLocationRequest.setFastestInterval(fastestUpdateIntervalInMilliseconds);

    mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
  }

  /**
   * Requests location updates from the FusedLocationApi.
   */
  @SuppressWarnings("MissingPermission") public void startLocationUpdates() {
    LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest,
        this);
  }

  /**
   * Removes location updates from the FusedLocationApi.
   */
  public void stopLocationUpdates() {
    // It is a good practice to remove location requests when the activity is in a paused or
    // stopped state. Doing so helps battery performance and is especially
    // recommended in applications that request frequent location updates.
    if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
      LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
      if (mGoogleApiClient != null) {
        mGoogleApiClient.disconnect();
      }
    }
  }

  @Override public void onConnected(Bundle bundle) {
    checkLocationSettings();
  }

  @Override public void onConnectionSuspended(int i) {
    mGoogleApiClient.connect();
  }

  @Override public void onLocationChanged(Location location) {
    onLocationCompleteListener.onLocationUpdate(location);
  }

  @Override public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    if (connectionResult.hasResolution()) {
      try {
        connectionResult.startResolutionForResult(activity, CONNECTION_FAILURE_RESOLUTION_REQUEST);
      } catch (IntentSender.SendIntentException e) {
        onLocationCompleteListener.onUnRecoverableError(CONST_API_CONNECTION, null);
      }
    } else {
      onLocationCompleteListener.onUnRecoverableError(CONST_API_CONNECTION,
          connectionResult.getErrorMessage());
    }
  }

  protected void checkLocationSettings() {
    if (mLocationRequest == null) {
      createLocationRequest();
    }
    PendingResult<LocationSettingsResult> result =
        LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient,
            new LocationSettingsRequest.Builder().setAlwaysShow(true)
                .addLocationRequest(mLocationRequest)
                .build());
    result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
      @Override public void onResult(LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
          case LocationSettingsStatusCodes.SUCCESS:
            onLocationCompleteListener.onReadyForLocationUpdates();
            break;
          case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
            // Location is not available, but we can ask permission from users
            try {
              // Show the dialog by calling startResolutionForResult(),
              // and check the result in onActivityResult().
              status.startResolutionForResult(activity, REQUEST_CHECK_SETTINGS);
            } catch (IntentSender.SendIntentException e) {
              onLocationCompleteListener.onUnRecoverableError(CONST_LOCATION_SETTINGS, null);
            }
            break;
          case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
            onLocationCompleteListener.onUnRecoverableError(CONST_LOCATION_SETTINGS,
                status.getStatusMessage());
            break;
        }
      }
    });
  }

  public void terminate() {
    stopLocationUpdates();
    if (mGoogleApiClient != null) {
      mGoogleApiClient.unregisterConnectionCallbacks(this);
      mGoogleApiClient.unregisterConnectionFailedListener(this);
    }
    activity = null;
  }

  public interface LocationListener {

    void onLocationUpdate(Location location);

    void onReadyForLocationUpdates();

    void onUnRecoverableError(int what, @Nullable String errorMsg);

    void onUserDeclineRequest(int what);
  }
}
