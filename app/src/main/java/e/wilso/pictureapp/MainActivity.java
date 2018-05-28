package e.wilso.pictureapp;

import android.Manifest;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;
import android.widget.SimpleCursorAdapter;

import static android.Manifest.permission.*;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

   private static final int REQUEST_READ_STORAGE = 3;
   SimpleCursorAdapter adapter;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);

      int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

      /*Log.d("Permission", "permission: " + permission);
      Log.d("Granted", "PackageManager: " + PackageManager.PERMISSION_GRANTED);*/

      if(permission != PackageManager.PERMISSION_GRANTED) {
         ActivityCompat.requestPermissions(this, new String[]{READ_EXTERNAL_STORAGE}, REQUEST_READ_STORAGE);
      }
      else {
         readThumbnails();
      }
   }

   @Override
   public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
      super.onRequestPermissionsResult(requestCode, permissions, grantResults);

      switch(requestCode) {
         case REQUEST_READ_STORAGE:
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
               readThumbnails();
            }
            else {
               new AlertDialog.Builder(this).setMessage("必須允許外部讀取權限").setPositiveButton("OK", null).show();
            }
            return;
      }
   }

   private void readThumbnails() {
      GridView grid = findViewById(R.id.grid);
      String[] from = {MediaStore.Images.Thumbnails.DATA, MediaStore.Images.Media.DISPLAY_NAME};
      int[] to = new int[] {R.id.thumb_image, R.id.thumb_text};
      adapter = new SimpleCursorAdapter(
              getBaseContext(),
              R.layout.thumb_item,
              null,
              from,
              to,
              0);
      grid.setAdapter(adapter);
      getLoaderManager().initLoader(0, null, this);
   }

   @Override
   public Loader<Cursor> onCreateLoader(int id, Bundle args) {
      Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
      return new CursorLoader(this, uri, null,null,null,null);
   }

   @Override
   public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
      adapter.swapCursor(data);
   }

   @Override
   public void onLoaderReset(Loader<Cursor> loader) {

   }
}
