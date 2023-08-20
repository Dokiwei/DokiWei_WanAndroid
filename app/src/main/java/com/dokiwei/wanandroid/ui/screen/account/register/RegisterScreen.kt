package com.dokiwei.wanandroid.ui.screen.account.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.dokiwei.wanandroid.model.util.HomeScreen
import com.dokiwei.wanandroid.model.util.ToastAndLogcatUtil
import com.dokiwei.wanandroid.model.util.mainBody
import com.dokiwei.wanandroid.ui.widgets.Loading

/**
 * @author DokiWei
 * @date 2023/7/8 21:05
 */
@Composable
fun RegisterScreen(navController: NavHostController) {
    val viewModel: RegisterViewModel = viewModel()
    val registerState by viewModel.registerState.collectAsState()
    var name by remember { mutableStateOf("") }
    var psd by remember { mutableStateOf("") }
    var rePsd by remember { mutableStateOf("") }
    LaunchedEffect(registerState.isSuccess) {
        if (registerState.isSuccess) {
            viewModel.dispatch(
                RegisterIntent.SaveUserData(
                    isLoggedIn = true, isChecked = false, name, psd
                )
            )
            navController.navigate(HomeScreen.Main.route)
        }
    }
    LaunchedEffect(registerState.msg) {
        if (registerState.msg.isNotEmpty()) ToastAndLogcatUtil.showMsg(registerState.msg)
    }
    Column(
        modifier = Modifier.mainBody(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(0.8f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "WanAndroid",
                modifier = Modifier.padding(bottom = 30.dp),
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
            OutlinedTextField(label = { Text(text = "用户名") },
                modifier = Modifier.fillMaxWidth(),
                value = name,
                isError = registerState.check.name,
                leadingIcon = { Icon(Icons.Filled.Person, contentDescription = null) },
                onValueChange = {
                    name = it
                    viewModel.dispatch(RegisterIntent.CheckText(it, field = "name"))
                })
            if (registerState.check.name) {
                Text(
                    text = "用户名不能为空",
                    color = MaterialTheme.colorScheme.onError,
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 12.sp
                )
            }
            OutlinedTextField(label = { Text(text = "密码") },
                modifier = Modifier.fillMaxWidth(),
                value = psd,
                isError = registerState.check.psd,
                leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) },
                visualTransformation = PasswordVisualTransformation(),
                onValueChange = {
                    psd = it
                    viewModel.dispatch(RegisterIntent.CheckText(it, field = "psd"))
                })
            if (registerState.check.psd) {
                Text(
                    text = "密码不能为空",
                    color = MaterialTheme.colorScheme.onError,
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 12.sp
                )
            }
            OutlinedTextField(label = { Text(text = "确认密码") },
                modifier = Modifier.fillMaxWidth(),
                value = rePsd,
                isError = registerState.check.rePsd,
                leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) },
                visualTransformation = PasswordVisualTransformation(),
                onValueChange = {
                    rePsd = it
                    viewModel.dispatch(RegisterIntent.CheckText(it, rePsd))
                })
            if (registerState.check.rePsd) {
                Text(
                    text = "密码不相同",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 12.sp
                )
            }
        }
        Button(modifier = Modifier
            .fillMaxWidth(0.7f)
            .padding(top = 10.dp), onClick = {
            viewModel.dispatch(RegisterIntent.Register(name, psd, rePsd))
        }) {
            Text(text = "注册", style = MaterialTheme.typography.titleLarge)
        }
    }
    if (registerState.isLoading) Loading(onClick = {})
}