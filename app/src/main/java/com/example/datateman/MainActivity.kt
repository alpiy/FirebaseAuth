package com.example.datateman

import android.content.Intent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.example.datateman.databinding.ActivityMainBinding
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import android.widget.RadioGroup
import android.widget.RadioButton

class MainActivity : AppCompatActivity(),View.OnClickListener {
    private var auth:FirebaseAuth? = null
    private val  RC_SIGN_IN = 1
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.logout.setOnClickListener(this)
        binding.save.setOnClickListener(this)
        binding.showData.setOnClickListener(this)

        auth= FirebaseAuth.getInstance()
    }

    private fun isEmpty(s: String): Boolean{
        return  TextUtils.isEmpty(s)
    }

    override fun onClick(p0: View?) {
        when (p0?.getId()){
            R.id.save ->    {
                //get user id dari user yg terauth
                val getUserID = auth!!.currentUser!!.uid

                //get instance dari database
                val database = FirebaseDatabase.getInstance()

                //save data yg diinputkan user kedlm variable
                val getNama: String = binding.nama.getText().toString()
                val getAlamat: String = binding.alamat.getText().toString()
                val getNoHP: String = binding.noHp.getText().toString()

                val selectedGenderId = binding.genderGroup.checkedRadioButtonId
                val genderRadioButton: RadioButton = findViewById(selectedGenderId)
                val getGender: String = genderRadioButton.text.toString()

                //get referensi dari Database
                val getReference: DatabaseReference
                getReference = database.reference

                //cek apakah ada data yang kosong
                if (isEmpty(getNama) || isEmpty(getAlamat) || isEmpty(getNoHP)|| selectedGenderId == -1){
                    //if ada , maka akan show pesan like this
                    Toast.makeText(this@MainActivity, "Data Tidak Boleh Kosong",
                        Toast.LENGTH_SHORT).show()
                } else {
                    //if tidak ada, maka saved ke database sesuai id masing" akun
                    getReference.child("Admin").child(getUserID).child("DataTeman").push()
                        .setValue(data_teman(getNama,getAlamat,getNoHP,getGender))
                        .addOnCompleteListener(this){
                            //bagian ini terjadi when user success save data
                            binding.nama.setText("")
                            binding.alamat.setText("")
                            binding.noHp.setText("")
                            binding.genderGroup.clearCheck()
                            Toast.makeText(this@MainActivity,"Data Tersimpan",
                                Toast.LENGTH_SHORT).show()
                        }
                }
            }
            R.id.logout ->  {
                AuthUI.getInstance().signOut(this)
                    .addOnCompleteListener(object : OnCompleteListener<Void>{
                        override fun onComplete(p0: Task<Void>) {
                            Toast.makeText(this@MainActivity,"Logout Okeee",Toast.LENGTH_SHORT).show()
                            intent = Intent(applicationContext, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    })
                }
            R.id.show_data -> {
                startActivity(Intent(this@MainActivity, MyListData::class.java))
            }
        }
    }
}