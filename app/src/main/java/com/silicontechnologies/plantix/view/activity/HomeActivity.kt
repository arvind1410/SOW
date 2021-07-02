package com.silicontechnologies.plantix.view.activity

import android.content.Intent
import android.media.MediaScannerConnection
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.ImageView
import com.silicontechnologies.plantfix.SowActivity
import com.silicontechnologies.plantix.CottonActivity
import com.silicontechnologies.plantix.R
import com.silicontechnologies.plantix.app.AppConstants
import com.silicontechnologies.plantix.base.BaseActivity
import com.silicontechnologies.plantix.view.HomePagerAdapter
import kotlinx.android.synthetic.main.activity_home.fab_menu
import kotlinx.android.synthetic.main.activity_home.fab_menu_camera
import kotlinx.android.synthetic.main.activity_home.fab_menu_gallery
import kotlinx.android.synthetic.main.activity_home.tabs
import kotlinx.android.synthetic.main.activity_home.viewpager
import java.io.File

class HomeActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        copyAssetFilesToMemory()


        val viewPagerAdapter = HomePagerAdapter(
                supportFragmentManager)
        viewpager.adapter = viewPagerAdapter
        tabs.setupWithViewPager(viewpager)
        fab_menu_camera.setOnClickListener {
            selectCameraAttachment()

        }
        fab_menu_gallery.setOnClickListener {
            selectGalleryAttachment()
        }

    }

    fun selectCameraAttachment() {
        fab_menu.close(true);
        val cameraIntent = Intent(this, PhotoDetailsActivity::class.java)
                .putExtra("requestCode", AppConstants.REQUEST_CAMERA_PICK)
        startActivityForResult(cameraIntent, AppConstants.REQUEST_CAMERA_PICK)
        //object = cnn_model(IMAGE).{"username":"arvindpori","key":"af12ffefcb96100597da1bd2ee313dd2"}

    }

    fun selectGalleryAttachment() {
        fab_menu.close(true);
        val cameraIntent = Intent(this, PhotoDetailsActivity::class.java)
                .putExtra("requestCode", AppConstants.REQUEST_GALLERY_PICK)
        startActivityForResult(cameraIntent, AppConstants.REQUEST_GALLERY_PICK)
        //object = cnn_model(IMAGE).{"username":"arvindpori","key":"af12ffefcb96100597da1bd2ee313dd2"}
    }

    fun copyAssetFilesToMemory() {

        val path = getGalleryPath() + "SOW";
        var file = File(path);
        if (!file.exists()) {
            file.mkdir()
        }
        val assetManager = assets
        val listofFiles = assetManager.list("photos");
        for (item in listofFiles) {
            var outputFile = File(file, item)
            assets.open("photos/" + item).use { input ->
                outputFile.outputStream().buffered().use { output ->
                    input.copyTo(output, 10240)
                }
            }
        }
        MediaScannerConnection.scanFile(this, arrayOf(file.toString()), null
        ) { _, _ ->

        }
    }

    private fun getGalleryPath(): String {
        return Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).toString()
    }
}