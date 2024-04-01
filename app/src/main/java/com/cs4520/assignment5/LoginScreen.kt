package com.cs4520.assignment5

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun LoginFields(navController: NavController) {
    MaterialTheme {
        Surface (color = MaterialTheme.colors.background, modifier = Modifier.fillMaxSize()) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val edit1Text = remember { mutableStateOf("") }
                val edit2Text = remember { mutableStateOf("") }
                val context = LocalContext.current
                val loginError = stringResource(R.string.error)
                TextField(
                    value = edit1Text.value,
                    placeholder = { Text(stringResource(R.string.username_hint)) },
                    onValueChange = { edit1Text.value = it },
                    modifier = Modifier.padding(12.dp)
                )
                TextField(
                    value = edit2Text.value,
                    placeholder = { Text(stringResource(R.string.password_hint)) },
                    onValueChange = { edit2Text.value = it },
                    modifier = Modifier.padding(12.dp),
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )
                Button(
                    onClick = {
                              if (edit1Text.value == "admin" && edit2Text.value == "admin") {
                                navController.navigate("productList")
                              } else {
                                  val toast = Toast.makeText(context, loginError, Toast.LENGTH_SHORT)
                                  toast.show()
                              }
                    },
                ) {
                    Text(text = stringResource(id = R.string.login_button))
                }
            }
        }
    }
}

//@Preview
//@Composable
//fun PreviewLoginFields() {
//    LoginFields()
//}