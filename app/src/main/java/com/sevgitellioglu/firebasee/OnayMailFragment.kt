package com.sevgitellioglu.firebasee


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.EmailAuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class OnayMailFragment : DialogFragment() {
    lateinit var emailText:EditText
    lateinit var sifreText:EditText
    lateinit var mContext:FragmentActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_onay_mail2, container, false)
        emailText=view.findViewById(R.id.eTDialogMail)
        sifreText=view.findViewById(R.id.eTDialogSifre)
        mContext= activity!!

        var btnDialogMailiptal=view.findViewById<Button>(R.id.btnDialogMailiptal)
        btnDialogMailiptal.setOnClickListener {
            dialog.dismiss()
        }

        var btnDialogGonder=view.findViewById<Button>(R.id.btnDialogGonder)
        btnDialogGonder.setOnClickListener {

            if(emailText.text.toString().isNotEmpty() && sifreText.text.toString().isNotEmpty())
            {

                girisYapVeOnayMailiniTekrarGonder(emailText.text.toString(),sifreText.text.toString())

            }
            else{
                Toast.makeText(mContext,"Boş alanları doldurunuz",Toast.LENGTH_SHORT).show()
            }
        }


        return view

    }

    private fun girisYapVeOnayMailiniTekrarGonder(email: String, sifre: String) {

        var credential=EmailAuthProvider.getCredential(email,sifre)
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener{task->
                if(task.isSuccessful)
                {
                    onayMailiniTekrarGonder()
                    dialog.dismiss()
                }
                else{
                    Toast.makeText(activity,"E-mail veya şifre yanlış",Toast.LENGTH_SHORT).show()
                }
            }

    }

    private fun onayMailiniTekrarGonder() {
        var kullanici=FirebaseAuth.getInstance().currentUser

        if(kullanici !=null){
            kullanici.sendEmailVerification()
                .addOnCompleteListener (object : OnCompleteListener<Void> {
                    override fun onComplete(p0: Task<Void>) {
                        if(p0.isSuccessful){
                            Toast.makeText(mContext,"E-mailinizi kontrol ediniz,hesabınızı onaylayın" +p0.exception?.message,Toast.LENGTH_SHORT).show()
                        }
                        else{
                            Toast.makeText(mContext,"E-mailinizi gönderilirken hata oluştu" +p0.exception?.message,Toast.LENGTH_SHORT).show()

                        }
                    }

                })
        }
    }


}
