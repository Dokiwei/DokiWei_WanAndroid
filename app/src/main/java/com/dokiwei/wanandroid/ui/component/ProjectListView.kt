package com.dokiwei.wanandroid.ui.component

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.dokiwei.wanandroid.data.ProjectData

/**
 * @author DokiWei
 * @date 2023/7/17 2:24
 */
@SuppressLint("UnrememberedMutableState")
@Composable
fun ProjectListView(
    modifier: Modifier,
    projectList: List<ProjectData>,
    onProjectClick: (ProjectData) -> Unit,
    onLikeClick: (ProjectData, Boolean) -> Boolean,
    lazyListState: LazyListState,
) {
    LazyColumn(
        modifier,
        state = lazyListState
    ) {
        items(projectList.size) {
            val project = projectList[it]
            var like by mutableStateOf(project.collect)
            CardContent(onClick = {
                onProjectClick(project)
            }) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    AsyncImage(
                        model = project.envelopePic,
                        contentDescription = null,
                        modifier = Modifier
                            .size(96.dp, 148.dp)
                            .clip(
                                RoundedCornerShape(20.dp)
                            ),
                        contentScale = ContentScale.Crop
                    )
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            Modifier
                                .height(148.dp)
                                .padding(horizontal = 5.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = project.title,
                                fontSize = 16.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                fontWeight = FontWeight.Bold,
                            )
                            Text(
                                text = project.desc,
                                fontSize = 14.sp,
                                maxLines = 3,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = project.niceDate + "  " + project.author,
                                fontSize = 12.sp,
                            )
                        }
                        LikeIcon(
                            modifier = Modifier.align(Alignment.BottomEnd),
                            size = 30.dp,
                            liked = like
                        ) {
                            onLikeClick(project, like)
                            like = !like
                        }
                    }
                }
            }
        }
    }
}