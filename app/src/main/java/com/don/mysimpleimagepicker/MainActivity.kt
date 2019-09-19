package com.don.mysimpleimagepicker

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.fxn.utility.ImageQuality
import com.fxn.pix.Options
import com.fxn.pix.Pix
import android.widget.Toast
import android.content.pm.PackageManager
import com.fxn.utility.PermUtil
import android.app.Activity
import android.content.Intent
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView


class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    lateinit var myAdapter: MyAdapter
    lateinit var options: Options
    var returnValue: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.setLayoutManager(LinearLayoutManager(this))
        myAdapter = MyAdapter(this)
        options = Options.init()
            .setRequestCode(100)
            .setCount(3)
            .setFrontfacing(false)
            .setImageQuality(ImageQuality.LOW)
            .setPreSelectedUrls(returnValue)
            .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT)
            .setPath("/akshay/new")

        recyclerView.adapter = myAdapter

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener {
            options.preSelectedUrls = returnValue
            Pix.start(this@MainActivity, options)
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //Log.e("val", "requestCode ->  " + requestCode+"  resultCode "+resultCode);
        when (requestCode) {
            100 -> {
                if (resultCode == Activity.RESULT_OK) {
                    returnValue = data!!.getStringArrayListExtra(Pix.IMAGE_RESULTS)
                    myAdapter.addImage(returnValue)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Pix.start(this@MainActivity, options)
                } else {
                    Toast.makeText(this@MainActivity, "Approve permissions to open Pix ImagePicker", Toast.LENGTH_LONG)
                        .show()
                }
                return
            }
        }
    }
}
