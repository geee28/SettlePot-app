package com.example.trial


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_roles_subevent.*
import maes.tech.intentanim.CustomIntent
import maes.tech.intentanim.CustomIntent.customType

class rolesSubevent : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_roles_subevent)

        var eid: String? = null
        var sid:String? = null

        var callerintent = intent

        //receive data from caller intent
        if(callerintent.hasExtra("Currenteventid")) {             //from eventactivity
            eid  = callerintent.getStringExtra("Currenteventid")
            sid  = callerintent.getStringExtra("Currentsubeventid")
        }
        else if(callerintent.hasExtra("backtorolespid - eid")){
            eid = callerintent.getStringExtra("backtorolespid - eid")
            sid = callerintent.getStringExtra("backtorolespid - sid")
        }
        else if(callerintent.hasExtra("backtorolesnpid_subevent - eid")){
            eid = callerintent.getStringExtra("backtorolesnpid_subevent - eid")
            sid = callerintent.getStringExtra("backtorolesnpid_subevent - sid")
        }

        //go to the previous page
        backbuttonrolespage_subevent.setOnClickListener{
            val backbuttonrolespage_intent = Intent(this, SubeventActivity::class.java)
            backbuttonrolespage_intent.putExtra("Backfromrolestosubevent eid",eid)
            backbuttonrolespage_intent.putExtra("Backfromrolestosubevent sid",sid)
            startActivity(backbuttonrolespage_intent)
            customType(this, "right-to-left")
            finish()
        }

        //go to payers input for subevents
        payers_subevent.setOnClickListener {
            val payers_intent = Intent(this, PayersInputSubevents::class.java)
            payers_intent.putExtra("payerid_subevents - eid",eid)
            payers_intent.putExtra("payerid_subevents - sid",sid)
            startActivity(payers_intent)
            customType(this, "left-to-right")
            finish()
        }

        //go to nonpayers input for subevents
        nonpayers_subevent.setOnClickListener {
            val nonpayers_intent = Intent(this, NonPayersInputSubevents::class.java)
            nonpayers_intent.putExtra("nonpayerid_subevents - eid",eid)
            nonpayers_intent.putExtra("nonpayerid_subevents - sid",sid)
            startActivity(nonpayers_intent)
            customType(this, "left-to-right")
            finish()
        }

        //go to payers input for subevents
        both_subevent.setOnClickListener {
            val intent1 = Intent(this, PayersInputSubevents::class.java)
            intent1.putExtra("callerfromboth_subevents - eid", eid)
            intent1.putExtra("callerfromboth_subevents - sid", sid)
            startActivity(intent1)
            customType(this, "left-to-right")
            finish()
        }
    }
}