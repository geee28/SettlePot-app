package com.example.trial

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_homepageevents.*
import maes.tech.intentanim.CustomIntent
import maes.tech.intentanim.CustomIntent.customType

class homepageevents : AppCompatActivity() {

    //static variables
    companion object {
        var eventscounter: Int = 0
    }

    //global variables
    private lateinit var reference: DatabaseReference
    var layoutList: LinearLayout? = null
    private lateinit var GetEventsref: DatabaseReference
    private lateinit var GetDeleteEventsref: DatabaseReference
    var x: Int = 0
    var i: Int = 0
    var deleteventflag: Int = 0
    lateinit var geteventsdata : ValueEventListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepageevents)


        layoutList = findViewById(R.id.layout_list)
        layoutList!!.clearAnimation()


        //read events data to display in homepage
        GetEventsref = FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().currentUser!!.uid)
                .child("Events")
        geteventsdata = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (counterobj in snapshot.children) {
                        val eventsobj: events? = counterobj.child("Event Details").getValue(events::class.java)
                      if(deleteventflag == 0) {
                            readeventsView(eventsobj)
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(baseContext, "Firebase Database Exceptions called - onCancelled(PayersInput)", Toast.LENGTH_SHORT).show()
            }
        }
        GetEventsref.addValueEventListener(geteventsdata)


        //ratio split activity is called
        ratiosplitbutton.setOnClickListener {
            val ratiosplitintent = Intent(this, RatioSplitmainpage::class.java)
            startActivity(ratiosplitintent)
            customType(this, "left-to-right")
            finish()
        }

        //edit user profile info activity is called
        editprofileicon.setOnClickListener {
            val profileintent = Intent(this, profilepage::class.java)
            startActivity(profileintent)
            customType(this, "fadein-to-fadeout")
            finish()
        }

        //logout from SettlePot
        logoutbutton.setOnClickListener {
            MainActivity.flag = 0
            FirebaseAuth.getInstance().signOut()
            Toast.makeText(baseContext, "Logged out successfully", Toast.LENGTH_SHORT).show()
            val logoutIntent = Intent(this, MainActivity::class.java)
            logoutIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(logoutIntent)
            customType(this, "fadein-to-fadeout")
            finish()

        }

        //Add events activity is called
        addeventbutton.setOnClickListener {
            eventscounter++
            reference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().currentUser!!.uid).child("Events")
            var neweid = reference.push().key.toString()
            val intent = Intent(applicationContext, EventActivity::class.java)
            intent.putExtra("neweventid", neweid)
            startActivity(intent)
            EventActivity.buttonflag=0
            NotesInput.read_temp_notes_list.clear()
            NotesInput.notes = null
            NonPayersInput.readnonpayersList.clear()
            NonPayersInput.nonpayercount = 1
            PayersInput.readpayersList.clear()
            PayersInput.payercount = 1
            EventActivity.subeventscounter = 0
            SubeventActivity.subeventnamecounter = 0
            finish()
        }
    }

    //remove event view when clicked on delete
    private fun removeView(view: View) {
        layoutList!!.removeView(view) //removeView is an inbuilt func
    }

    //view the events data read
    private fun readeventsView(sampleobject: events?) {
        val eventView: View = layoutInflater.inflate(R.layout.row_add_events, null, false)
        val eventwrite = eventView.findViewById<View>(R.id.events_name) as Button
        eventwrite.setText(sampleobject?.ename.toString())
        eventwrite.setTag(sampleobject?.eid.toString())

        val deleteevent = eventView.findViewById<View>(R.id.delete_events) as ImageButton
        deleteevent.setOnClickListener {

            //show alert message before deleting
            var builder = AlertDialog.Builder(this)
            builder.setMessage("Are you sure?")
                .setTitle("Confirm Delete")
                .setPositiveButton(R.string.positive, DialogInterface.OnClickListener { dialog, id ->
                    // CONFIRM

                    deleteventflag = 1

                    GetDeleteEventsref = FirebaseDatabase.getInstance().getReference("Users")
                        .child(FirebaseAuth.getInstance().currentUser!!.uid)
                        .child("Events")
                    GetDeleteEventsref.child(sampleobject?.eid.toString()).removeValue()

                    removeView(eventView)
                    dialog.cancel()
                })
                .setNegativeButton(R.string.negative, DialogInterface.OnClickListener { dialog, id ->
                    // CANCEL
                    dialog.cancel()
                })
            // Create the AlertDialog object and return it
            var alert = builder.create()
            alert.show()

        }

        Log.d("Inside readevents view - Name ", eventwrite.text.toString())
        Log.d("Inside readevents view - Id ", eventwrite.getTag().toString())

        layoutList!!.addView(eventView)


        //when a read event is clicked, event activity is called
        eventwrite.setOnClickListener {
            val eventstartintent = Intent(this,EventActivity::class.java)
            eventstartintent.putExtra("readeventid", eventwrite.getTag().toString())
            NotesInput.read_temp_notes_list.clear()
            NotesInput.notes = null
            EventActivity.buttonflag=1
            NonPayersInput.readnonpayersList.clear()
            NonPayersInput.nonpayercount = 1
            PayersInput.readpayersList.clear()
            PayersInput.payercount = 1
            EventActivity.subeventscounter = 0
            SubeventActivity.subeventnamecounter = 0
            startActivity(eventstartintent)
            customType(this, "bottom-to-top")
            finish()
        }
    }

    //save user's activity when the app is sent to the background
    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putInt("userstatusflag",MainActivity.flag)
        outPersistentState.putInt("userstatusflag",MainActivity.flag)
        Log.d("Inside onsaveinstance: ", MainActivity.flag.toString())
    }
}