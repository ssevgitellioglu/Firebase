package com.sevgitellioglu.firebasee

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        btnsignup.setOnClickListener{
            if(edTemail.text.isNotEmpty() && edTpassword.text.isNotEmpty()&& edTpasswordagain.text.isNotEmpty())
            {

                if(edTpassword.text.toString().equals(edTpasswordagain.text.toString())) {
                    yeniUyeKayit(edTemail.text.toString(),edTpassword.text.toString())

                }
                else {
                    Toast.makeText(this,"Şifreler aynı değil",Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(this,"Lütfen boş alanları doldurunuz",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun yeniUyeKayit(Email:String,Sifre:String) {
        progressBarGoster()
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(Email,Sifre)
            .addOnCompleteListener(object:OnCompleteListener<AuthResult>{
                override fun onComplete(p0: Task<AuthResult>) {

                    if(p0.isSuccessful){


                        onayMailiGonder()

                        var veritabaninaEklenekKullanici=Kullanici()
                        veritabaninaEklenekKullanici.isim=edTemail.text.toString().substring(0,edTemail.text.toString().indexOf("@"))
                        veritabaninaEklenekKullanici.kullanici_id=FirebaseAuth.getInstance().currentUser?.uid
                        veritabaninaEklenekKullanici.profil_resmi=""
                        veritabaninaEklenekKullanici.telefon="123456"
                        veritabaninaEklenekKullanici.seviye="1"

                        FirebaseDatabase.getInstance().reference
                            .child("kullanici")
                            .child(FirebaseAuth.getInstance().currentUser!!.uid)
                            .setValue(veritabaninaEklenekKullanici).addOnCompleteListener { task ->  
                              if (task.isSuccessful){
                                  Toast.makeText(this@RegisterActivity,"Üye kaydı başarılı"+FirebaseAuth.getInstance().currentUser,Toast.LENGTH_SHORT).show()
                                  FirebaseAuth.getInstance().signOut()
                                  anasayfayaYonlendir()
                              }

                            }
                    }
                    else{

                        Toast.makeText(this@RegisterActivity,"Üye kaydedilirken hata oluştu"+p0.exception?.message,Toast.LENGTH_SHORT).show()
                    }

                }

            })
        progressBarGizle()


    }

    private fun onayMailiGonder() {
        var kullanici=FirebaseAuth.getInstance().currentUser

        if(kullanici !=null){
            kullanici.sendEmailVerification()
                .addOnCompleteListener (object :OnCompleteListener<Void>{
                    override fun onComplete(p0: Task<Void>) {
                        if(p0.isSuccessful){
                            Toast.makeText(this@RegisterActivity,"E-mailinizi kontrol ediniz,hesabınızı onaylayın" ,Toast.LENGTH_SHORT).show()
                        }
                        else{
                            Toast.makeText(this@RegisterActivity,"E-mailinizi gönderilirken hata oluştu" ,Toast.LENGTH_SHORT).show()

                        }
                    }

                })
        }
    }

    private fun progressBarGoster(){
        progressBar.visibility=View.VISIBLE
    }
    private fun progressBarGizle(){
        progressBar.visibility=View.INVISIBLE
    }
   private fun anasayfayaYonlendir(){
        // Toast.makeText(this@LoginActivity,"E-mail onaylanmıs giriş yapılabilir",Toast.LENGTH_SHORT).show()
        var intent= Intent(this@RegisterActivity,LoginActivity::class.java)
        startActivity(intent)
        finish()

    }
}
