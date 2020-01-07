package com.github.cta_elevator_alerts_kotlin.activities

/**
 * AllLinesActivity displays all CTA L lines
 * so user can click on them to go to a specific line.
 *
 * @author Southport Developers (Sam Siner & Tyler Arndt)
 */

//class AllLinesActivity : AppCompatActivity() {
//    lateinit var allLinesViewModel: AllLinesViewModel
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_all_lines)
//
//        allLinesViewModel = ViewModelProviders.of(this).get(AllLinesViewModel::class.java)
//
//        val linesRecyclerView = findViewById<RecyclerView>(R.id.recycler_all_lines)
//        val linesAdapter = AllLinesAdapter(this)
//        linesAdapter.setToolbarTextView()
//        linesRecyclerView.adapter = linesAdapter
//        linesRecyclerView.layoutManager = LinearLayoutManager(this)
//
//        val about = findViewById<ImageView>(R.id.img_home_icon)
//        about.visibility = View.INVISIBLE
//    }
//
//    fun toMainActivity(v: View) {
//        val intent = Intent(this@AllLinesActivity, MainActivity::class.java)
//        startActivity(intent)
//    }
//
//    fun onBackPressed(v: View) {
//        this.onBackPressed()
//    }
//
//    override fun onBackPressed() {
//        super.onBackPressed()
//        if (!intent.getBooleanExtra("fromFavorites", false)) {
//            val intent = Intent(this@AllLinesActivity, MainActivity::class.java)
//            startActivity(intent)
//        }
//    }
//}
