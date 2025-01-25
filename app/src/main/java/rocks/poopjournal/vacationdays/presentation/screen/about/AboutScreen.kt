package rocks.poopjournal.vacationdays.presentation.screen.about

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavHostController
import rocks.poopjournal.vacationdays.R
import rocks.poopjournal.vacationdays.presentation.ui.theme.gray
import rocks.poopjournal.vacationdays.presentation.ui.theme.lightGray

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun AboutScreen(navController: NavHostController) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 50.dp)
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        TopBar(onClose = { navController.popBackStack() }, context = context)
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MarvinRow(context = context)
            Spacer(modifier = Modifier.height(8.dp))
            CodeAquariaRow(context = context)
            Spacer(modifier = Modifier.height(8.dp))
            MubeenRow(context = context)
        }
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(MaterialTheme.colorScheme.secondary),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.contribute),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            ContributionRow(context = context)
        }

        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(MaterialTheme.colorScheme.secondary),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.openSourceLicences),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            LicenseRow(context = context)
        }
    }
}


@RequiresApi(Build.VERSION_CODES.P)
@Composable
private fun TopBar(
    onClose: () -> Unit,
    context: Context
) {
    val packageManager = context.packageManager
    val packageName = context.packageName
    val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        packageManager.getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(0))
    } else {
        packageManager.getPackageInfo(packageName, 0)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .background(MaterialTheme.colorScheme.primary),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = { onClose() },
                modifier = Modifier.padding(top = 20.dp, start = 10.dp, end = 10.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.background
                )
            }

            IconButton(
                onClick = { /*TODO*/ },
                modifier = Modifier.padding(top = 20.dp, start = 10.dp, end = 10.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_about),
                    contentDescription = "about",
                    tint = MaterialTheme.colorScheme.background
                )
            }
        }

        // Top bar content
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center,

                ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .background(MaterialTheme.colorScheme.primary)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .background(MaterialTheme.colorScheme.background)
                        .align(Alignment.BottomCenter)
                )
                Image(
                    painter = painterResource(id = R.drawable.rounded_logo),
                    contentDescription = "rounded icon",
                    modifier = Modifier
                        .size(96.dp)
                        .clip(CircleShape)
                        .border(
                            width = 3.dp,
                            color = MaterialTheme.colorScheme.background,
                            shape = CircleShape
                        )
                )
            }


            Text(
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            val stylizedPoetry = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W400,
                        color = MaterialTheme.colorScheme.primary
                    )
                ) {
                    append("v${packageInfo.longVersionCode}")
                    append("—")
                    withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                        append("GNU GPL v3.0")
                        addStringAnnotation(
                            tag = "URL",
                            annotation = "https://www.apache.org/licenses/LICENSE-2.0",
                            start = length - "Apache License 2.0".length,
                            end = length
                        )
                    }
                }
            }

            ClickableText(text = stylizedPoetry, onClick = {})
        }
    }
}

@Composable
fun MarvinRow(context: Context) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp), shape = RoundedCornerShape(10.dp), colors = CardDefaults.cardColors(
            containerColor = lightGray,
            contentColor = MaterialTheme.colorScheme.onBackground
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.crazymarvin),
                    contentDescription = "crazy marvin",
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = stringResource(id = R.string.marvin),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = stringResource(id = R.string.developer),
                        style = MaterialTheme.typography.bodyLarge,
                        color = gray
                    )

                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_mail),
                    contentDescription = "message",
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .clickable {
                            val intent = Intent(Intent.ACTION_SENDTO).apply {
                                data =
                                    Uri.parse("mailto:marvin@poopjournal.rocks?subject=Fucks%20Given")
                            }
                            startActivity(context, intent, null)
                        }
                )
                Image(
                    painter = painterResource(id = R.drawable.ic_github),
                    contentDescription = "github",
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .clickable {
                            val intent = Intent(Intent.ACTION_VIEW).apply {
                                data = Uri.parse("https://github.com/Crazy-Marvin")
                            }
                            startActivity(context, intent, null)
                        }
                )
                Image(
                    painter = painterResource(id = R.drawable.ic_x),
                    contentDescription = "mail",
                    modifier = Modifier.clickable {
                        val intent = Intent(Intent.ACTION_VIEW).apply {
                            data = Uri.parse("https://fosstodon.org/@CrazyMarvinApps")
                        }
                        startActivity(context, intent, null)
                    }
                )
            }
        }
    }
}

@Composable
fun CodeAquariaRow(context: Context) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp), shape = RoundedCornerShape(10.dp), colors = CardDefaults.cardColors(
            containerColor = lightGray,
            contentColor = MaterialTheme.colorScheme.onBackground
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.codeaquaria),
                    contentDescription = "crazy marvin",
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.FillBounds
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = stringResource(id = R.string.codeaquaria),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = stringResource(id = R.string.developer),
                        style = MaterialTheme.typography.bodyLarge,
                        color = gray
                    )

                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_mail),
                    contentDescription = "message",
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .clickable {
                            val intent = Intent(Intent.ACTION_SENDTO).apply {
                                data =
                                    Uri.parse("mailto:mubeen1519@gmail.com?subject=Fucks%20Given")
                            }
                            context.startActivity(intent)
                        }
                )
                Image(
                    painter = painterResource(id = R.drawable.ic_github),
                    contentDescription = "github",
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .clickable {
                            val intent = Intent(Intent.ACTION_VIEW).apply {
                                data = Uri.parse("https://github.com/mubeen1519")
                            }
                            context.startActivity(intent)
                        }
                )
                Image(
                    painter = painterResource(id = R.drawable.ic_x),
                    contentDescription = "x",
                    modifier = Modifier.clickable {
                        val intent = Intent(Intent.ACTION_VIEW).apply {
                            data = Uri.parse("https://twitter.com/MubeenA74")
                        }
                        context.startActivity(intent)
                    }
                )
            }
        }
    }
}

@Composable
fun MubeenRow(context: Context) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp), shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = lightGray,
            contentColor = MaterialTheme.colorScheme.onBackground
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.mubeen),
                    contentDescription = "mubeen",
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.FillBounds
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = stringResource(id = R.string.mubeen),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = stringResource(id = R.string.developer),
                        style = MaterialTheme.typography.bodyLarge,
                        color = gray
                    )

                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_mail),
                    contentDescription = "message",
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .clickable {
                            val intent = Intent(Intent.ACTION_SENDTO).apply {
                                data =
                                    Uri.parse("mailto:mubeen1519@gmail.com?subject=Fucks%20Given")
                            }
                            context.startActivity(intent)
                        }
                )
                Image(
                    painter = painterResource(id = R.drawable.ic_github),
                    contentDescription = "github",
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .clickable {
                            val intent = Intent(Intent.ACTION_VIEW).apply {
                                data = Uri.parse("https://github.com/mubeen1519")
                            }
                            context.startActivity(intent)
                        }
                )
                Image(
                    painter = painterResource(id = R.drawable.ic_x),
                    contentDescription = "x",
                    modifier = Modifier.clickable {
                        val intent = Intent(Intent.ACTION_VIEW).apply {
                            data = Uri.parse("https://twitter.com/MubeenA74")
                        }
                        context.startActivity(intent)
                    }
                )
            }
        }
    }
}

@Composable
fun ContributionRow(context: Context) {
    Column(
        modifier = Modifier.padding(start = 8.dp, end = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .clickable {
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse("https://hosted.weblate.org/engage/fucks-given/")
                    }
                    context.startActivity(intent)
                }, verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_globe),
                contentDescription = "translate",
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = stringResource(id = R.string.translate),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        Divider(modifier = Modifier.padding(start = 20.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .clickable {
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse("https://github.com/Crazy-Marvin/FucksGiven/issues")
                    }
                    context.startActivity(intent)
                }, verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_error),
                contentDescription = "report",
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = stringResource(id = R.string.reportaproblem),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        Divider(modifier = Modifier.padding(start = 20.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .clickable {
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse("https://github.com/Crazy-Marvin/FucksGiven")
                    }
                    context.startActivity(intent)
                }, verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_source),
                contentDescription = "source",
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = stringResource(id = R.string.viewSource),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
fun LicenseRow(context: Context) {
    Column(modifier = Modifier
        .padding(8.dp)
        .clickable {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://github.com/feathericons/feather/blob/main/LICENSE")
            }
            context.startActivity(intent)
        }) {
        Text(
            text = stringResource(id = R.string.materialdesignicons),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = stringResource(id = R.string.apachelicense),
            style = MaterialTheme.typography.bodyLarge,
            color = gray
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
    Divider(modifier = Modifier.padding(start = 20.dp))

    Column(modifier = Modifier
        .padding(8.dp)
        .clickable {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://github.com/feathericons/feather/blob/main/LICENSE")
            }
            context.startActivity(intent)
        }) {
        Text(
            text = stringResource(id = R.string.featherIcons),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = stringResource(id = R.string.mitLicence),
            style = MaterialTheme.typography.bodyLarge,
            color = gray
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
    Divider(modifier = Modifier.padding(start = 20.dp))
    Column(modifier = Modifier
        .padding(8.dp)
        .clickable {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data =
                    Uri.parse("https://github.com/androidx/androidx/blob/androidx-main/LICENSE.txt")
            }
            context.startActivity(intent)
        }) {
        Text(
            text = stringResource(id = R.string.androidJetpack),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = stringResource(id = R.string.apacheLicense),
            style = MaterialTheme.typography.bodyLarge,
            color = gray
        )
    }
}



