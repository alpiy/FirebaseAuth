package com.example.datateman

class data_teman {

    var nama: String? = null
    var alamat: String? = null
    var no_hp : String? = null
    var key : String? = null
    var gender : String? = null

    //membuat konstruktor kosong untuk data snapshot
    constructor(){}

    //konstruktor dgn parameter, untuk mendapatkan Input Data dari user konstruktor
    constructor(nama: String?,alamat: String?, no_hp: String?,gender: String){
        this.nama = nama
        this.alamat = alamat
        this.no_hp = no_hp
        this.gender = gender
    }
}