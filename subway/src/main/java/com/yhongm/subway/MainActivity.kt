package com.yhongm.subway

import android.support.v4.app.Fragment
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.widget.CheckBox
import android.widget.FrameLayout
import com.yhongm.subway.fragment.AboutFragment
import com.yhongm.subway.fragment.MainSubwayFragment
import com.yhongm.subway.fragment.SearchFragment
import com.yhongm.subway.fragment.SearchResultFragment
import com.yhongm.subway.xml.Subway
import kotlinx.android.synthetic.main.app_bar_main.*
import android.os.Build
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.app.FragmentManager


class MainActivity : AppCompatActivity(),
        NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {


    private val mDrawerLayout: DrawerLayout? = null
    private var frameLayout: FrameLayout? = null
    private var ckShowName: CheckBox? = null
    private var ckShowStation: CheckBox? = null
    private var fabReduce: FloatingActionButton? = null
    private var fabExpand: FloatingActionButton? = null
    private var fabInfo: FloatingActionButton? = null
    private var mainSubwayFragment: MainSubwayFragment? = null
    private var searchFragment: SearchFragment? = null
    private var aboutFragment: AboutFragment? = null


    private var searchResultFragment: SearchResultFragment? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()



        mainSubwayFragment = MainSubwayFragment()

        searchFragment = SearchFragment()

        aboutFragment = AboutFragment()


        searchResultFragment = SearchResultFragment()

        var list = ArrayList<android.support.v4.app.Fragment>()
        list.add(mainSubwayFragment!!)
        list.add(searchFragment!!)
        list.add(aboutFragment!!)
        list.add(searchResultFragment!!)

        var mAdapter = MPageAdapter(supportFragmentManager, list)
        main_vp_fragment.adapter = mAdapter
        main_vp_fragment.offscreenPageLimit = 3

        turnToFragment(mainSubwayFragment!!)

        toolbar.title = "地铁图"


        ckShowName!!.setOnCheckedChangeListener({ _, b ->

            if (main_vp_fragment.currentItem == 0) {
                mainSubwayFragment!!.setIsShowStationName(b)
            } else if (main_vp_fragment.currentItem == 3) {
                searchResultFragment!!.setIsShowStationName(b)
            }
        })

        ckShowStation!!.setOnCheckedChangeListener({ _, b ->

            if (main_vp_fragment.currentItem == 0) {
                mainSubwayFragment!!.setIsShowStationLocation(b)
            } else if (main_vp_fragment.currentItem == 3) {
                searchResultFragment!!.setIsShowStationLocation(b)
            }

        })
        fabReduce!!.setOnClickListener(this)
        fabExpand!!.setOnClickListener(this)
        fabInfo!!.setOnClickListener(this)

    }

    class MPageAdapter(fragmentManager: android.support.v4.app.FragmentManager, list: ArrayList<android.support.v4.app.Fragment>) : FragmentPagerAdapter(fragmentManager) {
        private var mList: ArrayList<android.support.v4.app.Fragment> = list

        override fun getItem(position: Int): android.support.v4.app.Fragment {
            return mList[position]
        }


        override fun getCount(): Int {
            return mList.size
        }


    }


    private fun getBitmap(context: Context, vectorDrawableId: Int): Bitmap? {
        var bitmap: Bitmap? = null
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            val vectorDrawable = context.getDrawable(vectorDrawableId)
            bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                    vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight())
            vectorDrawable.draw(canvas)
        } else {
            bitmap = BitmapFactory.decodeResource(context.getResources(), vectorDrawableId)
        }
        return bitmap
    }

    override fun onBackPressed() {
        if (main_vp_fragment.currentItem != 0) {
            turnToFragment(mainSubwayFragment!!)
        }
        super.onBackPressed()
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.main_fab_expand -> {
                if (main_vp_fragment.currentItem == 0) {
                    mainSubwayFragment!!.setScale(2.0)
                } else if (main_vp_fragment.currentItem == 3) {
                    searchResultFragment!!.setScale(2.0)
                }
            }
            R.id.main_fab_reduce -> {
                if (main_vp_fragment.currentItem == 0) {
                    mainSubwayFragment!!.setScale(0.5)
                } else if (main_vp_fragment.currentItem == 3) {
                    searchResultFragment!!.setScale(0.5)
                }
            }
            R.id.main_fab_info -> {
                if (main_vp_fragment.currentItem == 0) {
                    runOnUiThread { mainSubwayFragment!!.showAlertLineDialog() }

                } else if (main_vp_fragment.currentItem == 3) {
//                    searchResultFragment.setScale(2.0)
                }
            }
        }
    }

    fun initView() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)

        setSupportActionBar(toolbar)

        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

//        frameLayout = findViewById(R.id.vp_fragment)

        ckShowName = findViewById<CheckBox>(R.id.main_ck_show_name)

        ckShowStation = findViewById<CheckBox>(R.id.main_ck_show_station)

        fabReduce = findViewById<FloatingActionButton>(R.id.main_fab_reduce)

        fabExpand = findViewById<FloatingActionButton>(R.id.main_fab_expand)

        fabInfo = findViewById<FloatingActionButton>(R.id.main_fab_info)
    }


    private fun turnToFragment(fragment: android.support.v4.app.Fragment, params: Any? = null) {
        when (fragment) {
            mainSubwayFragment -> main_vp_fragment.currentItem = 0
            searchFragment -> main_vp_fragment.currentItem = 1
            aboutFragment -> main_vp_fragment.currentItem = 2
            searchResultFragment -> {
                val bundle = Bundle()
                bundle.putParcelableArrayList("results", params as java.util.ArrayList<Subway>?)
                searchResultFragment!!.arguments = bundle
                main_vp_fragment.currentItem = 3
                searchResultFragment!!.start()
            }
        }


        if (fragment == searchFragment || fragment == aboutFragment) {

            ckShowName!!.gone()
            ckShowStation!!.gone()
            fabInfo!!.gone()
            fabExpand!!.gone()
            fabReduce!!.gone()


        } else if (fragment.equals(mainSubwayFragment)) {
            ckShowName!!.show()
            ckShowStation!!.show()
            fabInfo!!.show()
            fabExpand!!.show()
            fabReduce!!.show()
        } else if (fragment.equals(searchResultFragment)) {
            ckShowName!!.show()
            ckShowStation!!.show()

            fabExpand!!.show()
            fabReduce!!.show()
        }

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.nav_main -> {
                turnToFragment(mainSubwayFragment!!)
                toolbar.title = "地铁图"
            }
            R.id.nav_search -> {
                turnToFragment(searchFragment!!)
                toolbar.title = "搜索"
            }
            R.id.nav_about -> {
                turnToFragment(aboutFragment!!)
                toolbar.title = "关于"

            }
        }

        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    fun View.gone() {
        visibility = GONE
    }

    fun View.show() {
        visibility = View.VISIBLE

    }

    fun searchResult(subways: ArrayList<Subway>) {
        this.turnToFragment(searchResultFragment!!, subways)
    }


}
