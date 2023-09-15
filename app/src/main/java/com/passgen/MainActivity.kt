package com.passgen

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.passgen.ui.theme.PassGenTheme
import java.util.*
import kotlin.math.ln

data class Password (val inputPassword : String) {
    val password = inputPassword
    val entropy = calculatePasswordEntropy(inputPassword)

    private fun calculatePasswordEntropy(password: String): Double {
        val passwordLength = password.length.toDouble()
        val characterSet = password.toCharArray().toSet()
        val characterSetSize = characterSet.size.toDouble() // Размер актуального символьного набора в пароле

        return passwordLength * (ln(characterSetSize) / ln(2.0))
    }

}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PassGenTheme {
                SetUI()
                }
            }
        }
}

@Composable
fun SetUI() {
        Scaffold(
            topBar = { TopAppBar(title = {
                Text("Password Generator")
            }) },
            )
            {it
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    val mContext = LocalContext.current
                    val password = remember{mutableStateOf(Password(""))}
                    Button(onClick = {
                        password.value = genPassword(mContext)
                    })
                    {
                        Text(text = "Cгенерировать пароль")
                    }
                    Row {
                        Text("Сложность пароля: ")
                        Text(String.format("%.2f", password.value.entropy))
                    }
                    Row {
                        Text("Пароль: ")
                        Text(password.value.password)
                    }
                }
        }
}


fun genPassword(context: Context): Password {
    val characterSet = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
    val random = Random(System.nanoTime())
    val passwordBuilder = StringBuilder()

    val rand2  = random.nextInt(5)+4
    for (i in 0 until rand2) {
        val rIndex = random.nextInt(characterSet.length)
        passwordBuilder.append(characterSet[rIndex])
    }
    val resStr = passwordBuilder.toString()
    Toast.makeText(context, "Пароль $resStr сгенерирован!", Toast.LENGTH_SHORT).show()

    return Password(resStr)
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PassGenTheme {
        SetUI()
    }
}