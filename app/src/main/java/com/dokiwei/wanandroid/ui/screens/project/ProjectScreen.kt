package com.dokiwei.wanandroid.ui.screens.project

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.List
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.dokiwei.wanandroid.ui.component.CardContent
import com.dokiwei.wanandroid.ui.component.MyScrollableTabRow
import com.dokiwei.wanandroid.ui.component.mainBody
import java.net.URLEncoder

/**
 * @author DokiWei
 * @date 2023/7/15 16:05
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectScreen(navController: NavHostController) {
    val vm: ProjectViewModel = viewModel()
    val projectTitleList by vm.projectTitleList.collectAsState()
    val projectList by vm.projectList.collectAsState()
    val selectedTabIndex by vm.selectedTabIndex
    val isTabExpand by vm.isTabExpand
    var titleList by remember {
        mutableStateOf(listOf("加载中"))
    }
    if (projectTitleList.isNotEmpty()) {
        val list = mutableListOf<String>()
        projectTitleList.forEach {
            list.add(it.name)
        }
        titleList = list
    }
    Scaffold(
        topBar = {
            if (!isTabExpand) {
                TopAppBar(
                    title = {
                        MyScrollableTabRow(
                            titleList = titleList,
                            selectedTabIndex = selectedTabIndex,
                            onClick = {
                                vm.onTabSelected(it)
                            }
                        )
                    },
                    actions = {
                        IconButton(onClick = { vm.onTabExpanded(true) }) {
                            Icon(
                                imageVector = Icons.Rounded.List,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                )
            } else {
                TopAppBar(
                    title = {
                        Text(
                            text = "全部项目分类",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { vm.onTabExpanded(false) }) {
                            Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = null)
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        AnimatedVisibility(
            visible = isTabExpand,
            enter = slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(500, easing = LinearOutSlowInEasing)
            ) + fadeIn(animationSpec = tween(500)),
            exit = slideOutHorizontally(
                targetOffsetX = { -it },
                animationSpec = tween(500, easing = FastOutLinearInEasing)
            ) + fadeOut(animationSpec = tween(500))
        ) {
            LazyVerticalGrid(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                columns = GridCells.Fixed(3),
            ) {
                items(projectTitleList.size) {
                    TextButton(onClick = { vm.onTabSelected(it) }) {
                        Text(text = projectTitleList[it].name)
                    }
                }
            }
        }
        AnimatedVisibility(
            visible = projectTitleList.isNotEmpty() && projectList.isNotEmpty() && !isTabExpand,
            enter = slideInVertically(animationSpec = tween(800))
        ) {
            LazyColumn(
                Modifier
                    .mainBody()
                    .padding(innerPadding)
            ) {
                items(projectList.size) {
                    CardContent(onClick = {
                        val link = URLEncoder.encode(
                            projectList[it].link,
                            "UTF-8"
                        )
                        navController.navigate("网页/$link")
                    }) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                        ) {
                            AsyncImage(
                                model = projectList[it].envelopePic,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(96.dp, 148.dp)
                                    .clip(
                                        RoundedCornerShape(20.dp)
                                    ),
                                contentScale = ContentScale.Crop
                            )
                            Column(
                                Modifier
                                    .height(148.dp)
                                    .padding(horizontal = 5.dp),
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {

                                Text(
                                    text = projectList[it].title,
                                    fontSize = 16.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    fontWeight = FontWeight.Bold,
                                )
                                Text(
                                    text = projectList[it].desc,
                                    fontSize = 14.sp,
                                    maxLines = 3,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = projectList[it].niceDate + "  " + projectList[it].author,
                                    fontSize = 12.sp,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun Pre() {
    Column(Modifier.height(144.dp), verticalArrangement = Arrangement.SpaceBetween) {
        Text(
            text = "projectList[it].title",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = "projectList[it].desc",
            maxLines = 4,
            overflow = TextOverflow.Ellipsis
        )
        Text(text = "projectList[it].niceDate + projectList[it].author")
    }
}