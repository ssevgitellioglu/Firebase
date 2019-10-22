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
import com.google.firebase.auth.FirebaseAuth


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SifremiUnuttumFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class SifremiUnuttumFragment : DialogFragment() {

    lateinit var emailEditText: EditText
    lateinit var mContext: FragmentActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        var view=inflater.inflate(R.layout.fragment_sifremi_unuttum2, container, false)

        mContext= activity!!
        emailEditText=view.findViewById(R.id.tvsifreTekrarYolla)

        var btnIptal=view.findViewById<Button>(R.id.btnSifreIptal)
        btnIptal.setOnClickListener {
            dialog.dismiss()
        }
        var btnSifreTekrarGonder=view.findViewById<Button>(R.id.btnSifreTekrarGonder)

        btnSifreTekrarGonder.setOnClickListener {
            dialog.dismiss()
         FirebaseAuth.getInstance().sendPasswordResetEmail(emailEditText.text.toString())
            .addOnCompleteListener{ task ->
                if(task.isSuccessful)
                {
                    Toast.makeText(mContext,"Şifre sıfırlama e-maili gönderildi", Toast.LENGTH_SHORT).show()
//                    dialog.dismiss()
                }
                else{
                    Toast.makeText(mContext,"Hata oluştu", Toast.LENGTH_SHORT).show()
                }
            }
        }
    return view
    }








}
