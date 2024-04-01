import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
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
import androidx.core.content.ContextCompat.startActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import fr.isen.OusseniLaid.androiderestaurant.R
import fr.isen.OusseniLaid.androiderestaurant.ui.theme.ProjectAndroidTheme
import org.json.JSONObject

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
            // Récupération des éléments depuis le serveur
            fetchMenuItems(context, category)
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

fun fetchMenuItems(context: Context, category: String) {
    val queue = Volley.newRequestQueue(context)
    val url = "http://test.api.catering.bluecodegames.com/menu"

    val jsonObject = JSONObject()
    jsonObject.put("id_shop", "1")

    val request = JsonObjectRequest(Request.Method.POST, url, jsonObject,
        { response ->
            // Traitement de la réponse
            val itemList = response.getJSONArray(category)
            val items = mutableListOf<MenuItem>()
            val gson = Gson()
            for (i in 0 until itemList.length()) {
                val itemJson = itemList.getJSONObject(i)
                val menuItem = gson.fromJson(itemJson.toString(), MenuItem::class.java)
                items.add(menuItem)
            }
            // Redirection vers la page de catégorie avec les éléments récupérés
            val intent = Intent(context, CategoryActivity::class.java).apply {
                putExtra("items", ArrayList(items))
                putExtra("categoryName", category)
            }
            context.startActivity(intent)
        },
        { error ->
            // Gestion des erreurs
            Toast.makeText(context, "Erreur de récupération des éléments: $error", Toast.LENGTH_SHORT).show()
        })

    queue.add(request)
}

data class MenuItem(
    val title: String,
    val imageUrl: String,
    val price: String
)

class CategoryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val categoryName = intent.getStringExtra("categoryName")
        val items = intent.getSerializableExtra("items") as? List<MenuItem>

        setContent {
            ProjectAndroidTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    CategoryPage(categoryName ?: "", items ?: emptyList())
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryPage(categoryName: String, items: List<MenuItem>) {
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
            for (item in items) {
                ListItem(
                    icon = {
                        CoilImage(url = item.imageUrl, contentDescription = item.title)
                    },
                    text = {
                        Text(text = item.title)
                    },
                    secondaryText = {
                        Text(text = item.price)
                    },
                    onClick = {
                        // Redirection vers la page de détail du plat choisi
                        val intent = Intent(this@CategoryPage, DetailActivity::class.java).apply {
                            putExtra("item", item)
                        }
                        startActivity(intent)
                    }
                )
            }
        }
    }
}


class DetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val item = intent.getSerializableExtra("item") as? MenuItem

        setContent {
            ProjectAndroidTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    DetailPage(item)
                }
            }
        }
    }
}

@Composable
fun DetailPage(item: MenuItem?) {
    Surface(color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (item != null) {
                Text(text = "Titre: ${item.title}")
                Text(text = "Prix: ${item.price}")
                // Afficher d'autres informations du plat si nécessaire
            } else {
                Text(text = "Détails de l'article non disponibles")
            }
        }
    }
}
