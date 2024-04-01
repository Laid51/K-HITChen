import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.isen.OusseniLaid.androiderestaurant.R
import fr.isen.OusseniLaid.androiderestaurant.ui.theme.ProjectAndroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProjectAndroidTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    HomePage()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("MainActivity", "L'activité Home est détruite")
    }
}

@Composable
fun HomePage() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            Image(
                painter = painterResource(id = R.drawable.khitchen),
                contentDescription = "Contact profile picture",
                modifier = Modifier.size(300.dp)
            )

            // Titre en haut à gauche
            Text(
                text = "Bienvenue chez \n\nK'HITCHen",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 16.dp)
            )

            // Espacement entre le titre et les catégories
            Spacer(modifier = Modifier.height(16.dp))

            // Catégories en colonne avec séparation
            CategoryItem(category = "Entrée")
            Spacer(modifier = Modifier.height(6.dp))
            CategoryItem(category = "Plats")
            Spacer(modifier = Modifier.height(6.dp))
            CategoryItem(category = "Dessert")
        }
    }
}

@Composable
fun CategoryItem(category: String) {
    val context = LocalContext.current
    Surface(
        modifier = Modifier.fillMaxWidth(), border = BorderStroke(1.dp, Color.Gray),
        contentColor = Color.Red,
        onClick = {
            // Redirection vers la page de catégorie
            val intent = Intent(context, CategoryActivity::class.java).apply {
                putExtra("categoryName", category)
            }
            context.startActivity(intent)
        }
    ) {
        Text(
            text = category,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(8.dp)
        )
    }
}

class CategoryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val categoryName = intent.getStringExtra("categoryName")

        setContent {
            ProjectAndroidTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    CategoryPage(categoryName ?: "")
                }
            }
        }
    }
}

@Composable
fun CategoryPage(categoryName: String) {
    val listItems = when (categoryName) {
        "Entrée" -> listOf("Entrée 1", "Entrée 2", "Entrée 3")
        "Plats" -> listOf("Plat 1", "Plat 2", "Plat 3")
        "Dessert" -> listOf("Dessert 1", "Dessert 2", "Dessert 3")
        else -> emptyList()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = categoryName)
                }
            )
        }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            for (item in listItems) {
                ListItem(text = { Text(text = item) }, onClick = {
                    // Redirection vers la page de détail du plat choisi
                    val intent = Intent(this@CategoryPage, DetailActivity::class.java).apply {
                        putExtra("itemName", item)
                    }
                    startActivity(intent)
                })
            }
        }
    }
}

class DetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val itemName = intent.getStringExtra("itemName")

        setContent {
            ProjectAndroidTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    DetailPage(itemName ?: "")
                }
            }
        }
    }
}

@Composable
fun DetailPage(itemName: String) {
    Surface(color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Détails de l'article : $itemName")
        }
    }
}
