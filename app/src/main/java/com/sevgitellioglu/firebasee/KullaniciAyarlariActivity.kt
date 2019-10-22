package com.sevgitellioglu.firebasee

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.android.synthetic.main.activity_kullanici_ayarlari.*

class KullaniciAyarlariActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kullanici_ayarlari)

        var kullanici=FirebaseAuth.getInstance().currentUser!!

        etDetayKullaniciAdi.setText(kullanici.displayName.toString())
        //etDetayMail.setText(kullanici.email.toString())

        btnDetaySifre.setOnClickListener {
            FirebaseAuth.getInstance().sendPasswordResetEmail(FirebaseAuth.getInstance().currentUser?.email.toString())
                .addOnCompleteListener{ task ->
                    if(task.isSuccessful)
                    {
                        Toast.makeText(this,"Şifre sıfırlama e-maili gönderildi", Toast.LENGTH_SHORT).show()
//
                    }
                    else{
                        Toast.makeText(this,"Hata oluştu", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        btnDegisiklikKaydet.setOnClickListener {
            if(etDetayKullaniciAdi.text.toString().isNotEmpty() )
            {
                if(!etDetayKullaniciAdi.text.toString().equals(FirebaseAuth.getInstance().currentUser?.displayName.toString()))
                {

                    var bilgileriniGuncelle=UserProfileChangeRequest.Builder()
                        .setDisplayName(etDetayKullaniciAdi.text.toString())
                        .build()
                    kullanici.updateProfile(bilgileriniGuncelle)
                        .addOnCompleteListener{task ->
                            if (task.isSuccessful){
                                Toast.makeText(this,"Değişiklikler yapıldı ",Toast.LENGTH_SHORT).show()
                            }
                        }
                }

            }
            else{
                Toast.makeText(this,"Boş alanları doldurunuz",Toast.LENGTH_SHORT).show()

            }
        }

        btnEmailveyaSifreGuncelle.setOnClickListener {
            if(etDetaySifre.text.toString().isNotEmpty()){
                var credential=EmailAuthProvider.getCredential(kullanici.email.toString(),etDetaySifre.text.toString())
                kullanici.reauthenticate(credential)
                    .addOnCompleteListener{
                        task ->
                        if(task.isSuccessful)
                        {
                            guncelleLayout.visibility= View.VISIBLE

                            btnMailGuncelle.setOnClickListener {
                                mailAdresiniGuncelle()
                            }
                            btnSifreGuncelle.setOnClickListener {
                                sifreGuncelle()
                            }

                        }else
                        {
                            Toast.makeText(this,"Şimdi ki şifrenizi yanlış girdiniz",Toast.LENGTH_SHORT).show()
                        }
                    }

            }else{
                Toast.makeText(this,"Güncellemeler için geçerli şifrenizi yazmalısınız ",Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun sifreGuncelle() {
        var kullanici=FirebaseAuth.getInstance().currentUser!!

        if(kullanici!=null)
        {
            kullanici.updatePassword(etYeniSifre.text.toString())
                .addOnCompleteListener { task->
                    Toast.makeText(this,"Şifreniz güncellendi tekrar giriş yapın",Toast.LENGTH_SHORT).show()
                    FirebaseAuth.getInstance().signOut()
                    anasayfayaYonlendir()
                }
        }

    }

    private fun mailAdresiniGuncelle() {
        var kullanici=FirebaseAuth.getInstance().currentUser!!

        if(kullanici!=null)
        {
            FirebaseAuth.getInstance().fetchSignInMethodsForEmail(etYeniMail.text.toString())
                .addOnCompleteListener() {task ->
                    if(task.isSuccessful)
                    {
                        if(task.getResult()?.signInMethods?.size==1)
                        {
                            Toast.makeText(this,"E-mail kullanımda",Toast.LENGTH_SHORT).show()
                        }else{
                            kullanici.updateEmail(etYeniMail.text.toString())
                                .addOnCompleteListener { task->

                                    Toast.makeText(this,"E-mailiniz güncellendi tekrar giriş yapın",Toast.LENGTH_SHORT).show()
                                    FirebaseAuth.getInstance().signOut()
                                    anasayfayaYonlendir()
                        }
                    }

                }else{
                        Toast.makeText(this,"E-mail güncellenemedi",Toast.LENGTH_SHORT).show()
                    }

                }
        }

    }
    fun anasayfayaYonlendir(){
       // Toast.makeText(this@LoginActivity,"E-mail onaylanmıs giriş yapılabilir",Toast.LENGTH_SHORT).show()
        var intent= Intent(this@KullaniciAyarlariActivity,LoginActivity::class.java)
        startActivity(intent)
        finish()

    }
}
