package com.example.trial

class notesclass {
    var n_id: String? = ""
    var notesdata: MutableList<String> = mutableListOf()
    constructor(){}

    //store notes data and notes data
    constructor(n_id: String?, notesdata: MutableList<String>) {
        this.n_id = n_id
        this.notesdata = notesdata
    }

}