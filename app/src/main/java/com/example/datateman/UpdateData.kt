package com.example.datateman

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.view.get
import com.example.datateman.databinding.ActivityUpdateDataBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UpdateData : AppCompatActivity() {
    //Deklarasi variabel
    private var database: DatabaseReference? = null
    private var auth: FirebaseAuth? = null
    private var cekNama: String? = null
    private var cekAlamat: String? = null
    private var cekNoHP: String? = null
    private var cekGender: String? = null
    private lateinit var binding : ActivityUpdateDataBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateDataBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Update Data"
        //Mendapatkan Instance autentikasi dan Referensi dari Database
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference
        data //memanggil method "data"
        binding.update.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                //Mendapatkan Data Mahasiswa yang akan dicek
                cekNama = binding.newNama.getText().toString()
                cekAlamat = binding.newAlamat.getText().toString()
                cekNoHP = binding.newNoHp.getText().toString()
                val selectedGenderId = binding.genderGroup.checkedRadioButtonId
                //Mengecek agar tidak ada data yang kosong, saat proses update
                if (isEmpty(cekNama!!) || isEmpty(cekAlamat!!) || isEmpty(cekNoHP!!) || selectedGenderId == -1) {
                    Toast.makeText(this@UpdateData, "Data tidak boleh ada yang kosong", Toast.LENGTH_SHORT).show()
                } else {
                    /*Menjalankan proses update data.
                    Method Setter digunakan untuk mendapakan data baru yang diinputkan
                    User.*/
                    val genderRadioButton: RadioButton = findViewById(selectedGenderId)
                    val cekGender = genderRadioButton.text.toString()
                    val setTeman = data_teman()
                    setTeman.nama = binding.newNama.getText().toString()
                    setTeman.alamat = binding.newAlamat.getText().toString()
                    setTeman.no_hp = binding.newNoHp.getText().toString()
                    setTeman.gender = cekGender!!
                    updateTeman(setTeman)
                }
            }
        })
    }
    // Mengecek apakah ada data yang kosong, sebelum diupdate
    private fun isEmpty(s: String): Boolean {
        return TextUtils.isEmpty(s)
    }

    //Menampilkan data yang akan di update
    private val data: Unit
        private get() {
            //Menampilkan data dari item yang dipilih sebelumnya
            val getNama = intent.extras!!.getString("dataNama")
            val getAlamat = intent.extras!!.getString("dataAlamat")
            val getNoHp = intent.extras!!.getString("dataNoHp")
            val getGender = intent.extras!!.getString("Jenis Kelamin")
            binding.newNama!!.setText(getNama)
            binding.newAlamat!!.setText(getAlamat)
            binding.newNoHp!!.setText(getNoHp)
            if (getGender == "Male") {
                binding.genderGroup.check(R.id.male)
            } else if (getGender == "Female") {
                binding.genderGroup.check(R.id.female)
            }
        }
    //Proses Update data yang sudah ditentukan  
    private fun updateTeman(teman: data_teman) {
        val userID = auth!!.uid
        val getKey = intent.extras!!.getString("getPrimaryKey")
        database!!.child("Admin").child(userID!!).child("DataTeman").child(getKey!!)
            .setValue(teman)
            .addOnSuccessListener {
                binding.newNama!!.setText("")
                binding.newAlamat!!.setText("")
                binding.newNoHp!!.setText("")
                binding.genderGroup.clearCheck()
                Toast.makeText(this@UpdateData, "Data Berhasil diubah", Toast.LENGTH_SHORT).show()
                finish()
            }
    }
}
