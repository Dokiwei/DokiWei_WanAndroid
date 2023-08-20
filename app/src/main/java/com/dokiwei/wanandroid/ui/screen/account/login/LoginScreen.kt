package com.dokiwei.wanandroid.ui.screen.account.login

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.dokiwei.wanandroid.model.util.AccountScreen
import com.dokiwei.wanandroid.model.util.HomeScreen
import com.dokiwei.wanandroid.model.util.ToastAndLogcatUtil
import com.dokiwei.wanandroid.model.util.mainBody
import com.dokiwei.wanandroid.ui.theme.MainTheme
import com.dokiwei.wanandroid.ui.widgets.Loading

/**
 * @author DokiWei
 * @date 2023/7/7 17:53
 */
@Composable
fun LoginScreen(navController: NavHostController) {
    val vm: LoginViewModel = viewModel()
    val loginState by vm.loginState.collectAsState()

    var name by remember { mutableStateOf("") }
    var psd by remember { mutableStateOf("") }
    LaunchedEffect(key1 = loginState.errorMsg, key2 = loginState.isLoggedIn) {
        when {
            loginState.errorMsg.isNotEmpty() -> {
                ToastAndLogcatUtil.showMsg(loginState.errorMsg)
            }

            loginState.isLoggedIn -> {
                vm.dispatch(
                    LoginIntent.SaveUserData(
                        true,
                        loginState.isRememberPasswordChecked,
                        name,
                        psd
                    )
                )
                navController.navigate(HomeScreen.Main.route)
            }
        }
    }
    LaunchedEffect(loginState.rememberLoginSate) {
        //默认会自动填充保存的用户名,并且在保存的数据中记住密码为true的话,密码也会自动填充
        loginState.rememberLoginSate.also {
            it.username?.let { itName -> name = itName }
        }.takeIf {
            it.isRememberPassword
        }?.let {
            vm.dispatch(LoginIntent.UpdateRememberPasswordChecked(true))
            it.password?.let { itPsd -> psd = itPsd }
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
                isError = loginState.check.name,
                leadingIcon = { Icon(Icons.Filled.Person, contentDescription = null) },
                onValueChange = {
                    name = it
                    vm.dispatch(LoginIntent.CheckText(it, "name"))
                })
            if (loginState.check.name) {
                Text(
                    text = "用户名不能为空",
                    color = MaterialTheme.colorScheme.onError,
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
                isError = loginState.check.psd,
                leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) },
                visualTransformation = PasswordVisualTransformation(),
                onValueChange = {
                    psd = it
                    vm.dispatch(LoginIntent.CheckText(it, "psd"))
                })
            if (loginState.check.psd) {
                Text(
                    text = "密码不能为空",
                    color = MaterialTheme.colorScheme.error,
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
                    onCheckedChange = { vm.dispatch(LoginIntent.UpdateRememberPasswordChecked(it)) })
                Text(text = "记住密码")
            }
            TextButton(onClick = { navController.navigate(AccountScreen.Register.route) }) {
                Text(text = "注册")
            }
        }
        Button(modifier = Modifier
            .fillMaxWidth(0.7f)
            .padding(top = 10.dp),
            onClick = {
                vm.dispatch(LoginIntent.Login(name, psd))
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
