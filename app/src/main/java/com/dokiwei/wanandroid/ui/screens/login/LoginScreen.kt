package com.dokiwei.wanandroid.ui.screens.login

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.dokiwei.wanandroid.ui.component.Loading
import com.dokiwei.wanandroid.ui.component.mainBody
import com.dokiwei.wanandroid.ui.theme.MainTheme
import kotlin.system.exitProcess

/**
 * @author DokiWei
 * @date 2023/7/7 17:53
 */
@Composable
fun LoginScreen(navController: NavHostController) {
    val viewModel: LoginViewModel = viewModel()
    val context = LocalContext.current
    val loginState by viewModel.loginState.collectAsState()
    val isError by viewModel.loginIsError.collectAsState()
    var name by remember { mutableStateOf("") }
    var psd by remember { mutableStateOf("") }
    //返回监听
    val backPressed = remember { mutableLongStateOf(0L) }
    BackHandler(onBack = {
        if (backPressed.longValue + 2000 > System.currentTimeMillis()) {
            exitProcess(0)
        } else {
            Toast.makeText(context, "再按一次退出", Toast.LENGTH_SHORT).show()
            backPressed.longValue = System.currentTimeMillis()
        }
    })
    LaunchedEffect(Unit) {
        //默认会自动填充保存的用户名,并且在保存的数据中记住密码为true的话,密码也会自动填充
        viewModel.getUserData(context).also {
            it.username?.let { itName -> name = itName }
        }.takeIf {
            it.isRememberPassword
        }?.let {
            viewModel.updateRememberPasswordChecked(checked = true)
            it.password?.let { itPsd -> psd = itPsd }
        }
    }
    LaunchedEffect(loginState) {
        if (loginState.isLoggedIn) {
            viewModel.saveUserData(context, true, loginState.isRememberPasswordChecked, name, psd)
            navController.navigate("主页")
        }
    }
    Column(
        modifier = Modifier.mainBody(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.8f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "WanAndroid",
                modifier = Modifier.padding(bottom = 30.dp),
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
            OutlinedTextField(
                label = { Text(text = "用户名") },
                modifier = Modifier
                    .fillMaxWidth(),
                value = name,
                isError = isError.name,
                leadingIcon = { Icon(Icons.Filled.Person, contentDescription = null) },
                onValueChange = {
                    name = it
                    viewModel.checkText(it, "name")
                })
            if (isError.name) {
                Text(
                    text = "用户名不能为空",
                    color = Color.Red,
                    modifier = Modifier
                        .fillMaxWidth(),
                    fontSize = 12.sp
                )
            }
            OutlinedTextField(
                label = { Text(text = "密码") },
                modifier = Modifier
                    .fillMaxWidth(),
                value = psd,
                isError = isError.psd,
                leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) },
                visualTransformation = PasswordVisualTransformation(),
                onValueChange = {
                    psd = it
                    viewModel.checkText(it, "psd")
                })
            if (isError.psd) {
                Text(
                    text = "密码不能为空",
                    color = Color.Red,
                    modifier = Modifier
                        .fillMaxWidth(),
                    fontSize = 12.sp
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(0.8f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = loginState.isRememberPasswordChecked,
                    onCheckedChange = { viewModel.updateRememberPasswordChecked(it) })
                Text(text = "记住密码")
            }
            TextButton(onClick = { navController.navigate("注册页") }) {
                Text(text = "注册")
            }
        }
        Button(modifier = Modifier
            .fillMaxWidth(0.7f)
            .padding(top = 10.dp), onClick = {
            viewModel.login(context, name, psd)
        }) {
            Text(text = "登录", style = MaterialTheme.typography.titleLarge)
        }
    }
    if (loginState.isLoading) {
        Loading(onClick = {})
    }
}

@Preview(showBackground = true)
@Composable
fun Pre() {
    MainTheme {
        LoginScreen(navController = rememberNavController())
    }
}
