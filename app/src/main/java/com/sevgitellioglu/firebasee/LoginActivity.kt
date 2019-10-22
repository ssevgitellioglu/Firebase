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
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    lateinit var mAuthStateListener: FirebaseAuth.AuthStateListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initMyAuthStateListener()

        tvRegister.setOnClickListener{
            var intent= Intent(this,RegisterActivity::class.java)
            startActivity(intent)

        }
      tvOnayMaili.setOnClickListener {
          var dialogGoster=OnayMailFragment()
          dialogGoster.show(supportFragmentManager,"Göndere tıklandı")
      }

        tvsifreTekrarYolla.setOnClickListener {
            var dialogSifreTekrarGonder=SifremiUnuttumFragment()
            dialogSifreTekrarGonder.show(supportFragmentManager,"dialogsifre")

        }

        btnGirisyap.setOnClickListener {

            if(edtEmailLogin.text.isNotEmpty() && edtPasswordLogin.text.isNotEmpty())
            {progressBarGoster()
                FirebaseAuth.getInstance().signInWithEmailAndPassword(edtEmailLogin.text.toString(),edtPasswordLogin.text.toString())
                    .addOnCompleteListener(object :OnCompleteListener<AuthResult>{
                        override fun onComplete(p0: Task<AuthResult>) {

                            if(p0.isSuccessful)
                            {
                                progressBarGizle()


                                if(!p0.result?.user?.isEmailVerified!!)
                                {
                                    FirebaseAuth.getInstance().signOut()

                                }
                            
                            }
                              else{

                                progressBarGizle()
                                Toast.makeText(this@LoginActivity,"Hatalı Giriş" ,Toast.LENGTH_SHORT).show()
                            }
                        }

                    })


            }else{
                Toast.makeText(this@LoginActivity,"Boş alanları doldurunuz",Toast.LENGTH_SHORT).show()
            }

        }
    }
    private fun progressBarGoster(){
        progressBar2.visibility= View.VISIBLE
    }
    private fun progressBarGizle(){
        progressBar2.visibility= View.INVISIBLE
    }

    private fun initMyAuthStateListener(){

        mAuthStateListener=object :FirebaseAuth.AuthStateListener{
            override fun onAuthStateChanged(p0: FirebaseAuth) {
                var kullanici=p0.currentUser
                if(kullanici!=null){
                    if(kullanici.isEmailVerified){
                        Toast.makeText(this@LoginActivity,"E-mail adresiniz onaylanmış giriş yapabilirsiniz",Toast.LENGTH_SHORT).show()
                        var intent=Intent(this@LoginActivity,MainActivity::class.java)
                        startActivity(intent)
                        finish()

                    }else{
                        Toast.makeText(this@LoginActivity,"E-mail adresinizi onaylayıp öyle giriş yapınız",Toast.LENGTH_SHORT).show()

                    }
                }
            }

        }

    }
    override fun onStart() {
        super.onStart()
        FirebaseAuth.getInstance().addAuthStateListener (mAuthStateListener)
    }

    override fun onStop() {
        super.onStop()
        FirebaseAuth.getInstance().removeAuthStateListener (mAuthStateListener)
    }
}
