package com.better.cordova;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import java.io.File;

import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONException;

public class CustomFileChooser extends CordovaPlugin {

    private static final String TAG = "CustomFileChooser";
    private static final String ACTION_OPEN = "open";
    private static final String ACTION_CREATE = "create";
    private static final String ACTION_GET_DB_PATH = "getDatabasePath";
    private static final int PICK_FILE_REQUEST = 1;
    CallbackContext callback;

    @Override
    public boolean execute(String action, CordovaArgs args, CallbackContext callbackContext) throws JSONException {

        if (action.equals(ACTION_OPEN)) {
            chooseFile(callbackContext, args.getString(0));
            return true;
        }
        if (action.equals(ACTION_CREATE)) {
            createFile(callbackContext, args.getString(0), args.getString(1));
            return true;
        }
        if (action.equals(ACTION_GET_DB_PATH)) {
            getDbPath(callbackContext, args.getString(0));
            return true;
        }

        return false;
    }

    public void getDbPath(CallbackContext callbackContext,String name) {
        File source = cordova.getActivity().getDatabasePath(name);
        callbackContext.success("file://" + source.getAbsolutePath());
    }
	
    public void createFile(CallbackContext callbackContext,String type, String name) {

        // type and title should be configurable
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.setType(type);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.putExtra(Intent.EXTRA_TITLE, name);

        Intent chooser = Intent.createChooser(intent, "Select File");
        cordova.startActivityForResult(this, chooser, PICK_FILE_REQUEST);

        PluginResult pluginResult = new PluginResult(PluginResult.Status.NO_RESULT);
        pluginResult.setKeepCallback(true);
        callback = callbackContext;
        callbackContext.sendPluginResult(pluginResult);
    }

    public void chooseFile(CallbackContext callbackContext,String type) {

        // type and title should be configurable

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType(type);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);

        Intent chooser = Intent.createChooser(intent, "Select File");
        cordova.startActivityForResult(this, chooser, PICK_FILE_REQUEST);

        PluginResult pluginResult = new PluginResult(PluginResult.Status.NO_RESULT);
        pluginResult.setKeepCallback(true);
        callback = callbackContext;
        callbackContext.sendPluginResult(pluginResult);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PICK_FILE_REQUEST && callback != null) {

            if (resultCode == Activity.RESULT_OK) {

                Uri uri = data.getData();

                if (uri != null) {

                    Log.w(TAG, uri.toString());
                    callback.success(uri.toString());

                } else {

                    callback.error("File uri was null");

                }

            } else if (resultCode == Activity.RESULT_CANCELED) {

                // TODO NO_RESULT or error callback?
                PluginResult pluginResult = new PluginResult(PluginResult.Status.NO_RESULT);
                callback.sendPluginResult(pluginResult);

            } else {

                callback.error(resultCode);
            }
        }
    }
}
