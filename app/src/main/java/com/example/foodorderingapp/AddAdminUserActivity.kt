package com.example.foodorderingapp
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_admin_user.*
import kotlinx.android.synthetic.main.activity_add_item2.backButton

class AddAdminUserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_admin_user)
        backButton.setOnClickListener {
            finish()
        }
        createadminuserButton.setOnClickListener {
            val userName = userName.text.toString()
            val userEmail = userEmail.text.toString()
            val userPassword = userPassword.text.toString()
            if (userName.isEmpty() || userEmail.isEmpty() || userPassword.isEmpty()) {
                Toast.makeText(this, "Please Fill All Fields", Toast.LENGTH_SHORT).show()
            } else if (!isValidEmail(userEmail)) {
                Toast.makeText(this, "Please Enter a Valid Email", Toast.LENGTH_SHORT).show()
            } else {
                addadminuser(userName,userEmail,userPassword)
            }
        }
    }
    val databaseHelper = DatabaseHelper(this)
    private fun addadminuser(name:String,email: String,password:String){
        val insertedrowId = databaseHelper.addAdminUser(name,email,password)
        if (insertedrowId != -1L){
            Toast.makeText(this,"Added New Admin User Successful",Toast.LENGTH_SHORT).show()
            userName.setText("")
            userEmail.setText("")
            userPassword.setText("")

        }else{
            Toast.makeText(this,"Added New Admin User Failed",Toast.LENGTH_SHORT).show()
        }
    }
    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
