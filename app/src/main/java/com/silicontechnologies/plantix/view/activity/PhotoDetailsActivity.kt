package com.silicontechnologies.plantix.view.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.Point
import android.graphics.drawable.BitmapDrawable
import android.media.Image
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.provider.MediaStore.Images.Media
import android.support.design.widget.Snackbar
import android.support.v4.content.FileProvider
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.silicontechnologies.plantix.BuildConfig
import com.silicontechnologies.plantix.R
import com.silicontechnologies.plantix.app.AppConstants
import com.silicontechnologies.plantix.base.BaseActivity
import kotlinx.android.synthetic.main.activity_details.*
import java.io.File
import java.io.IOException
import java.nio.channels.AsynchronousFileChannel.open
import javax.microedition.khronos.opengles.GL10

class PhotoDetailsActivity : BaseActivity() {

  private var mCameraUri: Uri? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)         
    setContentView(R.layout.activity_details)
    var requestCode = intent.getIntExtra("requestCode", -1)
    setSupportActionBar(toolbar)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    if (requestCode == AppConstants.REQUEST_CAMERA_PICK)
      selectCameraAttachment()
    else
      selectGalleryAttachment()
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (resultCode != Activity.RESULT_OK) {
      finish()
      return
    }
    handleImageFromGalleryAndCamera(requestCode, data)
  }

  fun handleImageFromGalleryAndCamera(
    requestCode: Int,
    data: Intent?
  ) {
    if (requestCode == AppConstants.REQUEST_GALLERY_PICK) {
      var mBitmap = Media.getBitmap(contentResolver, data?.getData())
      if (mBitmap.width > mBitmap.height) {
        mBitmap = changeOrientation(mBitmap)
        val ov = createResizedBitmap(mBitmap);
        iv_selected.setImageDrawable(ov)
      }
      if (mBitmap.height > GL10.GL_MAX_TEXTURE_SIZE || mBitmap.width > GL10.GL_MAX_TEXTURE_SIZE) {
        val ob = createResizedBitmap(mBitmap)
        iv_selected.setImageDrawable(ob)
      } else {
        val ob = BitmapDrawable(resources, mBitmap)
        iv_selected.setImageDrawable(ob)
      }
      validateImage(getNameFromUri(data?.data!!))
    }
    if (requestCode == AppConstants.REQUEST_CAMERA_PICK) {
      var mBitmap = Media.getBitmap(contentResolver, mCameraUri)
      if (mBitmap.width > mBitmap.height) {
        mBitmap = changeOrientation(mBitmap)
      }
      if (mBitmap.height > GL10.GL_MAX_TEXTURE_SIZE || mBitmap.width > GL10.GL_MAX_TEXTURE_SIZE) {
        val ob = createResizedBitmap(mBitmap)
        iv_selected.setImageDrawable(ob)
      } else {
        val ob = BitmapDrawable(resources, mBitmap)
        iv_selected.setImageDrawable(ob)
      }
      Handler().postDelayed({
        ll_status.visibility = View.VISIBLE
        pb_indeterminate.visibility = View.GONE
        tv_image_name.setText("Unable to identify the image")
        tv_status.setText("N/A")
        showMaterialDialogue()
      }, 5000)

    }
  }

  fun showMaterialDialogue() {
    val view = View.inflate(this, R.layout.item_input, null);
    val materialDialogue = MaterialDialog.Builder(this)
        .title("Unable to Identify")
        .positiveText("Proceed")
        .negativeText("Cancel")
        .onPositive { dialog, which ->
          val input = view.findViewById<EditText>(R.id.et_input).text.toString().trim()
          if (input.isEmpty()) {
            Snackbar.make(view, "Enter Plant Name", Snackbar.LENGTH_SHORT).show()
            Toast.makeText(this, "The Details are sent to KVK Center", Toast.LENGTH_SHORT).show()

            return@onPositive
          }
          displayMaize(input)
          dialog.dismiss()
        }
    materialDialogue!!.customView(view, true)
    materialDialogue!!.build()
    materialDialogue.show()
  }

  fun validateImage(name: String) {
    Handler().postDelayed({
      pb_indeterminate.visibility = View.GONE
      ll_status.visibility = View.VISIBLE
      displayUI(name)
    }, 5000)
  }

  private fun displayMaize(name: String) {
    if (name.contains("Maize", true)) {
      ll_normal.visibility = View.VISIBLE
      ll_disease.visibility = View.GONE
      tv_image_name.setText("Choose Image is Maize")
      tv_status.setText("Normal")
    } else {
      ll_status.visibility = View.VISIBLE
      pb_indeterminate.visibility = View.GONE
      tv_image_name.setText("Unable to identify the image")
      tv_status.setText("N/A")
    }
  }

  private fun displayUI(name: String) {
    if (getNormalImages().contains(name)) {
      ll_normal.visibility = View.VISIBLE
      ll_disease.visibility = View.GONE
      tv_image_name.setText("Chosen Image is Maize")
      tv_status.setText("Normal")
    }
    else if (getDiseaseImages().contains(name)) {
      ll_normal.visibility = View.GONE
      ll_disease.visibility = View.VISIBLE
      tv_image_name.setText("Chosen Image is Maize")
      tv_status.setText("Diseased")
    }
    else if (getPepperImages().contains(name)){
      item_pepper_disease.visibility = View.GONE
      item_pepper_normal.visibility = View.VISIBLE
      tv_image_name.setText("Chosen Image is pepper")
      tv_status.setText("Normal")
    }
    else if (getPepperDiseaseImages().contains(name)){
      item_pepper_normal.visibility = View.GONE
      item_pepper_disease.visibility = View.VISIBLE
      tv_image_name.setText("Chosen Image is Pepper")
      tv_status.setText("Diseased")
    }
    else if (getCottonImages().contains(name)){
      item_pepper_disease.visibility = View.GONE
      item_pepper_normal.visibility = View.VISIBLE
      tv_image_name.setText("Chosen Image is Cotton")
      tv_status.setText("Normal")
    }
    else if (getCottonDiseaseImages().contains(name)){
      item_pepper_normal.visibility = View.GONE
      item_pepper_disease.visibility = View.VISIBLE
      tv_image_name.setText("Chosen Image is Cotton")
      tv_status.setText("Diseased")
    }
    else {
      ll_status.visibility = View.VISIBLE
      pb_indeterminate.visibility = View.GONE
      tv_image_name.setText("Unable to identify the image")
      tv_status.setText("N/A")
      showMaterialDialogue()
    }
  }

  private fun changeOrientation(mBitmap: Bitmap): Bitmap {
    val mat = Matrix()
    mat.postRotate(90f)
    return Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.width,
        mBitmap.height, mat, true)
  }

  private fun createResizedBitmap(mBitmap: Bitmap): BitmapDrawable {
    val display = windowManager.defaultDisplay
    val size = Point()
    display.getSize(size)
    val putImage = Bitmap.createScaledBitmap(mBitmap, size.x, size.y / 2, true)
    return BitmapDrawable(resources, putImage)
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.menu_nearbylocation, menu)
    return super.onCreateOptionsMenu(menu)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    if (item.itemId == android.R.id.home) {
      super.onBackPressed()
      return true;
    } else if (item.itemId == R.id.action_location) {
      startActivity(Intent(this, NearByActivity::class.java))
    }

    return super.onOptionsItemSelected(item)

  }

  fun getNameFromUri(uri: Uri): String {
    val selectedImage = uri
    val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
    val cursor = contentResolver.query(selectedImage,
        filePathColumn, null, null, null)
    cursor!!.moveToFirst()
    val columnIndex = cursor.getColumnIndex(filePathColumn[0])
    val picturePath = cursor.getString(columnIndex)
    val file = File(picturePath)
    return file.getName()
  }

  //Maize Normal
  fun getNormalImages(): List<String> {
    var normalList = ArrayList<String>()
    normalList.add("maize_normal_1.JPG")
    normalList.add("maize_normal_2.JPG")
    normalList.add("maize_normal_3.JPG")
    normalList.add("maize_normal_4.JPG")
    normalList.add("maize_normal_5.JPG")
    return normalList
  }
  //Maize Disease
  fun getDiseaseImages(): List<String> {
    var normalList = ArrayList<String>()
    normalList.add("maize_disease_1.JPG")
    normalList.add("maize_disease_2.JPG")
    normalList.add("maize_disease_3.JPG")
    normalList.add("maize_disease_4.JPG")
    normalList.add("maize_disease_5.JPG")
    return normalList
  }

  //Disease Rice
  fun getPepperDiseaseImages(): List<String> {
    var normalList = ArrayList<String>()
    normalList.add("pepper_disease1.JPG")
    normalList.add("pepper_disease2.JPG")
    return normalList
  }
  //Rice normal
  fun getPepperImages(): List<String> {
    var normalList = ArrayList<String>()
    normalList.add("pepper_normal1.JPG")
    normalList.add("pepper_normal2.JPG")
    return normalList
  }
  //sugarcane normal
  /*fun getSugarImages(): List<String> {
    var normalList = ArrayList<String>()

    return normalList
  }
  //Diseased sugarcane
  fun getSugarDiseaseImages(): List<String> {
    var normalList = ArrayList<String>()

    return normalList
  }*/
  //Cotton normal
  fun getCottonImages(): List<String> {
    var normalList = ArrayList<String>()
    normalList.add("cotton_normal1.jpg")
    normalList.add("cotton_normal2.jpg")
    return normalList
  }
  //Cotton DiseaseImages
  fun getCottonDiseaseImages(): List<String> {
    var normalList = ArrayList<String>()
    normalList.add("cotton_disease1.jpg")
    normalList.add("cotton_disease2.jpg")
    return normalList
  }
  //Weeds
  fun getWeedsImages(): List<String> {
    var normalList = ArrayList<String>()
    normalList.add("maize_weed_1.JPG")
    normalList.add("maize_weed_2.JPG")
    normalList.add("maize_weed_3.JPG")
    return normalList
  }

  fun selectCameraAttachment() {
    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    try {
      mCameraUri = createUriForCameraIntent(this)
      cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCameraUri)
      cameraIntent.flags = Intent.FLAG_GRANT_WRITE_URI_PERMISSION or
          Intent.FLAG_GRANT_READ_URI_PERMISSION
      if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
        val resInfoList = packageManager.queryIntentActivities(cameraIntent,
            PackageManager.MATCH_DEFAULT_ONLY)
        for (resolveInfo in resInfoList) {
          val packageName = resolveInfo.activityInfo.packageName
          grantUriPermission(packageName, mCameraUri,
              Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
      }
    } catch (e: IOException) {
      e.printStackTrace()
    }

    startActivityForResult(cameraIntent, AppConstants.REQUEST_CAMERA_PICK)

  }

  fun selectGalleryAttachment() {
    val pickIntent = Intent(Intent.ACTION_PICK,
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
    pickIntent.type = "image/*"
    startActivityForResult(pickIntent, AppConstants.REQUEST_GALLERY_PICK)

  }

  private fun createUriForCameraIntent(context: Context): Uri {
    val parent = File(context.filesDir, BuildConfig.ATTACHMENT_FILES_FOLDER)
    parent.mkdirs()
    val file = File(parent, System.currentTimeMillis().toString() + ".jpg")
    if (!file.exists())
      file.createNewFile()

    return FileProvider.getUriForFile(context, BuildConfig.FILE_PROVIDER_AUTHORITY, file)
  }

}